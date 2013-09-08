/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.forge.furnace.util.Streams;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Adam Wyłuda
 */
public class GradleModelLoaderTest
{
   private static GradleEffectiveModel model;

   @BeforeClass
   public static void init() throws IOException
   {
      String source = Streams.toString(GradleModelLoaderTest.class.getResourceAsStream("/loader/forge-output.xml"));
      model = GradleModelLoadUtil.load(source);
      model.setScript(Streams.toString(GradleModelLoaderTest.class.getResourceAsStream("/loader/build.gradle")));
   }

   @Test
   public void testGroup()
   {
      assertEquals("groupgroup", model.getGroup());
   }

   @Test
   public void testName()
   {
      assertEquals("Gradle Test Project", model.getName());
   }

   @Test
   public void testVersion()
   {
      assertEquals("0.1-SNAPSHOT", model.getVersion());
   }
   
   @Test
   public void testProjectPath()
   {
      assertEquals(":", model.getProjectPath());
   }
   
   @Test
   public void testRootProjectPath()
   {
      // No idea for a better test
      assertTrue(model.getRootProjectPath().replace('\\', '/').endsWith("src/test/resources/loader"));
   }

   @Test
   public void testPackaging()
   {
      assertEquals("jar", model.getPackaging());
   }

   @Test
   public void testArchiveName()
   {
      assertEquals("Gradle Test Project-0.1-SNAPSHOT", model.getArchiveName());
   }

   @Test
   public void testSetArchiveName()
   {
      model.setArchiveName("NEWNAME");
      assertEquals("build/libs/NEWNAME.jar", model.getArchivePath());
   }

   @Test
   public void testArchivePath()
   {
      assertEquals("build/libs/Gradle Test Project-0.1-SNAPSHOT.jar", model.getArchivePath());
   }

   @Test
   public void testTasks()
   {
      boolean zxyzSet = false;
      for (GradleTask task : model.getTasks())
      {
         if (task.getName().equals("zxyz"))
         {
            zxyzSet = true;
            List<GradleTask> dependsOnSet = task.getDependsOn();

            boolean buildSet = false, testSet = false;
            for (GradleTask dependsOn : dependsOnSet)
            {
               if (dependsOn.getName().equals("build"))
               {
                  buildSet = true;
               }
               else if (dependsOn.getName().equals("test"))
               {
                  testSet = true;
               }
            }
            assertTrue("No dependency on build", buildSet);
            assertTrue("No dependency on test", testSet);
         }
      }
      assertTrue("zxyz task not found", zxyzSet);
   }

   @Test
   public void testDependencies()
   {
      boolean gradleToolingSet = false, junitSet = false, xSet = false;
      for (GradleDependency dep : model.getEffectiveDependencies())
      {
         if (dep.getName().equals("gradle-tooling-api"))
         {
            gradleToolingSet = true;
            assertEquals(GradleDependencyConfiguration.COMPILE, dep.getConfiguration());
            assertEquals("org.gradle", dep.getGroup());
            assertEquals("1.6", dep.getVersion());
         }
         else if (dep.getName().equals("junit"))
         {
            junitSet = true;
            assertEquals(GradleDependencyConfiguration.TEST_COMPILE, dep.getConfiguration());
            assertEquals("junit", dep.getGroup());
            assertEquals("4.11", dep.getVersion());
         }
         else if (dep.getName().equals("y"))
         {
            xSet = true;
            assertEquals(GradleDependencyConfiguration.TEST_RUNTIME, dep.getConfiguration());
            assertEquals("x", dep.getGroup());
            assertEquals("z", dep.getVersion());
         }
      }
      assertTrue("gradle-tooling-api dependency not found", gradleToolingSet);
      assertTrue("junit dependency not found", junitSet);
      assertTrue("x:y:z dependency not found", xSet);
   }

