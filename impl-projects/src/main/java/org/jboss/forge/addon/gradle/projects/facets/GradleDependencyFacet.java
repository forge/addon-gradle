/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.gradle.jarjar.com.google.common.collect.Lists;
import org.gradle.tooling.model.DomainObjectSet;
import org.gradle.tooling.model.ExternalDependency;
import org.gradle.tooling.model.GradleModuleVersion;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.DependencyQuery;
import org.jboss.forge.addon.dependencies.DependencyRepository;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.GradleConnectionManager;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

/**
 * @author Adam Wyłuda
 */
public class GradleDependencyFacet extends AbstractFacet<Project> implements DependencyFacet
{
   @Inject
   private GradleConnectionManager gradleManager;

   @Override
   public boolean install()
   {
      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return getFaceted().hasFacet(GradleFacet.class);
   }

   @Override
   public void addDirectDependency(Dependency dep)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void addManagedDependency(Dependency managedDependency)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void addDirectManagedDependency(Dependency dep)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void addRepository(String name, String url)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public List<Dependency> getDependencies()
   {
      return null;
   }

   @Override
   public List<Dependency> getDependenciesInScopes(String... scopes)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Dependency getDirectDependency(Dependency dependency)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Dependency> getEffectiveDependencies()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Dependency> getEffectiveDependenciesInScopes(String... scopes)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Dependency getEffectiveDependency(Dependency dependency)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Dependency getEffectiveManagedDependency(Dependency manDep)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Dependency> getManagedDependencies()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Dependency getManagedDependency(Dependency managedDependency)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Map<String, String> getProperties()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getProperty(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<DependencyRepository> getRepositories()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean hasDirectDependency(Dependency dependency)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean hasEffectiveDependency(Dependency dependency)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean hasEffectiveManagedDependency(Dependency managedDependency)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean hasDirectManagedDependency(Dependency managedDependency)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean hasRepository(String url)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void removeDependency(Dependency dependency)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void removeManagedDependency(Dependency managedDependency)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public String removeProperty(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DependencyRepository removeRepository(String url)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Coordinate> resolveAvailableVersions(Dependency dep)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Coordinate> resolveAvailableVersions(String gavs)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Coordinate> resolveAvailableVersions(DependencyQuery query)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Dependency resolveProperties(Dependency dependency)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setProperty(String name, String value)
   {
      // TODO Auto-generated method stub
   }

   static String toForgeScopeType(String scopeType)
   {
      // TODO scope type conversion
      return scopeType;
   }
}
