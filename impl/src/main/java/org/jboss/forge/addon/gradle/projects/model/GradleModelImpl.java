/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.addon.resource.FileResource;

/**
 * @author Adam Wyłuda
 */
public class GradleModelImpl implements GradleModel
{
   private final FileResource<?> gradleResource;
   
   private String projectName;
   private String version;
   private List<GradleTask> tasks;
   private List<GradleDependency> dependencies;
   private List<GradleDependency> managedDependencies;
   private List<GradleProfile> profiles;
   private List<GradlePlugin> plugins;
   private List<GradleRepository> repositories;
   
   public GradleModelImpl(FileResource<?> gradleResource, String projectName, String version, List<GradleTask> tasks,
            List<GradleDependency> dependencies, List<GradleDependency> managedDependencies,
            List<GradleProfile> profiles, List<GradlePlugin> plugins, List<GradleRepository> repositories)
   {
      this.gradleResource = gradleResource;
      this.projectName = projectName;
      this.version = version;
      this.tasks = tasks;
      this.dependencies = dependencies;
      this.managedDependencies = managedDependencies;
      this.profiles = profiles;
      this.plugins = plugins;
      this.repositories = repositories;
   }

   @Override
   public String getProjectName()
   {
      return projectName;
   }

   @Override
   public String getVersion()
   {
      return version;
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
   public boolean hasTask(String name)
   {
      for (GradleTask task : tasks) {
         if (task.getName().equals(name)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasDependency(GradleDependencyBuilder builder)
   {
      for (GradleDependency dep : dependencies) {
         if (builder.equalsToDep(dep)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasManagedDependency(GradleDependencyBuilder builder)
   {
      for (GradleDependency dep : managedDependencies) {
         if (builder.equalsToDep(dep)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasProfile(String name)
   {
      for (GradleProfile profile : profiles) {
         if (profile.getName().equals(name)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasPlugin(String clazz)
   {
      for (GradlePlugin plugin : plugins) {
         if (plugin.getClazz().equals(clazz)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasRepository(String url)
   {
      for (GradleRepository repo : repositories) {
         if (repo.getURL().equals(url)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public void setProjectName(String name) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void setVersion(String version) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void createTask(GradleTaskBuilder builder)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void createDependency(GradleDependencyBuilder builder)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void createManagedDependency(GradleDependencyBuilder builder)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void createProfile(String name)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void applyPlugin(String name)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void createGradleRepository(GradleRepositoryBuilder builder)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void removeDependency(GradleDependencyBuilder builder) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void removeManagedDependency(GradleDependencyBuilder builder) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void removeProfile(String name)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void removeAppliedPlugin(String name) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void removeGradleRepository(GradleRepositoryBuilder builder) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }
}
