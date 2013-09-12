/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.furnace.util.Strings;

/**
 * @author Adam Wyłuda
 */
public class GradleModelMergeUtil
{
   private GradleModelMergeUtil()
   {
   }

   /**
    * Calculates differences between the old and new model, and then persists these changes in script.
    */
   public static String merge(String source, GradleModel oldModel, GradleModel newModel)
   {
      source = setGroup(source, newModel.getGroup());
      source = setVersion(source, newModel.getVersion());
      source = setArchiveName(source, newModel.getArchiveName());

      source = addTasks(source, newModel.getTasks());

      source = addDependencies(source, subtract(newModel.getDependencies(), oldModel.getDependencies()));
      source = removeDependencies(source, subtract(oldModel.getDependencies(), newModel.getDependencies()));

      source = addManagedDependencies(source,
               subtract(newModel.getManagedDependencies(), oldModel.getManagedDependencies()));
      source = removeManagedDependencies(source,
               subtract(oldModel.getManagedDependencies(), newModel.getManagedDependencies()));

      source = addPlugins(source, subtract(newModel.getPlugins(), oldModel.getPlugins()));
      source = removePlugins(source, subtract(oldModel.getPlugins(), newModel.getPlugins()));

      source = addRepositories(source, subtract(newModel.getRepositories(), oldModel.getRepositories()));
      source = removeRepositories(source, subtract(oldModel.getRepositories(), newModel.getRepositories()));

      source = removeProperties(source, subtract(oldModel.getProperties(), newModel.getProperties()).keySet());
      source = setProperties(source, subtract(newModel.getProperties(), oldModel.getProperties()));

      return source;
   }

   private static String setGroup(String source, String group)
   {
      source = GradleSourceUtil.setProperty(source, "group", group);
      return source;
   }

   private static String setVersion(String source, String version)
   {
      source = GradleSourceUtil.setProperty(source, "version", version);
      return source;
   }

   private static String setArchiveName(String source, String archiveName)
   {
      source = GradleSourceUtil.setArchiveName(source, archiveName);
      return source;
   }

   private static String addTasks(String source, List<GradleTask> tasks)
   {
      for (GradleTask task : tasks)
      {
         source = GradleSourceUtil.insertTask(source, task.getName(), dependsOn(task.getDependsOn()),
                  task.getType(), task.getCode());
      }
      return source;
   }

   private static List<String> dependsOn(List<GradleTask> tasks)
   {
      List<String> names = Lists.newArrayList();
      for (GradleTask task : tasks)
      {
         names.add(task.getName());
      }
      return names;
   }

   private static String addDependencies(String source, List<GradleDependency> deps)
   {
      for (GradleDependency dep : deps)
      {
         if (!Strings.isNullOrEmpty(dep.getVersion()) && !Strings.isNullOrEmpty(dep.getConfigurationName()))
         {
            source = GradleSourceUtil.insertDependency(source, dep.getGroup(), dep.getName(),
                     dep.getVersion(), dep.getConfigurationName());
         }
         else
         {
            source = GradleSourceUtil.insertDirectDependency(source, dep.getGroup(), dep.getName());
         }
      }
      return source;
   }

   private static String removeDependencies(String source, List<GradleDependency> deps)
   {
      for (GradleDependency dep : deps)
      {
         if (!Strings.isNullOrEmpty(dep.getVersion()) && !Strings.isNullOrEmpty(dep.getConfigurationName()))
         {
            source = GradleSourceUtil.removeDependency(source, dep.getGroup(), dep.getName(),
                     dep.getVersion(), dep.getConfigurationName());
         }
         else
         {
            source = GradleSourceUtil.removeDirectDependency(source, dep.getGroup(), dep.getName());
         }
      }
      return source;
   }

   private static String addManagedDependencies(String source, List<GradleDependency> deps)
   {
      for (GradleDependency dep : deps)
      {
         source = GradleSourceUtil.insertManagedDependency(source, dep.getGroup(), dep.getName(),
                  dep.getVersion(), dep.getConfigurationName());
      }
      return source;
   }

   private static String removeManagedDependencies(String source, List<GradleDependency> deps)
   {
      for (GradleDependency dep : deps)
      {
         source = GradleSourceUtil.removeManagedDependency(source, dep.getGroup(), dep.getName(),
                  dep.getVersion(), dep.getConfigurationName());
      }
      return source;
   }

   private static String addPlugins(String source, List<GradlePlugin> plugins)
   {
      for (GradlePlugin plugin : plugins)
      {
         source = GradleSourceUtil.insertPlugin(source, plugin.getClazz());
      }
      return source;
   }

   private static String removePlugins(String source, List<GradlePlugin> plugins)
   {
      for (GradlePlugin plugin : plugins)
      {
         source = GradleSourceUtil.removePlugin(source, plugin.getClazz());
      }
      return source;
   }

   private static String addRepositories(String source, List<GradleRepository> repos)
   {
      for (GradleRepository repo : repos)
      {
         source = GradleSourceUtil.insertRepository(source, repo.getUrl());
      }
      return source;
   }

   private static String removeRepositories(String source, List<GradleRepository> repos)
   {
      for (GradleRepository repo : repos)
      {
         source = GradleSourceUtil.removeRepository(source, repo.getUrl());
      }
      return source;
   }

   private static String setProperties(String source, Map<String, String> properties)
   {
      for (Map.Entry<String, String> entry : properties.entrySet())
      {
         source = GradleSourceUtil.setProperty(source, GradleSourceUtil.PROJECT_PROPERTY_PREFIX + entry.getKey(),
                  entry.getValue());
      }
      return source;
   }

   private static String removeProperties(String source, Set<String> properties)
   {
      for (String property : properties)
      {
         source = GradleSourceUtil.removeProperty(source, GradleSourceUtil.PROJECT_PROPERTY_PREFIX + property);
      }
      return source;
   }

   /**
    * Calculates difference between first and second (all first elements minus second).
    */
   private static <T> List<T> subtract(List<T> first, List<T> second)
   {
      List<T> result = Lists.newArrayList(first);
      result.removeAll(second);
      return result;
   }

   private static Map<String, String> subtract(Map<String, String> first, Map<String, String> second)
   {
      Map<String, String> result = Maps.newHashMap(first);
      for (Map.Entry<String, String> entry : second.entrySet())
      {
         if (entry.getValue().equals(first.get(entry.getKey())))
         {
            result.remove(entry.getKey());
         }
      }
      return result;
   }
}
