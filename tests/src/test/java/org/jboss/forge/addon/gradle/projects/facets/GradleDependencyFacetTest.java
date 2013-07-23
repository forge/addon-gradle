/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
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

   @Before
   public void setUp()
   {
      project = projectProvider.create();
   }

   @After
   public void cleanUp()
   {
      projectProvider.clean();
   }

   @Test
   public void testReadDependencies()
   {
      DependencyFacet facet = project.getFacet(DependencyFacet.class);
      List<Dependency> deps = facet.getDependencies();

      Map<String, Dependency> depsFromScript = Maps.newHashMap();
      depsFromScript.put("slf4j-api",
               DependencyBuilder.create()
                        .setArtifactId("slf4j-api")
                        .setGroupId("slf4j-simple")
                        .setScopeType("compile"));
      depsFromScript.put("sl4fj-simple",
               DependencyBuilder.create()
                        .setArtifactId("slf4j-simple")
                        .setGroupId("slf4j-simple")
                        .setScopeType("compile"));
      depsFromScript.put("junit",
               DependencyBuilder.create()
                        .setArtifactId("junit")
                        .setGroupId("junit")
                        .setScopeType("test"));

      project.getProjectRoot();

      for (Dependency dep : deps)
      {
         Dependency depFromMap = depsFromScript.get(dep.getCoordinate().getArtifactId());
         if (depFromMap != null)
         {
            assertEquals(depFromMap.getCoordinate().getArtifactId(), dep.getCoordinate().getArtifactId());
            assertEquals(depFromMap.getCoordinate().getGroupId(), dep.getCoordinate().getGroupId());
            assertEquals(depFromMap.getScopeType(), dep.getScopeType());
            depsFromScript.remove(dep.getCoordinate().getArtifactId());
         }
      }
      assertEquals(0, depsFromScript.size());
   }

   @Test
   public void testWriteDependencies()
   {
      DependencyFacet facet = project.getFacet(DependencyFacet.class);

      facet.addDirectDependency(
               DependencyBuilder
                        .create()
                        .setArtifactId("mydep")
                        .setGroupId("mygroup")
                        .setVersion("myversion")
                        .setScopeType("runtime"));

      Project theSameProject = projectProvider.findProject();
      DependencyFacet theNewFacet = theSameProject.getFacet(DependencyFacet.class);

      boolean newDependencyFound = false;
      for (Dependency dep : theNewFacet.getDependencies())
      {
         if (dep.getCoordinate().getArtifactId().equals("mydep"))
         {
            assertEquals("mygroup", dep.getCoordinate().getGroupId());
            assertEquals("runtime", dep.getScopeType());
            assertEquals("myversion", dep.getCoordinate().getVersion());
            newDependencyFound = true;
            break;
         }
      }
      assertTrue(newDependencyFound);
   }
}
