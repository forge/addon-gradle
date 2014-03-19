/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.gradle.projects.facets.GradleDependencyFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleJavaCompilerFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleJavaSourceFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleMetadataFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradlePackagingFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleResourcesFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProvidedProjectFacet;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.resource.Resource;

/**
 * @author Adam Wyłuda
 */
public class GradleProjectProviderImpl implements GradleProjectProvider
{
   @Inject
   private FacetFactory facetFactory;

   @Override
   public String getType()
   {
      return "Gradle";
   }

   @Override
   public Project createProject(Resource<?> targetDir)
   {
      Project project = new GradleProject(targetDir);

      facetFactory.install(project, GradleFacetImpl.class);
      facetFactory.install(project, GradleMetadataFacet.class);
      facetFactory.install(project, GradlePackagingFacet.class);
      facetFactory.install(project, GradleDependencyFacet.class);
      facetFactory.install(project, GradleResourcesFacet.class);
      facetFactory.install(project, GradleJavaCompilerFacet.class);
      facetFactory.install(project, GradleJavaSourceFacet.class);

      return project;
   }

   @Override
   public boolean containsProject(Resource<?> resource)
   {
      Resource<?> buildGradle = resource.getChild("build.gradle");
      return buildGradle != null && buildGradle.exists();
   }

   @Override
   public Set<Class<? extends ProvidedProjectFacet>> getProvidedFacetTypes()
   {
      Set<Class<? extends ProvidedProjectFacet>> result = new HashSet<>();
      result.add(GradleFacet.class);
      result.add(MetadataFacet.class);
      result.add(DependencyFacet.class);
      result.add(PackagingFacet.class);

      return Collections.unmodifiableSet(result);
   }

   @SuppressWarnings("unused")
   private void addSafe(Set<Class<? extends ProvidedProjectFacet>> result,
            Callable<Class<? extends ProvidedProjectFacet>> callable)
   {
      try
      {
         Class<? extends ProvidedProjectFacet> facetType = callable.call();
         if (facetType != null)
            result.add(facetType);
      }
      catch (NoClassDefFoundError e)
      {
      }
      catch (ClassNotFoundException e)
      {
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public int priority()
   {
      return 100;
   }
}
