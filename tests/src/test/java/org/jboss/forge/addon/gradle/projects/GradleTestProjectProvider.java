/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import javax.inject.Inject;

import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.furnace.util.OperatingSystemUtils;
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

   public static AddonArchive getDeployment(String resourcesPath, String... resources)
   {
      AddonArchive archive = ShrinkWrap.create(AddonArchive.class)
               .addBeansXML()
               .addClass(GradleTestProjectProvider.class)
               .addClass(ProjectAssert.class)
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:resources"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:gradle"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:projects"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:parser-java"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:maven"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:configuration"));
      for (String resource : resources)
      {
         archive = archive.addAsResource(resourcesPath + resource);
      }
      return archive;
   }

   @Inject
   private ProjectFactory projectFactory;
   @Inject
   private ResourceFactory resourceFactory;

   private DirectoryResource projectDir;

   public Project create(String projectPath, String resourcesPath, String... resources)
   {
      if (projectDir == null)
      {
         projectDir = (DirectoryResource) resourceFactory.create(OperatingSystemUtils.createTempDir());
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

   private void initResources(String resourcesPath, String... resources)
   {
      for (String resource : resources)
      {
         FileResource<?> resourceFile = projectDir.getChild(resource).reify(FileResource.class);
         resourceFile.createNewFile();
         resourceFile.setContents(getClass().getResourceAsStream("/" + resourcesPath + resource));
      }

      FileResource<?> resourceFile = projectDir.getChild(GradleSourceUtil.FORGE_LIBRARY).reify(FileResource.class);
      resourceFile.createNewFile();
      resourceFile.setContents(getClass().getResourceAsStream(GradleSourceUtil.FORGE_LIBRARY_RESOURCE));
   }
}
