/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
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
public class GradleMetadataFacetTest
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
      return GradleTestProjectProvider.getDeployment(
               GradleTestProjectProvider.SIMPLE_RESOURCES_PATH,
               GradleTestProjectProvider.SIMPLE_RESOURCES);
   }

   @Inject
   private GradleTestProjectProvider projectProvider;
   private Project project;
   private MetadataFacet facet;

   @Before
   public void setUp()
   {
      project = projectProvider.create("",
               GradleTestProjectProvider.SIMPLE_RESOURCES_PATH,
               GradleTestProjectProvider.SIMPLE_RESOURCES);
      facet = project.getFacet(MetadataFacet.class);
   }

   @After
   public void cleanUp()
   {
      projectProvider.clean();
   }

   @Test
   public void testGetProjectName()
   {
      String name = facet.getProjectName();
      assertEquals("test-project", name);
   }

   @Test
   public void testSetProjectName()
   {
      facet.setProjectName("new-name");

      Project sameProject = projectProvider.findProject();
      MetadataFacet sameFacet = sameProject.getFacet(MetadataFacet.class);

      String name = sameFacet.getProjectName();
      assertEquals("new-name", name);
   }

   @Test
   public void testGetTopLevelPackage()
   {
      String group = facet.getTopLevelPackage();
      assertEquals("org.testproject", group);
   }

   @Test
   public void testSetTopLevelPackage()
   {
      facet.setTopLevelPackage("org.new");

      Project sameProject = projectProvider.findProject();
      MetadataFacet sameFacet = sameProject.getFacet(MetadataFacet.class);

      String group = sameFacet.getTopLevelPackage();
      assertEquals("org.new", group);
   }

   @Test
   public void testGetProjectVersion()
   {
      String version = facet.getProjectVersion();
      assertEquals("0.7", version);
   }

   @Test
   public void testSetProjectVersion()
   {
      facet.setProjectVersion("0.8");

      Project sameProject = projectProvider.findProject();
      MetadataFacet sameFacet = sameProject.getFacet(MetadataFacet.class);

      String version = sameFacet.getProjectVersion();
      assertEquals("0.8", version);
   }

   @Test
   public void testGetOutputDependency()
   {
      Dependency dep = facet.getOutputDependency();

      assertEquals("test-project", dep.getCoordinate().getArtifactId());
      assertEquals("org.testproject", dep.getCoordinate().getGroupId());
      assertEquals("0.7", dep.getCoordinate().getVersion());
   }

   @Test
   public void testGetEffectiveProperties()
   {
      Map<String, String> props = facet.getEffectiveProperties();

      assertEquals("https://github.com/forge/addon-gradle", props.get("githubRepo"));
      assertEquals("JBoss", props.get("organization"));
      assertEquals(".45", props.get("someVersion"));
      assertNull(props.get("version"));
   }

   @Test
   public void testGetEffectiveProperty()
   {
      assertEquals("https://github.com/forge/addon-gradle", facet.getEffectiveProperty("githubRepo"));
      assertEquals("JBoss", facet.getEffectiveProperty("organization"));
      assertEquals(".45", facet.getEffectiveProperty("someVersion"));
      assertNull(facet.getEffectiveProperty("group"));
   }

   @Test
   public void testGetDirectProperties()
   {
      Map<String, String> props = facet.getDirectProperties();

      assertEquals("https://github.com/forge/addon-gradle", props.get("githubRepo"));
      assertEquals("JBoss", props.get("organization"));
      assertNull(props.get("someVersion"));
      assertNull(props.get("version"));
   }

   @Test
   public void testGetDirectProperty()
   {
      assertEquals("https://github.com/forge/addon-gradle", facet.getDirectProperty("githubRepo"));
      assertEquals("JBoss", facet.getDirectProperty("organization"));
      assertNull(facet.getDirectProperty("someVersion"));
      assertNull(facet.getDirectProperty("group"));
   }

   @Test
   public void testSetDirectProperty()
   {
      assertEquals("JBoss", facet.getEffectiveProperty("organization"));

      facet.setDirectProperty("organization", "EJB-OSS");

      Project sameProject = projectProvider.findProject();
      MetadataFacet sameFacet = sameProject.getFacet(MetadataFacet.class);

      assertEquals("EJB-OSS", sameFacet.getEffectiveProperty("organization"));
   }

   @Test
   public void testRemoveDirectProperty()
   {
      assertNotNull(facet.getDirectProperty("githubRepo"));

      facet.removeDirectProperty("githubRepo");

      Project sameProject = projectProvider.findProject();
      MetadataFacet sameFacet = sameProject.getFacet(MetadataFacet.class);

      assertNull(sameFacet.getDirectProperty("githubRepo"));
   }
}
