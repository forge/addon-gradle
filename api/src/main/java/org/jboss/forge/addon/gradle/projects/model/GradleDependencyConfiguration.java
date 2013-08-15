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
   COMPILE("compile", "compile"),
   RUNTIME("runtime", "runtime"),
   TEST_COMPILE("testCompile", "test"),
   TEST_RUNTIME("testRuntime", "test"),

   /**
    * Direct dependency configuration (which doesn't have defined version and config).
    */
   DIRECT("direct", null),

   /**
    * Dependency configuration not defined in {@link GradleDependencyConfiguration}.
    */
   OTHER("", "compile");

   private static class ConfigContainer
   {
      private static final Map<String, GradleDependencyConfiguration> BY_NAME_MAP =
               new HashMap<String, GradleDependencyConfiguration>();
      private static final Map<String, GradleDependencyConfiguration> BY_MAVEN_SCOPE_MAP = 
               new HashMap<String, GradleDependencyConfiguration>();
   }
   
   static {
      ConfigContainer.BY_MAVEN_SCOPE_MAP.put("compile", COMPILE);
      ConfigContainer.BY_MAVEN_SCOPE_MAP.put("provided", COMPILE);
      ConfigContainer.BY_MAVEN_SCOPE_MAP.put("runtime", RUNTIME);
      ConfigContainer.BY_MAVEN_SCOPE_MAP.put("test", TEST_COMPILE);
      ConfigContainer.BY_MAVEN_SCOPE_MAP.put("system", COMPILE);
      ConfigContainer.BY_MAVEN_SCOPE_MAP.put("import", COMPILE);
   }

   private final String name;
   private final String mavenScope;

   private GradleDependencyConfiguration(String name, String mavenScope)
   {
      this.name = name;
      this.mavenScope = mavenScope;
      ConfigContainer.BY_NAME_MAP.put(name, this);
   }

   public String getName()
   {
      return name;
   }

   public String toMavenScope()
   {
      return mavenScope;
   }

   /**
    * Searches map for config with specified name.
    * 
    * @param name Name of the configuration.
    * @return Configuration with specified name, if it doesn't exist, returns {@link #OTHER}.
    */
   public static GradleDependencyConfiguration fromName(String name)
   {
      GradleDependencyConfiguration config = ConfigContainer.BY_NAME_MAP.get(name);
      return config != null ? config : OTHER;
   }

   /**
    * Returns Gradle config corresponding to given maven scope.
    */
   public static GradleDependencyConfiguration fromMavenScope(String mavenScope)
   {
      return ConfigContainer.BY_MAVEN_SCOPE_MAP.get(mavenScope);
   }
}
