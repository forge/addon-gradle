/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

/**
 * @author Adam Wyłuda
 */
public class GradleDependencyBuilder
{
   private String name;
   private String group;
   private String version;
   private String configuration;
   
   private GradleDependencyBuilder()
   {
   }
   
   public static GradleDependencyBuilder create() {
      return new GradleDependencyBuilder();
   }

   public String getName()
   {
      return name;
   }

   public GradleDependencyBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   public String getGroup()
   {
      return group;
   }

   public GradleDependencyBuilder setGroup(String group)
   {
      this.group = group;
      return this;
   }

   public String getVersion()
   {
      return version;
   }

   public GradleDependencyBuilder setVersion(String version)
   {
      this.version = version;
      return this;
   }

   public String getConfiguration()
   {
      return configuration;
   }

   public GradleDependencyBuilder setConfiguration(String configuration)
   {
      this.configuration = configuration;
      return this;
   }
}
