/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.io.File;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.eclipse.EclipseProject;
import org.gradle.tooling.model.idea.IdeaModule;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.furnace.services.Exported;

/**
 * Provides Gradle connection.
 * 
 * @author Adam Wyłuda
 */
@Exported
public class GradleConnectionManager
{
   public ProjectConnection connectionFor(DirectoryResource dir)
   {
      GradleConnector connector = GradleConnector.newConnector();
      ProjectConnection connection = connector
               .forProjectDirectory(dir.getUnderlyingResourceObject())
               .useGradleUserHomeDir(new File(System.getenv("GRADLE_HOME")))
               .connect();
      return connection;
   }
}
