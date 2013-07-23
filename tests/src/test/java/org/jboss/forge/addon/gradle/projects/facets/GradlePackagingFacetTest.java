/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Wyłuda
 */
@RunWith(Arquillian.class)
public class GradlePackagingFacetTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:resources", version = "2.0.0-SNAPSHOT"),
            @AddonDependency(name = "org.jboss.forge.addon:projects", version = "2.0.0-SNAPSHOT"),
            @AddonDependency(name = "org.jboss.forge.addon:gradle", version = "2.0.0-SNAPSHOT")
   })
   public static ForgeArchive getDeployment()
   {
      return GradleTestProjectProvider.getDeployment();
   }
   
   @Inject
   private GradleTestProjectProvider projectProvider;
   private Project project;
   private PackagingFacet facet;

   @Before
   public void setUp()
   {
      project = projectProvider.create();
      facet = project.getFacet(PackagingFacet.class);
   }

   @After
   public void cleanUp()
   {
      projectProvider.clean();
   }
   
   @Test
   public void testGetPackagingType()
   {
      String packagingType = facet.getPackagingType();
      assertEquals("war", packagingType);
   }
   
   @Test
   public void testSetPackagingType()
   {
      facet.setPackagingType("ear");
      
      Project sameProject = projectProvider.findProject();
      PackagingFacet sameFacet = sameProject.getFacet(PackagingFacet.class);
      String packagingType = sameFacet.getPackagingType();
      assertEquals("ear", packagingType);
   }
   
   @Test
   public void testGetFinalArtifact()
   {
      facet.executeBuild();
      Resource<?> res = facet.getFinalArtifact();
      String name = res.getName();
      assertEquals("test-project-0.7.war", name);
   }
   
   @Test
   public void testCreateBuilder()
   {
      
   }
   
   @Test
   public void testCreateBuilderSkipTests()
   {
      
   }
   
   @Test
   public void testGetFinalName()
   {
      
   }
   
   public void testSetFinalName()
   {
   }
}
