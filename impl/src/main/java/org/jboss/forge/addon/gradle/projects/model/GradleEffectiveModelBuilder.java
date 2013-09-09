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
import org.gradle.jarjar.com.google.common.collect.Maps;

/**
 * @author Adam Wyłuda
 */
public class GradleEffectiveModelBuilder extends GradleDirectModelBuilder implements GradleEffectiveModel
{
   private String projectPath = "";
   private String rootProjectPath = "";
   private String archivePath = "";
   private List<GradleTask> tasks = Lists.newArrayList();
   private List<GradleDependency> dependencies = Lists.newArrayList();
   private List<GradleDependency> managedDependencies = Lists.newArrayList();
   private List<GradlePlugin> plugins = Lists.newArrayList();
   private List<GradleRepository> repositories = Lists.newArrayList();
   private List<GradleSourceSet> sourceSets = Lists.newArrayList();
   private Map<String, String> properties = Maps.newHashMap();
   
   private GradleEffectiveModelBuilder()
   {
   }
   
   public static GradleEffectiveModelBuilder create()
   {
      return new GradleEffectiveModelBuilder();
   }

   @Override
   public String getProjectPath()
   {
      return projectPath;
   }
   
   public GradleEffectiveModelBuilder setProjectPath(String projectPath)
   {
      this.projectPath = projectPath;
      return this;
   }

   @Override
   public String getRootProjectPath()
   {
      return rootProjectPath;
   }
   
   public GradleEffectiveModelBuilder setRootProjectPath(String rootProjectPath)
   {
      this.rootProjectPath = rootProjectPath;
      return this;
   }

   @Override
   public String getArchivePath()
   {
      return archivePath;
   }
   
   public GradleEffectiveModelBuilder setArchivePath(String archivePath)
   {
      this.archivePath = archivePath;
      return this;
   }

   @Override
   public List<GradleTask> getEffectiveTasks()
   {
      return tasks;
   }
   
   public GradleEffectiveModelBuilder setEffectiveTasks(List<GradleTask> tasks)
   {
      this.tasks = tasks;
      return this;
   }

   @Override
   public boolean hasEffectiveTask(GradleTask task)
   {
      return listContainsTask(tasks, task);
   }

   @Override
   public List<GradleDependency> getEffectiveDependencies()
   {
      return dependencies;
   }
   
   public GradleEffectiveModelBuilder setEffectiveDependencies(List<GradleDependency> deps)
   {
      this.dependencies = deps;
      return this;
   }

   @Override
   public boolean hasEffectiveDependency(GradleDependency dependency)
   {
      return listContainsDependency(dependencies, dependency);
   }

   @Override
   public List<GradleDependency> getEffectiveManagedDependencies()
   {
      return managedDependencies;
   }
   
   public GradleEffectiveModelBuilder setEffectiveManagedDependencies(List<GradleDependency> deps)
   {
      this.managedDependencies = deps;
      return this;
   }

   @Override
   public boolean hasEffectiveManagedDependency(GradleDependency dependency)
   {
      return listContainsDependency(managedDependencies, dependency);
   }

   @Override
   public List<GradlePlugin> getEffectivePlugins()
   {
      return plugins;
   }
   
   public GradleEffectiveModelBuilder setEffectivePlugins(List<GradlePlugin> plugins)
   {
      this.plugins = plugins;
      return this;
   }

   @Override
   public boolean hasEffectivePlugin(GradlePlugin plugin)
   {
      return listContainsPlugin(plugins, plugin);
   }

   @Override
   public List<GradleRepository> getEffectiveRepositories()
   {
      return repositories;
   }
   
   public GradleEffectiveModelBuilder setEffectiveRepositories(List<GradleRepository> repos)
   {
      this.repositories = repos;
      return this;
   }

   @Override
   public boolean hasEffectiveRepository(GradleRepository repo)
   {
      return listContainsRepository(repositories, repo);
   }

   @Override
   public List<GradleSourceSet> getEffectiveSourceSets()
   {
      return sourceSets;
   }
   
   public GradleEffectiveModelBuilder setEffectiveSourceSets(List<GradleSourceSet> sourceSets)
   {
      this.sourceSets = sourceSets;
      return this;
   }

   @Override
   public Map<String, String> getEffectiveProperties()
   {
      return properties;
   }
   
   public GradleEffectiveModelBuilder setEffectiveProperties(Map<String, String> properties)
   {
      this.properties = properties;
      return this;
   }
   
   boolean listContainsTask(List<GradleTask> tasks, GradleTask task)
   {
      for (GradleTask gradleTask : tasks)
      {
         if (gradleTask.getName().equals(task.getName()))
         {
            return true;
         }
      }
      return false;
   }
}
