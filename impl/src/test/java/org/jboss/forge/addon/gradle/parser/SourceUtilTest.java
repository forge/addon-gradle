/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Wyłuda
 */
public class SourceUtilTest
{

   @Test
   public void insertLineTest()
   {
      String source =
               "dependencies {compile 'a:b:1.0'\n" +
                        "}\n";
      String line = "\n     testCompile 'c:d:2.0";
      String expectedOutput =
               "dependencies {" +
                        line +
                        "compile 'a:b:1.0'\n" +
                        "}\n";

      String output = SourceUtil.insertString(source, line, 1, 15);
      assertEquals(expectedOutput, output);
   }

   @Test
   public void positionInSourceTest()
   {
      String source =
               "public interface AwardProvider{\n" + // 32 characters
                        "     void TShirt getTShirt();\n" + // 28 characters
                        "}\n";

      int position = SourceUtil.positionInSource(source, 2, 013);
      int answer = 42;
      assertEquals(answer, position);
   }

   @Test(expected = IllegalArgumentException.class)
   public void positionInSourceTestException()
   {
      String source =
               "#include <stdio.h>\n" +
                        "main(){}\n";

      SourceUtil.positionInSource(source, 3, 1);
   }

   @Test
   public void removeSourceCodeElementTest()
   {
      String source =
               "import java.util.Scanner;";
      String expectedOutput =
               "import util.Scanner;";

      String output = SourceUtil.removeSourceFragment(source, 1, 8, 1, 13);
      assertEquals(expectedOutput, output);
   }
}
