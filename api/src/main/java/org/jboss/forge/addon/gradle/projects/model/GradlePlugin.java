/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

/**
 * Gradle plugin applied to the project.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public interface GradlePlugin
{
   /**
    * Class of the plugin. 
    */
   String getClazz();

   /**
    * If this is one of the common plugins then it will return a convenient enum value, otherwise it will return
    * {@link GradlePluginType#OTHER}.
    */
   GradlePluginType getType();
}
