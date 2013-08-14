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
 * Performs Gradle specific operations.
 * 
 * @author Adam Wyłuda
 */
public interface GradleFacet extends ProjectFacet
{
   /**
    * Executes Gradle build with specified task.
    * 
    * @param task Task to be executed.
    */
   boolean executeTask(String task);

   /**
    * Runs Gradle applying given profile.
    * 
    * @param profile Used profile.
    * @see GradleFacet#executeTask(String)
    * @see GradleProfile
    */
   boolean executeTask(String task, String profile, String... arguments);

   GradleModel getModel();
   
   void setModel(GradleModel model);

   FileResource<?> getBuildScriptResource();
   
   FileResource<?> getSettingsScriptResource();
}
