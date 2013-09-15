/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
import org.jboss.forge.addon.gradle.projects.ProjectAssert;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
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
public class GradleResourceFacetTest
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
   private GradleTestProjectProvider projectProvider;
   private Project project;
   private ResourcesFacet facet;

   @Before
   public void setUp()
   {
      project = projectProvider.create();
      facet = project.getFacet(ResourcesFacet.class);
   }

   @After
   public void cleanUp()
   {
      projectProvider.clean();
   }

   @Test
   public void testGetResourceFolders()
   {
      List<DirectoryResource> resources = facet.getResourceFolders();
      assertEquals(4, resources.size());
      ProjectAssert.assertContainsDirectoryNamed(resources, "src/main/resources");
      ProjectAssert.assertContainsDirectoryNamed(resources, "src/main/images");
      ProjectAssert.assertContainsDirectoryNamed(resources, "src/test/resources");
      ProjectAssert.assertContainsDirectoryNamed(resources, "src/test/templates");
   }

   @Test
   public void testGetResourceFolder()
   {
      DirectoryResource dir = facet.getResourceFolder();
      ProjectAssert.assertDirectoryIsOneOf(dir, "src/main/resources", "src/main/images");
   }

   @Test
   public void testGetTestResourceFolder()
   {
      DirectoryResource dir = facet.getTestResourceFolder();
      ProjectAssert.assertDirectoryIsOneOf(dir, "src/test/resources", "src/test/templates");
   }

   @Test
   public void testCreateResource()
   {
      DirectoryResource dir = facet.getResourceFolder();
      facet.createResource("RESOURCE".toCharArray(), "test/resource.txt");
      FileResource<?> res = (FileResource<?>) dir.getChildDirectory("test").getChild("resource.txt");
      assertEquals("RESOURCE", res.getContents());
   }

   @Test
   public void testCreateTestResource()
   {
      DirectoryResource dir = facet.getTestResourceFolder();
      facet.createTestResource("TESTRESOURCE".toCharArray(), "test/resource.txt");
      FileResource<?> res = (FileResource<?>) dir.getChildDirectory("test").getChild("resource.txt");
      assertEquals("TESTRESOURCE", res.getContents());
   }

   @Test
   public void testGetResource()
   {
      FileResource<?> res = facet.getResource("forge.txt");
      assertTrue(res.exists());
      assertEquals("FORGE", res.getContents());
   }

   @Test
   public void testGetResourceNonExisting()
   {
      FileResource<?> res = facet.getResource("roo.txt");
      assertNotNull(res);
      assertFalse(res.exists());
   }

   @Test
   public void testGetTestResource()
   {
      FileResource<?> res = facet.getTestResource("pom.xml");
      assertTrue(res.exists());
      assertEquals("<project></project>", res.getContents());
   }

   @Test
   public void testGetTestResourceNonExisting()
   {
      FileResource<?> res = facet.getResource("build.gradle");
      assertNotNull(res);
      assertFalse(res.exists());
   }
}
