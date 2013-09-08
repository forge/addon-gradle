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
public class GradlePluginBuilder implements GradlePlugin
{
   private String clazz;
   
   private GradlePluginBuilder()
   {
   }
   
   public static GradlePluginBuilder create(String name)
   {
      GradlePluginBuilder builder = new GradlePluginBuilder();
      
      builder.clazz = name;
      
      return builder;
   }
   
   public static GradlePluginBuilder create(GradlePlugin plugin)
   {
      GradlePluginBuilder builder = new GradlePluginBuilder();
      
      builder.clazz = plugin.getClazz();
      
      return builder;
   }
   
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

   @Override
   public GradlePluginType getType()
   {
      return GradlePluginType.typeByClazz(clazz);
   }

}
