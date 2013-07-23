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
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.furnace.services.Exported;
import org.jboss.shrinkwrap.api.ShrinkWrap;

/**
 * @author Adam Wyłuda
 */
@Exported
public class GradleTestProjectProvider
{
   public static ForgeArchive getDeployment()
   {
      return ShrinkWrap
               .create(ForgeArchive.class)
               .addBeansXML()
               .addClass(GradleTestProjectProvider.class)
               .addAsResource("build.gradle")
               .addAsResource("test-profile.gradle")
               .addAsResource("settings.gradle")
               .addAsResource("src/main/images/forge.txt")
               .addAsResource("src/test/templates/pom.xml")
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace:container-cdi", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:resources", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:gradle", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:projects", "2.0.0-SNAPSHOT")
               );
   }
   
   @Inject
   private Furnace furnace;
   @Inject
   private ProjectFactory projectFactory;
   @Inject
   private ResourceFactory resourceFactory;

   private DirectoryResource projectDir;

   public Project create()
   {
      DirectoryResource addonDir = resourceFactory.create(furnace.getRepositories().get(0).getRootDirectory()).reify(
               DirectoryResource.class);
      projectDir = addonDir.createTempResource();

      FileResource<?> gradleFile = projectDir.getChild("build.gradle").reify(FileResource.class);
      gradleFile.createNewFile();
      gradleFile.setContents(getClass().getResourceAsStream("/build.gradle"));

      FileResource<?> testProfileFile = projectDir.getChild("test-profile.gradle").reify(FileResource.class);
      testProfileFile.createNewFile();
      testProfileFile.setContents(getClass().getResourceAsStream("/test-profile.gradle"));

      FileResource<?> settingsFile = projectDir.getChild("settings.gradle").reify(FileResource.class);
      settingsFile.createNewFile();
      settingsFile.setContents(getClass().getResourceAsStream("/settings.gradle"));

      FileResource<?> forgeFile = projectDir.getChild("src/main/images/forge.txt").reify(FileResource.class);
      forgeFile.createNewFile();
      forgeFile.setContents(getClass().getResourceAsStream("/src/main/images/forge.txt"));

      FileResource<?> pomFile = projectDir.getChild("src/test/templates/pom.xml").reify(FileResource.class);
      pomFile.createNewFile();
      pomFile.setContents(getClass().getResourceAsStream("/src/test/templates/pom.xml"));

      return findProject();
   }

   public Project findProject()
   {
      return projectFactory.findProject(projectDir);
   }

   public void clean()
   {
      projectDir.delete(true);
   }
}
