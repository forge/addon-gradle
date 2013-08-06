/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This is equivalent to Maven scope.
 * 
 * @author Adam Wyłuda
 */
public enum GradleDependencyConfiguration
{
   COMPILE("compile"), RUNTIME("runtime"), TEST_COMPILE("testCompile"), TEST_RUNTIME("testRuntime"),
   
   /**
    * Direct dependency configuration (which doesn't have defined version and config).
    */
   DIRECT("direct"),

   /**
    * Dependency configuration not defined in {@link GradleDependencyConfiguration}.
    */
   OTHER("");

   private static class ConfigContainer
   {
      private static final Map<String, GradleDependencyConfiguration> CONFIG_MAP = new HashMap<String, GradleDependencyConfiguration>();
   }

   private final String name;

   private GradleDependencyConfiguration(String name)
   {
      this.name = name;
      ConfigContainer.CONFIG_MAP.put(name, this);
   }

   public String getName()
   {
      return name;
   }

   /**
    * Searches map for config with specified name.
    * 
    * @param name Name of the configuration.
    * @return Configuration with specified name, if it doesn't exist, returns {@link #OTHER}.
    */
   public static GradleDependencyConfiguration configByName(String name)
   {
      GradleDependencyConfiguration config = ConfigContainer.CONFIG_MAP.get(name); 
      return config != null ? config : OTHER;
   }
}
