/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.furnace.util.Strings;

/**
 * @author Adam Wyłuda
 */
public class GradleModelImpl
{
   private String script;

   private String group;
   private String name;
   private String version;
   private String projectPath;
   private String rootProjectPath;
   private String packaging;
   private String archivePath;
   private List<GradleTask> tasks;
   private List<GradleDependency> dependencies;
   private List<GradleDependency> managedDependencies;
   private List<GradleProfile> profiles;
   private List<GradlePlugin> plugins;
   private List<GradleRepository> repositories;
   private List<GradleSourceSet> sourceSets;
   private Map<String, String> properties;

   /**
    * Creates empty Gradle model.
    */
   public GradleModelImpl()
   {
      this.script = "";
      this.group = "";
      this.name = "";
      this.projectPath = "";
      this.rootProjectPath = "";
      this.version = "";
      this.packaging = "";
      this.archivePath = "";
      this.tasks = Lists.newArrayList();
      this.dependencies = Lists.newArrayList();
      this.managedDependencies = Lists.newArrayList();
      this.profiles = Lists.newArrayList();
      this.plugins = Lists.newArrayList();
      this.repositories = Lists.newArrayList();
      this.sourceSets = Lists.newArrayList();
      this.properties = Maps.newHashMap();
   }

   public GradleModelImpl(String group, String name, String version,
            String projectPath, String rootProjectDirectory,
            String packaging, String archivePath,
            List<GradleTask> tasks,
            List<GradleDependency> dependencies,
            List<GradleDependency> managedDependencies,
            List<GradleProfile> profiles,
            List<GradlePlugin> plugins,
            List<GradleRepository> repositories,
            List<GradleSourceSet> sourceSets,
            Map<String, String> properties)
   {
      this.script = "";
      this.group = group;
      this.name = name;
      this.projectPath = projectPath;
      this.rootProjectPath = rootProjectDirectory;
      this.version = version;
      this.packaging = packaging;
      this.archivePath = archivePath;
      this.tasks = tasks;
      this.dependencies = dependencies;
      this.managedDependencies = managedDependencies;
      this.profiles = profiles;
      this.plugins = plugins;
      this.repositories = repositories;
      this.sourceSets = sourceSets;
      this.properties = properties;
   }

   /**
    * Performs copy of the given instance.
    */
   public GradleModelImpl(GradleEffectiveModel original)
   {
      this.group = original.getGroup();
      this.name = original.getName();
      this.projectPath = original.getProjectPath();
      this.rootProjectPath = original.getRootProjectPath();
      this.version = original.getVersion();
      this.packaging = original.getPackaging();
      this.archivePath = original.getArchivePath();
      this.tasks = Lists.newArrayList(original.getTasks());
      this.dependencies = Lists.newArrayList(original.getEffectiveDependencies());
      this.managedDependencies = Lists.newArrayList(original.getEffectiveManagedDependencies());
      this.profiles = Lists.newArrayList();
      // Performs a copy of profile list
      for (GradleProfile profile : original.getProfiles())
      {
         GradleProfile newProfile = new GradleProfileImpl(profile.getName(),
                  new GradleModelImpl(profile.getModel()));
         newProfile.setProfileScriptResource((FileResource<?>) profile.getProfileScriptResource());
         this.profiles.add(newProfile);
      }
      this.plugins = Lists.newArrayList(original.getPlugins());
      this.repositories = Lists.newArrayList(original.getRepositories());
      this.sourceSets = Lists.newArrayList(original.getEffectiveSourceSets());
      this.properties = Maps.newHashMap(original.getProperties());
   }

   public String getScript()
   {
      return script;
   }

   public void setScript(String script)
   {
      this.script = script;
   }

   public String getGroup()
   {
      return group;
   }

   public String getName()
   {
      return name;
   }

   public String getVersion()
   {
      return version;
   }

   public String getProjectPath()
   {
      return projectPath;
   }

   public String getRootProjectPath()
   {
      return rootProjectPath;
   }

   public String getPackaging()
   {
      return packaging;
   }

   public String getArchiveName()
   {
      return archivePath.substring(archivePath.lastIndexOf("/") + 1, archivePath.lastIndexOf("."));
   }

