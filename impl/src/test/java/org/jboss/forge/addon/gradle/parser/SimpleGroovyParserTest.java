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
               "apply plugin: 'java'\n";

      SimpleGroovyParser parser = SimpleGroovyParser.fromSource(source);
      assertEquals(1, parser.getInvocationsWithClosure().size());
      assertEquals(1, parser.getInvocationsWithMap().size());
      assertEquals(1, parser.getInvocationsWithString().size());

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
   }

   @Test
   public void testNestedSource()
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
      assertEquals(1, parser.getInvocationsWithClosure().size());

      InvocationWithClosure subprojects = parser.getInvocationsWithClosure().get(0);
      assertEquals("subprojects", subprojects.getMethodName());
      assertEquals(2, subprojects.getInvocationsWithMap().size());
      assertEquals(1, subprojects.getInvocationsWithClosure().size());

      InvocationWithClosure dependencies = subprojects.getInvocationsWithClosure().get(0);
      assertEquals(1, dependencies.getInvocationsWithString().size());

      InvocationWithString compile = dependencies.getInvocationsWithString().get(0);
      assertEquals("compile", compile.getMethodName());
      assertEquals("group:artifact:1.0.0", compile.getString());
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
}
