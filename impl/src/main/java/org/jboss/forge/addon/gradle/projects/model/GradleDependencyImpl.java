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
public class GradleDependencyImpl implements GradleDependency
{
   private final String group;
   private final String name;
   private final String version;
   private final GradleDependencyConfiguration configuration;
   private final String configurationName;

   public GradleDependencyImpl(String group, String name, String version, GradleDependencyConfiguration configuration,
            String configurationName)
   {
      this.group = group;
      this.name = name;
      this.version = version;
      this.configuration = configuration;
      this.configurationName = configurationName;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public String getGroup()
   {
      return group;
   }

   @Override
   public String getVersion()
   {
      return version;
   }

   @Override
   public GradleDependencyConfiguration getConfiguration()
   {
      return configuration;
   }

   @Override
   public String getConfigurationName()
   {
      return configurationName;
   }

   @Override
   public String toGradleString()
   {
      return String.format("%s:%s:%s", group, name, version);
   }

}
