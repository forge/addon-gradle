/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;

import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;

/**
 * Contains information about Gradle build script.
 * 
 * @author Adam Wyłuda
 */
public interface GradleModel
{
   String getScript();
   
   String getGroup();
   
   String getName();

   String getVersion();
   
   String getPackaging();
   
   String getArchivePath();

   List<GradleTask> getTasks();

   List<GradleDependency> getDependencies();

   List<GradleDependency> getManagedDependencies();

   List<GradleProfile> getProfiles();

   List<GradlePlugin> getPlugins();

   List<GradleRepository> getRepositories();
   
   List<GradleSourceSet> getSourceSets();
   
   Map<String, String> getProperties();

   /**
    * Returns true if exists task with given name.
    */
   boolean hasTask(String name);

   /**
    * Returns true if there exists dependency for which {@link GradleDependencyBuilder#equalsToDep(GradleDependency)}
    * return true.
    */
   boolean hasDependency(GradleDependencyBuilder builder);

   /**
    * Returns true if there exists managed dependency for which
    * {@link GradleDependencyBuilder#equalsToDep(GradleDependency)} return true.
    */
   boolean hasManagedDependency(GradleDependencyBuilder builder);

   /**
    * Returns true if there is profile with given name. 
    */
   boolean hasProfile(String name);

   /**
    * Returns true if project has applied plugin with given name. 
    */
   boolean hasPlugin(String clazz);

   /**
    * Returns true if project defined repository with given name. 
    */
   boolean hasRepository(String url);
   
   boolean hasProperty(String key);
   
   void setGroup(String group) throws UnremovableElementException;

   void setName(String name) throws UnremovableElementException;

   void setVersion(String version) throws UnremovableElementException;

   void setPackaging(String packaging);
   
   void setArchiveName(String archiveName);

   void createTask(GradleTaskBuilder builder);

   void createDependency(GradleDependencyBuilder builder);

   void createManagedDependency(GradleDependencyBuilder builder);

   /**
    * @see GradleProfile
    */
   void createProfile(String name);

   /**
    * Applies plugin with given name or class.
    */
   void applyPlugin(String name);

   void createRepository(GradleRepositoryBuilder builder);
   
   void setProperty(String name, String value);

   void removeDependency(GradleDependencyBuilder builder) throws UnremovableElementException;

   void removeManagedDependency(GradleDependencyBuilder builder) throws UnremovableElementException;

   void removeProfile(String name);

   void removeAppliedPlugin(String name) throws UnremovableElementException;

   void removeRepository(GradleRepositoryBuilder builder) throws UnremovableElementException;
   
   void removeProperty(String name) throws UnremovableElementException;
}
