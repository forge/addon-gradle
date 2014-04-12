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
 * Default implementation of the {@link GradleModel}.
 * 
 * @author Adam Wyłuda
 */
public class GradleModelBuilder implements GradleModel
{
   private String group = "";
   private String name = "";
   private String version = "";
   private String packaging = "";
   private String archiveName = "";
   private String projectPath = "";
   private String rootProjectPath = "";
   private String archivePath = "";
   private String sourceCompatibility = "";
   private String targetCompatibility = "";
   private List<GradleTask> tasks = new ArrayList<GradleTask>();
   private List<GradleTask> effectiveTasks = new ArrayList<GradleTask>();
   private List<GradleDependency> dependencies = new ArrayList<GradleDependency>();
   private List<GradleDependency> effectiveDependencies = new ArrayList<GradleDependency>();
   private List<GradleDependency> managedDependencies = new ArrayList<GradleDependency>();
   private List<GradleDependency> effectiveManagedDependencies = new ArrayList<GradleDependency>();
   private List<GradleProfile> profiles = new ArrayList<GradleProfile>();
   private List<GradlePlugin> plugins = new ArrayList<GradlePlugin>();
   private List<GradlePlugin> effectivePlugins = new ArrayList<GradlePlugin>();
   private List<GradleRepository> repositories = new ArrayList<GradleRepository>();
   private List<GradleRepository> effectiveRepositories = new ArrayList<GradleRepository>();
   private Map<String, String> properties = new HashMap<String, String>();
   private Map<String, String> effectiveProperties = new HashMap<String, String>();
   private List<GradleSourceSet> effectiveSourceSets = new ArrayList<GradleSourceSet>();

   GradleModelBuilder()
   {
   }

   public static GradleModelBuilder create()
   {
      return new GradleModelBuilder();
   }

   /**
    * Creates a copy of given model.
    */
   public static GradleModelBuilder create(GradleModel model)
   {
      GradleModelBuilder builder = new GradleModelBuilder();

      builder.group = model.getGroup();
      builder.name = model.getName();
      builder.version = model.getVersion();
      builder.packaging = model.getPackaging();
      builder.archiveName = model.getArchiveName();
      builder.projectPath = model.getProjectPath();
      builder.rootProjectPath = model.getRootProjectPath();
      builder.archivePath = model.getArchivePath();
      builder.sourceCompatibility = model.getSourceCompatibility();
      builder.targetCompatibility = model.getTargetCompatiblity();
      builder.tasks = GradleTaskBuilder.deepCopy(model.getTasks());
      builder.effectiveTasks = GradleTaskBuilder.deepCopy(model.getEffectiveTasks());
      builder.dependencies = GradleDependencyBuilder.deepCopy(model.getDependencies());
      builder.effectiveDependencies = GradleDependencyBuilder.deepCopy(model.getEffectiveDependencies());
      builder.managedDependencies = GradleDependencyBuilder.deepCopy(model.getManagedDependencies());
      builder.effectiveManagedDependencies = GradleDependencyBuilder.deepCopy(model.getEffectiveManagedDependencies());
      builder.profiles = GradleProfileBuilder.deepCopy(model.getProfiles());
      builder.plugins = GradlePluginBuilder.deepCopy(model.getPlugins());
      builder.effectivePlugins = GradlePluginBuilder.deepCopy(model.getEffectivePlugins());
      builder.repositories = GradleRepositoryBuilder.deepCopy(model.getRepositories());
      builder.effectiveRepositories = GradleRepositoryBuilder.deepCopy(model.getEffectiveRepositories());
      builder.properties = new HashMap<String, String>(model.getProperties());
      builder.effectiveProperties = new HashMap<String, String>(model.getEffectiveProperties());
      builder.effectiveSourceSets = GradleSourceSetBuilder.deepCopy(model.getEffectiveSourceSets());

      return builder;
   }

