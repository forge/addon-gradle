/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

/**
 * @author Adam Wyłuda
 */
public interface GradleDependency
{
   String getName();
   
   String getGroup();
   
   String getVersion();
   
   GradleDependencyConfiguration getConfiguration();

   /**
    * Returns real configuration name, if {@link #getConfiguration()} is set to {@link GradleDependencyConfiguration#OTHER}. 
    */
   String getConfigurationName();
}
