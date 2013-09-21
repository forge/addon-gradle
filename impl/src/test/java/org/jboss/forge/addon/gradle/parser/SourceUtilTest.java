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
   public void testInsertLine()
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
   public void testPositionInSource()
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
   public void testPositionInSourceException()
   {
      String source =
               "#include <stdio.h>\n" +
                        "main(){}\n";

      SourceUtil.positionInSource(source, 3, 1);
   }

   @Test
   public void testRemoveSourceFragment()
   {
      String source =
               "import java.util.Scanner;";
      String expectedOutput =
               "import util.Scanner;";

      String output = SourceUtil.removeSourceFragment(source, 1, 8, 1, 13);
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testRemoveSourceFragmentWithLine()
   {
      String source =
               "subprojects {\n" +
                        "    println 'abcdef'\n" +
                        "    abcdef\n" +
                        "}\n";
      String expected =
               "subprojects {\n" +
                        "    println 'abcdef'\n" +
                        "}\n";
      String result = SourceUtil.removeSourceFragmentWithLine(source, 3, 5, 3, 11);
      assertEquals(expected, result);
   }

   @Test
   public void testRemoveSourceFragmentWithLineNested()
   {
      String source = "" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            println AAA\n" +
               "            println BBB\n" +
               "            println CCC\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String expected = "" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            println AAA\n" +
               "            println CCC\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String result = SourceUtil.removeSourceFragmentWithLine(source, 5, 13, 5, 24);
      assertEquals(expected, result);
   }

   @Test
   public void testAppendCodeToClosure()
   {
      String source = "" +
               "abc\n" +
               "invo {\n" +
               "    cation {\n" +
               "        very important stuff\n" +
               "    }  \n" +
               "}\n" +
               "\n" +
               "println xyz\n";
      String expected = "" +
               "abc\n" +
               "invo {\n" +
               "    cation {\n" +
               "        very important stuff\n" +
               "        something\n" +
               "        new\n" +
               "    }  \n" +
               "}\n" +
               "\n" +
               "println xyz\n";
      String codeToBeInserted = "" +
               "something\nnew\n";

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      InvocationWithClosure invocation;
      invocation = parser.invocationWithClosureByName("invo").get();
      invocation = invocation.invocationWithClosureByName("cation").get();

      String result = SourceUtil.appendCodeToClosure(source, invocation, codeToBeInserted);
      assertEquals(expected, result);
   }

   @Test
   public void testIndentCode()
   {
      String source = "" +
               "x {\n" +
               "    yz\n" +
               "}\n";
      String expected = "" +
               "       x {\n" +
               "           yz\n" +
               "       }\n";
      String result = SourceUtil.indentCode(source, 7);
      assertEquals(expected, result);
   }

   @Test
   public void testInsertIntoInvocationAtPath()
   {
      String source = "" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            compile 'x:y:z'\n" +
               "        }\n" +
               "        d {\n" +
               "            e {\n" +
               "                xyz\n" +
               "            }\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String expected = "" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            compile 'x:y:z'\n" +
               "            configName\n" +
               "            xyzABC\n" +
               "        }\n" +
               "        d {\n" +
               "            e {\n" +
               "                xyz\n" +
               "            }\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String result = SourceUtil.insertIntoInvocationAtPath(source, "configName\nxyzABC", "a", "b", "c");
      assertEquals(expected, result);
   }

   @Test
   public void testInsertIntoInvocationAtPathCreatePath()
   {
      String source = "abcdefxyz";
      String expected = "" +
               "abcdefxyz\n" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            println x\n" +
               "        }\n" +
               "    }\n" +
               "}\n" +
               "";
      String result = SourceUtil.insertIntoInvocationAtPath(source, "println x", "a", "b", "c");
      assertEquals(expected, result);
   }

   @Test
   public void testInsertIntoInvocationAtPathPartiallyCreatePath()
   {
      String source = "" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            compile 'x:y:z'\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String expected = "" +
               "a {\n" +
               "    b {\n" +
               "        c {\n" +
               "            compile 'x:y:z'\n" +
               "            d {\n" +
               "                e {\n" +
               "                    xyz\n" +
               "                }\n" +
               "            }\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String result = SourceUtil.insertIntoInvocationAtPath(source, "xyz", "a", "b", "c", "d", "e");
      assertEquals(expected, result);
   }

   @Test
   public void testCreateInvocationPath()
   {
      String expected = "" +
               "x {\n" +
               "    y {\n" +
               "        z {\n" +
               "            con\n" +
               "            tent\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
      String result = SourceUtil.createInvocationPath("con\ntent", "x", "y", "z");
      assertEquals(expected, result);
   }

   @Test
   public void testFixClosureColumn()
   {
      assertEquals(5, SourceUtil.fixClosureColumn("x {}  \n", 1, 7));
      assertEquals(5, SourceUtil.fixClosureColumn("x {}\t \n\n\n", 1, 7));
   }
}
