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
public class GradleSourceDirectoryImpl implements GradleSourceDirectory
{
   private final String path;
   
   public GradleSourceDirectoryImpl(String path)
   {
      this.path = path;
   }

   @Override
   public String getPath()
   {
      return path;
   }
}
