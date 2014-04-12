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
 * Default implementation of the {@link GradlePlugin}.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public class GradlePluginBuilder implements GradlePlugin
{
   private String clazz = "";

   private GradlePluginBuilder()
   {
   }

   public static GradlePluginBuilder create()
   {
      return new GradlePluginBuilder();
   }
   
   public static GradlePluginBuilder create(GradlePluginType type)
   {
      return new GradlePluginBuilder().setClazz(type.getClazz());
   }

   /**
    * Creates a copy of given plugin.
    */
   public static GradlePluginBuilder create(GradlePlugin plugin)
   {
      GradlePluginBuilder builder = new GradlePluginBuilder();

      builder.clazz = plugin.getClazz();

      return builder;
   }

   /**
    * Performs a deep copy of given plugins.
    */
   public static List<GradlePlugin> deepCopy(List<GradlePlugin> plugins)
   {
      List<GradlePlugin> list = new ArrayList<GradlePlugin>();

      for (GradlePlugin plugin : plugins)
      {
         list.add(create(plugin));
      }

      return list;
   }

   @Override
   public String getClazz()
   {
      return clazz;
   }

   public GradlePluginBuilder setClazz(String clazz)
   {
      this.clazz = clazz;
      return this;
   }

   @Override
   public GradlePluginType getType()
   {
      return GradlePluginType.typeByClazz(clazz);
   }

   public GradlePluginBuilder setType(GradlePluginType type)
   {
      this.clazz = type.getClazz();
      return this;
   }

   @Override
   public String toString()
   {
      return String.format("apply plugin: '%s'", 
               getType() != GradlePluginType.OTHER ? getType().getShortName() : getClazz());
   }
}
