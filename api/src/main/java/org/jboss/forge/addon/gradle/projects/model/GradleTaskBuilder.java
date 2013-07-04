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
public class GradleTaskBuilder
{
   private String name = "forgeTask";
   private List<String> dependsOn = new ArrayList<String>();
   private String type = "";
   private String code = "";

   private GradleTaskBuilder()
   {
   }

   public static GradleTaskBuilder create()
   {
      return new GradleTaskBuilder();
   }

   public String getName()
   {
      return name;
   }

   public GradleTaskBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   public List<String> getDependsOn()
   {
      return dependsOn;
   }

   public GradleTaskBuilder setDependsOn(List<String> dependsOn)
   {
      this.dependsOn = dependsOn;
      return this;
   }
   
   public GradleTaskBuilder setDependsOn(String dependsOn) {
      this.dependsOn.add(dependsOn);
      return this;
   }

   public GradleTaskBuilder setDependsOn(GradleTask task)
   {
      dependsOn.add(task.getName());
      return this;
   }

   public String getType()
   {
      return type;
   }

   public GradleTaskBuilder setType(String type)
   {
      this.type = type;
      return this;
   }

   public String getCode()
   {
      return code;
   }

   public GradleTaskBuilder setCode(String code)
   {
      this.code = code;
      return this;
   }
}
