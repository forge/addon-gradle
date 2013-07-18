/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.io.IOException;

import org.jboss.forge.addon.gradle.projects.model.GradleModel;

/**
 * Simple caching service for Gradle project models.
 * 
 * @author Adam Wyłuda
 */
public interface GradleProjectCache
{
   GradleModel getModel(String buildScriptPath) throws IOException;
}
