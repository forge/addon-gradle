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
   // ---------- Project metadata

   String getGroup();

   String getName();

   String getVersion();
   
   String getPackaging();

   String getArchiveName();

   // ---------- Tasks

   List<GradleTask> getTasks();

   boolean hasTask(String name);

   // ---------- Dependencies
   
   List<GradleDependency> getDependencies();

   List<GradleDependency> getManagedDependencies();

   // ---------- Profiles

   List<GradleProfile> getProfiles();

   boolean hasProfile(String name);

   // ---------- Plugins

   List<GradlePlugin> getPlugins();

   boolean hasPlugin(String clazz);

   // ---------- Repositories

   List<GradleRepository> getRepositories();

   boolean hasRepository(String url);

   // ---------- Properties

   Map<String, String> getProperties();

   boolean hasProperty(String key);
}
