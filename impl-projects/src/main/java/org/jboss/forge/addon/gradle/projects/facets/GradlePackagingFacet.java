/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.building.ProjectBuilder;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.resource.Resource;

/**
 * @author Adam Wyłuda
 */
public class GradlePackagingFacet extends AbstractFacet<Project> implements PackagingFacet
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
   public void setPackagingType(String type)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public String getPackagingType()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Resource<?> getFinalArtifact()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public ProjectBuilder createBuilder()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Resource<?> executeBuild(String... args)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getFinalName()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setFinalName(String finalName)
   {
      // TODO Auto-generated method stub
      
   }

}
