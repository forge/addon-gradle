/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;

import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;

/**
 * @author Adam Wyłuda
 */
public interface GradleModel
{
   // ---------- Script

   String getScript();

   void setScript(String script);

   // ---------- Project paths

   String getProjectPath();

   String getRootProjectPath();

   // ---------- Group

   String getGroup();

   void setGroup(String group) throws UnremovableElementException;

   // ---------- Name

   String getName();

   void setName(String name) throws UnremovableElementException;

   // ---------- Version

   String getVersion();

   void setVersion(String version) throws UnremovableElementException;
   
   // ---------- Packaging

   String getPackaging();

   void setPackaging(String packaging);
   
   // ---------- Archive path/name

   String getArchivePath();

   String getArchiveName();

   void setArchiveName(String archiveName);

   // ---------- Tasks
   
   List<GradleTask> getTasks();

   boolean hasTask(String name);

   void addTask(GradleTaskBuilder builder);
   
   // ---------- Dependencies

   List<GradleDependency> getEffectiveDependencies();

   boolean hasEffectiveDependency(GradleDependencyBuilder builder);

   void addDependency(GradleDependencyBuilder builder);

   void removeDependency(GradleDependencyBuilder builder) throws UnremovableElementException;
   
   // ---------- Managed dependencies

   List<GradleDependency> getEffectiveManagedDependencies();

   boolean hasEffectiveManagedDependency(GradleDependencyBuilder builder);

   void addManagedDependency(GradleDependencyBuilder builder);

   void removeManagedDependency(GradleDependencyBuilder builder) throws UnremovableElementException;
   
   // ---------- Profiles

   List<GradleProfile> getProfiles();

   boolean hasProfile(String name);

   void addProfile(String name);

   void removeProfile(String name);
   
   // ---------- Plugins

   List<GradlePlugin> getPlugins();

   boolean hasPlugin(String clazz);

   void addPlugin(String name);

   void removePlugin(String name) throws UnremovableElementException;
   
   // ---------- Repositories

   List<GradleRepository> getRepositories();

   boolean hasRepository(String url);

   void addRepository(String url);

   void removeRepository(String url) throws UnremovableElementException;
   
   // --------- Source sets

   List<GradleSourceSet> getSourceSets();

   // ---------- Properties
   
   Map<String, String> getProperties();

   boolean hasProperty(String key);

   void setProperty(String name, String value);

   void removeProperty(String name) throws UnremovableElementException;
}
