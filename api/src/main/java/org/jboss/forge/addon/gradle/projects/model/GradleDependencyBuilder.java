/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import org.jboss.forge.addon.gradle.projects.util.Preconditions;

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

   public static GradleDependencyBuilder create()
   {
      return new GradleDependencyBuilder();
   }

   /**
    * Creates gradle dependency using given configuration and parsing gradleString in format: {@code group:name:version}
    */
   public static GradleDependencyBuilder fromGradleString(String configuration, String gradleString)
   {
      String[] split = gradleString.split(":");
      Preconditions.checkArgument(split.length == 3, "Invalid gradle string format");
      String group = split[0];
      String name = split[1];
      String version = split[2];
      return create()
               .setName(name)
               .setGroup(group)
               .setVersion(version)
               .setConfiguration(configuration);
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

   /**
    * Compares this builder to given {@link GradleDependency}.  
    */
   public boolean equalsToDependency(GradleDependency dep)
   {
      return group.equals(dep.getGroup()) && name.equals(dep.getName()) && version.equals(dep.getVersion())
               && configuration.equals(dep.getConfigurationName());
   }

   /**
    * Does the same thing as {@link #equalsToDependency(GradleDependency)} but only compares group and name.
    */
   public boolean equalsToDirectDependency(GradleDependency dep)
   {
      return group.equals(dep.getGroup()) && name.equals(dep.getName());
   }
}
