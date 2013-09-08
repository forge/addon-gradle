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

   String getConfigurationName();
   
   String getPackaging();

   /**
    * Returns Gradle string in format: <br/>
    * <i>group:name:version</i>
    */
   String toGradleString();
}
