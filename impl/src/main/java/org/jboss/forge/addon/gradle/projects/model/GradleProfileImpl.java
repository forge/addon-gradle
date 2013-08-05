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
   private final GradleModel model;
   private FileResource<?> profileResource;

   public GradleProfileImpl(String name, GradleModel model)
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
   public GradleModel getModel()
   {
      return model;
   }

   @Override
   public void setModel(GradleModel model)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public FileResource<?> getProfileResource()
   {
      return profileResource;
   }
   
   public void setProfileResource(FileResource<?> profileResource)
   {
      this.profileResource = profileResource;
   }
}
