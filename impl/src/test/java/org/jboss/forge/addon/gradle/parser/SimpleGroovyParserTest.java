/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Map;

import org.gradle.jarjar.com.google.common.base.Optional;
import org.junit.Test;

/**
 * @author Adam Wyłuda
 */
public class SimpleGroovyParserTest
{
   @Test
   public void testFlatSource()
   {
      String source = "\n" +
               "compile 'com.google:summer:2.0.1.3'\n" +
               "testCompile {'abc:def:0.1'}\n" +
               "apply plugin: 'java'\n" +
               "number = 30\n" +
               "def newVar = 'xyz'\n" +
               "x = 'y'";

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      assertEquals(1, parser.getInvocationsWithClosure().size());
      assertEquals(1, parser.getInvocationsWithMap().size());
      assertEquals(1, parser.getInvocationsWithString().size());
      assertEquals(1, parser.getVariableAssignments().size());

      InvocationWithString invocationWithString = parser.getInvocationsWithString().get(0);
      assertEquals("compile", invocationWithString.getMethodName());
      assertEquals("com.google:summer:2.0.1.3", invocationWithString.getString());

      InvocationWithClosure invocationWithClosure = parser.getInvocationsWithClosure().get(0);
      assertEquals("testCompile", invocationWithClosure.getMethodName());

      InvocationWithMap invocationWithMap = parser.getInvocationsWithMap().get(0);
      assertEquals("apply", invocationWithMap.getMethodName());

      Map<String, String> parameters = invocationWithMap.getParameters();
      assertEquals(1, parameters.size());

      Map.Entry<String, String> entry = parameters.entrySet().iterator().next();
      assertEquals("plugin", entry.getKey());
      assertEquals("java", entry.getValue());

      VariableAssignment assignment = parser.getVariableAssignments().get(0);
      assertEquals("x", assignment.getVariable());
      assertEquals("y", assignment.getValue());
   }

   @Test
   public void testNestedSource()
   {
      String source = "// comment\n" +
               "subprojects {\n" +
               "    apply plugin: 'groovy'\n" +
               "    apply plugin: 'java'\n" +
               "    \n" +
               "    abc = 'def'\n" +
               "    a.b.c = 'd.e.f'\n" +
               "    getX().y = 'xyz'\n" +
               "    \n" +
               "    dependencies {\n" +
               "        compile 'group:artifact:1.0.0'\n" +
               "    }\n" +
               "}\n";

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      assertEquals(1, parser.getInvocationsWithClosure().size());

      InvocationWithClosure subprojects = parser.getInvocationsWithClosure().get(0);
      assertEquals("subprojects", subprojects.getMethodName());
      assertEquals(2, subprojects.getInvocationsWithMap().size());
      assertEquals(1, subprojects.getInvocationsWithClosure().size());
      assertEquals(3, subprojects.getVariableAssignments().size());

      InvocationWithClosure dependencies = subprojects.getInvocationsWithClosure().get(0);
      assertEquals(1, dependencies.getInvocationsWithString().size());

      InvocationWithString compile = dependencies.getInvocationsWithString().get(0);
      assertEquals("compile", compile.getMethodName());
      assertEquals("group:artifact:1.0.0", compile.getString());

      VariableAssignment assignment = subprojects.getVariableAssignments().get(0);
      assertEquals("abc", assignment.getVariable());
      assertEquals("def", assignment.getValue());

      VariableAssignment assignment2 = subprojects.getVariableAssignments().get(1);
      assertEquals("a.b.c", assignment2.getVariable());
      assertEquals("d.e.f", assignment2.getValue());

      VariableAssignment assignment3 = subprojects.getVariableAssignments().get(2);
      // Seems that Groovy core parser changes getX() to this.getX()
      assertEquals("this.getX().y", assignment3.getVariable());
      assertEquals("xyz", assignment3.getValue());
   }

   @Test
   public void testInvocationWithClosureByName()
   {
      String source = "// comment\n" +
               "subprojects {\n" +
               "    apply plugin: 'groovy'\n" +
               "    apply plugin: 'java'\n" +
               "    \n" +
               "    dependencies {\n" +
               "        compile 'group:artifact:1.0.0'\n" +
               "    }\n" +
               "}\n";

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);

      InvocationWithClosure subprojects = parser.invocationWithClosureByName("subprojects").get();
      assertEquals("subprojects", subprojects.getMethodName());

      InvocationWithMap applyPlugin = subprojects.invocationWithMapByName("apply").get();
      assertEquals("apply", applyPlugin.getMethodName());

      InvocationWithClosure dependencies = subprojects.invocationWithClosureByName("dependencies").get();
      assertEquals("dependencies", dependencies.getMethodName());

