/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModelBuilder;
import org.jboss.forge.addon.gradle.projects.model.GradlePluginBuilder;
import org.jboss.forge.addon.gradle.projects.model.GradlePluginType;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet;
import org.jboss.forge.addon.projects.Project;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@FacetConstraints({
         @FacetConstraint({ GradleFacet.class })
})
public class GradleJavaCompilerFacet extends AbstractFacet<Project> implements JavaCompilerFacet
{
   @Override
   public boolean install()
   {
      if (!this.isInstalled())
      {
         GradleModelBuilder model = GradleModelBuilder.create(getFaceted().getFacet(GradleFacet.class).getModel());
         if (!model.hasPlugin(GradlePluginBuilder.create(GradlePluginType.JAVA)))
         {
            model.addPlugin(GradlePluginBuilder.create(GradlePluginType.JAVA));
            getFaceted().getFacet(GradleFacet.class).setModel(model);
         }
      }
      return isInstalled();
   }

   @Override
   public boolean isInstalled()
   {
      GradleModelBuilder model = GradleModelBuilder.create(getFaceted().getFacet(GradleFacet.class).getModel());
      return model.hasPlugin(GradlePluginBuilder.create(GradlePluginType.JAVA));
   }

   @Override
   public void setSourceCompilerVersion(CompilerVersion version)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setSourceCompatibility(version.toString());
      getGradleFacet().setModel(model);
   }

   @Override
   public void setTargetCompilerVersion(CompilerVersion version)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setTargetCompatibility(version.toString());
      getGradleFacet().setModel(model);
   }

   @Override
   public CompilerVersion getSourceCompilerVersion()
   {
      return CompilerVersion.getValue(getGradleFacet().getModel().getSourceCompatibility());
   }

   @Override
   public CompilerVersion getTargetCompilerVersion()
   {
      return CompilerVersion.getValue(getGradleFacet().getModel().getTargetCompatiblity());
   }

   private GradleFacet getGradleFacet()
   {
      return getFaceted().getFacet(GradleFacet.class);
   }
}
