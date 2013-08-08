/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.gradle.jarjar.com.google.common.collect.Maps;
import org.gradle.jarjar.com.google.common.collect.Sets;
import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.furnace.util.Strings;

/**
 * @author Adam Wyłuda
 */
public class GradleModelImpl implements GradleModel
{
   private String script;

   private String group;
   private String name;
   private String version;
   private String projectPath;
   private String rootProjectDirectory;
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
      this.rootProjectDirectory = "";
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
      this.rootProjectDirectory = rootProjectDirectory;
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
   public GradleModelImpl(GradleModel original)
   {
      this.script = original.getScript();
      this.group = original.getGroup();
      this.name = original.getName();
      this.projectPath = original.getProjectPath();
      this.rootProjectDirectory = original.getRootProjectDirectory();
      this.version = original.getVersion();
      this.packaging = original.getPackaging();
      this.archivePath = original.getArchivePath();
      this.tasks = Lists.newArrayList(original.getTasks());
      this.dependencies = Lists.newArrayList(original.getDependencies());
      this.managedDependencies = Lists.newArrayList(original.getManagedDependencies());
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
      this.sourceSets = Lists.newArrayList(original.getSourceSets());
   }

   @Override
   public String getScript()
   {
      return script;
   }

   @Override
   public void setScript(String script)
   {
      this.script = script;
   }

   @Override
   public String getGroup()
   {
      return group;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public String getVersion()
   {
      return version;
   }

   @Override
   public String getProjectPath()
   {
      return projectPath;
   }

   @Override
   public String getRootProjectDirectory()
   {
      return rootProjectDirectory;
   }

   @Override
   public String getPackaging()
   {
      return packaging;
   }

   @Override
   public String getArchiveName()
   {
      return archivePath.substring(archivePath.lastIndexOf("/") + 1, archivePath.lastIndexOf("."));
   }

   @Override
   public String getArchivePath()
   {
      return archivePath;
   }

   @Override
   public List<GradleTask> getTasks()
   {
      return tasks;
   }

   @Override
   public List<GradleDependency> getDependencies()
   {
      return dependencies;
   }

   @Override
   public List<GradleDependency> getManagedDependencies()
   {
      return managedDependencies;
   }

   @Override
   public List<GradleProfile> getProfiles()
   {
      return profiles;
   }

   @Override
   public List<GradlePlugin> getPlugins()
   {
      return plugins;
   }

   @Override
   public List<GradleRepository> getRepositories()
   {
      return repositories;
   }

   @Override
   public List<GradleSourceSet> getSourceSets()
   {
      return sourceSets;
   }

   @Override
   public Map<String, String> getProperties()
   {
      return properties;
   }

   @Override
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

   @Override
   public boolean hasDependency(GradleDependencyBuilder builder)
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

   @Override
   public boolean hasManagedDependency(GradleDependencyBuilder builder)
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

   @Override
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

   @Override
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

   @Override
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

   @Override
   public boolean hasProperty(String key)
   {
      return properties.containsKey(key);
   }

   @Override
   public void setGroup(String group) throws UnremovableElementException
   {
      script = GradleSourceUtil.setProperty(script, "group", group);
      this.group = group;
   }

   @Override
   public void setName(String name) throws UnremovableElementException
   {
      // That's all. GradleFacet will check if name of the project has changed and then
      // perform change in the settings.gradle file.
      this.name = name;
   }

   @Override
   public void setVersion(String version) throws UnremovableElementException
   {
      script = GradleSourceUtil.setProperty(script, "version", version);
      this.version = version;
   }

   @Override
   public void setPackaging(String packaging)
   {
      for (GradlePluginType type : GradlePluginType.values())
      {
         if (type.getPackaging().equals(packaging))
         {
            applyPlugin(!Strings.isNullOrEmpty(type.getShortName())
                     ? type.getShortName()
                     : type.getClazz());
            this.packaging = packaging;
            return;
         }
      }

      throw new IllegalArgumentException("There is no such packaging: " + packaging);
   }

   @Override
   public void setArchiveName(String archiveName)
   {
      script = GradleSourceUtil.setArchiveName(script, archiveName);
      this.archivePath = archivePath.substring(0, archivePath.lastIndexOf("/") + 1) + archiveName + "." + packaging;
   }

   @Override
   public void createTask(GradleTaskBuilder builder)
   {
      script = GradleSourceUtil.insertTask(script,
               builder.getName(), builder.getDependsOn(), builder.getType(), builder.getCode());
      Set<GradleTask> dependsOn = Sets.newHashSet();
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

   @Override
   public void createDependency(GradleDependencyBuilder builder)
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
                  GradleDependencyConfiguration.configByName(builder.getConfiguration()),
                  builder.getConfiguration()));
      }
   }

   @Override
   public void createManagedDependency(GradleDependencyBuilder builder)
   {
      script = GradleSourceUtil.insertManagedDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());
      this.managedDependencies.add(new GradleDependencyImpl(
               builder.getGroup(), builder.getName(), builder.getVersion(),
               GradleDependencyConfiguration.configByName(builder.getConfiguration()),
               builder.getConfiguration()));
   }

   @Override
   public void createProfile(String name)
   {
      profiles.add(new GradleProfileImpl(name, new GradleModelImpl()));
   }

   @Override
   public void applyPlugin(String name)
   {
      script = GradleSourceUtil.insertPlugin(script, name);
      GradlePluginType type = GradlePluginType.typeByClazz(name);
      this.plugins.add(new GradlePluginImpl(name, type));
   }

   @Override
   public void createRepository(GradleRepositoryBuilder builder)
   {
      script = GradleSourceUtil.insertRepository(script, builder.getName(), builder.getUrl());
      this.repositories.add(new GradleRepositoryImpl(builder.getName(), builder.getUrl()));
   }

   @Override
   public void setProperty(String name, String value)
   {
      script = GradleSourceUtil.setProperty(script, "ext." + name, value);
      this.properties.put(name, value);
   }

   @Override
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

   @Override
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

   @Override
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

   @Override
   public void removeAppliedPlugin(String name) throws UnremovableElementException
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

   @Override
   public void removeRepository(GradleRepositoryBuilder builder) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeRepository(script, builder.getName(), builder.getUrl());

      for (GradleRepository repo : this.repositories)
      {
         if (repo.getUrl().equals(builder.getUrl()))
         {
            this.repositories.remove(repo);
            return;
         }
      }
   }

   @Override
   public void removeProperty(String name) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeProperty(script, "ext." + name);
      this.properties.remove(name);
   }
}
