/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Adam Wyłuda
 */
public class GradleDependencyBuilderTest
{
   @Test
   public void testCreateFromStringSimple()
   {
      GradleDependency dep = GradleDependencyBuilder
               .create("compile", "group:name:version");
      
      assertEquals("compile", dep.getConfigurationName());
      assertEquals("group", dep.getGroup());
      assertEquals("name", dep.getName());
      assertEquals("version", dep.getVersion());
      assertEquals("", dep.getClassifier());
      assertEquals("jar", dep.getPackaging());
   }
   
   @Test
   public void testCreateFromStringWithClassifier()
   {
      GradleDependency dep = GradleDependencyBuilder
               .create("compile", "group:name:version:classifier");
      
      assertEquals("compile", dep.getConfigurationName());
      assertEquals("group", dep.getGroup());
      assertEquals("name", dep.getName());
      assertEquals("version", dep.getVersion());
      assertEquals("classifier", dep.getClassifier());
      assertEquals("jar", dep.getPackaging());
   }
   
   @Test
   public void testCreateFromStringWithPackaging()
   {
      GradleDependency dep = GradleDependencyBuilder
               .create("compile", "group:name:version@packaging");
      
      assertEquals("compile", dep.getConfigurationName());
      assertEquals("group", dep.getGroup());
      assertEquals("name", dep.getName());
      assertEquals("version", dep.getVersion());
      assertEquals("", dep.getClassifier());
      assertEquals("packaging", dep.getPackaging());
   }
   
   @Test
   public void testCreateFromStringWithClassifierAndPackaging()
   {
      GradleDependency dep = GradleDependencyBuilder
               .create("compile", "group:name:version:classifier@packaging");
      
      assertEquals("compile", dep.getConfigurationName());
      assertEquals("group", dep.getGroup());
      assertEquals("name", dep.getName());
      assertEquals("version", dep.getVersion());
      assertEquals("classifier", dep.getClassifier());
      assertEquals("packaging", dep.getPackaging());
   }
   
   @Test
   public void testConfigurationNameFromEnum()
   {
      GradleDependency dep = GradleDependencyBuilder.create()
               .setConfiguration(GradleDependencyConfiguration.RUNTIME);
      
      assertEquals(GradleDependencyConfiguration.RUNTIME.getName(), dep.getConfigurationName());
   }
   
   @Test
   public void testConfigurationEnumFromName()
   {
      GradleDependency dep = GradleDependencyBuilder.create()
               .setConfigurationName(GradleDependencyConfiguration.RUNTIME.getName());
      
      assertEquals(GradleDependencyConfiguration.RUNTIME, dep.getConfiguration());
   }
}
