/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.gradle.projects.facets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFilter;
import org.jboss.forge.addon.resource.visit.ResourceVisit;
import org.jboss.forge.addon.resource.visit.ResourceVisitor;

/**
 * @author Adam Wyłuda
 */
@Dependent
@FacetConstraint({ PackagingFacet.class })
public class GradleWebResourcesFacet extends AbstractFacet<Project> implements WebResourcesFacet
{

   @SuppressWarnings("deprecation")
   @Override
   public DirectoryResource getWebRootDirectory()
   {
      Project project = getFaceted();
      // According to Gradle documentation this is default webapp location, although it's probably possible to change
      // by setting plugin properties
      // TODO Read (string) properties of Gradle plugins
      String webappFolderName = "src" + File.separator + "main" + File.separator + "webapp";
      DirectoryResource projectRoot = project.getRootDirectory();
      return projectRoot.getChildDirectory(webappFolderName);
   }

   @Override
   public List<DirectoryResource> getWebRootDirectories()
   {
      List<DirectoryResource> result = new ArrayList<>();
      result.add(getWebRootDirectory());
      return result;
   }

   @Override
   public boolean isInstalled()
   {
      Project project = getFaceted();
      String packagingType = project.getFacet(PackagingFacet.class).getPackagingType();

      return packagingType.equals("war");
   }

   @Override
   public boolean install()
   {
      if (!this.isInstalled())
      {
         getFaceted().getFacet(PackagingFacet.class).setPackagingType("war");
         for (DirectoryResource folder : this.getWebRootDirectories())
         {
            folder.mkdirs();
         }
      }
      return true;
   }

   @Override
   public FileResource<?> getWebResource(final String relativePath)
   {
      return (FileResource<?>) getWebRootDirectory().getChild(relativePath);
   }

   @Override
   public FileResource<?> createWebResource(final char[] data, final String relativePath)
   {
      FileResource<?> file = (FileResource<?>) getWebRootDirectory().getChild(relativePath);
      file.setContents(data);
      return file;
   }

   @Override
   public FileResource<?> createWebResource(final String data, final String relativePath)
   {
      return createWebResource(data.toCharArray(), relativePath);
   }

   @Override
   public void visitWebResources(ResourceVisitor visitor)
   {
      for (DirectoryResource root : getWebRootDirectories())
      {
         ResourceVisit visit = new ResourceVisit(root);
         visit.perform(visitor, new ResourceFilter()
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

         if (visit.isTerminated())
            break;
      }
   }

}
