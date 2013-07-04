/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.List;

import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;

/**
 * @author Adam Wyłuda
 */
public class GradleUtil
{
   public static String insertDependency(String source, String name, String group, String version, String configuration)
   {
      // TODO
      return source;
   }

   public static String removeDependency(String source, String name, String group, String version, String configuration)
            throws UnremovableElementException
   {
      // TODO
      return source;
   }

   public static String insertPlugin(String source, String clazz)
   {
      // TODO
      return source;
   }

   public static String removePlugin(String source, String clazz)
            throws UnremovableElementException
   {
      // TODO
      return source;
   }

   public static String insertRepository(String source, String name, String url)
   {
      // TODO
      return source;
   }

   public static String removeRepository(String source, String name, String url)
            throws UnremovableElementException
   {
      // TODO
      return source;
   }

   public static String insertTask(String source, String name, List<String> dependsOn, String type, String code)
   {
      // TODO
      return source;
   }

   /**
    * Checks if source contains profile declaration and if not, then it inserts it.
    */
   public static String checkForIncludeProfileAndInsert(String source)
   {
      // TODO
      return source;
   }

}