   public String getArchivePath()
   {
      return archivePath;
   }

   public List<GradleTask> getTasks()
   {
      return Collections.unmodifiableList(tasks);
   }

   public List<GradleDependency> getEffectiveDependencies()
   {
      return Collections.unmodifiableList(dependencies);
   }

   public List<GradleDependency> getEffectiveManagedDependencies()
   {
      return Collections.unmodifiableList(managedDependencies);
   }

   public List<GradleProfile> getProfiles()
   {
      return Collections.unmodifiableList(profiles);
   }

   public List<GradlePlugin> getPlugins()
   {
      return Collections.unmodifiableList(plugins);
   }

   public List<GradleRepository> getRepositories()
   {
      return Collections.unmodifiableList(repositories);
   }

   public List<GradleSourceSet> getEffectiveSourceSets()
   {
      return Collections.unmodifiableList(sourceSets);
   }

   public Map<String, String> getProperties()
   {
      return Collections.unmodifiableMap(properties);
   }

   public boolean hasTask(String name)
   {
      for (GradleTask task : tasks)
      {
         if (task.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   public boolean hasEffectiveDependency(GradleDependencyBuilder builder)
   {
      for (GradleDependency dep : dependencies)
      {
         if (builder.equalsToDependency(dep))
         {
            return true;
         }
      }
      return false;
   }

   public boolean hasEffectiveManagedDependency(GradleDependencyBuilder builder)
   {
      for (GradleDependency dep : managedDependencies)
      {
         if (builder.equalsToDependency(dep))
         {
            return true;
         }
      }
      return false;
   }

   public boolean hasProfile(String name)
   {
      for (GradleProfile profile : profiles)
      {
         if (profile.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   public boolean hasPlugin(String clazz)
   {
      for (GradlePlugin plugin : plugins)
      {
         if (plugin.getClazz().equals(clazz))
         {
            return true;
         }
      }
      return false;
   }

   public boolean hasRepository(String url)
   {
      for (GradleRepository repo : repositories)
      {
         if (repo.getUrl().equals(url))
         {
            return true;
         }
      }
      return false;
   }

   public boolean hasProperty(String key)
   {
      return properties.containsKey(key);
   }

   public void setGroup(String group) throws UnremovableElementException
   {
      script = GradleSourceUtil.setProperty(script, "group", group);
      this.group = group;
   }

   public void setName(String name) throws UnremovableElementException
   {
      // That's all. GradleFacet will check if name of the project has changed and then
      // perform change in the settings.gradle file.
      this.name = name;
   }

   public void setVersion(String version) throws UnremovableElementException
   {
      script = GradleSourceUtil.setProperty(script, "version", version);
      this.version = version;
   }

   public void setPackaging(String packaging)
   {
      for (GradlePluginType type : GradlePluginType.values())
      {
         if (type.getPackaging().equals(packaging))
         {
            addPlugin(!Strings.isNullOrEmpty(type.getShortName())
                     ? type.getShortName()
                     : type.getClazz());
            this.packaging = packaging;
            return;
         }
      }

      throw new IllegalArgumentException("There is no such packaging: " + packaging);
   }

   public void setArchiveName(String archiveName)
   {
      script = GradleSourceUtil.setArchiveName(script, archiveName);
      this.archivePath = archivePath.substring(0, archivePath.lastIndexOf("/") + 1) + archiveName + "." + packaging;
   }

   public void addTask(GradleTaskBuilder builder)
   {
      script = GradleSourceUtil.insertTask(script,
               builder.getName(), builder.getDependsOn(), builder.getType(), builder.getCode());
      List<GradleTask> dependsOn = Lists.newArrayList();
      for (String dependsOnString : builder.getDependsOn())
      {
         for (GradleTask task : tasks)
         {
            if (task.getName().equals(dependsOnString))
            {
               dependsOn.add(task);
               break;
            }
         }
      }
      this.tasks.add(new GradleTaskImpl(builder.getName(), dependsOn));
   }

   public void addDependency(GradleDependencyBuilder builder)
   {
      if (!Strings.isNullOrEmpty(builder.getVersion()) && !Strings.isNullOrEmpty(builder.getConfiguration()))
      {
         script = GradleSourceUtil.insertDependency(script,
                  builder.getGroup(),
                  builder.getName(),
                  builder.getVersion(),
                  builder.getConfiguration());
         this.dependencies.add(new GradleDependencyImpl(
                  builder.getGroup(), builder.getName(), builder.getVersion(),
                  GradleDependencyConfiguration.DIRECT,
                  GradleDependencyConfiguration.DIRECT.getName()));
      }
      else
      {
         script = GradleSourceUtil.insertDirectDependency(script,
                  builder.getGroup(),
                  builder.getName());
         this.dependencies.add(new GradleDependencyImpl(
                  builder.getGroup(), builder.getName(), builder.getVersion(),
                  GradleDependencyConfiguration.fromName(builder.getConfiguration()),
                  builder.getConfiguration()));
      }
   }

   public void addManagedDependency(GradleDependencyBuilder builder)
   {
      script = GradleSourceUtil.insertManagedDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());
      this.managedDependencies.add(new GradleDependencyImpl(
               builder.getGroup(), builder.getName(), builder.getVersion(),
               GradleDependencyConfiguration.fromName(builder.getConfiguration()),
               builder.getConfiguration()));
   }

   public void addProfile(String name)
   {
      profiles.add(new GradleProfileImpl(name, new GradleModelImpl()));
   }

   public void addPlugin(String name)
   {
      script = GradleSourceUtil.insertPlugin(script, name);
      GradlePluginType type = GradlePluginType.typeByClazz(name);
      this.plugins.add(new GradlePluginImpl(name, type));
   }

   public void addRepository(String url)
   {
      script = GradleSourceUtil.insertRepository(script, url);
      this.repositories.add(new GradleRepositoryImpl("MavenRepo", url));
   }

   public void setProperty(String name, String value)
   {
      script = GradleSourceUtil.setProperty(script, "ext." + name, value);
      this.properties.put(name, value);
   }

   public void removeDependency(GradleDependencyBuilder builder) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());

      for (GradleDependency dep : this.dependencies)
      {
         if (builder.equalsToDependency(dep) ||
                  (Strings.isNullOrEmpty(builder.getVersion()) &&
                           Strings.isNullOrEmpty(builder.getConfiguration()) &&
                  builder.equalsToDirectDependency(dep)))
         {
            this.dependencies.remove(dep);
            return;
         }
      }
   }

   public void removeManagedDependency(GradleDependencyBuilder builder) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeManagedDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());

      for (GradleDependency dep : this.managedDependencies)
      {
         if (builder.equalsToDependency(dep))
         {
            this.managedDependencies.remove(dep);
            return;
         }
      }
   }

   public void removeProfile(String name)
   {
      for (GradleProfile profile : profiles)
      {
         if (profile.getName().equals(name))
         {
            profiles.remove(profile);
            return;
         }
      }

      throw new RuntimeException("Can't remove profile non existing profile named " + name);
   }

   public void removePlugin(String name) throws UnremovableElementException
   {
      GradlePluginType type = GradlePluginType.typeByClazz(name);
      if (type != GradlePluginType.OTHER)
      {
         try
         {
            script = GradleSourceUtil.removePlugin(script, type.getClazz());
         }
         catch (UnremovableElementException exception)
         {
            // This could be a case when plugin was declared with it's short name
            script = GradleSourceUtil.removePlugin(script, type.getShortName());
         }
      }
      else
      {
         script = GradleSourceUtil.removePlugin(script, name);
      }

      for (GradlePlugin plugin : this.plugins)
      {
         if (plugin.getClazz().equals(name))
         {
            this.plugins.remove(plugin);
            return;
         }
      }
   }

   public void removeRepository(String url) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeRepository(script, url);

      for (GradleRepository repo : this.repositories)
      {
         if (repo.getUrl().equals(url))
         {
            this.repositories.remove(repo);
            return;
         }
      }
   }

   public void removeProperty(String name) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeProperty(script, "ext." + name);
      this.properties.remove(name);
   }
}
