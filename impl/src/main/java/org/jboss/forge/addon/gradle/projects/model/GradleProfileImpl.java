/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import org.jboss.forge.addon.resource.FileResource;

/**
 * @author Adam Wyłuda
 */
public class GradleProfileImpl implements GradleProfile
{
   private final String name;
   private GradleEffectiveModel model;
   private FileResource<?> profileResource;

   public GradleProfileImpl(String name, GradleEffectiveModel model)
   {
      this.name = name;
      this.model = model;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public GradleEffectiveModel getModel()
   {
      return model;
   }

   @Override
   public void setModel(GradleEffectiveModel model)
   {
      this.model = model;
   }

   @Override
   public FileResource<?> getProfileScriptResource()
   {
      return profileResource;
   }
   
   @Override
   public void setProfileScriptResource(FileResource<?> profileResource)
   {
      this.profileResource = profileResource;
   }
}