   @Test
   public void testManagedDependencies()
   {
      assertEquals(1, model.getEffectiveManagedDependencies().size());
      GradleDependency dep = model.getEffectiveManagedDependencies().get(0);
      assertEquals("com.google.guava", dep.getGroup());
      assertEquals("guava", dep.getName());
      assertEquals("14.0.1", dep.getVersion());
      assertEquals("compile", dep.getConfigurationName());
      assertEquals(GradleDependencyConfiguration.COMPILE, dep.getConfiguration());
   }

   @Test
   public void testProfiles()
   {
      assertEquals("There are more or less than 2 profiles", 2, model.getProfiles().size());
      boolean glassfishSet = false, wildflySet = false;
      for (GradleProfile profile : model.getProfiles())
      {
         if (profile.getName().equals("glassfish"))
         {
            glassfishSet = true;
            assertTrue("Glassfish profile doesn't contain runApplicationServer task",
                     profile.getModel().hasTask("runApplicationServer"));
            assertTrue(
                     "Glassfish profile doesn't contain specified dependency",
                     profile.getModel().hasEffectiveDependency(
                              GradleDependencyBuilder.create("compile", "javax.annotation:jsr250-api:1.0")));
         }
         else if (profile.getName().equals("wildfly"))
         {
            wildflySet = true;
            assertTrue("Wildfly profile doesn't contain runApplicationServer task",
                     profile.getModel().hasTask("runApplicationServer"));
            assertTrue(
                     "Wildfly profile doesn't contain specified dependency",
                     profile.getModel().hasEffectiveDependency(
                              GradleDependencyBuilder.create("compile", "log4j:log4j:1.2.17")));
         }
      }
      assertTrue("glassfish profile not found", glassfishSet);
      assertTrue("wildfly profile not found", wildflySet);
   }

   @Test
   public void testPlugins()
   {
      assertTrue("There is no java plugin", model.hasPlugin(GradlePluginType.JAVA.getClazz()));
      assertTrue("There is no groovy plugin", model.hasPlugin(GradlePluginType.GROOVY.getClazz()));
      assertTrue("There is no scala plugin", model.hasPlugin(GradlePluginType.SCALA.getClazz()));
      assertTrue("There is no eclipse plugin", model.hasPlugin(GradlePluginType.ECLIPSE.getClazz()));
   }

   @Test
   public void testRepositories()
   {
      assertTrue("There is no Gradle repository",
               model.hasRepository("http://repo.gradle.org/gradle/libs-releases-local/"));
   }

   @Test
   public void testSourceSets()
   {
      assertEquals("There are more or less than two source sets", 2, model.getEffectiveSourceSets().size());
      boolean mainSetSet = false, testSetSet = false;
      for (GradleSourceSet sourceSet : model.getEffectiveSourceSets())
      {
         if (sourceSet.getName().equals("main"))
         {
            mainSetSet = true;

            assertEquals(2, sourceSet.getJavaDirectories().size());
            boolean javaSet = false, alterSet = false;
            for (GradleSourceDirectory dir : sourceSet.getJavaDirectories())
            {
               if (dir.getPath().equals("src/main/java"))
               {
                  javaSet = true;
               }
               if (dir.getPath().equals("src/main/alter"))
               {
                  alterSet = true;
               }
            }
            assertTrue("Java directory set not found", javaSet);
            assertTrue("Alter directory set not found", alterSet);

            assertEquals(1, sourceSet.getResourceDirectories().size());
            assertEquals("src/main/resources", sourceSet.getResourceDirectories().get(0).getPath());
         }
         if (sourceSet.getName().equals("test"))
         {
            testSetSet = true;

            assertEquals(1, sourceSet.getJavaDirectories().size());
            assertEquals("src/test/java", sourceSet.getJavaDirectories().get(0).getPath());

            assertEquals(1, sourceSet.getResourceDirectories().size());
            assertEquals("src/test/resources", sourceSet.getResourceDirectories().get(0).getPath());
         }
      }
      assertTrue("main source set not found", mainSetSet);
      assertTrue("test source set not found", testSetSet);
   }

   @Test
   public void testProperties()
   {
      Map<String, String> props = model.getProperties();

      assertEquals("testMe", props.get("someProperty"));
      assertEquals("xyz", props.get("otherProperty"));
   }
}
