/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.gradle.jarjar.com.google.common.base.Joiner;
import org.gradle.jarjar.com.google.common.collect.Lists;
import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.furnace.util.Strings;

/**
 * @author Adam Wyłuda
 */
public class GradleUtil
{
   public static final String INCLUDE_FORGE_LIBRARY = "apply from: 'forge.gradle'\n";
   
   public static String insertDependency(String source, String name, String group, String version, String configuration)
   {
      String depString = String.format("%s '%s:%s:%s'", configuration, group, name, version);
      source = SourceUtil.insertIntoInvocationAtPath(source, depString, "dependencies");
      return source;
   }

   public static String removeDependency(String source, String name, String group, String version, String configuration)
            throws UnremovableElementException
   {
      String depString = String.format("%s '%s:%s:%s'", configuration, group, name, version);
      Map<String, String> depMap = Maps.newHashMap();
      depMap.put("group", group);
      depMap.put("name", name);
      depMap.put("version", version);
      
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      for (InvocationWithClosure deps : parser.allInvocationsAtPath("dependencies"))
      {
         // Search in string invocations
         for (InvocationWithString invocation : deps.getInternalStringInvocations())
         {
            if (invocation.getMethodName().equals(configuration))
            {
               
            }
         }
      }
      
      throw new UnremovableElementException();
   }
   
   public static String insertManagedDependency(String source, String name, String group, String version, String configuration)
   {
      String depString = String.format("managed config: '%s', group: '%s', name: '%s', version: '%s'",
               configuration, group, name, version);
      source = SourceUtil.insertIntoInvocationAtPath(source, depString, "allprojects", "dependencies");
      return source;
   }
   
   public static String removeManagedDependency(String source, String name, String group, String version, String configuration)
   {
      // TODO
      return source;
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
         InvocationWithMap lastInvocation = parser.getInvocationsWithMap().get(parser.getInvocationsWithMap().size() - 1);
         source = SourceUtil.insertString(source, pluginString, lastInvocation.getLastLineNumber(), lastInvocation.getLastColumnNumber());
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
      // TODO
      return source;
   }

   public static String insertRepository(String source, String name, String url)
   {
      String repoString = String.format("url '%s'", url);
      source = SourceUtil.insertIntoInvocationAtPath(source, repoString, "repositories", "maven");
      return source;
   }

   public static String removeRepository(String source, String name, String url)
            throws UnremovableElementException
   {
      // TODO
      return source;
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
         return String.format("(name: '%s'%s%s)", name, dependsOnString, typeString);
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
}
