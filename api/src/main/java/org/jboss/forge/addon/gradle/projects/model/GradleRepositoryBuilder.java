/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

/**
 * @author Adam Wyłuda
 */
public class GradleRepositoryBuilder
{
   private String name;
   private String url;
   
   private GradleRepositoryBuilder()
   {
   }
   
   public static GradleRepositoryBuilder create() {
      return new GradleRepositoryBuilder();
   }

   public String getName()
   {
      return name;
   }

   public GradleRepositoryBuilder setName(String name)
   {
      this.name = name;
      return this;
   }

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
