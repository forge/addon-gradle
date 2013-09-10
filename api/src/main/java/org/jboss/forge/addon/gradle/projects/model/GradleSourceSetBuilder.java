/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Adam Wyłuda
 */
public class GradleSourceSetBuilder implements GradleSourceSet
{
   private String name = "";
   private List<GradleSourceDirectory> javaDirs = new ArrayList<GradleSourceDirectory>();
   private List<GradleSourceDirectory> resourceDirs = new ArrayList<GradleSourceDirectory>();
   
   private GradleSourceSetBuilder()
   {
   }
   
   public static GradleSourceSetBuilder create()
   {
      return new GradleSourceSetBuilder();
   }
   
   public static GradleSourceSetBuilder create(GradleSourceSet sourceSet)
   {
      GradleSourceSetBuilder builder = new GradleSourceSetBuilder();
      
      builder.name = sourceSet.getName();
      builder.javaDirs = GradleSourceDirectoryBuilder.deepCopy(sourceSet.getJavaDirectories());
      builder.resourceDirs = GradleSourceDirectoryBuilder.deepCopy(sourceSet.getResourceDirectories());
      
      return builder;
   }

   @Override
   public String getName()
   {
      return name;
   }
   
   public GradleSourceSetBuilder setName(String name)
   {
      this.name = name;
      return this;
   }
   
   @Override
   public List<GradleSourceDirectory> getJavaDirectories()
   {
      return Collections.unmodifiableList(javaDirs);
   }
   
   public GradleSourceSetBuilder setJavaDirectories(List<GradleSourceDirectory> dirs)
   {
      this.javaDirs = dirs;
      return this;
   }
   
   @Override
   public List<GradleSourceDirectory> getResourceDirectories()
   {
      return Collections.unmodifiableList(resourceDirs);
   }
   
   public GradleSourceSetBuilder setResourceDirectories(List<GradleSourceDirectory> dirs)
   {
      this.resourceDirs = dirs;
      return this;
   }
}
