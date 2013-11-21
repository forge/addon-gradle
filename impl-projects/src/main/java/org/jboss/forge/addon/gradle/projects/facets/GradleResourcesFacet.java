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
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleSourceDirectory;
import org.jboss.forge.addon.gradle.projects.model.GradleSourceSet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFilter;
import org.jboss.forge.addon.resource.visit.ResourceVisit;
import org.jboss.forge.addon.resource.visit.ResourceVisitor;

/**
 * @author Adam Wyłuda
 */
@FacetConstraints({
         @FacetConstraint(GradleFacet.class)
})
public class GradleResourcesFacet extends AbstractFacet<Project> implements ResourcesFacet
{
   @Override
   public boolean install()
   {
      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return true;
   }

   @Override
   public List<DirectoryResource> getResourceDirectories()
   {
      List<DirectoryResource> resources = Lists.newArrayList();
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();

      for (GradleSourceSet sourceSet : model.getEffectiveSourceSets())
      {
         for (GradleSourceDirectory sourceDir : sourceSet.getResourceDirectories())
         {
            resources.add(directoryResourceFromRelativePath(sourceDir.getPath()));
         }
      }

      return resources;
   }

   @Override
   public DirectoryResource getResourceDirectory()
   {
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();
      GradleSourceDirectory dir = GradleResourceUtil.findSourceSetNamed(model.getEffectiveSourceSets(), "main")
               .getResourceDirectories().get(0);
      return directoryResourceFromRelativePath(dir.getPath());
   }

   @Override
   public DirectoryResource getTestResourceDirectory()
   {
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();
      GradleSourceDirectory dir = GradleResourceUtil.findSourceSetNamed(model.getEffectiveSourceSets(), "test")
               .getResourceDirectories().get(0);
      return directoryResourceFromRelativePath(dir.getPath());
   }

   @Override
   public FileResource<?> createResource(char[] bytes, String relativeFilename)
   {
      FileResource<?> resource = (FileResource<?>) getResourceDirectory().getChild(relativeFilename);
      resource.setContents(bytes);
      return resource;
   }

   @Override
   public FileResource<?> createTestResource(char[] bytes, String relativeFilename)
   {
      FileResource<?> resource = (FileResource<?>) getTestResourceDirectory().getChild(relativeFilename);
      resource.setContents(bytes);
      return resource;
   }

   @Override
   public FileResource<?> getResource(String relativePath)
   {
      return GradleResourceUtil.findFileResource(getMainResources(), relativePath);
   }

   @Override
   public FileResource<?> getTestResource(String relativePath)
   {
      return GradleResourceUtil.findFileResource(getTestResources(), relativePath);
   }

   private List<DirectoryResource> getMainResources()
   {
      return getResourcesFromSourceSet("main");
   }

   private List<DirectoryResource> getTestResources()
   {
      return getResourcesFromSourceSet("test");
   }

   private List<DirectoryResource> getResourcesFromSourceSet(String sourceSetName)
   {
      List<DirectoryResource> resources = Lists.newArrayList();
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();

      for (GradleSourceDirectory sourceDir : GradleResourceUtil
               .findSourceSetNamed(model.getEffectiveSourceSets(), sourceSetName)
               .getResourceDirectories())
      {
         resources.add(directoryResourceFromRelativePath(sourceDir.getPath()));
      }

      return resources;
   }

   private DirectoryResource directoryResourceFromRelativePath(String path)
   {
      return getFaceted().getFacet(GradleFacet.class).getBuildScriptResource().getParent()
               .getChildDirectory(path);
   }

   @Override
   public void visitResources(ResourceVisitor visitor)
   {
      new ResourceVisit(getResourceDirectory()).perform(visitor, new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> resource)
         {
            return resource instanceof DirectoryResource;
         }
      }, new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> type)
         {
            return true;
         }
      });
   }

   @Override
   public void visitTestResources(ResourceVisitor visitor)
   {
      new ResourceVisit(getTestResourceDirectory()).perform(visitor, new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> resource)
         {
            return resource instanceof DirectoryResource;
         }
      }, new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> type)
         {
            return true;
         }
      });
   }
}
