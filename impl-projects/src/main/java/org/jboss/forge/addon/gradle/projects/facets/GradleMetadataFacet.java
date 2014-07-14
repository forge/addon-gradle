/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.GradleProjectProvider;
import org.jboss.forge.addon.gradle.projects.model.GradleModelBuilder;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectProvider;
import org.jboss.forge.addon.projects.facets.MetadataFacet;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author Adam Wyłuda
 */
@FacetConstraints({
         @FacetConstraint(GradleFacet.class)
})
public class GradleMetadataFacet extends AbstractFacet<Project> implements MetadataFacet
{
   @Inject
   private GradleProjectProvider projectProvider;

   @Override
   public ProjectProvider getProjectProvider()
   {
      return projectProvider;
   }

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
   public String getProjectName()
   {
      return getGradleFacet().getModel().getName();
   }

   @Override
   public GradleMetadataFacet setProjectName(String name)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setName(name);
      getGradleFacet().setModel(model);
      return this;
   }

   @Override
   public String getTopLevelPackage()
   {
      return getGradleFacet().getModel().getGroup();
   }

   @Override
   public String getProjectGroupName()
   {
      return getTopLevelPackage();
   }

   @Override
   public GradleMetadataFacet setTopLevelPackage(String groupId)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setGroup(groupId);
      getGradleFacet().setModel(model);
      return this;
   }

   @Override
   public MetadataFacet setProjectGroupName(String groupId)
   {
      return setTopLevelPackage(groupId);
   }

   @Override
   public String getProjectVersion()
   {
      return getGradleFacet().getModel().getVersion();
   }

   @Override
   public GradleMetadataFacet setProjectVersion(String version)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setVersion(version);
      getGradleFacet().setModel(model);
      return this;
   }

   @Override
   public Dependency getOutputDependency()
   {
      return DependencyBuilder.create().setGroupId(getTopLevelPackage()).setArtifactId(getProjectName())
               .setVersion(getProjectVersion());
   }

   @Override
   public Map<String, String> getEffectiveProperties()
   {
      return getGradleFacet().getModel().getEffectiveProperties();
   }

   @Override
   public Map<String, String> getDirectProperties()
   {
      return getGradleFacet().getModel().getProperties();
   }

   @Override
   public String getEffectiveProperty(String name)
   {
      return getEffectiveProperties().get(name);
   }

   @Override
   public String getDirectProperty(String name)
   {
      return getDirectProperties().get(name);
   }

   @Override
   public GradleMetadataFacet setDirectProperty(String name, String value)
   {
      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      model.setProperty(name, value);
      getGradleFacet().setModel(model);
      return this;
   }

   @Override
   public String removeDirectProperty(String name)
   {
      String property = null;

      GradleModelBuilder model = GradleModelBuilder.create(getGradleFacet().getModel());
      property = model.getProperties().get(name);
      model.removeProperty(name);
      getGradleFacet().setModel(model);

      return property;
   }

   private GradleFacet getGradleFacet()
   {
      return getFaceted().getFacet(GradleFacet.class);
   }

   @Override
   public boolean isValid()
   {
      try
      {
         /*
          * Naive attempt to determine if things are in a valid state.
          */
         GradleModelBuilder.create(getGradleFacet().getModel()).getProperties();
         return true;
      }
      catch (Exception e)
      {
         return false;
      }
   }
}
