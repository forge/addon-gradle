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
public class GradleRepositoryImpl implements GradleRepository
{
   private final String name;
   private final String url;

   public GradleRepositoryImpl(String name, String url)
   {
      this.name = name;
      this.url = url;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public String getUrl()
   {
      return url;
   }
}
