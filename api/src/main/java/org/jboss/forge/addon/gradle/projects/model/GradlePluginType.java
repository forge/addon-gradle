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
 * Common Gradle plugin types.
 * 
 * @author Adam Wyłuda
 */
public enum GradlePluginType
{
   OTHER("");

   private static class TypeContainer
   {
      private static final Map<String, GradlePluginType> TYPE_MAP = new HashMap<String, GradlePluginType>();
   }

   private final String clazz;

   private GradlePluginType(String clazz)
   {
      this.clazz = clazz;
      TypeContainer.TYPE_MAP.put(clazz, this);
   }
   
   public String getClazz() {
      return clazz;
   }
   
   /**
    * @return Plugin type for given class. If there is no such type then it returns {@link #OTHER}.
    */
   public static GradlePluginType typeByClazz(String clazz) {
      GradlePluginType type = TypeContainer.TYPE_MAP.get(clazz);
      return type != null ? type : OTHER;
   }
}
