/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.projects.ProjectFacet;

/**
 * Performs Gradle specific operations.
 * 
 * @author Adam Wyłuda
 */
public interface GradleFacet extends ProjectFacet
{
   void executeTask(String task);
   
   void executeTask(String task, String profile);
   
   GradleModel getModel();
}
