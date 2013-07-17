/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.services.Exported;

/**
 * @author Adam Wyłuda
 */
@Exported
public class GradleTestProjectProvider
{
   @Inject
   private Furnace furnace;
   @Inject
   private GradleProjectLocator locator;
   @Inject
   private ProjectFactory projectFactory;
   @Inject
   private ResourceFactory resourceFactory;

   private DirectoryResource projectDir;

   public Project create()
   {
      DirectoryResource addonDir = resourceFactory.create(furnace.getRepositories().get(0).getRootDirectory()).reify(
               DirectoryResource.class);
      projectDir = addonDir.createTempResource();
      FileResource<?> gradleFile = projectDir.getChild("build.gradle").reify(FileResource.class);
      gradleFile.createNewFile();
      gradleFile.setContents(getClass().getResourceAsStream("/build.gradle"));
      return findProject();
   }

   public Project findProject()
   {
      return projectFactory.findProject(projectDir);
   }

   public void clean()
   {
      projectDir.delete(true);
   }
}
