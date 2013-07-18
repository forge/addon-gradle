/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.FileResource;

/**
 * @author Adam Wyłuda
 */
public class GradleFacetImpl extends AbstractFacet<Project> implements GradleFacet
{
   @Inject
   private GradleManager manager;
   @Inject
   private GradleProjectCache cache;

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
   public void executeTask(String task)
   {
      executeTask(task, "");
   }

   @Override
   public void executeTask(String task, String profile)
   {
      manager.runGradleBuild(getFaceted().getProjectRoot().getFullyQualifiedName(), task, profile);
   }

   @Override
   public GradleModel getModel()
   {
      try
      {
         String buildScriptFilePath = new File(new File(getFaceted().getProjectRoot().getFullyQualifiedName()), "build.gradle").getAbsolutePath();
         return cache.getModel(buildScriptFilePath);
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public FileResource<?> getGradleResource()
   {
      // TODO Auto-generated method stub
      return null;
   }
}
