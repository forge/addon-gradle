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
public class GradleSourceSetImpl implements GradleSourceSet
{
   private final String name;
   private final List<GradleSourceDirectory> javaDirectories;
   private final List<GradleSourceDirectory> resourcesDirectories;

   public GradleSourceSetImpl(String name, List<GradleSourceDirectory> javaDirectories,
            List<GradleSourceDirectory> resourcesDirectories)
   {
      this.name = name;
      this.javaDirectories = javaDirectories;
      this.resourcesDirectories = resourcesDirectories;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public List<GradleSourceDirectory> getJavaDirectories()
   {
      return javaDirectories;
   }

   @Override
   public List<GradleSourceDirectory> getResourcesDirectories()
   {
      return resourcesDirectories;
   }

}
