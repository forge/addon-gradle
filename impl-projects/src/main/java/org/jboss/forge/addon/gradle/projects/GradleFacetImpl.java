/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.gradle.parser.GradleSourceUtil;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleModelLoader;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.furnace.util.Streams;

/**
 * @author Adam Wyłuda
 */
public class GradleFacetImpl extends AbstractFacet<Project> implements GradleFacet
{
   @Inject
   private GradleManager manager;
   @Inject
   private GradleModelLoader modelLoader;
   @Inject
   private ResourceFactory resourceFactory;
   @Inject
   private GradleProjectCache cache;

   @Override
   public boolean install()
   {
      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return true;
   }

   @Override
   public void executeTask(String task)
   {
      executeTask(task, "");
   }

   @Override
   public void executeTask(String task, String profile)
   {
      manager.runGradleBuild(getFaceted().getProjectRoot().getFullyQualifiedName(), task, profile);
   }

   @Override
   public GradleModel getModel()
   {
      try
      {
         String buildScriptPath = new File(new File(getFaceted().getProjectRoot().getFullyQualifiedName()),
                  "build.gradle").getAbsolutePath();
         // TODO Check if file has been modified since last use and reload
         GradleModel model = cache.getModel(buildScriptPath);
         if (model != null)
         {
            return model;
         }
         checkIfIsForgeLibraryInstalled(buildScriptPath);
         model = loadModel(buildScriptPath);
         cache.putModel(buildScriptPath, model);
         return model;
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public FileResource<?> getGradleResource()
   {
      return (FileResource<?>) getFaceted().getProjectRoot().getChild("build.gradle");
   }

   private GradleModel loadModel(String buildScriptPath) throws IOException
   {
      String directory = new File(buildScriptPath).getParent();

      manager.runGradleBuild(directory, GradleSourceUtil.FORGE_OUTPUT_TASK, "");

      FileResource<?> forgeOutputfile = (FileResource<?>) resourceFactory.create(new File(directory,
               GradleSourceUtil.FORGE_OUTPUT_XML));
      String forgeOutput = Streams.toString(forgeOutputfile.getResourceInputStream());

      forgeOutputfile.delete();

      // TODO create file resource instance for forge output XML
      return modelLoader.loadFromXML(null, forgeOutput);
   }

   private void checkIfIsForgeLibraryInstalled(String buildScriptPath) throws IOException
   {
      File directory = new File(buildScriptPath).getParentFile();
      File scriptFile = new File(buildScriptPath);
      FileResource<?> scriptResource = (FileResource<?>) resourceFactory.create(scriptFile);

      InputStream scriptInputStream = scriptResource.getResourceInputStream();
      String script = Streams.toString(scriptInputStream);
      Streams.closeQuietly(scriptInputStream);
      String newScript = GradleSourceUtil.checkForIncludeForgeLibraryAndInsert(script);

      // If Forge library is not included
      if (!script.equals(newScript))
      {
         scriptResource.setContents(newScript);
      }

      FileResource<?> forgeLib = (FileResource<?>) resourceFactory
               .create(new File(directory, GradleSourceUtil.FORGE_LIBRARY));
      // TODO check existing forge library version and replace with newer if necessary
      if (!forgeLib.exists())
      {
         forgeLib.setContents(getClass().getResourceAsStream(GradleSourceUtil.FORGE_LIBRARY_RESOURCE));
      }
   }

   @Override
   public void setModel(GradleModel model)
   {
      // TODO Auto-generated method stub
      
   }
}
