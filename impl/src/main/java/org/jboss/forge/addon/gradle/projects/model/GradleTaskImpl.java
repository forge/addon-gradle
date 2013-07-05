/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.Set;

/**
 * @author Adam Wyłuda
 */
public class GradleTaskImpl implements GradleTask
{
   private final String name;
   private final Set<GradleTask> dependsOn;

   public GradleTaskImpl(String name, Set<GradleTask> dependsOn)
   {
      this.name = name;
      this.dependsOn = dependsOn;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public Set<GradleTask> getDependsOn()
   {
      return dependsOn;
   }
}
