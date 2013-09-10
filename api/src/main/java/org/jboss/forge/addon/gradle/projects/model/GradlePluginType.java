/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.furnace.util.Strings;

/**
 * Common Gradle plugin types.
 * 
 * @author Adam Wyłuda
 */
public enum GradlePluginType
{
   JAVA("org.gradle.api.plugins.JavaPlugin", "java", "jar"), 
   GROOVY("org.gradle.api.plugins.GroovyPlugin", "groovy"),
   SCALA("org.gradle.api.plugins.scala.ScalaPlugin", "scala"), 
   WAR("org.gradle.api.plugins.WarPlugin", "war", "war"),
   EAR("org.gradle.plugins.EarPlugin", "ear", "ear"), 
   JETTY("org.gradle.api.plugins.jetty.JettyPlugin", "jetty"),
   CHECKSTYLE("org.gradle.api.plugins.quality.CheckstylePlugin", "checkstyle"),
   CODENARC("org.gradle.api.plugins.quality.CodeNarcPlugin", "codenarc"),
   FIND_BUGS("org.gradle.api.plugins.quality.FindBugsPlugin", "findbugs"),
   JDEPEND("org.gradle.api.plugins.quality.JDependPlugin", "jdepend"),
   PMD("org.gradle.api.plugins.quality.PmdPlugin", "pmd"), 
   JACOCO("org.gradle.testing.jacoco.plugins.JacocoPlugin", "jacoco"),
   SONAR("org.gradle.api.plugins.sonar.SonarPlugin", "sonar"), 
   OSGI("org.gradle.api.plugins.osgi.OsgiPlugin", "osgi"),
   ECLIPSE("org.gradle.plugins.ide.eclipse.EclipsePlugin", "eclipse"), 
   IDEA("org.gradle.plugins.ide.idea.IdeaPlugin", "idea"),
   ANTLR("org.gradle.api.plugins.antlr.AntlrPlugin", "antlr"), 
   PROJECT_REPORTS("org.gradle.api.plugins.ProjectReportsPlugin", "project-report"),
   ANNOUNCE("org.gradle.api.plugins.announce.AnnouncePlugin", "announce"),
   BUILD_ANNOUNCEMENTS("org.gradle.api.plugins.announce.BuildAnnouncementsPlugin", "build-announcements"),
   DISTRIBUTION("org.gradle.api.distribution.plugins.DistributionPlugin", "distribution"),
   APPLICATION("org.gradle.api.plugins.ApplicationPlugin", "application"),
   JAVA_LIBRARY_DISTRIBUTION("org.gradle.api.plugins.JavaLibraryDistributionPlugin", "java-library-distribution"),
   BUILD_DASHBOARD("org.gradle.api.reporting.plugins.BuildDashboardPlugin", "build-dashboard"),
   MAVEN("org.gradle.api.plugins.MavenPlugin", "maven"), 
   SIGNING("org.gradle.plugins.signing.SigningPlugin", "signing"),
   IVY_PUBLISH("org.gradle.api.publish.ivy.plugins.IvyPublishPlugin", "ivy-publish"),
   MAVEN_PUBLISH("org.gradle.api.publish.maven.plugins.MavenPublishPlugin", "maven-publish"),

   OTHER("");

   private static class TypeContainer
   {
      private static final Map<String, GradlePluginType> TYPE_BY_CLAZZ_MAP = new HashMap<String, GradlePluginType>();
      private static final Map<String, GradlePluginType> TYPE_BY_PACKAGING_MAP = new HashMap<String, GradlePluginType>();
   }

   private final String clazz;
   private final String shortName;
   private final String packaging;

   private GradlePluginType(String clazz)
   {
      this(clazz, "", "");
   }
   
   private GradlePluginType(String clazz, String shortName)
   {
      this(clazz, shortName, "");
   }
   
   private GradlePluginType(String clazz, String shortName, String packaging)
   {
      this.clazz = clazz;
      TypeContainer.TYPE_BY_CLAZZ_MAP.put(clazz, this);
      TypeContainer.TYPE_BY_CLAZZ_MAP.put(shortName, this);
      this.shortName = shortName;
      this.packaging = packaging;
      if (!Strings.isNullOrEmpty(packaging))
      {
         TypeContainer.TYPE_BY_PACKAGING_MAP.put(packaging, this);
      }
   }

   public String getClazz()
   {
      return clazz;
   }
   
   public String getShortName()
   {
      return shortName;
   }
   
   public String getPackaging()
   {
      return packaging;
   }
   
   /**
    * @return Plugin type for given class. If there is no such type then it returns {@link #OTHER}.
    */
   public static GradlePluginType typeByClazz(String clazz)
   {
      GradlePluginType type = TypeContainer.TYPE_BY_CLAZZ_MAP.get(clazz);
      return type != null ? type : OTHER;
   }
   
   public static GradlePluginType typeByPackaging(String packaging)
   {
      return TypeContainer.TYPE_BY_PACKAGING_MAP.get(packaging);
   }
}
