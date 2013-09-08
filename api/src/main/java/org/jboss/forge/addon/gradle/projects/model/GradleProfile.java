/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

/**
 * It represents Gradle simulated profile. It offers similar functionality as Maven profiles. Gradle profiles are
 * implemented as additional Gradle build scripts using convention: {@code name-profile.gradle}
 * 
 * @author Adam Wyłuda
 */
public interface GradleProfile
{
   String getName();

   GradleDirectModel getModel();
}
