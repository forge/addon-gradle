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

   GradleDirectModelBuilder()
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

      builder.tasks = GradleTaskBuilder.deepCopy(model.getTasks());
      builder.dependencies = GradleDependencyBuilder.deepCopy(model.getDependencies());
      builder.managedDependencies = GradleDependencyBuilder.deepCopy(model.getManagedDependencies());
      builder.profiles = GradleProfileBuilder.deepCopy(model.getProfiles());
      builder.plugins = GradlePluginBuilder.deepCopy(model.getPlugins());
      builder.repositories = GradleRepositoryBuilder.deepCopy(model.getRepositories());
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

   public List<GradleTask> getTasks()
   {
      return Collections.unmodifiableList(tasks);
   }

   public GradleDirectModelBuilder addTask(GradleTask task)
   {
      tasks.add(task);
      return this;
   }

   @Override
   public List<GradleDependency> getDependencies()
   {
      return Collections.unmodifiableList(dependencies);
   }

   @Override
   public boolean hasDependency(GradleDependency dep)
   {
      return listContainsDependency(dependencies, dep);
   }

   public GradleDirectModelBuilder addDependency(GradleDependency dep)
   {
      dependencies.add(dep);
      return this;
   }

   public GradleDirectModelBuilder removeDependency(GradleDependency dep)
   {
      dependencies.remove(dep);
      return this;
   }

   @Override
   public List<GradleDependency> getManagedDependencies()
   {
      return Collections.unmodifiableList(managedDependencies);
   }

   @Override
   public boolean hasManagedDependency(GradleDependency dep)
   {
      return listContainsDependency(managedDependencies, dep);
   }

   public GradleDirectModelBuilder addManagedDependency(GradleDependency dep)
   {
      managedDependencies.add(dep);
      return this;
   }

   public GradleDirectModelBuilder removeManagedDependency(GradleDependency dep)
   {
      managedDependencies.remove(dep);
      return this;
   }

   @Override
   public List<GradleProfile> getProfiles()
   {
      return Collections.unmodifiableList(profiles);
   }

   @Override
   public boolean hasProfile(GradleProfile profile)
   {
      return listContainsProfile(profiles, profile);
   }

   public GradleDirectModelBuilder addProfile(GradleProfile profile)
   {
      profiles.add(profile);
      return this;
   }

   public GradleDirectModelBuilder removeProfile(GradleProfile profile)
   {
      profiles.remove(profile);
      return this;
   }

   @Override
   public List<GradlePlugin> getPlugins()
   {
      return Collections.unmodifiableList(plugins);
   }
   
   @Override
   public boolean hasPlugin(GradlePlugin plugin)
   {
      return listContainsPlugin(plugins, plugin);
   }

   public GradleDirectModelBuilder addPlugin(GradlePlugin plugin)
   {
      plugins.add(plugin);
      return this;
   }

   public GradleDirectModelBuilder removePlugin(GradlePlugin plugin)
   {
      plugins.remove(plugin);
      return this;
   }

   @Override
   public List<GradleRepository> getRepositories()
   {
      return Collections.unmodifiableList(repositories);
   }
   
   @Override
   public boolean hasRepository(GradleRepository repo)
   {
      return listContainsRepository(repositories, repo);
   }

   public GradleDirectModelBuilder addRepository(GradleRepository repo)
   {
      repositories.add(repo);
      return this;
   }

   public GradleDirectModelBuilder removeRepository(GradleRepository repo)
   {
      repositories.remove(repo);
      return this;
   }

   @Override
   public Map<String, String> getProperties()
   {
      return Collections.unmodifiableMap(properties);
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
   
   boolean listContainsDependency(List<GradleDependency> deps, GradleDependency dep)
   {
      GradleDependencyBuilder depBuilder = GradleDependencyBuilder.create(dep);
      for (GradleDependency gradleDep : deps)
      {
         if (depBuilder.equalsToDependency(gradleDep))
         {
            return true;
         }
      }
      return false;
   }
   
   boolean listContainsProfile(List<GradleProfile> profiles, GradleProfile profile)
   {
      for (GradleProfile gradleProfile : profiles)
      {
         if (gradleProfile.getName().equals(profile.getName()))
         {
            return true;
         }
      }
      return false;
   }
   
   boolean listContainsPlugin(List<GradlePlugin> plugins, GradlePlugin plugin)
   {
      for (GradlePlugin gradlePlugin : plugins)
      {
         if (plugin.getClazz().equals(gradlePlugin.getClazz()))
         {
            return true;
         }
      }
      return false;
   }
   
   boolean listContainsRepository(List<GradleRepository> repos, GradleRepository repo)
   {
      for (GradleRepository gradleRepo : repos)
      {
         if (gradleRepo.getUrl().equals(repo.getUrl()))
         {
            return true;
         }
      }
      return false;
   }
}
