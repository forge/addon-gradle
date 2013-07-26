/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.DependencyRepository;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
import org.jboss.forge.addon.gradle.projects.ProjectAssert;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
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
public class GradleDependencyFacetTest
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
   private DependencyFacet facet;

   @Before
   public void setUp()
   {
      project = projectProvider.create();
      facet = project.getFacet(DependencyFacet.class);
   }

   @After
   public void cleanUp()
   {
      projectProvider.clean();
   }

   @Test
   public void testAddDirectDependency()
   {
      facet.addDirectDependency(
               DependencyBuilder
                        .create()
                        .setArtifactId("mydep")
                        .setGroupId("mygroup")
                        .setVersion("myversion")
                        .setScopeType("runtime"));

      Project theSameProject = projectProvider.findProject();
      DependencyFacet theNewFacet = theSameProject.getFacet(DependencyFacet.class);
      
      ProjectAssert.assertContainsDependency(theNewFacet.getDependencies(), 
               "runtime", "mydep", "mygroup", "myversion");
   }
   
   @Test
   public void testAddManagedDependency()
   {
      facet.addManagedDependency(
               DependencyBuilder
                        .create()
                        .setArtifactId("mydep")
                        .setGroupId("mygroup")
                        .setVersion("myversion")
                        .setScopeType("runtime"));

      Project theSameProject = projectProvider.findProject();
      DependencyFacet theNewFacet = theSameProject.getFacet(DependencyFacet.class);
      
      ProjectAssert.assertContainsDependency(theNewFacet.getManagedDependencies(), 
               "runtime", "mydep", "mygroup", "myversion");
   }
   
   @Test
   public void testAddDirectManagedDependency()
   {
      facet.addDirectManagedDependency(
               DependencyBuilder
                        .create()
                        .setArtifactId("mydep")
                        .setGroupId("mygroup")
                        .setVersion("myversion")
                        .setScopeType("runtime"));

      Project theSameProject = projectProvider.findProject();
      DependencyFacet theNewFacet = theSameProject.getFacet(DependencyFacet.class);
      
      ProjectAssert.assertContainsDependency(theNewFacet.getManagedDependencies(), 
               "runtime", "mydep", "mygroup", "myversion");
   }
   
   @Test
   public void testAddRepository()
   {
      facet.addRepository("RepoName", "http://repo.com/");
      
      Project sameProject = projectProvider.findProject();
      DependencyFacet sameFacet = sameProject.getFacet(DependencyFacet.class);
      
      ProjectAssert.assertContainsRepository(sameFacet.getRepositories(), "http://repo.com/");
   }
   
   @Test
   public void testGetDependencies()
   {
      List<Dependency> deps = facet.getDependencies();
      
      ProjectAssert.assertContainsDependency(deps, "compile", "slf4j-api", "org.slf4j", "1.7.5");
      ProjectAssert.assertContainsDependency(deps, "compile", "slf4j-simple", "org.slf4j", "1.7.5");
      ProjectAssert.assertContainsDependency(deps, "testCompile", "junit", "junit", "4.11");
      ProjectAssert.assertContainsDependency(deps, "runtime", "guice", "com.google.code.guice", "1.0");
   }
   
   @Test
   public void testGetDependenciesInScopes()
   {
      List<Dependency> deps = facet.getDependenciesInScopes("testCompile", "runtime");
      
      ProjectAssert.assertContainsDependency(deps, "testCompile", "junit", "junit", "4.11");
      ProjectAssert.assertContainsDependency(deps, "runtime", "guice", "com.google.code.guice", "1.0");
   }
   
   @Test
   public void testGetDirectDependency()
   {
      Dependency dep = facet.getDirectDependency(DependencyBuilder.create("junit:junit:4.11"));
      
      assertNotNull(dep);
      assertEquals("junit", dep.getCoordinate().getGroupId());
      assertEquals("junit", dep.getCoordinate().getArtifactId());
      assertEquals("4.11", dep.getCoordinate().getVersion());
      assertEquals("testCompile", dep.getScopeType());
   }
   
   @Test
   public void testGetDirectDependencyOnManaged()
   {
      Dependency dep = facet.getDirectDependency(DependencyBuilder.create("org.group:name:1.0-SNAPSHOT"));
      
      assertNull(dep);
   }
   
   @Test
   public void testGetEffectiveDependencies()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testGetEffectiveDependenciesInScopes()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testGetEffectiveDependency()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testGetEffectiveManagedDependency()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testGetManagedDependencies()
   {
      List<Dependency> deps = facet.getManagedDependencies();
      
      ProjectAssert.assertContainsDependency(deps, "compile", "name", "org.group", "1.0-SNAPSHOT");
   }
   
   @Test
   public void testGetManagedDependency()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testGetProperties()
   {
      // TODO How to declare build properties for Gradle?
   }
   
   @Test
   public void testGetProperty()
   {
      // TODO How to declare build properties for Gradle?
   }
   
   @Test
   public void testGetRepositories()
   {
      List<DependencyRepository> repos = facet.getRepositories();
      
      ProjectAssert.assertContainsRepository(repos, "http://maven-repo.com/");
   }
   
   @Test
   public void testHasDirectDependency()
   {
      assertTrue(facet.hasDirectDependency(DependencyBuilder.create("org.slf4j:slf4j-api:1.7.5")));
   }
   
   @Test
   public void testHasEffectiveDependency()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testHasEffectiveManagedDependency()
   {
      // TODO Make tests for effective dependencies
   }
   
   @Test
   public void testHasDirectManagedDependency()
   {
      // TODO Test multi-project build
   }
   
   @Test
   public void testHasRepository()
   {
      assertTrue(facet.hasRepository("http://maven-repo.com/"));
   }
   
   @Test
   public void testRemoveDependency()
   {
      facet.removeDependency(DependencyBuilder.create("org.slf4j:slf4j-api:1.7.5").setScopeType("compile"));
      
      Project sameProject = projectProvider.findProject();
      DependencyFacet sameFacet = sameProject.getFacet(DependencyFacet.class);
      List<Dependency> deps = sameFacet.getDependencies();
      
      ProjectAssert.assertNotContainsDependency(deps, "compile", "slf4j-api", "org.slf4j", "1.7.5");
   }
   
   @Test
   public void testRemoveManagedDependency()
   {
      facet.removeManagedDependency(DependencyBuilder.create("com.google.code.guice:guice:1.0"));
      
      Project sameProject = projectProvider.findProject();
      DependencyFacet sameFacet = sameProject.getFacet(DependencyFacet.class);
      List<Dependency> managedDeps = sameFacet.getManagedDependencies();
      
      assertEquals(0, managedDeps.size());
   }
   
   @Test
   public void testRemoveProperty()
   {
      // TODO How to declare build properties for Gradle?
   }
   
   @Test
   public void testRemoveRepository()
   {
      facet.removeRepository("http://maven-repo.com/");
      
      Project sameProject = projectProvider.findProject();
      DependencyFacet sameFacet = sameProject.getFacet(DependencyFacet.class);
      List<DependencyRepository> repos = sameFacet.getRepositories();
      
      assertEquals(0, repos.size());
   }
   
   @Test
   public void testResolveAvailableVersionsForDependency()
   {
      // TODO Dependency resolution in Gradle
   }
   
   @Test
   public void testResolveAvailableVersionsForGavs()
   {
      // TODO Dependency resolution in Gradle
   }
   
   @Test
   public void testResolveAvailableVersionsForQuery()
   {
      // TODO Dependency resolution in Gradle
   }
   
   @Test
   public void testResolveProperties()
   {
      // TODO Dependency resolution in Gradle
   }
   
   @Test
   public void testSetProperty()
   {
      // TODO How to declare build properties for Gradle?
   }
}
