/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;

import org.jboss.forge.addon.gradle.projects.GradleFacet;

/**
 * Describes single Gradle project. Full project model is loaded in two ways: by parsing build.gradle file (the
 * <i>direct</i> model) and by running task from inside Gradle which provides precise project information (the <i>
 * effective</i> model). Direct model is obtained by parsing scripts so it means that its every element can be either
 * modified or removed. Effective model might be defined in the script file but that doesn't necessarily mean that it
 * could be removed. For example in case of dependency which uses variables for coordinates it's impossible to predict
 * their value basing on variable name so this script element is unremovable.
 * 
 * @see GradleFacet
 * @see GradleModelBuilder
 * 
 * @author Adam Wyłuda
 */
public interface GradleModel
{
   /**
    * Returns value of the project.group property.
    */
   String getGroup();

   /**
    * Returns value of the project.name property.
    */
   String getName();

   /**
    * Returns value of the project.version property.
    */
   String getVersion();

   /**
    * Returns packaging of the project defined by applied plugins.
    */
   String getPackaging();

   /**
    * Returns output archive name of the project.
    */
   String getArchiveName();

   /**
    * Returns project path.
    */
   String getProjectPath();

   /**
    * Returns root project path.
    */
   String getRootProjectPath();

   /**
    * Returns path to the output archive.
    */
   String getArchivePath();
   
   /**
    * Returns version compatibility for Java sources.
    */
   String getSourceCompatibility();
   
   /**
    * Returns target version of generated binaries.
    */
   String getTargetCompatiblity();

   /**
    * Returns list of tasks defined in the project.
    */
   List<GradleTask> getTasks();

   /**
    * Returns effective list of tasks defined in the project.
    */
   List<GradleTask> getEffectiveTasks();

   /**
    * Returns true if {@link #getEffectiveTasks()} contains a task with the same name as the given task.
    * 
    * @see GradleTaskBuilder
    */
   boolean hasEffectiveTask(GradleTask task);

   /**
    * Returns list of dependencies parsed from the project.
    */
   List<GradleDependency> getDependencies();

   /**
    * Returns true if {@link #getDependencies()} contains a dependency with the same coordinates and configuration as
    * the given dependency.
    * 
    * @see GradleDependencyBuilder
    */
   boolean hasDependency(GradleDependency dep);

   /**
    * Returns list of dependencies evaluated from the project.
    */
   List<GradleDependency> getEffectiveDependencies();

   /**
    * Returns true if {@link #getEffectiveDependencies()} contains a dependency with the same coordinates and
    * configuration as the given dependency.
    * 
    * @see GradleDependencyBuilder
    */
   boolean hasEffectiveDependency(GradleDependency dependency);

   /**
    * Returns list of managed dependencies parsed from the project. Managed dependencies are defined by <i>managed</i>
    * closure.
    */
   List<GradleDependency> getManagedDependencies();

   /**
    * Returns true if {@link #getManagedDependencies()} contains a dependency with the same coordinates and
    * configuration as the given dependency.
    * 
    * @see GradleDependencyBuilder
    */
   boolean hasManagedDependency(GradleDependency dep);

   /**
    * Returns list of managed dependencies evaluated from the project. Managed dependencies are defined by
    * <i>managed</i> closure.
    */
   List<GradleDependency> getEffectiveManagedDependencies();

   /**
    * Returns true if {@link #getEffectiveManagedDependencies()} contains a dependency with the same coordinates and
    * configuration as the given dependency.
    * 
    * @see GradleDependencyBuilder
    */
   boolean hasEffectiveManagedDependency(GradleDependency dependency);

   /**
    * Returns list of Gradle profiles defined in project directory.
    */
   List<GradleProfile> getProfiles();

   /**
    * Returns true if {@link #getProfiles()} contains a profile with the same name as the given profile.
    * 
    * @see GradleProfileBuilder
    */
   boolean hasProfile(GradleProfile profile);

   /**
    * Returns list of plugins applied to the project by <i>apply plugin: 'name'</i>.
    */
   List<GradlePlugin> getPlugins();

   /**
    * Returns true if {@link #getPlugins()} contains a plugin with the same name as given plugin.
    * 
    * @see GradlePluginBuilder
    */
   boolean hasPlugin(GradlePlugin plugin);

   /**
    * Returns list of effective plugins applied to the project by <i>apply plugin: 'name'</i>.
    */
   List<GradlePlugin> getEffectivePlugins();

   /**
    * Returns true if {@link #getEffectivePlugins()} contains a plugin with the same name as given plugin.
    * 
    * @see GradlePluginBuilder
    */
   boolean hasEffectivePlugin(GradlePlugin plugin);

   /**
    * Returns list of repositories defined for this project.
    */
   List<GradleRepository> getRepositories();

   /**
    * Returns true if {@link #getRepositories()} contains a repository with the same URL as given repository.
    * 
    * @see GradleRepositoryBuilder
    */
   boolean hasRepository(GradleRepository repo);

   /**
    * Returns list of effective repositories defined for this project.
    */
   List<GradleRepository> getEffectiveRepositories();

   /**
    * Returns true if {@link #getEffectiveRepositories()} contains a repository with the same URL as given repository.
    * 
    * @see GradleRepositoryBuilder
    */
   boolean hasEffectiveRepository(GradleRepository repo);

   /**
    * Returns map of project properties defines by <i>ext.property = 'value'</i>.
    */
   Map<String, String> getProperties();

   /**
    * Returns map of effective project properties defines by <i>ext.property = 'value'</i>.
    */
   Map<String, String> getEffectiveProperties();

   /**
    * Returns list of Gradle source sets.
    */
   List<GradleSourceSet> getEffectiveSourceSets();
}
