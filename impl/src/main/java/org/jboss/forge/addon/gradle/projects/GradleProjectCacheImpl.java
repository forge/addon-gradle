/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.gradle.jarjar.com.google.common.collect.Maps;
import org.jboss.forge.addon.gradle.parser.GradleUtil;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleModelLoader;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;

/**
 * @author Adam Wyłuda
 */
public class GradleProjectCacheImpl implements GradleProjectCache
{
   @Inject
   private GradleManager manager;
   @Inject
   private GradleModelLoader modelLoader;
   @Inject
   private ResourceFactory resourceFactory;
   
   private Map<String, GradleModel> cache = Maps.newHashMap();

   @Override
   public GradleModel getModel(String buildScriptPath) throws IOException
   {
      // TODO Check if file has been modified since last use and reload
      GradleModel model = cache.get(buildScriptPath);
      if (model != null)
      {
         return model;
      }
      checkIfIsForgeLibraryInstalled(buildScriptPath);
      model = loadModel(buildScriptPath);
      return model;
   }
   
   private GradleModel loadModel(String buildScriptPath) throws IOException
   {
      String directory = new File(buildScriptPath).getParent();
      
      manager.runGradleBuild(directory, GradleUtil.FORGE_OUTPUT_TASK, "");
      
      File forgeOutputFile = new File(directory, GradleUtil.FORGE_OUTPUT_XML);
      String forgeOutput = FileUtils.readFileToString(forgeOutputFile);
      
//      forgeOutputFile.delete();
      
      // TODO create file resource instance for forge output XML
      return modelLoader.loadFromXML(null, forgeOutput);
   }
   
   private void checkIfIsForgeLibraryInstalled(String buildScriptPath) throws IOException
   {
      File directory = new File(buildScriptPath).getParentFile();
      File scriptFile = new File(buildScriptPath);
      
      String script = FileUtils.readFileToString(scriptFile);
      String newScript = GradleUtil.checkForIncludeForgeLibraryAndInsert(script);
      
      // If Forge library is not included
      if (!script.equals(newScript))
      {
         FileUtils.writeStringToFile(scriptFile, newScript);
      }
      
      File forgeLib = new File(directory, GradleUtil.FORGE_LIBRARY);
      // TODO check existing forge library version and replace with newer if necessary
      if (!forgeLib.exists())
      {
         FileUtils.copyInputStreamToFile(getClass().getResourceAsStream(GradleUtil.FORGE_LIBRARY_RESOURCE), forgeLib);
      }
   }
}
