/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleTask;
import org.jboss.forge.addon.projects.Project;

/**
 * @author Adam Wyłuda
 */
public class GradleFacetImpl extends AbstractFacet<Project> implements GradleFacet
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
   public void executeTask(String task)
   {
      executeTask(task, "");
   }

   @Override
   public void executeTask(String task, String profile)
   {
      // TODO
   }

   @Override
   public GradleModel getModel()
   {
      return null;
   }
}
