/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.Furnace;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Wyłuda
 */
@RunWith(Arquillian.class)
public class GradleJavaProjectTypeTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:resources", version = "2.0.0-SNAPSHOT"),
            @AddonDependency(name = "org.jboss.forge.addon:projects", version = "2.0.0-SNAPSHOT"),
            @AddonDependency(name = "org.jboss.forge.addon:parser-java", version = "2.0.0-SNAPSHOT"),
            @AddonDependency(name = "org.jboss.forge.addon:gradle", version = "2.0.0-SNAPSHOT"),
            @AddonDependency(name = "org.jboss.forge.addon:maven", version = "2.0.0-SNAPSHOT")
   })
   public static ForgeArchive getDeployment()
   {
      return GradleTestProjectProvider.getDeployment();
   }
   
   @Inject
   private ProjectFactory projectLocator;
   @Inject
   private ResourceFactory resourceFactory;
   @Inject
   private Furnace furnace;
   
   @Test
   public void testCreateProject()
   {
      DirectoryResource addonDir = resourceFactory.create(furnace.getRepositories().get(0).getRootDirectory()).reify(
               DirectoryResource.class);
      DirectoryResource projectDir = addonDir.createTempResource();
      Project project = projectLocator.createProject(projectDir, new GradleJavaProjectType().getRequiredFacets());
      
      assertEquals(projectDir, project.getProjectRoot());
      assertTrue(projectDir.getChild("build.gradle").exists());
      assertFalse(projectDir.getChild("pom.xml").exists());
      assertTrue(projectDir.getChildDirectory("src/main/java").exists());
   }
}
