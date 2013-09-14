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
 * Default implementation of {@link GradleProfile}.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public class GradleProfileBuilder implements GradleProfile
{
   private String name = "";
   private GradleModel model = GradleModelBuilder.create();
   
   private GradleProfileBuilder()
   {
   }
   
   public static GradleProfileBuilder create()
   {
      return new GradleProfileBuilder();
   }

   /**
    * Creates a copy of given profile. 
    */
   public static GradleProfileBuilder create(GradleProfile profile)
   {
      GradleProfileBuilder builder = new GradleProfileBuilder();
      
      builder.name = profile.getName();
      builder.model = GradleModelBuilder.create(profile.getModel());
      
      return builder;
   }
   
   /**
    * Performs a deep copy of given profiles. 
    */
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
   public GradleModel getModel()
   {
      return model;
   }

   public GradleProfileBuilder setModel(GradleModel model)
   {
      this.model = GradleModelBuilder.create(model);
      return this;
   }
}
