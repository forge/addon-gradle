/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.util.List;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;

/**
 * @author Adam Wyłuda
 */
public class GradleResourceFacet extends AbstractFacet<Project> implements ResourcesFacet
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
   public List<DirectoryResource> getResourceFolders()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DirectoryResource getResourceFolder()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DirectoryResource getTestResourceFolder()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileResource<?> createResource(char[] bytes, String relativeFilename)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileResource<?> createTestResource(char[] bytes, String relativeFilename)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileResource<?> getResource(String relativePath)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public FileResource<?> getTestResource(String relativePath)
   {
      // TODO Auto-generated method stub
      return null;
   }
}
