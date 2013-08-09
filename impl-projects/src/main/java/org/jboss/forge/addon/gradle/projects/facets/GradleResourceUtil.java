/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.util.List;

import org.jboss.forge.addon.gradle.projects.model.GradleSourceSet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;

/**
 * @author Adam Wyłuda
 */
public class GradleResourceUtil
{
   public static GradleSourceSet findSourceSetNamed(List<GradleSourceSet> sourceSets, String name)
   {
      for (GradleSourceSet sourceSet : sourceSets)
      {
         if (sourceSet.getName().equals(name))
         {
            return sourceSet;
         }
      }
   
      throw new RuntimeException("Source set named " + name + " not found");
   }

   public static FileResource<?> findFileResource(List<DirectoryResource> dirs, String path)
   {
      FileResource<?> foundFile = null;
      for (DirectoryResource dir : dirs)
      {
         foundFile = (FileResource<?>) dir.getChild(path);
         if (foundFile.exists())
         {
            break;
         }
      }
      return foundFile;
   }
}
