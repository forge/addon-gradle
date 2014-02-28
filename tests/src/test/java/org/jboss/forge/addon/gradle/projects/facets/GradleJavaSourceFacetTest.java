/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.gradle.projects.GradleTestProjectProvider;
import org.jboss.forge.addon.gradle.projects.ProjectAssert;
import org.jboss.forge.addon.parser.java.JavaSourceFactory;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.parser.java.resources.JavaResourceVisitor;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.visit.VisitContext;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Wyłuda
 */
@RunWith(Arquillian.class)
public class GradleJavaSourceFacetTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:resources"),
            @AddonDependency(name = "org.jboss.forge.addon:projects"),
            @AddonDependency(name = "org.jboss.forge.addon:parser-java"),
            @AddonDependency(name = "org.jboss.forge.addon:gradle"),
            @AddonDependency(name = "org.jboss.forge.addon:maven"),
            @AddonDependency(name = "org.jboss.forge.addon:configuration")
   })
   public static ForgeArchive getDeployment()
   {
      return GradleTestProjectProvider.getDeployment(
               GradleTestProjectProvider.SIMPLE_RESOURCES_PATH,
               GradleTestProjectProvider.SIMPLE_RESOURCES);
   }

   @Inject
   private GradleTestProjectProvider injectedProjectProvider;
   private Project project;
   private JavaSourceFacet facet;

   @Inject
   private JavaSourceFactory factory;

   @Before
   public void setUp()
   {
      project = injectedProjectProvider.create("",
               GradleTestProjectProvider.SIMPLE_RESOURCES_PATH,
               GradleTestProjectProvider.SIMPLE_RESOURCES);
      facet = project.getFacet(JavaSourceFacet.class);
   }

   @Test
   public void testCalculateName() throws FileNotFoundException
   {
      JavaResource res = facet.getJavaResource("org/testproject/Service.java");
      String name = facet.calculateName(res);
      assertEquals("Service", name);
   }

   @Test
   public void testCalculatePackage() throws FileNotFoundException
   {
      JavaResource res = facet.getJavaResource("org/testproject/Service.java");
      String pack = facet.calculatePackage(res);
      assertEquals("org.testproject", pack);
   }

   @Test
   public void testGetBasePackage()
   {
      String basePackage = facet.getBasePackage();
      assertEquals("org.testproject", basePackage);
   }

   @Test
   public void testGetBasePackageResource()
   {
      DirectoryResource res = facet.getBasePackageDirectory();
      assertEquals("testproject", res.getName());
      assertEquals("org", res.getParent().getName());
   }

   @Test
   public void testGetSourceFolders()
   {
      List<DirectoryResource> folders = facet.getSourceDirectories();
      assertEquals(4, folders.size());
      ProjectAssert.assertContainsDirectoryNamed(folders, "src/main/java");
      ProjectAssert.assertContainsDirectoryNamed(folders, "src/main/interfaces");
      ProjectAssert.assertContainsDirectoryNamed(folders, "src/test/java");
      ProjectAssert.assertContainsDirectoryNamed(folders, "src/test/mocks");
   }

   @Test
   public void testGetSourceFolder()
   {
      DirectoryResource folder = facet.getSourceDirectory();
      ProjectAssert.assertDirectoryIsOneOf(folder, "src/main/java", "src/main/interfaces");
   }

   @Test
   public void testGetTestSourceFolder()
   {
      DirectoryResource folder = facet.getTestSourceDirectory();
      ProjectAssert.assertDirectoryIsOneOf(folder, "src/test/java", "src/test/mocks");
   }

   @Test
   public void testSaveJavaSource() throws FileNotFoundException
   {
      JavaSource<JavaClass> source = factory.create(JavaClass.class);
      source.setName("ServiceImpl");
      source.setPackage("org.testproject.impl");
      source.setPublic();
      facet.saveJavaSource(source);

      JavaResource res = facet.getJavaResource("org/testproject/impl/ServiceImpl.java");
      assertTrue(res.exists());
      assertEquals("ServiceImpl", res.getJavaSource().getName());
      assertEquals("org.testproject.impl", res.getJavaSource().getPackage());
      assertTrue(res.getJavaSource().isPublic());
   }

   @Test
   public void testSaveTestJavaSource() throws FileNotFoundException
   {
      JavaSource<JavaClass> source = factory.create(JavaClass.class);
      source.setName("ServiceImplTest");
      source.setPackage("org.testproject.impl.tests");
      source.setPackagePrivate();
      facet.saveTestJavaSource(source);

      JavaResource res = facet.getTestJavaResource("org/testproject/impl/tests/ServiceImplTest.java");
      assertTrue(res.exists());
      assertEquals("ServiceImplTest", res.getJavaSource().getName());
      assertEquals("org.testproject.impl.tests", res.getJavaSource().getPackage());
      assertTrue(res.getJavaSource().isPackagePrivate());
   }

   @Test
   public void testGetJavaResource() throws FileNotFoundException
   {
      JavaResource res = facet.getJavaResource("org/testproject/Service.java");
      assertTrue(res.exists());
      assertEquals(1, res.getJavaSource().getMembers().size());
      assertEquals("someMethod", res.getJavaSource().getMembers().get(0).getName());
   }

   @Test
   public void testGetTestJavaResource() throws FileNotFoundException
   {
      JavaResource res = facet.getTestJavaResource("org/testproject/TestMainClass.java");
      assertTrue(res.exists());
      assertEquals(1, res.getJavaSource().getMembers().size());
      assertEquals("main", res.getJavaSource().getMembers().get(0).getName());
   }

   @Test
   public void testVisitJavaSources()
   {
      class Holder
      {
         boolean value = false;
      }
      final Holder holder = new Holder();
      facet.visitJavaSources(new JavaResourceVisitor()
      {

         @Override
         public void visit(VisitContext context, JavaResource javaResource)
         {
            try
            {
               if (javaResource.getJavaSource().getName().equals("Service"))
               {
                  holder.value = true;
               }
               else
               {
                  fail("Another java source found");
               }
            }
            catch (FileNotFoundException e)
            {
               fail(e.getMessage());
            }
         }
      });
      assertTrue(holder.value);
   }

   @Test
   public void testVisitJavaTestSources()
   {
      class Holder
      {
         boolean value = false;
      }
      final Holder holder = new Holder();
      facet.visitJavaTestSources(new JavaResourceVisitor()
      {

         @Override
         public void visit(VisitContext context, JavaResource javaResource)
         {
            try
            {
               if (javaResource.getJavaSource().getName().equals("TestMainClass"))
               {
                  holder.value = true;
               }
               else
               {
                  fail("Another test java source found");
               }
            }
            catch (FileNotFoundException e)
            {
               fail(e.getMessage());
            }
         }
      });
      assertTrue(holder.value);
   }
}
