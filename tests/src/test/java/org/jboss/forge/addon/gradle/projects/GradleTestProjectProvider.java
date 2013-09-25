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
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;

import com.google.common.base.Strings;

/**
 * @author Adam Wyłuda
 */
public class GradleTestProjectProvider
{
   public static final String SIMPLE_RESOURCES_PATH = "simple/";
   public static final String[] SIMPLE_RESOURCES = new String[] {
            "build.gradle",
            "test-profile.gradle",
            "settings.gradle",
            "src/main/interfaces/org/testproject/Service.java",
            "src/main/images/forge.txt",
            "src/test/mocks/org/testproject/TestMainClass.java",
            "src/test/templates/pom.xml"
   };

   public static final String COMPLEX_RESOURCES_PATH = "complex/";
   public static final String[] COMPLEX_RESOURCES = new String[] {
            "build.gradle",
            "settings.gradle",
            "subproject/build.gradle"
   };

   public static ForgeArchive getDeployment(String resourcesPath, String... resources)
   {
      ForgeArchive archive = ShrinkWrap.create(ForgeArchive.class)
               .addBeansXML()
               .addClass(GradleTestProjectProvider.class)
               .addClass(ProjectAssert.class)
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:resources", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:gradle", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:projects", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:parser-java", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:maven", "2.0.0-SNAPSHOT")
               );
      for (String resource : resources)
      {
         archive = archive.addAsResource(resourcesPath + resource);
      }
      archive = archive.addAsResource("forge.gradle");
      return archive;
   }

   @Inject
   private Furnace furnace;
   @Inject
   private ProjectFactory projectFactory;
   @Inject
   private ResourceFactory resourceFactory;

   private DirectoryResource projectDir;

   public Project create(String projectPath, String resourcesPath, String... resources)
   {
      if (projectDir == null) {
         DirectoryResource addonDir = resourceFactory.create(furnace.getRepositories().get(0).getRootDirectory()).reify(
                  DirectoryResource.class);
   
         projectDir = addonDir.createTempResource();
      }
      
      initResources(resourcesPath, resources);

      if (!Strings.isNullOrEmpty(projectPath))
      {
         projectDir = projectDir.getChildDirectory(projectPath);
      }

      return findProject();
   }

   public Project findProject()
   {
      return projectFactory.findProject(projectDir);
   }

   public void clean()
   {
      for (Resource<?> fileResource : projectDir.listResources())
      {
         fileResource.delete(true);
      }
   }

   private void initResources(String resourcesPath, String... resources)
   {
      for (String resource : resources)
      {
         FileResource<?> resourceFile = projectDir.getChild(resource).reify(FileResource.class);
         resourceFile.createNewFile();
         resourceFile.setContents(getClass().getResourceAsStream("/" + resourcesPath + resource));
      }
      FileResource<?> resourceFile = projectDir.getChild("forge.gradle").reify(FileResource.class);
      resourceFile.createNewFile();
      resourceFile.setContents(getClass().getResourceAsStream("/forge.gradle"));
   }
}
