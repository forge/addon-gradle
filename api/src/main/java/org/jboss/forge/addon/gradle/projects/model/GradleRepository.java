/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

/**
 * Default implementation of {@link GradleRepository}.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public interface GradleRepository
{
   /**
    * Returns name of the repository. 
    */
   String getName();
   
   /**
    * Returns URL of the repository. 
    */
   String getUrl();
}
