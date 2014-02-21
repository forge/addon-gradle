/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.gradle.projects.facets;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.EnterpriseResourcesFacet;
import org.jboss.forge.addon.projects.facets.PackagingFacet;

/**
 * @author Adam Wyłuda
 */
@FacetConstraint({ PackagingFacet.class })
public class GradleEnterpriseResourcesFacet extends AbstractFacet<Project> implements EnterpriseResourcesFacet
{
   @Override
   public boolean install()
   {
      if (!isInstalled())
      {
         Project project = getFaceted();
         project.getFacet(PackagingFacet.class).setPackagingType("ear");
      }
      return isInstalled();
   }

   @Override
   public boolean isInstalled()
   {
      Project project = getFaceted();
      String packagingType = project.getFacet(PackagingFacet.class).getPackagingType();

      return packagingType.equals("ear");
   }

}
