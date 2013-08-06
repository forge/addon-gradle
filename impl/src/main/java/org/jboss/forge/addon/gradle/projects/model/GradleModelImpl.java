/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;

/**
 * @author Adam Wyłuda
 */
public class GradleModelImpl implements GradleModel
{
   private String script;

   private String group;
   private String name;
   private String version;
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
      this.name = "";
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
   }

   public GradleModelImpl(String script, String group, String name, String version,
            String packaging, String archivePath, List<GradleTask> tasks,
            List<GradleDependency> dependencies, List<GradleDependency> managedDependencies,
            List<GradleProfile> profiles, List<GradlePlugin> plugins, List<GradleRepository> repositories,
            List<GradleSourceSet> sourceSets, Map<String, String> properties)
   {
      this.script = script;
      this.group = group;
      this.name = name;
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
      this.name = original.getName();
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
         GradleProfileImpl newProfile = new GradleProfileImpl(profile.getName(),
                  new GradleModelImpl(profile.getModel()));
         newProfile.setProfileResource(profile.getProfileResource());
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
   public String getPackaging()
   {
      return packaging;
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
         if (builder.equalsToDep(dep))
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
         if (builder.equalsToDep(dep))
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
         if (repo.getURL().equals(url))
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
      // TODO Update model
   }

   @Override
   public void setName(String name) throws UnremovableElementException
   {
      // TODO Set properties by GradleSourceUtil

      // For this case we also need to add Gradle project path to forgeOutput
      // and path to settings.gradle file
      // (this is necessary to modify project name in Gradle)
   }

   @Override
   public void setVersion(String version) throws UnremovableElementException
   {
      script = GradleSourceUtil.setProperty(script, "version", version);
      // TODO Update model
   }

   @Override
   public void setPackaging(String packaging)
   {
      // TODO Add packaging info to GradlePluginType to figure out which plugin must be applied to set given packaging
   }

   @Override
   public void setArchiveName(String archiveName)
   {
      // TODO Set archive name by GradleSourceUtil
   }

   @Override
   public void createTask(GradleTaskBuilder builder)
   {
      script = GradleSourceUtil.insertTask(script,
               builder.getName(), builder.getDependsOn(), builder.getType(), builder.getCode());
      // TODO Update model
   }

   @Override
   public void createDependency(GradleDependencyBuilder builder)
   {
      script = GradleSourceUtil.insertDependency(script, builder.getGroup(), builder.getName(), builder.getVersion(),
               builder.getConfiguration());
      // TODO Update model
   }

   @Override
   public void createManagedDependency(GradleDependencyBuilder builder)
   {
      script = GradleSourceUtil.insertManagedDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());
      // TODO Update model
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
      // TODO Update model
   }

   @Override
   public void createRepository(GradleRepositoryBuilder builder)
   {
      script = GradleSourceUtil.insertRepository(script, builder.getName(), builder.getUrl());
      // TODO Update model
   }

   @Override
   public void setProperty(String name, String value)
   {
      script = GradleSourceUtil.setProperty(script, "ext." + name, value);
      // TODO Update model
   }

   @Override
   public void removeDependency(GradleDependencyBuilder builder) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());
      // TODO Update model
   }

   @Override
   public void removeManagedDependency(GradleDependencyBuilder builder) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeManagedDependency(script,
               builder.getGroup(), builder.getName(), builder.getVersion(), builder.getConfiguration());
      // TODO Update model
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
         try {
            script = GradleSourceUtil.removePlugin(script, type.getClazz());
         } catch (UnremovableElementException exception) {
            // This could be a case when plugin was declared with it's short name
            script = GradleSourceUtil.removePlugin(script, type.getShortName());
         }
      }
      else
      {
         script = GradleSourceUtil.removePlugin(script, name);
      }
      // TODO Update model
   }

   @Override
   public void removeRepository(GradleRepositoryBuilder builder) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeRepository(script, builder.getName(), builder.getUrl());
      // TODO Update model
   }

   @Override
   public void removeProperty(String name) throws UnremovableElementException
   {
      script = GradleSourceUtil.removeProperty(script, "ext." + name);
      // TODO Update model
   }
}
