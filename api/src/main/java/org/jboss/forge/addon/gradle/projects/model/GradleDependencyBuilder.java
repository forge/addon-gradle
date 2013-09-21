/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.furnace.util.Strings;

/**
 * Default implementation of the {@link GradleDependency}.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public class GradleDependencyBuilder implements GradleDependency
{
   static final String DEFAULT_PACKAGING = "jar";

   private String configurationName = "";
   private String group = "";
   private String name = "";
   private String version = "";
   private String classifier = "";
   private String packaging = DEFAULT_PACKAGING;
   private List<GradleDependency> excludedDependencies = new ArrayList<GradleDependency>();

   private GradleDependencyBuilder()
   {
   }

   public static GradleDependencyBuilder create()
   {
      return new GradleDependencyBuilder();
   }

   /**
    * Creates a copy of given dependency.
    */
   public static GradleDependencyBuilder create(GradleDependency dependency)
   {
      GradleDependencyBuilder builder = new GradleDependencyBuilder();

      builder.configurationName = dependency.getConfigurationName();
      builder.group = dependency.getGroup();
      builder.name = dependency.getName();
      builder.version = dependency.getVersion();
      builder.classifier = dependency.getClassifier();
      builder.packaging = dependency.getPackaging();
      builder.excludedDependencies = dependency.getExcludedDependencies();

      return builder;
   }

   /**
    * Creates gradle dependency using given configuration and parsing gradleString in format:
    * {@code group:name:version[:classifier][@packaging]}
    */
   public static GradleDependencyBuilder create(String configuration, String gradleString)
   {
      String[] packagingSplit = gradleString.split("@");
      String[] split = packagingSplit[0].split(":");

      String group = split[0];
      String name = split[1];
      String version = split[2];
      String classifier = "";

      if (split.length == 4)
      {
         classifier = split[3];
      }

      String packaging = DEFAULT_PACKAGING;
      if (packagingSplit.length == 2)
      {
         packaging = packagingSplit[1];
      }

      return create()
               .setName(name)
               .setGroup(group)
               .setVersion(version)
               .setClassifier(classifier)
               .setPackaging(packaging)
               .setConfigurationName(configuration);
   }

   /**
    * Performs a deep copy of given dependencies.
    */
   public static List<GradleDependency> deepCopy(List<GradleDependency> deps)
   {
      List<GradleDependency> list = new ArrayList<GradleDependency>();

      for (GradleDependency dep : deps)
      {
         list.add(create(dep));
      }

      return list;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public GradleDependencyBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   @Override
   public String getGroup()
   {
      return group;
   }

   public GradleDependencyBuilder setGroup(String group)
   {
      this.group = group;
      return this;
   }

   @Override
   public String getVersion()
   {
      return version;
   }

   public GradleDependencyBuilder setVersion(String version)
   {
      this.version = version;
      return this;
   }

   @Override
   public String getClassifier()
   {
      return classifier;
   }

   public GradleDependencyBuilder setClassifier(String classifier)
   {
      this.classifier = classifier;
      return this;
   }

   @Override
   public String getConfigurationName()
   {
      return configurationName;
   }

   public GradleDependencyBuilder setConfigurationName(String configuration)
   {
      this.configurationName = configuration;
      return this;
   }

   @Override
   public String getPackaging()
   {
      return packaging;
   }

   public GradleDependencyBuilder setPackaging(String packaging)
   {
      this.packaging = packaging;
      return this;
   }

   @Override
   public GradleDependencyConfiguration getConfiguration()
   {
      return GradleDependencyConfiguration.fromName(configurationName);
   }

   public GradleDependencyBuilder setConfiguration(GradleDependencyConfiguration configuration)
   {
      this.configurationName = configuration.getName();
      return this;
   }

   @Override
   public List<GradleDependency> getExcludedDependencies()
   {
      return excludedDependencies;
   }
   
   public GradleDependencyBuilder setExcludedDependencies(List<GradleDependency> excludedDependencies)
   {
      this.excludedDependencies = excludedDependencies;
      return null;
   }

   @Override
   public String toGradleString()
   {
      return String.format("%s:%s:%s%s%s", group, name, version,
               !Strings.isNullOrEmpty(classifier) ? ":" + classifier : "",
               !Strings.isNullOrEmpty(packaging) && !packaging.equals(DEFAULT_PACKAGING) ? "@" + packaging : "");
   }
   
   @Override
   public String toGradleMapString() {
      return String.format("group: \"%s\", name: \"%s\", version: \"%s\"%s%s",
               group, name, version,
               !Strings.isNullOrEmpty(classifier) ? ", classifier: \"" + classifier + "\"" : "",
               !Strings.isNullOrEmpty(packaging) && !packaging.equals("jar") ? ", ext: \"" + packaging + "\"" : "");
   }

   /**
    * Compares this builder to given {@link GradleDependency}.
    */
   public boolean equalsToDependency(GradleDependency dep)
   {
      boolean coordsEquals = group.equals(dep.getGroup()) && name.equals(dep.getName())
               && version.equals(dep.getVersion());

      boolean classifierEquals = true;
      if (!Strings.isNullOrEmpty(dep.getClassifier()))
      {
         classifierEquals = dep.getClassifier().equals(classifier);
      }

      boolean packagingEquals = true;
      if (!Strings.isNullOrEmpty(dep.getPackaging()))
      {
         packagingEquals = dep.getPackaging().equals(packaging);
      }

      return coordsEquals && classifierEquals && packagingEquals;
   }

   /**
    * Does the same thing as {@link #equalsToDependency(GradleDependency)} but only compares group and name.
    */
   public boolean equalsToDirectDependency(GradleDependency dep)
   {
      return group.equals(dep.getGroup()) && name.equals(dep.getName());
   }

   @Override
   public String toString()
   {
      return String.format("%s '%s'", configurationName, toGradleString());
   }
   
   @Override
   public boolean equals(Object other) {
      return equalsToDependency((GradleDependency) other);
   }
}
