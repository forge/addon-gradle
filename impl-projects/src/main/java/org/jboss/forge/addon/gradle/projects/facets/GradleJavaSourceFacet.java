/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.io.FileNotFoundException;
import java.util.List;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.parser.java.resources.JavaResourceVisitor;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.parser.java.JavaSource;

/**
 * @author Adam Wyłuda
 */
public class GradleJavaSourceFacet extends AbstractFacet<Project> implements JavaSourceFacet
{

   @Override
   public boolean install()
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean isInstalled()
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public String calculateName(JavaResource resource)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String calculatePackage(JavaResource resource)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getBasePackage()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DirectoryResource getBasePackageResource()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<DirectoryResource> getSourceFolders()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DirectoryResource getSourceFolder()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DirectoryResource getTestSourceFolder()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JavaResource saveJavaSource(JavaSource<?> source) throws FileNotFoundException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JavaResource saveTestJavaSource(JavaSource<?> source) throws FileNotFoundException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JavaResource getJavaResource(String relativePath) throws FileNotFoundException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JavaResource getJavaResource(JavaSource<?> javaClass) throws FileNotFoundException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JavaResource getTestJavaResource(String relativePath) throws FileNotFoundException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JavaResource getTestJavaResource(JavaSource<?> javaClass) throws FileNotFoundException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void visitJavaSources(JavaResourceVisitor visitor)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void visitJavaTestSources(JavaResourceVisitor visitor)
   {
      // TODO Auto-generated method stub
   }
}
