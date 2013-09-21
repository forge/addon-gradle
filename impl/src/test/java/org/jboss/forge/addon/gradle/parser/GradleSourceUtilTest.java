/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;
import org.jboss.forge.addon.gradle.projects.model.GradleDependency;
import org.jboss.forge.addon.gradle.projects.model.GradleDependencyBuilder;
import org.jboss.forge.addon.gradle.projects.model.GradlePlugin;
import org.jboss.forge.addon.gradle.projects.model.GradleRepository;
import org.junit.Test;

/**
 * @author Adam Wyłuda
 */
public class GradleSourceUtilTest
{
   @Test
   public void testSetArchiveName()
   {
      String source = "" +
               "dependencies {\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "}\n" +
               "archiveName 'XYZ'\n";
      String result = GradleSourceUtil.setArchiveName(source, "XYZ");
      assertEquals(expected, result);
   }

   @Test
   public void testSetArchiveNameExisting()
   {
      String source = "" +
               "dependencies {\n" +
               "}\n" +
               "archiveName 'oldArchive'\n" +
               "repositories {\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "}\n" +
               "archiveName 'newArchive'\n" +
               "repositories {\n" +
               "}\n";
      String result = GradleSourceUtil.setArchiveName(source, "newArchive");
      assertEquals(expected, result);
   }

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
               "    testRuntime \"x:$y:3.0\"\n" +
               "}";
      String result = GradleSourceUtil.insertDependency(source, "testRuntime", "x", "$y", "3.0", "", "");
      assertEquals(expected, result);
   }

   @Test
   public void testInsertDependencyWithClassifierAndPackaging()
   {

      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "    testRuntime \"x:$y:3.0:cl@pom\"\n" +
               "}";
      String result = GradleSourceUtil.insertDependency(source, "testRuntime", "x", "$y", "3.0", "cl", "pom");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependencyDefinedByString()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "    compile 'a:b:1.0'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "", "");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependencyDefinedByStringWithClassifierAndPackaging()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "    compile 'a:b:1.0:cl@pom'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "cl", "pom");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependencyDefinedByStringWithConfiguration()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "    compile('a:b:1.0') {\n" +
               "        transitive = false\n" +
               "        exclude module: 'x'\n" +
               "    }\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "", "");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependencyDefinedByMap()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "    compile name: 'b', version: '1.0', group: 'a'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "", "");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependencyDefinedByMapWithClassifierAndPackaging()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "    compile name: 'b', version: '1.0', group: 'a', classifier: 'cl', ext: 'pom'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "cl", "pom");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDependencyDefinedByMapWithConfiguration()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "    compile(name: 'b', version: '1.0', group: 'a') {\n" +
               "        force = true\n" +
               "    }\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "", "");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemoveDependencyForException()
   {
      String source = "" +
               "dependencies {\n" +
               "    def alias = compile" +
               "    alias 'a:b:1.0'\n" +
               "}";
      GradleSourceUtil.removeDependency(source, "compile", "a", "b", "1.0", "", "");
   }

   @Test
   public void testGetDependencies()
   {
      String source = "" +
               "dependencies {\n" +
               "    println 'x:y:z'\n" +
               "    testRuntime 'x:z:4.0'\n" +
               "}\n" +
               "def x = {it * it}\n" +
               "dependencies {\n" +
               "    compile name: 'b', version: '1.0', group: 'a'\n" +
               "    direct group: 'ggg', name: 'nnn'\n" +
               "    println 'Hello'\n" +
               "}";

      List<GradleDependency> deps = GradleSourceUtil.getDependencies(source);

      assertEquals(2, deps.size());
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("testRuntime").setGroup("x").setName("z").setVersion("4.0"));
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("compile").setGroup("a").setName("b").setVersion("1.0"));
   }

   @Test
   public void testGetDependenciesWithClassifierAndPackaging()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile group: 'x', name: 'y', version: 'v', classifier: 'c', ext: 'pom'\n" +
               "}\n" +
               "def y = {it / it}\n" +
               "dependencies {\n" + 
               "    runtime 'd:e:f:xx@ear'\n" +
               "}\n// dependencies { compile 'a:b:c' }";
      
      List<GradleDependency> deps = GradleSourceUtil.getDependencies(source);
      
      assertEquals(2, deps.size());
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("compile").setGroup("x").setName("y").setVersion("v")
               .setClassifier("c").setPackaging("pom"));
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("runtime").setGroup("d").setName("e").setVersion("f")
               .setClassifier("xx").setPackaging("ear"));
   }
   
   @Test
   public void testGetDependenciesWithExcludes()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile(group: 'x', name: 'y', version: 'v') {\n" +
               "        transitive = true\n" +
               "        force = false\n" +
               "        exclude module: 'qwerty'\n" +
               "        exclude group: 'b', module: 'c'\n" +
               "    }\n" +
               "}\n";
      
      List<GradleDependency> deps = GradleSourceUtil.getDependencies(source);
      
      assertEquals(1, deps.size());
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("compile").setGroup("x").setName("y").setVersion("v"));
      
      GradleDependency dep = deps.get(0);
      List<GradleDependency> excludes = dep.getExcludedDependencies();
      
      assertEquals(2, excludes.size());
      assertContainsDirectDependency(excludes, GradleDependencyBuilder.create()
               .setGroup("x").setName("qwerty"));
      assertContainsDirectDependency(excludes, GradleDependencyBuilder.create()
               .setGroup("b").setName("c"));
   }

   @Test
   public void testInsertDirectDependency()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}   \n  \n" +
               "xyz {}";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "    direct group: \"x\", name: \"y\"\n" +
               "}   \n  \n" +
               "xyz {}";
      String result = GradleSourceUtil.insertDirectDependency(source, "x", "y");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveDirectDependency()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "    direct group: 'x', name: 'y'\n" +
               "}";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}";
      String result = GradleSourceUtil.removeDirectDependency(source, "x", "y");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemoveDirectDependencyForException() throws UnremovableElementException
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}";
      GradleSourceUtil.removeDirectDependency(source, "x", "y");
   }

   @Test
   public void testGetDirectDependencies()
   {
      String source = "" +
               "dependencies {\n" +
               "    testRuntime \"x:z:4.0\"\n" +
               "    compile name: 'b', version: '1.0', group: 'a'\n" +
               "    direct group: 'ggg', name: \"nnn\"\n" +
               "}";

      List<GradleDependency> deps = GradleSourceUtil.getDirectDependencies(source);

      assertEquals(1, deps.size());
      assertContainsDirectDependency(deps, GradleDependencyBuilder.create().setGroup("ggg").setName("nnn"));
   }

   @Test
   public void testInsertManagedDependency()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "    }  \t\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "        managed configuration: \"compile\", group: \"xx\", name: \"yy\", version: \"vv\"\n" +
               "    }  \t\n" +
               "}\n";
      String result = GradleSourceUtil.insertManagedDependency(source, "xx", "yy", "vv", "compile", "", "");
      assertEquals(expected, result);
   }
   
   @Test
   public void testInsertManagedDependencyWithClassifierAndPackaging()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "    }  \t\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "        managed configuration: \"compile\", group: \"xx\", name: \"yy\", version: \"vv\"" +
               ", classifier: \"clas\", ext: \"pom\"\n" +
               "    }  \t\n" +
               "}\n";
      String result = GradleSourceUtil.insertManagedDependency(source, "xx", "yy", "vv", "compile", "clas", "pom");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveManagedDependency()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "        managed configuration: \"compile\", group: 'xx', name: 'yy', version: 'vv'\n" +
               "    }\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "    }\n" +
               "}\n";
      String result = GradleSourceUtil.removeManagedDependency(source, "xx", "yy", "vv", "compile", "", "");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveManagedDependencyWithClassifierAndPackaging()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "        managed configuration: \"compile\", group: 'xx', name: 'yy', version: 'vv'" +
               ", classifier: \"clas\", ext: \"pom\"\n" +
               "    }\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "    }\n" +
               "}\n";
      String result = GradleSourceUtil.removeManagedDependency(source, "xx", "yy", "vv", "compile", "clas", "pom");
      assertEquals(expected, result);
   }

   @Test
   public void testGetManagedDependencies()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "        managed configuration: 'compile', group: \"xx\", name: 'yy', version: 'vv'\n" +
               "    }\n" +
               "}\n";

      List<GradleDependency> deps = GradleSourceUtil.getManagedDependencies(source);

      assertEquals(1, deps.size());
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("compile").setGroup("xx").setName("yy").setVersion("vv"));
   }

   @Test
   public void testGetManagedDependenciesWithClassifierAndPackaging()
   {
      String source = "" +
               "dependencies {\n" +
               "    compile 'a:b:1.0'\n" +
               "}\n" +
               "allprojects {\n" +
               "    dependencies {\n" +
               "        managed configuration: 'compile', group: \"xx\", name: 'yy', version: 'vv'" +
               ", classifier: 'clas', ext: 'pom'\n" +
               "    }\n" +
               "}\n";

      List<GradleDependency> deps = GradleSourceUtil.getManagedDependencies(source);

      assertEquals(1, deps.size());
      assertContainsDependency(deps, GradleDependencyBuilder.create()
               .setConfigurationName("compile").setGroup("xx").setName("yy").setVersion("vv")
               .setClassifier("clas").setPackaging("pom"));
   }

   @Test
   public void testGetPlugins()
   {
      String source = "" +
               "version = '4.0'\n" +
               "\n" +
               "apply plugin: 'java'\n" +
               "\n" +
               "repositories {}\n";
      List<GradlePlugin> plugins = GradleSourceUtil.getPlugins(source);

      assertEquals(1, plugins.size());
      GradlePlugin plugin = plugins.get(0);
      assertEquals("java", plugin.getType().getShortName());
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
      String result = GradleSourceUtil.insertPlugin(source, "groovy");
      assertEquals(expected, result);
   }

   @Test
   public void testRemovePluginDefinedByApply()
   {
      String source = "" +
               "version = '4.0'\n" +
               "\n" +
               "apply plugin: 'java'\n" +
               "\n" +
               "repositories {}\n";
      String expected = "" +
               "version = '4.0'\n" +
               "repositories {}\n";
      String result = GradleSourceUtil.removePlugin(source, "java");
      assertEquals(expected, result);
   }

   @Test
   public void testRemovePluginDefinedByProjectApply()
   {
      String source = "" +
               "version = '4.0'\n" +
               "\n" +
               "project.apply plugin: 'java'\n" +
               "\n" +
               "repositories {}\n";
      String expected = "" +
               "version = '4.0'\n" +
               "repositories {}\n";
      String result = GradleSourceUtil.removePlugin(source, "java");
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
      GradleSourceUtil.removePlugin(source, "scala");
   }

   @Test
   public void testGetRepositories()
   {
      String source = "" +
               "repositories {\n" +
               "    maven {\n" +
               "        url 'http://repo.com'\n" +
               "    }\n" +
               "}";
      List<GradleRepository> repos = GradleSourceUtil.getRepositories(source);

      assertEquals(1, repos.size());
      GradleRepository repo = repos.get(0);
      assertEquals("http://repo.com", repo.getUrl());
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
      String result = GradleSourceUtil.insertRepository(source, "http://repo.com");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveRepository()
   {
      String source = "" +
               "repositories {\n" +
               "    maven {\n" +
               "        url 'http://repo.com'\n" +
               "    }\n" +
               "}";
      String expected = "" +
               "repositories {\n" +
               "    maven {\n" +
               "    }\n" +
               "}";
      String result = GradleSourceUtil.removeRepository(source, "http://repo.com");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemoveRepositoryForException()
   {
      String source = "" +
               "repositories {\n" +
               "}";
      GradleSourceUtil.removeRepository(source, "http://repo.com");
   }

   @Test
   public void testSetPropertyNewProperty()
   {
      String source = "" +
               "dependencies {\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "}\n" +
               "myProp = \"value\"\n";
      String result = GradleSourceUtil.setProperty(source, "myProp", "value");
      assertEquals(expected, result);
   }

   @Test
   public void testSetPropertyChangeExistingProperty()
   {
      String source = "" +
               "dependencies {\n" +
               "}\n" +
               "myProperty = 'oldValue'\n" +
               "repositories {\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "}\n" +
               "myProperty = \"newValue\"\n" +
               "repositories {\n" +
               "}\n";
      String result = GradleSourceUtil.setProperty(source, "myProperty", "newValue");
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveProperty()
   {
      String source = "" +
               "dependencies {\n" +
               "}\n" +
               "myProperty = 'oldValue'\n" +
               "repositories {\n" +
               "}\n";
      String expected = "" +
               "dependencies {\n" +
               "}\n" +
               "repositories {\n" +
               "}\n";
      String result = GradleSourceUtil.removeProperty(source, "myProperty");
      assertEquals(expected, result);
   }

   @Test(expected = UnremovableElementException.class)
   public void testRemovePropertyForException()
   {
      String source = "" +
               "dependencies {\n" +
               "}\n";
      GradleSourceUtil.removeProperty(source, "myProp");
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
      String result = GradleSourceUtil.insertTask(source, "efgh", Lists.<String> newArrayList(), "", "" +
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
               "task ('efgh', dependsOn: ['x', 'y', 'z'], type: Copy) << {\n" +
               "    def variable = 10\n" +
               "    println variable\n" +
               "}\n";
      String result = GradleSourceUtil.insertTask(source, "efgh", Lists.<String> newArrayList("x", "y", "z"), "Copy",
               "" +
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
               "task ('efgh', dependsOn: 'x', type: Copy) << {\n" +
               "    def variable = 10\n" +
               "    println variable\n" +
               "}\n";
      String result = GradleSourceUtil.insertTask(source, "efgh", Lists.<String> newArrayList("x"), "Copy", "" +
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
      String result = GradleSourceUtil.checkForIncludeForgeLibraryAndInsert(source);
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
      String result = GradleSourceUtil.checkForIncludeForgeLibraryAndInsert(source);
      assertEquals(expected, result);
   }

   @Test
   public void testGetDirectProperties()
   {
      // Assuming that direct properties are those which can be obtained by analyzing source
      String source = "" +
               "dependencies {\n" +
               "    ext.dependencyProperty = 'xxxx'\n" +
               "}\n" +
               "badProperty = 'yyy'\n" +
               "ext.goodProperty = 'zzz'\n" +
               "def propertyMaker = {\n" +
               "    project.ext.makerProperty = it\n" +
               "}\n" +
               "propertyMaker '7'\n" +
               "task myTask << {}\n" +
               "myTask.ext.taskProperty = 'majtaski'\n";
      Map<String, String> result = GradleSourceUtil.getDirectProperties(source);

      assertEquals(1, result.size());
      assertEquals("zzz", result.get("goodProperty"));
   }

   private static void assertContainsDependency(List<GradleDependency> list, GradleDependency dep)
   {
      for (GradleDependency listDep : list)
      {
         if (GradleDependencyBuilder.create(listDep).equalsToDependency(dep))
         {
            return;
         }
      }
      fail("List doesn't contain dependency " + dep.toString());
   }

   private static void assertContainsDirectDependency(List<GradleDependency> list, GradleDependency dep)
   {
      for (GradleDependency listDep : list)
      {
         if (GradleDependencyBuilder.create(listDep).equalsToDirectDependency(dep))
         {
            return;
         }
      }
      fail("List doesn't contain direct dependency " + dep.toString());
   }
}
