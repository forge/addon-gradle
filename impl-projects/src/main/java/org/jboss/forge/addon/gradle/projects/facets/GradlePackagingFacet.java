/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.io.PrintStream;
import java.util.List;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModelBuilder;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.building.BuildException;
import org.jboss.forge.addon.projects.building.ProjectBuilder;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.resource.Resource;

/**
 * @author Adam Wyłuda
 */
@FacetConstraints({
         @FacetConstraint(GradleFacet.class)
})
public class GradlePackagingFacet extends AbstractFacet<Project> implements PackagingFacet
{
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
   public void setPackagingType(String type)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setPackaging(type);
      getGradleFacet().setModel(model);
   }

   @Override
   public String getPackagingType()
   {
      return getGradleFacet().getModel().getPackaging();
   }

   @Override
   public Resource<?> getFinalArtifact()
   {
      return getFaceted().getRootDirectory().getChild(getGradleFacet().getModel().getArchivePath());
   }

   @Override
   public ProjectBuilder createBuilder()
   {
      return new ProjectBuilder()
      {
         private List<String> arguments = Lists.newArrayList();
         private boolean runTests = false;

         @Override
         public ProjectBuilder addArguments(String... args)
         {
            for (String arg : args)
            {
               arguments.add(arg);
            }
            return this;
         }

         @Override
         public ProjectBuilder runTests(boolean test)
         {
            runTests = test;
            return this;
         }

         @Override
         public Resource<?> build() throws BuildException
         {
            if (!(arguments.contains("build") || arguments.contains("assemble")))
            {
               // According to:
               // http://www.gradle.org/docs/current/userguide/img/javaPluginTasks.png
               // build is assemble + test
               arguments.add(runTests ? "build" : "assemble");
            }
            getGradleFacet().executeTask(runTests ? "test" : "", "",
                     (String[]) arguments.toArray(new String[arguments.size()]));
            return getFinalArtifact();
         }

         @Override
         public Resource<?> build(PrintStream out, PrintStream err) throws BuildException
         {
            // TODO: Redirect to provided out and err
            return build();
         }
      };
   }

   @Override
   public Resource<?> executeBuild(String... args)
   {
      getGradleFacet().executeTask("build", "", args);
      return getFinalArtifact();
   }

   @Override
   public String getFinalName()
   {
      return getGradleFacet().getModel().getArchiveName();
   }

   @Override
   public void setFinalName(String finalName)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setArchiveName(finalName);
      getGradleFacet().setModel(model);
   }

   private GradleFacet getGradleFacet()
   {
      return getFaceted().getFacet(GradleFacet.class);
   }
}
