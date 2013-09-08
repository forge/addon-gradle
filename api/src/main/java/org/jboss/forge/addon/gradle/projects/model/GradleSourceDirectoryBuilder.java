/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Wyłuda
 */
public class GradleSourceDirectoryBuilder implements GradleSourceDirectory
{
   private String path = "";
   
   private GradleSourceDirectoryBuilder()
   {
   }
   
   public static GradleSourceDirectoryBuilder create()
   {
      return new GradleSourceDirectoryBuilder();
   }
   
   public static GradleSourceDirectoryBuilder create(String path)
   {
      GradleSourceDirectoryBuilder builder = new GradleSourceDirectoryBuilder();
      
      builder.path = path;
      
      return builder;
   }
   
   public static GradleSourceDirectoryBuilder create(GradleSourceDirectory sourceDirectory)
   {
      GradleSourceDirectoryBuilder builder = new GradleSourceDirectoryBuilder();
      
      builder.path = sourceDirectory.getPath();
      
      return builder;
   }
   
   public static List<GradleSourceDirectory> deepCopy(List<GradleSourceDirectory> sourceDirs)
   {
      List<GradleSourceDirectory> list = new ArrayList<GradleSourceDirectory>();
      
      for (GradleSourceDirectory set : sourceDirs)
      {
         list.add(create(set));
      }
      
      return list;
   }

   @Override
   public String getPath()
   {
      return path;
   }
   
   public GradleSourceDirectoryBuilder setPath(String path)
   {
      this.path = path;
      return this;
   }
}
