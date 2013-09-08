/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adam Wyłuda
 */
public class GradleDirectModelBuilder implements GradleDirectModel
{
   private String group = "";
   private String name = "";
   private String version = "";
   private String packaging = "";
   private String archiveName = "";
   private List<GradleTask> tasks = new ArrayList<GradleTask>();
   private List<GradleDependency> dependencies = new ArrayList<GradleDependency>();
   private List<GradleDependency> managedDependencies = new ArrayList<GradleDependency>();
   private List<GradleProfile> profiles = new ArrayList<GradleProfile>();
   private List<GradlePlugin> plugins = new ArrayList<GradlePlugin>();
   private List<GradleRepository> repositories = new ArrayList<GradleRepository>();
   private Map<String, String> properties = new HashMap<String, String>();
   
   private GradleDirectModelBuilder()
   {
   }
   
   public static GradleDirectModelBuilder create()
   {
      return new GradleDirectModelBuilder();
   }
   
   public static GradleDirectModelBuilder create(GradleDirectModel model)
   {
      GradleDirectModelBuilder builder = new GradleDirectModelBuilder();
      
      builder.group = model.getGroup();
      builder.name = model.getName();
      builder.version = model.getVersion();
      builder.packaging = model.getPackaging();
      builder.archiveName = model.getArchiveName();
      
      // TODO Perform a deep copy of the lists
      builder.tasks = new ArrayList<GradleTask>(model.getTasks());
      builder.dependencies = new ArrayList<GradleDependency>(model.getDependencies());
      builder.managedDependencies = new ArrayList<GradleDependency>(model.getManagedDependencies());
      builder.profiles = new ArrayList<GradleProfile>(model.getProfiles());
      builder.plugins = new ArrayList<GradlePlugin>(model.getPlugins());
      builder.repositories = new ArrayList<GradleRepository>(model.getRepositories());
      builder.properties = new HashMap<String, String>(model.getProperties());
      
      return builder;
   }
   
   @Override
   public String getGroup()
   {
      return group;
   }

   public GradleDirectModelBuilder setGroup(String group)
   {
      this.group = group;
      return this;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public GradleDirectModelBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   @Override
   public String getVersion()
   {
      return version;
   }

   public GradleDirectModelBuilder setVersion(String version)
   {
      this.version = version;
      return this;
   }

   @Override
   public String getPackaging()
   {
      return packaging;
   }

   public GradleDirectModelBuilder setPackaging(String packaging)
   {
      this.packaging = packaging;
      return this;
   }

   @Override
   public String getArchiveName()
   {
      return archiveName;
   }

   public GradleDirectModelBuilder setArchiveName(String archiveName)
   {
      this.archiveName = archiveName;
      return this;
   }

   @Override
   public List<GradleTask> getTasks()
   {
      return Collections.unmodifiableList(tasks);
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

   public GradleDirectModelBuilder addTask(GradleTaskBuilder builder)
   {
      // TODO Complete this after merging GradleTaskImpl with GradleTaskBuilder
      return this;
   }

   @Override
   public List<GradleDependency> getDependencies()
   {
      return Collections.unmodifiableList(dependencies);
   }

   public GradleDirectModelBuilder addDependency(GradleDependencyBuilder builder)
   {
      // TODO Complete this after merging GradleDependencyImpl with GradleDependencyBuilder
      return this;
   }

   public GradleDirectModelBuilder removeDependency(GradleDependencyBuilder builder)
   {
      // TODO Complete this after merging GradleDependencyImpl with GradleDependencyBuilder
      return this;
   }

   @Override
   public List<GradleDependency> getManagedDependencies()
   {
      return Collections.unmodifiableList(managedDependencies);
   }

   public GradleDirectModelBuilder addManagedDependency(GradleDependencyBuilder builder)
   {
      // TODO Complete this after merging GradleDependencyImpl with GradleDependencyBuilder
      return this;
   }

   public GradleDirectModelBuilder removeManagedDependency(GradleDependencyBuilder builder)
   {
      // TODO Complete this after merging GradleDependencyImpl with GradleDependencyBuilder
      return this;
   }

   @Override
   public List<GradleProfile> getProfiles()
   {
      return Collections.unmodifiableList(profiles);
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

   public GradleDirectModelBuilder addProfile(String name)
   {
      // TODO Complete this after creating GradleProfileBuilder
      return this;
   }

   public GradleDirectModelBuilder removeProfile(String name)
   {
      for (GradleProfile profile : profiles)
      {
         if (profile.getName().equals(name))
         {
            profiles.remove(profile);
            break;
         }
      }
      return this;
   }

   @Override
   public List<GradlePlugin> getPlugins()
   {
      return Collections.unmodifiableList(plugins);
   }

   @Override
   public boolean hasPlugin(String clazz)
   {
      for (GradlePlugin plugin : plugins)
      {
         if (plugin.getClazz().equals(clazz) || plugin.getType().getShortName().equals(clazz))
         {
            return true;
         }
      }
      return false;
   }

   public GradleDirectModelBuilder addPlugin(String name)
   {
      // TODO Complete this after creating GradlePluginBuilder
      return this;
   }

   public GradleDirectModelBuilder removePlugin(String name)
   {
      for (GradlePlugin plugin : plugins)
      {
         if (plugin.getClazz().equals(name) || plugin.getType().getShortName().equals(name))
         {
            plugins.remove(plugin);
            break;
         }
      }
      return this;
   }

   @Override
   public List<GradleRepository> getRepositories()
   {
      return Collections.unmodifiableList(repositories);
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

   public GradleDirectModelBuilder addRepository(String url)
   {
      // TODO Complete after creating GradleRepositoryBuilder
      return this;
   }

   public GradleDirectModelBuilder removeRepository(String url)
   {
      for (GradleRepository repo : repositories)
      {
         if (repo.getUrl().equals(url))
         {
            repositories.remove(repo);
            break;
         }
      }
      return this;
   }

   @Override
   public Map<String, String> getProperties()
   {
      return Collections.unmodifiableMap(properties);
   }

   @Override
   public boolean hasProperty(String key)
   {
      // TODO Remove this?
      return properties.containsKey(key);
   }

   public GradleDirectModelBuilder setProperty(String name, String value)
   {
      properties.put(name, value);
      return this;
   }

   public GradleDirectModelBuilder removeProperty(String name)
   {
      properties.remove(name);
      return this;
   }
}
