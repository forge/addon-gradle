/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link GradleRepository}.
 * 
 * @see GradleModel
 * 
 * @author Adam Wyłuda
 */
public class GradleRepositoryBuilder implements GradleRepository
{
   private String name = "MavenRepo";
   private String url;
   
   private GradleRepositoryBuilder()
   {
   }
   
   public static GradleRepositoryBuilder create()
   {
      return new GradleRepositoryBuilder();
   }
   
   /**
    * Creates a copy of given repository.
    */
   public static GradleRepositoryBuilder create(GradleRepository repo)
   {
      GradleRepositoryBuilder builder = new GradleRepositoryBuilder();
      
      builder.url = repo.getUrl();
      
      return builder;
   }
   
   /**
    * Performs a deep copy of given repositories. 
    */
   public static List<GradleRepository> deepCopy(List<GradleRepository> repos)
   {
      List<GradleRepository> list = new ArrayList<GradleRepository>();
      
      for (GradleRepository repo : repos)
      {
         list.add(create(repo));
      }
      
      return list;
   }

   @Override
   public String getName()
   {
      return name;
   }
   
   public GradleRepositoryBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

   @Override
   public String getUrl()
   {
      return url;
   }

   public GradleRepositoryBuilder setUrl(String url)
   {
      this.url = url;
      return this;
   }
}
