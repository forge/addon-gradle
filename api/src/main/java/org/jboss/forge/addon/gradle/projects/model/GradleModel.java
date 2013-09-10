/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;

/**
 * @author Adam Wyłuda
 */
public interface GradleModel
{
   String getGroup();

   String getName();

   String getVersion();

   String getPackaging();

   String getArchiveName();

   String getProjectPath();

   String getRootProjectPath();

   String getArchivePath();

   List<GradleTask> getTasks();

   List<GradleTask> getEffectiveTasks();

   boolean hasEffectiveTask(GradleTask task);

   List<GradleDependency> getDependencies();

   boolean hasDependency(GradleDependency dep);

   List<GradleDependency> getEffectiveDependencies();

   boolean hasEffectiveDependency(GradleDependency dependency);

   List<GradleDependency> getManagedDependencies();

   boolean hasManagedDependency(GradleDependency dep);

   List<GradleDependency> getEffectiveManagedDependencies();

   boolean hasEffectiveManagedDependency(GradleDependency dependency);

   List<GradleProfile> getProfiles();

   boolean hasProfile(GradleProfile profile);

   List<GradlePlugin> getPlugins();

   boolean hasPlugin(GradlePlugin plugin);

   List<GradlePlugin> getEffectivePlugins();

   boolean hasEffectivePlugin(GradlePlugin plugin);

   List<GradleRepository> getRepositories();

   boolean hasRepository(GradleRepository repo);

   List<GradleRepository> getEffectiveRepositories();

   boolean hasEffectiveRepository(GradleRepository repo);

   Map<String, String> getProperties();

   Map<String, String> getEffectiveProperties();

   List<GradleSourceSet> getEffectiveSourceSets();
}
