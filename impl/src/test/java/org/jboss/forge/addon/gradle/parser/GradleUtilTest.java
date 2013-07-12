/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import static org.junit.Assert.assertEquals;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Adam Wyłuda
 */
public class GradleUtilTest
{
   @Test
   public void testInsertDependency()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "    testRuntime 'x:y:3.0'\n" +
               "}";
      String result = GradleUtil.insertDependency(source, "y", "x", "3.0", "testRuntime");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependency() throws UnremovableElementException
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "}";
      String result = GradleUtil.removeDependency(source, "b", "a", "1.0", "compile");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemoveDependencyForException() throws UnremovableElementException
   {
      String source = "" +
               "dependencies {\n" +
               "    def alias = compile" +
               "    alias 'a:b:1.0'\n" +
               "}";
      GradleUtil.removeDependency(source, "b", "a", "1.0", "compile");
   }

   @Test
   public void testInsertPlugin()
   {
      String source = "" +
               "version = '4.0'\n" +
               "\n" +
               "apply plugin: 'java'\n" +
               "\n" +
               "repositories {}\n";
      String expected = "" +
               "version = '4.0'\n" +
               "\n" +
               "apply plugin: 'java'\n" +
               "apply plugin: 'groovy'\n" +
               "\n" +
               "repositories {}\n";
      String result = GradleUtil.insertPlugin(source, "groovy");
      assertEquals(expected, result);
   }

   @Test
   public void testRemovePlugin() throws UnremovableElementException
   {
      String source = "" +
               "version = '4.0'\n" +
               "\n" +
               "apply plugin: 'java'\n" +
               "\n" +
               "repositories {}\n";
      String expected = "" +
               "version = '4.0'\n" +
               "\n" +
               "\n" +
               "repositories {}\n";
      String result = GradleUtil.removePlugin(source, "java");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemovePluginForException() throws UnremovableElementException
   {
      String source = "" +
               "version = '4.0'\n" +
               "\n" +
               "\n" +
               "repositories {}\n";
      GradleUtil.removePlugin(source, "scala");
   }

   @Test
   public void testInsertRepository()
   {
      String source = "" +
               "repositories {\n" +
               "}";
      String expected = "" +
               "repositories {\n" +
               "    maven {\n" +
               "        url 'http://repo.com'\n" +
               "    }\n" +
               "}";
      String result = GradleUtil.insertRepository(source, "", "http://repo.com");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveRepository() throws UnremovableElementException
   {
      String source = "" +
               "repositories {\n" +
               "    maven {\n" +
               "        url 'http://repo.com\n" +
               "    }\n" +
               "}";
      String expected = "" +
               "repositories {\n" +
               "}";
      String result = GradleUtil.removeRepository(source, "", "http://repo.com");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemoveRepositoryForException() throws UnremovableElementException
   {
      String source = "" +
               "repositories {\n" +
               "}";
      GradleUtil.removeRepository(source, "", "http://repo.com");
   }

   @Test
   public void testInsertTask()
   {
      String source = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String expected = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n" +
               "\n" +
               "task efgh << {\n" +
               "    def variable = 10\n" +
               "    println variable\n" +
               "}\n";
      String result = GradleUtil.insertTask(source, "efgh", Lists.<String> newArrayList(), "", "" +
               "    def variable = 10\n" +
               "    println variable");
      assertEquals(expected, result);
   }
   
   @Test
   public void testInsertTaskWithDependenciesAndType()
   {
      String source = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String expected = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n" +
               "\n" +
               "task (name: 'efgh', dependsOn: ['x', 'y', 'z'], type: Copy) << {\n" +
               "    def variable = 10\n" +
               "    println variable\n" +
               "}\n";
      String result = GradleUtil.insertTask(source, "efgh", Lists.<String> newArrayList("x", "y", "z"), "Copy", "" +
               "    def variable = 10\n" +
               "    println variable");
      assertEquals(expected, result);
   }
   
   @Test
   public void testInsertTaskWithOneDependency()
   {
      String source = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String expected = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n" +
               "\n" +
               "task (name: 'efgh', dependsOn: 'x', type: Copy) << {\n" +
               "    def variable = 10\n" +
               "    println variable\n" +
               "}\n";
      String result = GradleUtil.insertTask(source, "efgh", Lists.<String> newArrayList("x"), "Copy", "" +
               "    def variable = 10\n" +
               "    println variable");
      assertEquals(expected, result);
   }

   @Test
   public void testCheckForIncludeForgeLibraryAndInsert()
   {
      String source = "" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String expected = "" +
               "apply from: 'forge.gradle'\n" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String result = GradleUtil.checkForIncludeForgeLibraryAndInsert(source);
      assertEquals(expected, result);
   }

   @Test
   public void testCheckForIncludeForgeLibraryAndInsertExisting()
   {
      String source = "" +
               "apply from: 'forge.gradle'\n" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String expected = "" +
               "apply from: 'forge.gradle'\n" +
               "task abcd << {\n" +
               "    println 'ABCD!!'\n" +
               "}\n";
      String result = GradleUtil.checkForIncludeForgeLibraryAndInsert(source);
      assertEquals(expected, result);
   }
}
