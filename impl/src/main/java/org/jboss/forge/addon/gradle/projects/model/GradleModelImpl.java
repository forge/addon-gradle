/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

import org.jboss.forge.addon.gradle.parser.GradleUtil;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.addon.resource.FileResource;

/**
 * @author Adam Wyłuda
 */
public class GradleModelImpl implements GradleModel
{
   private final FileResource<?> gradleResource;

   private String script;

   private String name;
   private String group;
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

   public GradleModelImpl(FileResource<?> gradleResource, String script, String projectName, String version,
            String packaging, String archivePath, List<GradleTask> tasks,
            List<GradleDependency> dependencies, List<GradleDependency> managedDependencies,
            List<GradleProfile> profiles, List<GradlePlugin> plugins, List<GradleRepository> repositories,
            List<GradleSourceSet> sourceSets)
   {
      this.script = script;
      this.gradleResource = gradleResource;
      this.name = projectName;
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
   public void setGroup(String group) throws UnremovableElementException
   {

   }

   @Override
   public void setName(String name) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void setVersion(String version) throws UnremovableElementException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void setPackaging(String packaging)
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void setArchiveName(String archiveName)
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
      script = GradleUtil.insertDependency(script, builder.getGroup(), builder.getName(), builder.getVersion(),
               builder.getConfiguration());
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
