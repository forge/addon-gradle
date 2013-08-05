/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;

/**
 * @author Adam Wyłuda
 */
public class GradleModelImpl implements GradleModel
{
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

   public GradleModelImpl(String script, String projectName, String version,
            String packaging, String archivePath, List<GradleTask> tasks,
            List<GradleDependency> dependencies, List<GradleDependency> managedDependencies,
            List<GradleProfile> profiles, List<GradlePlugin> plugins, List<GradleRepository> repositories,
            List<GradleSourceSet> sourceSets)
   {
      this.script = script;
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
      script = GradleSourceUtil.insertDependency(script, builder.getGroup(), builder.getName(), builder.getVersion(),
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
