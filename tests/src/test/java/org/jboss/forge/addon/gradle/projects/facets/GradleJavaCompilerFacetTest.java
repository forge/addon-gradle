/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet.CompilerVersion;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Wyłuda
 */
@RunWith(Arquillian.class)
public class GradleJavaCompilerFacetTest
{
   @Deployment
   @AddonDependencies({
            @AddonDependency(name = "org.jboss.forge.addon:resources"),
            @AddonDependency(name = "org.jboss.forge.addon:projects"),
            @AddonDependency(name = "org.jboss.forge.addon:parser-java"),
            @AddonDependency(name = "org.jboss.forge.addon:gradle"),
            @AddonDependency(name = "org.jboss.forge.addon:maven"),
            @AddonDependency(name = "org.jboss.forge.addon:configuration")
   })
   public static AddonArchive getDeployment()
   {
      return GradleTestProjectProvider.getDeployment(
               GradleTestProjectProvider.SIMPLE_RESOURCES_PATH,
               GradleTestProjectProvider.SIMPLE_RESOURCES);
   }

   @Inject
   private GradleTestProjectProvider projectProvider;
   private Project project;
   private JavaCompilerFacet facet;

   @Before
   public void setUp()
   {
      project = projectProvider.create("",
               GradleTestProjectProvider.SIMPLE_RESOURCES_PATH,
               GradleTestProjectProvider.SIMPLE_RESOURCES);
      facet = project.getFacet(JavaCompilerFacet.class);
   }

   @Test
   public void testGetSourceAndTargetJavaVersion() throws FileNotFoundException
   {
      assertEquals(CompilerVersion.JAVA_1_5, facet.getSourceCompilerVersion());
      assertEquals(CompilerVersion.JAVA_1_7, facet.getTargetCompilerVersion());
   }

   @Test
   public void testSetSourceAndTargetJavaVersion() throws FileNotFoundException
   {
      facet.setSourceCompilerVersion(CompilerVersion.JAVA_1_4);
      facet.setTargetCompilerVersion(CompilerVersion.JAVA_1_6);

      Project sameProject = projectProvider.findProject();
      JavaCompilerFacet sameFacet = sameProject.getFacet(JavaCompilerFacet.class);

      assertEquals(CompilerVersion.JAVA_1_4, sameFacet.getSourceCompilerVersion());
      assertEquals(CompilerVersion.JAVA_1_6, sameFacet.getTargetCompilerVersion());
   }
}
