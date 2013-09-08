/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Wyłuda
 */
public class GradleProfileBuilder implements GradleProfile
{
   private String name = "";
   private GradleDirectModel model = GradleDirectModelBuilder.create();
   
   private GradleProfileBuilder()
   {
   }
   
   public static GradleProfileBuilder create()
   {
      return new GradleProfileBuilder();
   }
   
   public static GradleProfileBuilder create(GradleProfile profile)
   {
      GradleProfileBuilder builder = new GradleProfileBuilder();
      
      builder.name = profile.getName();
      builder.model = GradleDirectModelBuilder.create(profile.getModel());
      
      return builder;
   }
   
   public static List<GradleProfile> deepCopy(List<GradleProfile> profiles)
   {
      List<GradleProfile> list = new ArrayList<GradleProfile>();
      
      for (GradleProfile profile : profiles)
      {
         list.add(create(profile));
      }
      
      return list;
   }

   @Override
   public String getName()
   {
      return name;
   }
   
   public GradleProfileBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   @Override
   public GradleDirectModel getModel()
   {
      return model;
   }

   public GradleProfileBuilder setModel(GradleDirectModel model)
   {
      this.model = GradleDirectModelBuilder.create(model);
      return this;
   }
}
