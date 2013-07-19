/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.furnace.services.Exported;

/**
 * @author Adam Wyłuda
 */
@Exported
public interface GradleModelLoader
{
   /**
    * Parses XML source into Gradle model, setting given file resource as Gradle resource.
    */
   public GradleModel loadFromXML(FileResource<?> fileResource, String source);
}
