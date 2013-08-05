/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.util.Map;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.MetadataFacet;

/**
 * @author Adam Wyłuda
 */
public class GradleMetadataFacet extends AbstractFacet<Project> implements MetadataFacet
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
   public String getProjectName()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setProjectName(String name)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public String getTopLevelPackage()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setTopLevelPackage(String groupId)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public String getProjectVersion()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setProjectVersion(String version)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public Dependency getOutputDependency()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Map<String, String> getEffectiveProperties()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Map<String, String> getDirectProperties()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getEffectiveProperty(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getDirectProperty(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setProperty(String name, String value)
   {
      // TODO Auto-generated method stub

   }
}
