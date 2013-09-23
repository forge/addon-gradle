/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.List;
import java.util.Map;

import org.gradle.jarjar.com.google.common.base.Joiner;
import org.gradle.jarjar.com.google.common.collect.Lists;
import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.addon.gradle.projects.model.GradleDependency;
import org.jboss.forge.addon.gradle.projects.model.GradleDependencyBuilder;
import org.jboss.forge.addon.gradle.projects.model.GradleDependencyConfiguration;
import org.jboss.forge.addon.gradle.projects.model.GradlePlugin;
import org.jboss.forge.addon.gradle.projects.model.GradlePluginBuilder;
import org.jboss.forge.addon.gradle.projects.model.GradleRepository;
import org.jboss.forge.addon.gradle.projects.model.GradleRepositoryBuilder;
import org.jboss.forge.furnace.util.Strings;

/**
 * Set of pure functions manipulating Gradle build scripts.
 * 
 * @author Adam Wyłuda
 */
public class GradleSourceUtil
{
   public static final String FORGE_LIBRARY = "forge.gradle";
   public static final String FORGE_LIBRARY_RESOURCE = "/forge.gradle";
   public static final String FORGE_OUTPUT_TASK = "forgeOutput";
   public static final String FORGE_OUTPUT_XML = "forge-output.xml";
   public static final String PROFILE_SUFFIX = "-profile.gradle";

   public static final String INCLUDE_FORGE_LIBRARY = "apply from: 'forge.gradle'\n";
   public static final String MANAGED_CONFIG = "managed";
   public static final String DIRECT_CONFIG = GradleDependencyConfiguration.DIRECT.getName();

   public static final String ARCHIVE_NAME_METHOD = "archiveName";

   public static final String PROJECT_PROPERTY_PREFIX = "ext.";

   /**
    * Sets the project name in Gradle project settings script.
    * 
    * @param source Contents of settings.gradle from project root directory.
    * @param projectPath Project path in format like :rootProject:subproject
    * @param newName New name for the project.
    * @returns Content of new settings.gradle which changes project name.
    */
   public static String setProjectName(String source, String projectPath, String newName)
   {
      source = SourceUtil.addNewLineAtEnd(source);
      source += String.format("project('%s').name = '%s'\n", projectPath, newName);

      return source;
   }

   public static String setArchiveName(String source, String archiveName)
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      String archiveNameInvocationString = archiveNameString(archiveName);

      for (InvocationWithString invocation : parser.getInvocationsWithString())
      {
         if (invocation.getMethodName().equals(ARCHIVE_NAME_METHOD))
         {
            source = SourceUtil.removeSourceFragment(source, invocation);
            source = SourceUtil.insertString(source, archiveNameInvocationString,
                     invocation.getLineNumber(), invocation.getColumnNumber());
            return source;
         }
      }

      source = SourceUtil.addNewLineAtEnd(source) + archiveNameInvocationString + "\n";