      InvocationWithString compile = dependencies.invocationWithStringByName("compile").get();
      assertEquals("compile", compile.getMethodName());
   }

   @Test
   public void testInvocationWithClosureByNameAbsent()
   {
      String sauce = "" +
               "x {\n" +
               "    y {\n" +
               "    }\n" +
               "}\n";

      Optional<InvocationWithClosure> optional = SimpleGroovyParser.fromSource(sauce).invocationWithClosureByName("z");
      assertFalse(optional.isPresent());
   }

   @Test
   public void testAllInvocationAtPath()
   {
      String source = "// comment\n" +
               "subprojects {\n" +
               "    dependencies {\n" +
               "        compile 'group:artifact:1.0.0'\n" +
               "    }\n" +
               "}\n" +
               "xyz\n" +
               "subprojects {\n" +
               "    qwerty\n" +
               "    dependencies {\n" +
               "        testRuntime 'x:y:z'\n" +
               "    }\n" +
               "    println 'asdf'\n" +
               "}\n" +
               "";
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);

      List<InvocationWithClosure> list = parser.allInvocationsAtPath("subprojects", "dependencies");
      assertEquals(2, list.size());

      InvocationWithClosure invocation = list.get(0);
      assertEquals(1, invocation.getInvocationsWithString().size());
      assertEquals("compile", invocation.getInvocationsWithString().get(0).getMethodName());

      invocation = list.get(1);
      assertEquals(1, invocation.getInvocationsWithString().size());
      assertEquals("testRuntime", invocation.getInvocationsWithString().get(0).getMethodName());
   }

   @Test
   public void testLineColumnNumbers()
   {
      String source = "" +
               "abc 'def'\n" +
               "alpha beta: gamma\n" +
               "clojure {\n" +
               "}\n";
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);

      assertEquals(1, parser.getInvocationsWithString().size());
      assertEquals(1, parser.getInvocationsWithMap().size());
      assertEquals(1, parser.getInvocationsWithClosure().size());

      InvocationWithString strInv = parser.getInvocationsWithString().get(0);
      assertEquals(1, strInv.getLineNumber());
      assertEquals(1, strInv.getColumnNumber());
      assertEquals(1, strInv.getLastLineNumber());
      assertEquals(10, strInv.getLastColumnNumber());

      InvocationWithMap mapInv = parser.getInvocationsWithMap().get(0);
      assertEquals(2, mapInv.getLineNumber());
      assertEquals(1, mapInv.getColumnNumber());
      assertEquals(2, mapInv.getLastLineNumber());
      assertEquals(18, mapInv.getLastColumnNumber());

      InvocationWithClosure closureInv = parser.getInvocationsWithClosure().get(0);
      assertEquals(3, closureInv.getLineNumber());
      assertEquals(1, closureInv.getColumnNumber());
      assertEquals(4, closureInv.getLastLineNumber());
      assertEquals(2, closureInv.getLastColumnNumber());
   }

   @Test
   public void testGetCode()
   {
      String source = "" +
               "abc 'def'\n" +
               "alpha beta: gamma\n" +
               "clojure {\n" +
               "}\n";
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);

      assertEquals(1, parser.getInvocationsWithString().size());
      assertEquals(1, parser.getInvocationsWithMap().size());
      assertEquals(1, parser.getInvocationsWithClosure().size());

      InvocationWithString strInv = parser.getInvocationsWithString().get(0);
      assertEquals("abc 'def'", strInv.getCode());

      InvocationWithMap mapInv = parser.getInvocationsWithMap().get(0);
      assertEquals("alpha beta: gamma", mapInv.getCode());

      InvocationWithClosure closureInv = parser.getInvocationsWithClosure().get(0);
      assertEquals("clojure {\n}", closureInv.getCode());
   }

   @Test
   public void testGStringVariableAssignment()
   {
      String source = "" +
               "variable = \"2 + 2 = ${2 + 2}\"";
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);

      assertEquals(1, parser.getVariableAssignments().size());

      VariableAssignment variableAssignment = parser.getVariableAssignments().get(0);
      assertEquals("variable", variableAssignment.getVariable());
      assertEquals("2 + 2 = $(2 + 2)", variableAssignment.getValue());
   }

   @Test
   public void testGStringInvocations()
   {
      String source = "" +
               "strInv \"abc $xyz\"\n" +
               "mapInv a: \"b$c:x\"\n";
      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      
      assertEquals(1, parser.getInvocationsWithString().size());
      assertEquals(1, parser.getInvocationsWithMap().size());
      
      assertEquals("abc $xyz", parser.getInvocationsWithString().get(0).getString());
      assertEquals("b$c:x", parser.getInvocationsWithMap().get(0).getParameters().get("a"));
   }
}
