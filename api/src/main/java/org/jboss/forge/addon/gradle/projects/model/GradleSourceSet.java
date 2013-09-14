/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

/**
 * Represents Gradle source set.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public interface GradleSourceSet
{
   /**
    * Returns name of the source set. 
    */
   String getName();

   /**
    * Returns list of Java source directories of the source set. 
    */
   List<GradleSourceDirectory> getJavaDirectories();

   /**
    * Returns list of resource source directories of the source set. 
    */
   List<GradleSourceDirectory> getResourceDirectories();
}
