/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.furnace.services.Exported;

/**
 * Simple caching service for Gradle project models.
 * 
 * @author Adam Wyłuda
 */
@Exported
public interface GradleProjectCache
{
   GradleModel getModel(String buildScriptPath);
   
   void putModel(String buildScriptPath, GradleModel model);
}
