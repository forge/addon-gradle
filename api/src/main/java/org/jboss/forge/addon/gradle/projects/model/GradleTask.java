/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;

/**
 * Represents Gradle project task.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public interface GradleTask
{
   /**
    * Returns name of the task. 
    */
   String getName();
   
   /**
    * Returns list of the task on which this project depends on. 
    */
   List<GradleTask> getDependsOn();
   
   /**
    * Returns implementation code of the task. 
    */
   String getCode();
   
   /**
    * Returns type of the task. 
    */
   String getType();
}
