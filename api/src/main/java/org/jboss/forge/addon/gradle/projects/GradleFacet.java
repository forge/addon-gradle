/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleProfile;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.resource.FileResource;

/**
 * Main Gradle project facet. Responsible for loading and saving of the project model.
 * 
 * <p>
 * 
 * Example of usage (adding a dependency):
 * <pre>
 * GradleModelBuilder builder = GradleModelBuilder.create(gradleFacet.getModel());
 * builder.addDependency(GradleDependencyBuilder.create("compile", "org.x:y:1.0"));
 * gradleFacet.setModel(builder);
 * </pre>
 * 
 * @see GradleModel
 * @see GradleModelBuilder
 * 
 * @author Adam Wyłuda
 */
public interface GradleFacet extends ProjectFacet
{
   /**
    * Executes Gradle build with specified task.
    */
   boolean executeTask(String task);

   /**
    * Runs Gradle applying given profile.
    * 
    * @see GradleProfile
    */
   boolean executeTask(String task, String profile, String... arguments);

   /**
    * Returns evaluated Gradle project model. 
    */
   GradleModel getModel();
   
   /**
    * Merges all changes with the old model and persists them to the build script.
    */
   void setModel(GradleModel model);

   /**
    * Returns file resource pointing to the build.gradle script of the project. 
    */
   FileResource<?> getBuildScriptResource();
   
   /**
    * Returns file resource pointing to the build.gradle script of the root project. 
    */
   FileResource<?> getSettingsScriptResource();
}
