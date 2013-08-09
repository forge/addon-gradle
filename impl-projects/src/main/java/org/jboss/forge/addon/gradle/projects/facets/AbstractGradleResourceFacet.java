/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.util.List;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.RequiresFacet;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleSourceDirectory;
import org.jboss.forge.addon.gradle.projects.model.GradleSourceSet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;

/**
 * Base class for {@link GradleResourceFacet} and {@link GradleJavaSourceFacet}.
 * 
 * @author Adam Wyłuda
 */
@RequiresFacet(value = { GradleFacet.class })
public abstract class AbstractGradleResourceFacet extends AbstractFacet<Project>
{
   protected List<DirectoryResource> getMainResources()
   {
      return getResourcesFromSourceSet("main");
   }
   
   protected List<DirectoryResource> getTestResources()
   {
      return getResourcesFromSourceSet("test");
   }

   protected List<DirectoryResource> getResourcesFromSourceSet(String sourceSetName)
   {
      List<DirectoryResource> resources = Lists.newArrayList();
      GradleFacet gradleFacet = getFaceted().getFacet(GradleFacet.class);
      GradleModel model = gradleFacet.getModel();

      for (GradleSourceDirectory sourceDir : findSourceSetNamed(model.getSourceSets(), sourceSetName)
               .getResourcesDirectories())
      {
         resources.add(directoryResourceFromRelativePath(sourceDir.getPath()));
      }

      return resources;
   }

   protected DirectoryResource directoryResourceFromRelativePath(String path)
   {
      return getFaceted().getFacet(GradleFacet.class).getBuildScriptResource().getParent()
               .getChildDirectory(path);
   }

   protected GradleSourceSet findSourceSetNamed(List<GradleSourceSet> sourceSets, String name)
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

   protected FileResource<?> findFileResource(List<DirectoryResource> dirs, String path)
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
