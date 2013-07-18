/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.util.Map;

import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;

/**
 * @author Adam Wyłuda
 */
public class GradleProjectCacheImpl implements GradleProjectCache
{
   
   private Map<String, GradleModel> cache = Maps.newHashMap();

   @Override
   public GradleModel getModel(String buildScriptPath) 
   {
      return cache.get(buildScriptPath);
   }
   
   @Override
   public void putModel(String buildScriptPath, GradleModel model)
   {
      cache.put(buildScriptPath, model);
   }
}
