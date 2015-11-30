/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.gradle.projects.facets.GradleDependencyFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleEnterpriseResourcesFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleJavaCompilerFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleJavaSourceFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleMetadataFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradlePackagingFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleResourcesFacet;
import org.jboss.forge.addon.gradle.projects.facets.GradleWebResourcesFacet;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.AbstractProjectProvider;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProvidedProjectFacet;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.projects.facets.EnterpriseResourcesFacet;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.Resource;

/**
 * @author Adam Wyłuda
 */
public class GradleProjectProviderImpl extends AbstractProjectProvider implements GradleProjectProvider
{
   private static final Logger LOG = Logger.getLogger(GradleProjectProviderImpl.class.getName());
   private Map<Class<? extends ProjectFacet>, Class<? extends ProjectFacet>> facets = new IdentityHashMap<>();

   @Inject
   private FacetFactory facetFactory;

   public GradleProjectProviderImpl()
   {
      facets.put(DependencyFacet.class, GradleDependencyFacet.class);
      facets.put(JavaCompilerFacet.class, GradleJavaCompilerFacet.class);
      facets.put(JavaSourceFacet.class, GradleJavaSourceFacet.class);
      facets.put(MetadataFacet.class, GradleMetadataFacet.class);
      facets.put(PackagingFacet.class, GradlePackagingFacet.class);
      facets.put(ResourcesFacet.class, GradleResourcesFacet.class);
      facets.put(WebResourcesFacet.class, GradleWebResourcesFacet.class);
      facets.put(EnterpriseResourcesFacet.class, GradleEnterpriseResourcesFacet.class);
   }

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

      try
      {
         facetFactory.register(project, GradleWebResourcesFacet.class);
      }
      catch (IllegalStateException e)
      {
         LOG.log(Level.FINE, "Could not install [" + GradleWebResourcesFacet.class.getName() + "] into project ["
                  + project + "]", e);
      }

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

   @Override
   public int priority()
   {
      return 100;
   }

   @Override
   public Class<? extends ProjectFacet> resolveProjectFacet(Class<? extends ProjectFacet> facet)
   {
      return facets.getOrDefault(facet, facet);
   }
}
