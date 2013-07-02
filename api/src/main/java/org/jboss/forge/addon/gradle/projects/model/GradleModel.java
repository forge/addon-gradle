/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

/**
 * Contains information about Gradle build script.
 * 
 * @author Adam Wyłuda
 */
public interface GradleModel
{
   List<GradleTask> getTasks();
   
   List<GradleDependency> getDependencies();
   
   List<GradleProfile> getProfiles();
   
   List<GradlePlugin> getPlugins();
   
   List<GradleRepository> getRepositories();
   
   void createTask(GradleTaskBuilder builder);
}
