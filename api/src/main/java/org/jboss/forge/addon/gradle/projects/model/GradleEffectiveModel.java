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
public interface GradleEffectiveModel extends GradleDirectModel
{
   String getProjectPath();

   String getRootProjectPath();

   String getArchivePath();

   List<GradleTask> getEffectiveTasks();

   boolean hasEffectiveTask(GradleTask task);

   List<GradleDependency> getEffectiveDependencies();

   boolean hasEffectiveDependency(GradleDependency dependency);

   List<GradleDependency> getEffectiveManagedDependencies();

   boolean hasEffectiveManagedDependency(GradleDependency dependency);

   List<GradlePlugin> getEffectivePlugins();
   
   boolean hasEffectivePlugin(GradlePlugin plugin);

   List<GradleRepository> getEffectiveRepositories();
   
   boolean hasEffectiveRepository(GradleRepository repo);

   List<GradleSourceSet> getEffectiveSourceSets();

   Map<String, String> getEffectiveProperties();
}
