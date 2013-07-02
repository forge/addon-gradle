/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import org.jboss.forge.addon.projects.AbstractProject;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.resource.DirectoryResource;

/**
 * @author Adam Wyłuda
 */
public class GradleProject extends AbstractProject
{
   private final DirectoryResource root;

   public GradleProject(DirectoryResource root)
   {
      this.root = root;
   }

   @Override
   public DirectoryResource getProjectRoot()
   {
      return root;
   }

   @Override
   public <F extends ProjectFacet> boolean supports(F facet)
   {
      return true;
   }
}