   @Override
   public String getGroup()
   {
      return group;
   }

   public GradleModelBuilder setGroup(String group)
   {
      this.group = group;
      return this;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public GradleModelBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   @Override
   public String getVersion()
   {
      return version;
   }

   public GradleModelBuilder setVersion(String version)
   {
      this.version = version;
      return this;
   }

   @Override
   public String getPackaging()
   {
      return packaging;
   }

   public GradleModelBuilder setPackaging(String packaging)
   {
      this.packaging = packaging;
      return this;
   }

   @Override
   public String getArchiveName()
   {
      return archiveName;
   }

   public GradleModelBuilder setArchiveName(String archiveName)
   {
      this.archiveName = archiveName;
      return this;
   }

   @Override
   public String getProjectPath()
   {
      return projectPath;
   }

   public GradleModelBuilder setProjectPath(String projectPath)
   {
      this.projectPath = projectPath;
      return this;
   }

   @Override
   public String getRootProjectPath()
   {
      return rootProjectPath;
   }

   public GradleModelBuilder setRootProjectPath(String rootProjectPath)
   {
      this.rootProjectPath = rootProjectPath;
      return this;
   }

   @Override
   public String getArchivePath()
   {
      return archivePath;
   }

   public GradleModelBuilder setArchivePath(String archivePath)
   {
      this.archivePath = archivePath;
      return this;
   }

   @Override
   public String getSourceCompatibility()
   {
      return sourceCompatibility;
   }

   public GradleModelBuilder setSourceCompatibility(String sourceCompatibility)
   {
      this.sourceCompatibility = sourceCompatibility;
      return this;
   }

   @Override
   public String getTargetCompatiblity()
   {
      return targetCompatibility;
   }

   public GradleModelBuilder setTargetCompatibility(String targetCompatibility)
   {
      this.targetCompatibility = targetCompatibility;
      return this;
   }

   @Override
   public List<GradleTask> getTasks()
   {
      return Collections.unmodifiableList(tasks);
   }

   public GradleModelBuilder setTasks(List<GradleTask> tasks)
   {
      this.tasks = tasks;
      return this;
   }

   public GradleModelBuilder addTask(GradleTask task)
   {
      tasks.add(task);
      return this;
   }

   @Override
   public List<GradleTask> getEffectiveTasks()
   {
      return effectiveTasks;
   }

   public GradleModelBuilder setEffectiveTasks(List<GradleTask> tasks)
   {
      this.effectiveTasks = tasks;
      return this;
   }

   @Override
   public boolean hasEffectiveTask(GradleTask task)
   {
      return taskWhichEqualsTo(effectiveTasks, task) != null;
   }

   @Override
   public List<GradleDependency> getDependencies()
   {
      return Collections.unmodifiableList(dependencies);
   }

   public GradleModelBuilder setDependencies(List<GradleDependency> deps)
   {
      this.dependencies = deps;
      return this;
   }

   @Override
   public boolean hasDependency(GradleDependency dep)
   {
      return depWhichEqualsTo(dependencies, dep) != null;
   }

   public GradleModelBuilder addDependency(GradleDependency dep)
   {
      dependencies.add(dep);
      return this;
   }

   public GradleModelBuilder removeDependency(GradleDependency dep)
   {
      dependencies.remove(depWhichEqualsTo(dependencies, dep));
      return this;
   }

   @Override
   public List<GradleDependency> getEffectiveDependencies()
   {
      return effectiveDependencies;
   }

   public GradleModelBuilder setEffectiveDependencies(List<GradleDependency> deps)
   {
      this.effectiveDependencies = deps;
      return this;
   }

   @Override
   public boolean hasEffectiveDependency(GradleDependency dependency)
   {
      return depWhichEqualsTo(effectiveDependencies, dependency) != null;
   }

   @Override
   public List<GradleDependency> getManagedDependencies()
   {
      return Collections.unmodifiableList(managedDependencies);
   }

   public GradleModelBuilder setManagedDependencies(List<GradleDependency> deps)
   {
      this.managedDependencies = deps;
      return this;
   }

   @Override
   public boolean hasManagedDependency(GradleDependency dep)
   {
      return depWhichEqualsTo(managedDependencies, dep) != null;
   }

   public GradleModelBuilder addManagedDependency(GradleDependency dep)
   {
      managedDependencies.add(dep);
      return this;
   }

   public GradleModelBuilder removeManagedDependency(GradleDependency dep)
   {
      managedDependencies.remove(depWhichEqualsTo(managedDependencies, dep));
      return this;
   }

   @Override
   public List<GradleDependency> getEffectiveManagedDependencies()
   {
      return effectiveManagedDependencies;
   }

   public GradleModelBuilder setEffectiveManagedDependencies(List<GradleDependency> deps)
   {
      this.effectiveManagedDependencies = deps;
      return this;
   }

   @Override
   public boolean hasEffectiveManagedDependency(GradleDependency dependency)
   {
      return depWhichEqualsTo(effectiveManagedDependencies, dependency) != null;
   }

   @Override
   public List<GradleProfile> getProfiles()
   {
      return Collections.unmodifiableList(profiles);
   }

   public GradleModelBuilder setProfiles(List<GradleProfile> profiles)
   {
      this.profiles = profiles;
      return this;
   }

   @Override
   public boolean hasProfile(GradleProfile profile)
   {
      return profileWhichEqualsTo(profiles, profile) != null;
   }

   public GradleModelBuilder addProfile(GradleProfile profile)
   {
      profiles.add(profile);
      return this;
   }

   public GradleModelBuilder removeProfile(GradleProfile profile)
   {
      profiles.remove(profileWhichEqualsTo(profiles, profile));
      return this;
   }

   @Override
   public List<GradlePlugin> getPlugins()
   {
      return Collections.unmodifiableList(plugins);
   }

   public GradleModelBuilder setPlugins(List<GradlePlugin> plugins)
   {
      this.plugins = plugins;
      return this;
   }

   @Override
   public boolean hasPlugin(GradlePlugin plugin)
   {
      return pluginWhichEqualsTo(plugins, plugin) != null;
   }

   public GradleModelBuilder addPlugin(GradlePlugin plugin)
   {
      plugins.add(plugin);
      return this;
   }

   public GradleModelBuilder removePlugin(GradlePlugin plugin)
   {
      plugins.remove(pluginWhichEqualsTo(plugins, plugin));
      return this;
   }

   @Override
   public List<GradlePlugin> getEffectivePlugins()
   {
      return effectivePlugins;
   }

   public GradleModelBuilder setEffectivePlugins(List<GradlePlugin> plugins)
   {
      this.effectivePlugins = plugins;
      return this;
   }

   @Override
   public boolean hasEffectivePlugin(GradlePlugin plugin)
   {
      return pluginWhichEqualsTo(effectivePlugins, plugin) != null;
   }

   @Override
   public List<GradleRepository> getRepositories()
   {
      return Collections.unmodifiableList(repositories);
   }

   public GradleModelBuilder setRepositories(List<GradleRepository> repos)
   {
      this.repositories = repos;
      return this;
   }

   @Override
   public boolean hasRepository(GradleRepository repo)
   {
      return repoWhichEqualsTo(repositories, repo) != null;
   }

   public GradleModelBuilder addRepository(GradleRepository repo)
   {
      repositories.add(repo);
      return this;
   }

   public GradleModelBuilder removeRepository(GradleRepository repo)
   {
      repositories.remove(repoWhichEqualsTo(repositories, repo));
      return this;
   }

   @Override
   public List<GradleRepository> getEffectiveRepositories()
   {
      return effectiveRepositories;
   }

   public GradleModelBuilder setEffectiveRepositories(List<GradleRepository> repos)
   {
      this.effectiveRepositories = repos;
      return this;
   }

   @Override
   public boolean hasEffectiveRepository(GradleRepository repo)
   {
      return repoWhichEqualsTo(effectiveRepositories, repo) != null;
   }

   @Override
   public Map<String, String> getProperties()
   {
      return Collections.unmodifiableMap(properties);
   }

   public GradleModelBuilder setProperties(Map<String, String> props)
   {
      this.properties = props;
      return this;
   }

   public GradleModelBuilder setProperty(String name, String value)
   {
      properties.put(name, value);
      return this;
   }

   public GradleModelBuilder removeProperty(String name)
   {
      properties.remove(name);
      return this;
   }

   @Override
   public Map<String, String> getEffectiveProperties()
   {
      return effectiveProperties;
   }

   public GradleModelBuilder setEffectiveProperties(Map<String, String> properties)
   {
      this.effectiveProperties = properties;
      return this;
   }

   @Override
   public List<GradleSourceSet> getEffectiveSourceSets()
   {
      return effectiveSourceSets;
   }

   public GradleModelBuilder setEffectiveSourceSets(List<GradleSourceSet> sourceSets)
   {
      this.effectiveSourceSets = sourceSets;
      return this;
   }

   private GradleDependency depWhichEqualsTo(List<GradleDependency> deps, GradleDependency dependency)
   {
      GradleDependencyBuilder builder = GradleDependencyBuilder.create(dependency);
      for (GradleDependency dep : deps)
      {
         if (builder.equalsToDependency(dep))
         {
            return dep;
         }
      }
      return null;
   }

   private GradleProfile profileWhichEqualsTo(List<GradleProfile> profiles, GradleProfile profile)
   {
      for (GradleProfile gradleProfile : profiles)
      {
         if (gradleProfile.getName().equals(profile.getName()))
         {
            return gradleProfile;
         }
      }
      return null;
   }

   private GradlePlugin pluginWhichEqualsTo(List<GradlePlugin> plugins, GradlePlugin plugin)
   {
      for (GradlePlugin gradlePlugin : plugins)
      {
         if (plugin.getClazz().equals(gradlePlugin.getClazz())
                  || plugin.getType().getShortName().equals(gradlePlugin.getClazz()))
         {
            return gradlePlugin;
         }
      }
      return null;
   }

   private GradleRepository repoWhichEqualsTo(List<GradleRepository> repos, GradleRepository repo)
   {
      for (GradleRepository gradleRepo : repos)
      {
         if (gradleRepo.getUrl().equals(repo.getUrl()))
         {
            return gradleRepo;
         }
      }
      return null;
   }

   private GradleTask taskWhichEqualsTo(List<GradleTask> tasks, GradleTask task)
   {
      for (GradleTask gradleTask : tasks)
      {
         if (gradleTask.getName().equals(task.getName()))
         {
            return gradleTask;
         }
      }
      return null;
   }

   @Override
   public String toString()
   {
      return "GradleModelBuilder [group=" + group + ", name=" + name + ", version=" + version + ", packaging="
               + packaging + ", archiveName=" + archiveName + ", projectPath=" + projectPath + ", rootProjectPath="
               + rootProjectPath + ", archivePath=" + archivePath + ", tasks=" + tasks + ", effectiveTasks="
               + effectiveTasks + ", dependencies=" + dependencies + ", effectiveDependencies=" + effectiveDependencies
               + ", managedDependencies=" + managedDependencies + ", effectiveManagedDependencies="
               + effectiveManagedDependencies + ", profiles=" + profiles + ", plugins=" + plugins
               + ", effectivePlugins=" + effectivePlugins + ", repositories=" + repositories
               + ", effectiveRepositories=" + effectiveRepositories + ", properties=" + properties
               + ", effectiveProperties=" + effectiveProperties + ", effectiveSourceSets=" + effectiveSourceSets + "]";
   }
}