      return source;
   }

   private static String archiveNameString(String archiveName)
   {
      return String.format("%s '%s'", ARCHIVE_NAME_METHOD, archiveName);
   }

   public static String insertDependency(String source, GradleDependency dep)
   {
      String depString;
      List<GradleDependency> excludes = dep.getExcludedDependencies();

      if (excludes.size() == 0)
      {
         depString = String.format("%s \"%s\"", dep.getConfigurationName(), dep.toGradleString());
      }
      else
      {
         depString = dependencyDeclaration(
                  String.format("%s(\"%s\")", dep.getConfigurationName(), dep.toGradleString()),
                  dep.getGroup(), dep.getExcludedDependencies());
      }

      source = SourceUtil.insertIntoInvocationAtPath(source, depString, "dependencies");
      return source;
   }

   public static String removeDependency(String source, GradleDependency dep)
            throws UnremovableElementException
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure deps : allDependencyInvocations(parser))
      {
         // Search in string invocations
         for (InvocationWithString invocation : deps.getInvocationsWithString())
         {
            if (isDependencyInvocation(invocation) && dep.equals(dependencyFromInvocation(invocation)))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }

         // Search in map invocations
         for (InvocationWithMap invocation : deps.getInvocationsWithMap())
         {
            if (isDependencyInvocation(invocation) && dep.equals(dependencyFromInvocation(invocation)))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }

         // Search in invocations with closure
         for (InvocationWithClosure invocation : deps.getInvocationsWithClosure())
         {
            if (isDependencyInvocation(invocation) && dep.equals(dependencyFromInvocation(invocation)))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }
      }

      throw new UnremovableElementException("Couldn't remove dependency: " + dep.toString());
   }

   /**
    * Returns a list of dependencies which are declared in given source.
    */
   public static List<GradleDependency> getDependencies(String source)
   {
      List<GradleDependency> result = Lists.newArrayList();

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure deps : allDependencyInvocations(parser))
      {
         // Search in string invocations
         for (InvocationWithString invocation : deps.getInvocationsWithString())
         {
            if (isDependencyInvocation(invocation))
            {
               result.add(dependencyFromInvocation(invocation));
            }
         }

         // Search in map invocations
         for (InvocationWithMap invocation : deps.getInvocationsWithMap())
         {
            if (isDependencyInvocation(invocation))
            {
               result.add(dependencyFromInvocation(invocation));
            }
         }

         // Search in invocations with closure
         for (InvocationWithClosure invocation : deps.getInvocationsWithClosure())
         {
            if (isDependencyInvocation(invocation))
            {
               result.add(dependencyFromInvocation(invocation));
            }
         }
      }

      return result;
   }

   public static String insertDirectDependency(String source, String group, String name)
   {
      String depString = String.format("%s group: \"%s\", name: \"%s\"", DIRECT_CONFIG, group, name);
      source = SourceUtil.insertIntoInvocationAtPath(source, depString, "dependencies");
      return source;
   }

   public static String removeDirectDependency(String source, String group, String name)
            throws UnremovableElementException
   {
      Map<String, String> depMap = Maps.newHashMap();
      depMap.put("group", group);
      depMap.put("name", name);

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure deps : parser.allInvocationsAtPath("dependencies"))
      {
         for (InvocationWithMap invocation : deps.getInvocationsWithMap())
         {
            if (invocation.getMethodName().equals(DIRECT_CONFIG) &&
                     invocation.getParameters().equals(depMap))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }
      }

      throw new UnremovableElementException("Couldn't remove direct dependency with group: " + group +
               ", name: " + name);
   }

   /**
    * Returns a list of direct (defined using <i>direct</i> closure) dependencies declared in given source.
    */
   public static List<GradleDependency> getDirectDependencies(String source)
   {
      List<GradleDependency> deps = Lists.newArrayList();

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure invocation : allDependencyInvocations(parser))
      {
         for (InvocationWithMap mapInvocation : invocation.getInvocationsWithMap())
         {
            if (mapInvocation.getMethodName().equals(DIRECT_CONFIG))
            {
               Map<String, String> params = mapInvocation.getParameters();
               GradleDependencyBuilder dep = GradleDependencyBuilder.create()
                        .setGroup(params.get("group"))
                        .setName(params.get("name"))
                        .setConfiguration(GradleDependencyConfiguration.DIRECT);
               deps.add(dep);
            }
         }
      }

      return deps;
   }

   public static String insertManagedDependency(String source, GradleDependency dep)
   {
      String depString;
      List<GradleDependency> excludes = dep.getExcludedDependencies();

      if (excludes.size() == 0)
      {
         depString = String.format("%s configuration: \"%s\", %s", MANAGED_CONFIG,
                  dep.getConfigurationName(), dep.toGradleMapString());
      }
      else
      {
         depString = dependencyDeclaration(
                  String.format("%s(configuration: \"%s\", %s)",
                           MANAGED_CONFIG, dep.getConfigurationName(), dep.toGradleMapString()),
                  dep.getGroup(), dep.getExcludedDependencies());
      }

      source = SourceUtil.insertIntoInvocationAtPath(source, depString, "allprojects", "dependencies");
      return source;
   }

   public static String removeManagedDependency(String source, GradleDependency dep)
            throws UnremovableElementException
   {
      GradleDependencyBuilder builder = GradleDependencyBuilder.create(dep);
      builder.setConfigurationName(MANAGED_CONFIG);

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure deps : allDependencyInvocations(parser))
      {
         // Search in map invocations
         for (InvocationWithMap invocation : deps.getInvocationsWithMap())
         {
            if (invocation.getMethodName().equals(MANAGED_CONFIG) &&
                     dep.equals(dependencyFromInvocation(invocation)))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }

         // Search in invocations with closure
         for (InvocationWithClosure invocation : deps.getInvocationsWithClosure())
         {
            if (invocation.getMethodName().equals(MANAGED_CONFIG) &&
                     dep.equals(dependencyFromInvocation(invocation)))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }
      }

      throw new UnremovableElementException("Couldn't remove managed dependency: " + dep.toString());
   }

   /**
    * Returns a list of managed dependencies (declared using <i>managed</i> closure) in given source.
    */
   public static List<GradleDependency> getManagedDependencies(String source)
   {
      List<GradleDependency> list = Lists.newArrayList();

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure deps : allDependencyInvocations(parser))
      {
         // Search in map invocations
         for (InvocationWithMap invocation : deps.getInvocationsWithMap())
         {
            if (invocation.getMethodName().equals(MANAGED_CONFIG))
            {
               list.add(managedDependencyFromInvocation(invocation));
            }
         }

         // Search in invocations with closure
         for (InvocationWithClosure invocation : deps.getInvocationsWithClosure())
         {
            if (invocation.getMethodName().equals(MANAGED_CONFIG))
            {
               list.add(managedDependencyFromInvocation(invocation));
            }
         }
      }

      return list;
   }

   public static List<GradlePlugin> getPlugins(String source)
   {
      List<GradlePlugin> plugins = Lists.newArrayList();

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithMap invocation : parser.getInvocationsWithMap())
      {
         if (invocation.getMethodName().equals("apply"))
         {
            String plugin = invocation.getParameters().get("plugin");
            if (invocation.getParameters().size() == 1 && plugin != null)
            {
               plugins.add(GradlePluginBuilder.create().setClazz(plugin));
            }
         }
      }

      return plugins;
   }

   /**
    * Adds {@code apply plugin: 'clazz'} after the last plugin application, if there are no other applied plugins then
    * it adds it at the end of source.
    */
   public static String insertPlugin(String source, String clazz)
   {
      String pluginString = String.format("\napply plugin: '%s'", clazz);

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      if (parser.getInvocationsWithMap().size() > 0)
      {
         InvocationWithMap lastInvocation = parser.getInvocationsWithMap().get(
                  parser.getInvocationsWithMap().size() - 1);
         source = SourceUtil.insertString(source, pluginString, lastInvocation.getLastLineNumber(),
                  lastInvocation.getLastColumnNumber());
      }
      else
      {
         source += pluginString;
      }
      return source;
   }

   public static String removePlugin(String source, String clazz)
            throws UnremovableElementException
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithMap invocation : parser.getInvocationsWithMap())
      {
         if ((invocation.getMethodName().equals("apply") || invocation.getMethodName().equals("project.apply")) &&
                  invocation.getParameters().size() == 1 &&
                  clazz.equals(invocation.getParameters().get("plugin")))
         {
            return SourceUtil.removeSourceFragmentWithLine(source, invocation);
         }
      }

      throw new UnremovableElementException("Couldn't remove plugin: " + clazz);
   }

   public static List<GradleRepository> getRepositories(String source)
   {
      List<GradleRepository> repos = Lists.newArrayList();

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure closure : parser.allInvocationsAtPath("repositories", "maven"))
      {
         for (InvocationWithString invocation : closure.getInvocationsWithString())
         {
            if (invocation.getMethodName().equals("url"))
            {
               repos.add(GradleRepositoryBuilder.create().setUrl(invocation.getString()));
            }
         }
      }

      return repos;
   }

   public static String insertRepository(String source, String url)
   {
      // TODO Repository name?
      String repoString = String.format("url '%s'", url);
      source = SourceUtil.insertIntoInvocationAtPath(source, repoString, "repositories", "maven");
      return source;
   }

   public static String removeRepository(String source, String url)
            throws UnremovableElementException
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure repos : parser.allInvocationsAtPath("repositories", "maven"))
      {
         for (InvocationWithString invocation : repos.getInvocationsWithString())
         {
            if (invocation.getMethodName().equals("url") &&
                     invocation.getString().equals(url))
            {
               return SourceUtil.removeSourceFragmentWithLine(source, invocation);
            }
         }
      }

      throw new UnremovableElementException("Couldn't remove repository: " + url);
   }

   /**
    * Sets <b>dynamic</b> project property in the build script.
    * <p/>
    * For example setProperty for key X and value Y will append X = Y to the build script.
    * <p/>
    * Be aware that dynamic properties are scheduled to be removed in Gradle 2.0 so always use project's <b>ext</b>
    * namespace for non-standard properties, like setProperty(source, "ext.myProp", "value").
    */
   public static String setProperty(String source, String key, String value)
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      String assignmentString = variableAssignmentString(key, value);

      for (VariableAssignment assignment : parser.getVariableAssignments())
      {
         // If it's already defined somewhere
         if (assignment.getVariable().equals(key))
         {
            source = SourceUtil.removeSourceFragment(source, assignment);
            source = SourceUtil.insertString(source, assignmentString,
                     assignment.getLineNumber(), assignment.getColumnNumber());
            return source;
         }
      }

      // If it was not defined anywhere
      source = SourceUtil.addNewLineAtEnd(source) + assignmentString + "\n";

      return source;
   }

   private static String variableAssignmentString(String variable, String value)
   {
      return String.format("%s = \"%s\"", variable, value);
   }

   public static String removeProperty(String source, String key) throws UnremovableElementException
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (VariableAssignment assignment : parser.getVariableAssignments())
      {
         if (assignment.getVariable().equals(key))
         {
            return SourceUtil.removeSourceFragmentWithLine(source, assignment);
         }
      }

      throw new UnremovableElementException("Couldn't remove property: " + key);
   }

   // There is no way to remove a task because tasks are composed of many actions
   // so we can only insert new tasks
   public static String insertTask(String source, String name, List<String> dependsOn, String type, String code)
   {
      String taskDeclarationString = taskDeclarationString(name, dependsOn, type);
      String taskString = String.format("\ntask %s << {\n%s\n}\n", taskDeclarationString, code);
      source += taskString;
      return source;
   }

   private static String taskDeclarationString(String name, List<String> dependsOn, String type)
   {
      if (dependsOn.isEmpty() && Strings.isNullOrEmpty(type))
      {
         return name;
      }
      else
      {
         String dependsOnString = dependsOnString(dependsOn);
         String typeString = Strings.isNullOrEmpty(type) ? "" : ", type: " + type;
         return String.format("('%s'%s%s)", name, dependsOnString, typeString);
      }
   }

   private static String dependsOnString(List<String> dependsOn)
   {
      if (dependsOn.size() == 0)
      {
         return "";
      }
      else if (dependsOn.size() == 1)
      {
         return ", dependsOn: '" + dependsOn.get(0) + "'";
      }
      else
      {
         List<String> dependsOnInQuotes = Lists.newArrayList();
         for (String dep : dependsOn)
         {
            dependsOnInQuotes.add("'" + dep + "'");
         }
         return ", dependsOn: [" + Joiner.on(", ").join(dependsOnInQuotes) + "]";
      }
   }

   /**
    * Checks if source includes forge library and if not then adds it.
    */
   public static String checkForIncludeForgeLibraryAndInsert(String source)
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithMap invocation : parser.getInvocationsWithMap())
      {
         if (invocation.getMethodName().equals("apply"))
         {
            Map<String, String> map = invocation.getParameters();
            String from = map.get("from");

            // If it is already included in source then we just return the source
            if ("forge.gradle".equals(from))
            {
               return source;
            }
         }
      }

      // If statement including forge library was not found then we add it
      return INCLUDE_FORGE_LIBRARY + source;
   }

   /**
    * Returns a map of properties which are clearly declared in a given build script (i.e. they can be
    * modified/removed). Also, they must be project properties, declared in project.ext namespace.
    */
   public static Map<String, String> getDirectProperties(String source)
   {
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      Map<String, String> properties = Maps.newHashMap();
      for (VariableAssignment assignment : parser.getVariableAssignments())
      {
         if (assignment.getVariable().startsWith(PROJECT_PROPERTY_PREFIX))
         {
            properties.put(assignment.getVariable().substring(PROJECT_PROPERTY_PREFIX.length()),
                     assignment.getValue());
         }
      }
      return properties;
   }

   private static List<InvocationWithClosure> allDependencyInvocations(SimpleGroovyParser parser)
   {
      List<InvocationWithClosure> depsInvocations = Lists.newArrayList();
      depsInvocations.addAll(parser.allInvocationsAtPath("dependencies"));
      depsInvocations.addAll(parser.allInvocationsAtPath("allprojects", "dependencies"));
      return depsInvocations;
   }

   private static GradleDependency dependencyFromInvocation(InvocationWithString invocation)
   {
      return dependencyFromString(invocation.getMethodName(), invocation.getString());
   }

   private static GradleDependency dependencyFromInvocation(InvocationWithMap invocation)
   {
      return dependencyFromMap(invocation.getMethodName(), invocation.getParameters());
   }

   private static GradleDependency dependencyFromInvocation(InvocationWithClosure invocation)
   {
      GradleDependency gradleDep = null;
      if (!Strings.isNullOrEmpty(invocation.getStringParameter()))
      {
         gradleDep = dependencyFromString(invocation.getMethodName(), invocation.getStringParameter());
      }
      else
      {
         gradleDep = dependencyFromMap(invocation.getMethodName(), invocation.getMapParameter());
      }

      gradleDep = loadDependencyConfiguration(gradleDep, invocation);

      return gradleDep;
   }

   private static GradleDependency managedDependencyFromInvocation(InvocationWithMap invocation)
   {
      Map<String, String> map = Maps.newHashMap(invocation.getParameters());
      String config = map.remove("configuration");
      return dependencyFromMap(config, map);
   }

   private static GradleDependency managedDependencyFromInvocation(InvocationWithClosure invocation)
   {
      Map<String, String> map = Maps.newHashMap(invocation.getMapParameter());
      String config = map.remove("configuration");

      GradleDependency gradleDep = dependencyFromMap(config, map);
      gradleDep = loadDependencyConfiguration(gradleDep, invocation);

      return gradleDep;
   }

   private static GradleDependency loadDependencyConfiguration(GradleDependency dep, InvocationWithClosure invocation)
   {
      GradleDependencyBuilder builder = GradleDependencyBuilder.create(dep);

      // Search for excludes
      List<GradleDependency> excludes = Lists.newArrayList();
      for (InvocationWithMap mapInvocation : invocation.getInvocationsWithMap())
      {
         if (mapInvocation.getMethodName().equals("exclude"))
         {
            String group = mapInvocation.getParameters().get("group");
            String module = mapInvocation.getParameters().get("module");

            // If group is not set then by default it uses dep group
            if (Strings.isNullOrEmpty(group))
            {
               group = dep.getGroup();
            }

            excludes.add(GradleDependencyBuilder.create().setGroup(group).setName(module));
         }
      }
      builder.setExcludedDependencies(excludes);

      return builder;
   }

   private static GradleDependency dependencyFromString(String configurationName, String gradleString)
   {
      return GradleDependencyBuilder.create(configurationName, gradleString);
   }

   private static GradleDependency dependencyFromMap(String configurationName, Map<String, String> params)
   {
      String group = params.get("group");
      String name = params.get("name");
      String version = params.get("version");

      String classifier = params.get("classifier");
      if (Strings.isNullOrEmpty(classifier))
      {
         classifier = "";
      }

      String packaging = params.get("ext");
      if (Strings.isNullOrEmpty(packaging))
      {
         packaging = "jar";
      }

      return GradleDependencyBuilder.create().setConfigurationName(configurationName)
               .setGroup(group).setName(name).setVersion(version)
               .setClassifier(classifier).setPackaging(packaging);
   }

   private static boolean isDependencyInvocation(InvocationWithString invocation)
   {
      return isGradleDependencyConfiguration(invocation.getMethodName()) &&
               isGradleString(invocation.getString());
   }

   private static boolean isDependencyInvocation(InvocationWithMap invocation)
   {
      return isGradleDependencyConfiguration(invocation.getMethodName()) &&
               isDependencyMap(invocation.getParameters());
   }

   private static boolean isDependencyInvocation(InvocationWithClosure invocation)
   {
      if (!isGradleDependencyConfiguration(invocation.getMethodName()))
      {
         return false;
      }
      if (!Strings.isNullOrEmpty(invocation.getStringParameter()) &&
               isGradleString(invocation.getStringParameter()))
      {
         return true;
      }
      if (invocation.getMapParameter() != null && !invocation.getMapParameter().isEmpty() &&
               isDependencyMap(invocation.getMapParameter()))
      {
         return true;
      }
      return false;
   }

   private static boolean isDependencyMap(Map<String, String> params)
   {
      return params.containsKey("group") && params.containsKey("name") && params.containsKey("version");
   }

   private static boolean isGradleDependencyConfiguration(String configName)
   {
      for (GradleDependencyConfiguration config : GradleDependencyConfiguration.values())
      {
         if (!Strings.isNullOrEmpty(config.getName()) && config.getName().equals(configName))
         {
            return true;
         }
      }

      return false;
   }

   private static boolean isGradleString(String string)
   {
      try
      {
         GradleDependencyBuilder.create("", string);
         return true;
      }
      catch (Exception ex)
      {
         return false;
      }
   }

   private static String dependencyDeclaration(String declaration, String depGroup,
            List<GradleDependency> excludes)
   {
      StringBuilder sb = new StringBuilder(String.format("%s {\n", declaration));
      for (GradleDependency exclude : excludes)
      {
         sb.append("    exclude ");
         if (!exclude.getGroup().equals(depGroup))
         {
            sb.append(String.format("group: \"%s\", ", exclude.getGroup()));
         }
         sb.append(String.format("module: \"%s\"\n", exclude.getName()));
      }
      sb.append("}\n");
      return sb.toString();
   }
}
