/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

/**
 * @author Adam Wyłuda
 */
public interface GradleEffectiveModel extends GradleDirectModel
{
   // ---------- Project paths

   String getProjectPath();

   String getRootProjectPath();

   String getArchivePath();

   // ---------- Dependencies

   List<GradleDependency> getEffectiveDependencies();

   boolean hasEffectiveDependency(GradleDependencyBuilder builder);

   // ---------- Managed dependencies

   List<GradleDependency> getEffectiveManagedDependencies();

   boolean hasEffectiveManagedDependency(GradleDependencyBuilder builder);

   // ---------- Profiles
   
   // TODO Effective profiles

   // ---------- Plugins
   
   // TODO Effective plugins

   // ---------- Repositories

   // TODO Effective repositories

   // --------- Source sets

   List<GradleSourceSet> getEffectiveSourceSets();

   // ---------- Properties

   // TODO Effective properties
}
