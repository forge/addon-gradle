/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

/**
 * Project dependency defined in <i>project.dependencies</i>.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public interface GradleDependency
{
   /**
    * Returns group of the dependency.
    */
   String getGroup();

   /**
    * Returns name of the dependency.
    */
   String getName();

   /**
    * Returns version of the dependency.
    */
   String getVersion();
   
   /**
    * Returns dependency classifier. 
    */
   String getClassifier();

   /**
    * Returns dependency configuration.
    */
   GradleDependencyConfiguration getConfiguration();

   /**
    * Returns dependency configuration name.
    */
   String getConfigurationName();

   /**
    * Returns dependency packaging.
    */
   String getPackaging();
   
   /**
    * Returns excluded dependencies. 
    */
   List<GradleDependency> getExcludedDependencies();

   /**
    * Returns Gradle string in format: <br/>
    * <i>group:name:version</i>
    */
   String toGradleString();
}
