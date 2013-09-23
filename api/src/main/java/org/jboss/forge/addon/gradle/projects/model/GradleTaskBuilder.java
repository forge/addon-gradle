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
 * Default implementation of {@link GradleTask}.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public class GradleTaskBuilder implements GradleTask
{
   private String name = "forgeTask";
   private List<GradleTask> dependsOn = new ArrayList<GradleTask>();
   private String type = "";
   private String code = "";

   private GradleTaskBuilder()
   {
   }

   public static GradleTaskBuilder create()
   {
      return new GradleTaskBuilder();
   }
   
   /**
    * Creates a copy of given task. 
    */
   public static GradleTaskBuilder create(GradleTask task)
   {
      GradleTaskBuilder builder = new GradleTaskBuilder();
      
      builder.name = task.getName();
      builder.dependsOn = task.getDependsOn();
      builder.type = task.getType();
      builder.code = task.getCode();
      
      return builder;
   }
   
   /**
    * Performs a deep copy of given tasks.
    */
   public static List<GradleTask> deepCopy(List<GradleTask> tasks)
   {
      List<GradleTask> lists = new ArrayList<GradleTask>();
      
      for (GradleTask task : tasks)
      {
         lists.add(create(task));
      }
      
      return lists;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public GradleTaskBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   @Override
   public List<GradleTask> getDependsOn()
   {
      return dependsOn;
   }

   public GradleTaskBuilder setDependsOn(List<GradleTask> dependsOn)
   {
      this.dependsOn = dependsOn;
      return this;
   }

   public GradleTaskBuilder setDependsOn(GradleTask task)
   {
      dependsOn.add(task);
      return this;
   }

   @Override
   public String getType()
   {
      return type;
   }

   public GradleTaskBuilder setType(String type)
   {
      this.type = type;
      return this;
   }

   @Override
   public String getCode()
   {
      return code;
   }

   public GradleTaskBuilder setCode(String code)
   {
      this.code = code;
      return this;
   }
   
   @Override
   public String toString()
   {
      return String.format("%s(type: '%s', dependsOn: %s) {\n%s\n}", name, type, dependsOn, code);
   }
}
