/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.facets;

import java.io.File;
import java.util.List;

import org.gradle.internal.impldep.com.google.common.collect.Lists;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.gradle.projects.GradleFacet;
import org.jboss.forge.addon.gradle.projects.model.GradleModel;
import org.jboss.forge.addon.gradle.projects.model.GradleSourceDirectory;
import org.jboss.forge.addon.gradle.projects.model.GradleSourceSet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.parser.java.resources.JavaResourceVisitor;
import org.jboss.forge.addon.parser.java.utils.Packages;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceException;
import org.jboss.forge.addon.resource.ResourceFilter;
import org.jboss.forge.addon.resource.visit.ResourceVisit;
import org.jboss.forge.furnace.util.Strings;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaPackageInfoSource;
import org.jboss.forge.roaster.model.source.JavaSource;

/**
 * @author Adam Wyłuda
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@FacetConstraints({
         @FacetConstraint({ GradleFacet.class })
})
public class GradleJavaSourceFacet extends AbstractFacet<Project> implements JavaSourceFacet
{
   @Override
   public boolean install()
   {
      if (!this.isInstalled())
      {
         for (DirectoryResource folder : this.getSourceDirectories())
         {
            folder.mkdirs();
         }
      }
      return isInstalled();
   }

   @Override
   public boolean isInstalled()
   {
      return getSourceDirectory().exists();
   }

   @Override
   public String calculateName(JavaResource resource)
   {
      String fullPath = Packages.fromFileSyntax(resource.getFullyQualifiedName());
      String pkg = calculatePackage(resource);
      String name = fullPath.substring(fullPath.lastIndexOf(pkg) + pkg.length() + 1);
      name = name.substring(0, name.lastIndexOf(".java"));
      return name;
   }

   @Override
   public String calculatePackage(JavaResource resource)
   {
      List<DirectoryResource> folders = getSourceDirectories();
      String pkg = null;
      for (DirectoryResource folder : folders)
      {
         String sourcePrefix = folder.getFullyQualifiedName();
         pkg = resource.getParent().getFullyQualifiedName();
         if (pkg.startsWith(sourcePrefix))
         {
            pkg = pkg.substring(sourcePrefix.length() + 1);
            break;
         }
      }
      pkg = Packages.fromFileSyntax(pkg);

      return pkg;
   }

   @Override
   public String getBasePackage()
   {
      return Packages.toValidPackageName(getFaceted().getFacet(GradleFacet.class).getModel().getGroup());
   }

   @Override
   public DirectoryResource getBasePackageDirectory()
   {
      return getSourceDirectory().getChildDirectory(Packages.toFileSyntax(getBasePackage()));
   }

   @Override
   public List<DirectoryResource> getSourceDirectories()
   {
      List<DirectoryResource> resources = Lists.newArrayList();
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();

      for (GradleSourceSet sourceSet : model.getEffectiveSourceSets())
      {
         for (GradleSourceDirectory sourceDir : sourceSet.getJavaDirectories())
         {
            resources.add(directoryResourceFromRelativePath(sourceDir.getPath()));
         }
      }

      return resources;
   }

   @Override
   public DirectoryResource getSourceDirectory()
   {
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();
      GradleSourceDirectory dir = GradleResourceUtil.findSourceSetNamed(model.getEffectiveSourceSets(), "main")
               .getJavaDirectories().get(0);
      return directoryResourceFromRelativePath(dir.getPath());
   }

   @Override
   public DirectoryResource getTestSourceDirectory()
   {
      GradleModel model = getFaceted().getFacet(GradleFacet.class).getModel();
      GradleSourceDirectory dir = GradleResourceUtil.findSourceSetNamed(model.getEffectiveSourceSets(), "test")
               .getJavaDirectories().get(0);
      return directoryResourceFromRelativePath(dir.getPath());
   }

   @Override
   public JavaResource saveJavaSource(JavaSource<?> source)
   {
      return getJavaResource(Packages.toFileSyntax(source.getQualifiedName()) + ".java").setContents(source);
   }

   @Override
   public JavaResource saveTestJavaSource(JavaSource<?> source)
   {
      return getTestJavaResource(Packages.toFileSyntax(source.getQualifiedName()) + ".java").setContents(source);
   }

   @Override
   public JavaResource getJavaResource(String relativePath)
   {
      if (Strings.isNullOrEmpty(relativePath))
      {
         throw new ResourceException("Empty relative path");
      }
      String path = relativePath.trim().endsWith(".java")
               ? relativePath.substring(0, relativePath.lastIndexOf(".java")) : relativePath;

      path = Packages.toFileSyntax(path) + ".java";
      JavaResource target = GradleResourceUtil.findFileResource(getMainJavaSources(), path).reify(JavaResource.class);
      return target;
   }

   @Override
   public JavaResource getJavaResource(JavaSource<?> javaClass)
   {
      return getJavaResource(javaClass.getQualifiedName());
   }

   @Override
   public JavaResource getTestJavaResource(String relativePath)
   {
      if (Strings.isNullOrEmpty(relativePath))
      {
         throw new ResourceException("Empty relative path");
      }
      String path = relativePath.trim().endsWith(".java")
               ? relativePath.substring(0, relativePath.lastIndexOf(".java")) : relativePath;

      path = Packages.toFileSyntax(path) + ".java";
      JavaResource target = GradleResourceUtil.findFileResource(getTestJavaSources(), path).reify(JavaResource.class);
      return target;
   }

   @Override
   public JavaResource getTestJavaResource(JavaSource<?> javaClass)
   {
      return getTestJavaResource(javaClass.getQualifiedName());
   }

   @Override
   public void visitJavaSources(final JavaResourceVisitor visitor)
   {
      for (DirectoryResource sourceFolder : getMainJavaSources())
      {
         visitSources(sourceFolder, visitor);
      }
   }

   @Override
   public void visitJavaTestSources(final JavaResourceVisitor visitor)
   {
      for (DirectoryResource testSourceFolder : getTestJavaSources())
      {
         visitSources(testSourceFolder, visitor);
      }
   }

   private void visitSources(final Resource<?> searchFolder, final JavaResourceVisitor visitor)
   {
      new ResourceVisit(searchFolder).perform(visitor, new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> type)
         {
            return type instanceof DirectoryResource;
         }
      }, new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> type)
         {
            return type instanceof JavaResource;
         }
      });
   }

   private List<DirectoryResource> getMainJavaSources()
   {
      return getJavaSourcesFromSourceSet("main");
   }

   private List<DirectoryResource> getTestJavaSources()
   {
      return getJavaSourcesFromSourceSet("test");
   }

   private List<DirectoryResource> getJavaSourcesFromSourceSet(String sourceSetName)
   {
      List<DirectoryResource> resources = Lists.newArrayList();
      GradleFacet gradleFacet = getFaceted().getFacet(GradleFacet.class);

      for (GradleSourceDirectory sourceDir : GradleResourceUtil
               .findSourceSetNamed(gradleFacet.getModel().getEffectiveSourceSets(), sourceSetName)
               .getJavaDirectories())
      {
         resources.add(directoryResourceFromRelativePath(sourceDir.getPath()));
      }

      return resources;
   }

   private DirectoryResource directoryResourceFromRelativePath(String path)
   {
      return getFaceted().getFacet(GradleFacet.class).getBuildScriptResource().getParent()
               .getChildDirectory(path);
   }

   @Override
   public DirectoryResource savePackage(String packageName, boolean createPackageInfo)
   {
      DirectoryResource child = getPackage(packageName);
      if (!child.exists())
      {
         child.mkdirs();
      }
      if (createPackageInfo)
      {
         JavaPackageInfoSource packageInfo = Roaster.create(JavaPackageInfoSource.class).setPackage(packageName);
         JavaResource resource = child.getChild("package-info.java").reify(JavaResource.class);
         resource.setContents(packageInfo);
      }
      return child;
   }

   @Override
   public DirectoryResource saveTestPackage(String packageName, boolean createPackageInfo)
   {
      DirectoryResource child = getTestPackage(packageName);
      if (!child.exists())
      {
         child.mkdirs();
      }
      if (createPackageInfo)
      {
         JavaPackageInfoSource packageInfo = Roaster.create(JavaPackageInfoSource.class).setPackage(packageName);
         JavaResource resource = child.getChild("package-info.java").reify(JavaResource.class);
         resource.setContents(packageInfo);
      }
      return child;
   }

   @Override
   public DirectoryResource getPackage(String packageName)
   {
      return getSourceDirectory().getChildDirectory(packageName.replace('.', File.separatorChar));
   }

   @Override
   public DirectoryResource getTestPackage(String packageName)
   {
      return getTestSourceDirectory().getChildDirectory(packageName.replace('.', File.separatorChar));
   }
}
