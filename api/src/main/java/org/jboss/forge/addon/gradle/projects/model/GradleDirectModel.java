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
public interface GradleDirectModel
{
   String getGroup();

   String getName();

   String getVersion();

   String getPackaging();

   String getArchiveName();

   List<GradleTask> getTasks();
   
   List<GradleDependency> getDependencies();
   
   boolean hasDependency(GradleDependency dep);

   List<GradleDependency> getManagedDependencies();
   
   boolean hasManagedDependency(GradleDependency dep);

   List<GradleProfile> getProfiles();
   
   boolean hasProfile(GradleProfile profile);

   List<GradlePlugin> getPlugins();
   
   boolean hasPlugin(GradlePlugin plugin);

   List<GradleRepository> getRepositories();
   
   boolean hasRepository(GradleRepository repo);

   Map<String, String> getProperties();
}
