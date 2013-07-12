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
   // TODO short names for plugin types
   JAVA("org.gradle.api.plugins.JavaPlugin"), GROOVY("org.gradle.api.plugins.GroovyPlugin"),
   SCALA("org.gradle.api.plugins.scala.ScalaPlugin"), WAR("org.gradle.api.plugins.WarPlugin"),
   EAR("org.gradle.plugins.EarPlugin"), JETTY("org.gradle.api.plugins.jetty.JettyPlugin"),
   CHECKSTYLE("org.gradle.api.plugins.quality.CheckstylePlugin"),
   CODENARC("org.gradle.api.plugins.quality.CodeNarcPlugin"),
   FIND_BUGS("org.gradle.api.plugins.quality.FindBugsPlugin"),
   JDEPEND("org.gradle.api.plugins.quality.JDependPlugin"),
   PMD("org.gradle.api.plugins.quality.PmdPlugin"), JACOCO("org.gradle.testing.jacoco.plugins.JacocoPlugin"),
   SONAR("org.gradle.api.plugins.sonar.SonarPlugin"), OSGI("org.gradle.api.plugins.osgi.OsgiPlugin"),
   ECLIPSE("org.gradle.plugins.ide.eclipse.EclipsePlugin"), IDEA("org.gradle.plugins.ide.idea.IdeaPlugin"),
   ANTLR("org.gradle.api.plugins.antlr.AntlrPlugin"), PROJECT_REPORTS("org.gradle.api.plugins.ProjectReportsPlugin"),
   ANNOUNCE("org.gradle.api.plugins.announce.AnnouncePlugin"),
   BUILD_ANNOUNCEMENTS("org.gradle.api.plugins.announce.BuildAnnouncementsPlugin"),
   DISTRIBUTION("org.gradle.api.distribution.plugins.DistributionPlugin"),
   APPLICATION("org.gradle.api.plugins.ApplicationPlugin"),
   JAVA_LIBRARY_DISTRIBUTION("org.gradle.api.plugins.JavaLibraryDistributionPlugin"),
   BUILD_DASHBOARD("org.gradle.api.reporting.plugins.BuildDashboardPlugin"),
   MAVEN("org.gradle.api.plugins.MavenPlugin"), SIGNING("org.gradle.plugins.signing.SigningPlugin"),
   IVY_PUBLISH("org.gradle.api.publish.ivy.plugins.IvyPublishPlugin"),
   MAVEN_PUBLISH("org.gradle.api.publish.maven.plugins.MavenPublishPlugin"),

   OTHER("");

   private static class TypeContainer
   {
      private static final Map<String, GradlePluginType> TYPE_MAP = new HashMap<String, GradlePluginType>();
   }

   private final String clazz;
   private final String shortName;

   private GradlePluginType(String clazz)
   {
      this(clazz, "");
   }
   
   private GradlePluginType(String clazz, String shortName)
   {
      this.clazz = clazz;
      TypeContainer.TYPE_MAP.put(clazz, this);
      this.shortName = shortName;
   }

   public String getClazz()
   {
      return clazz;
   }
   
   public String getShortName()
   {
      return shortName;
   }
   
   /**
    * @return Plugin type for given class. If there is no such type then it returns {@link #OTHER}.
    */
   public static GradlePluginType typeByClazz(String clazz)
   {
      GradlePluginType type = TypeContainer.TYPE_MAP.get(clazz);
      return type != null ? type : OTHER;
   }
}
