package org.jboss.forge.addon.gradle.projects;

import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.DependencyRepository;
import org.jboss.forge.addon.resource.DirectoryResource;

public class ProjectAssert
{
   public static void assertContainsDirectoryNamed(List<DirectoryResource> dirs, String name)
   {
      for (DirectoryResource dir : dirs)
      {
         if (dir.getFullyQualifiedName().endsWith(name))
         {
            return;
         }
      }
      fail("List of directories doesn't contain directory named " + name);
   }

   public static void assertDirectoryIsOneOf(DirectoryResource dir, String... names)
   {
      for (String name : names)
      {
         if (dir.getFullyQualifiedName().endsWith(name))
         {
            return;
         }
      }
      fail(String.format("Directory [%s] is not one of %s", dir.getFullyQualifiedName(), names));
   }

   public static void assertContainsDependency(List<Dependency> deps, String scope, String artifact, String group,
            String version)
   {
      for (Dependency dep : deps)
      {
         if (dep.getCoordinate().getArtifactId().equals(artifact) &&
                  dep.getCoordinate().getGroupId().equals(group) &&
                  dep.getCoordinate().getVersion().equals(version) &&
                  dep.getScopeType().equals(scope))
         {
            return;
         }
      }
      fail(String.format("Deps doesn't contain dependency %s '%s:%s:%s'", scope, group, artifact, version));
   }
   
   public static void assertNotContainsDependency(List<Dependency> deps, String scope, String artifact, String group,
            String version)
   {
      for (Dependency dep : deps)
      {
         if (dep.getCoordinate().getArtifactId().equals(artifact) &&
                  dep.getCoordinate().getGroupId().equals(group) &&
                  dep.getCoordinate().getVersion().equals(version) &&
                  dep.getScopeType().equals(scope))
         {
            fail(String.format("Deps contains dependency %s '%s:%s:%s'", scope, group, artifact, version));
         }
      }
   }
   
   public static void assertContainsRepository(List<DependencyRepository> repos, String url)
   {
      for (DependencyRepository repo : repos)
      {
         if (repo.getUrl().equals(url))
         {
            return;
         }
      }
      fail(String.format("Repositories doesn't contain repository with url %s", url));
   }
   
   public static void assertNotContainsRepository(List<DependencyRepository> repos, String url)
   {
      for (DependencyRepository repo : repos)
      {
         if (repo.getUrl().equals(url))
         {
            fail(String.format("Repositories contains repository with url %s", url));
         }
      }
   }
}
