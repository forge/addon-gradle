/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.addon.gradle.projects.facets.GradleDependencyFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleJavaSourceFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleMetadataFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradlePackagingFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleResourceFacet;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProjectType;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;

/**
 * @author Adam Wyłuda
 */
public class GradleJavaProjectType implements ProjectType
{
   @Override
   public String getType()
   {
      return "Gradle - Java";
   }

   @Override
   public Class<? extends UIWizardStep> getSetupFlow()
   {
      // TODO Setup flow for Gradle Java project
      return null;
   }

   @Override
   public Iterable<Class<? extends ProjectFacet>> getRequiredFacets()
   {
      List<Class<? extends ProjectFacet>> result = new ArrayList<Class<? extends ProjectFacet>>();
      result.add(GradleFacetImpl.class);
      result.add(GradleMetadataFacet.class);
      result.add(GradlePackagingFacet.class);
      result.add(GradleDependencyFacet.class);
      result.add(GradleResourceFacet.class);
      result.add(GradleJavaSourceFacet.class);
      return result;
   }
   
   @Override
   public String toString()
   {
      return "java";
   }
}
