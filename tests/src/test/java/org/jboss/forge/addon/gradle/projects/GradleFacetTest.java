/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.gradle.projects.model.GradleTask;
import org.jboss.forge.addon.gradle.projects.model.GradleTaskBuilder;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.arquillian.Addon;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Adam Wyłuda
 */
@RunWith(Arquillian.class)
public class GradleFacetTest
{
   @Deployment
   @Dependencies({
            @Addon(name = "org.jboss.forge.addon:resources", version = "2.0.0-SNAPSHOT"),
            @Addon(name = "org.jboss.forge.addon:projects", version = "2.0.0-SNAPSHOT"),
            @Addon(name = "org.jboss.forge.addon:gradle", version = "2.0.0-SNAPSHOT")
   })
   public static ForgeArchive getDeployment()
   {
      return ShrinkWrap
               .create(ForgeArchive.class)
               .addBeansXML()
               .addAsAddonDependencies(
                        AddonDependencyEntry.create(AddonId.from("org.jboss.forge.addon:gradle", "2.0.0-SNAPSHOT")),
                        AddonDependencyEntry.create(AddonId.from("org.jboss.forge.addon:projects", "2.0.0-SNAPSHOT"))
               );
   }

   @Inject
   private ProjectFactory projectFactory;

   private Project project;

   @Before
   public void setUp()
   {
      project = projectFactory.createTempProject();
   }

   @Test
   public void testReadTaskList()
   {
      GradleFacet facet = project.getFacet(GradleFacet.class);

      List<GradleTask> tasks = facet.getModel().getTasks();

      List<String> taskNames = Lists.newArrayListWithCapacity(tasks.size());
      for (GradleTask task : tasks)
      {
         taskNames.add(task.getName());
      }

      assertTrue(taskNames.contains("abc"));
      assertTrue(taskNames.contains("ghi"));
      assertTrue(taskNames.contains("taskNum1"));
      assertTrue(taskNames.contains("taskNum2"));
      assertTrue(taskNames.contains("taskNum3"));
      assertTrue(taskNames.contains("taskNum4"));
      assertTrue(taskNames.contains("taskNum5"));
   }

   @Test
   public void testCreateTask()
   {
      GradleFacet facet = project.getFacet(GradleFacet.class);

      facet.getModel().createTask(
               GradleTaskBuilder
                        .create()
                        .setName("myTask")
                        .setDependsOn("abc")
                        .setType("Copy")
                        .setCode("println 'myTask!'"));

      Project theSameProject = projectFactory.findProject(project.getProjectRoot());
      GradleFacet newGradleFacet = theSameProject.getFacet(GradleFacet.class);
      
      boolean containsMyTask = false;
      for (GradleTask task : newGradleFacet.getModel().getTasks()) {
         if (task.getName().equals("myTask")) {
            containsMyTask = true;
            break;
         }
      }
      assertTrue(containsMyTask);
   }
}
