/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Wyłuda
 */
public class SimpleGroovyParserTest
{

   @Test
   public void flatSourceTest()
   {
      String source = "\n" +
               "compile 'com.google:summer:2.0.1.3'\n" +
               "testCompile {'abc:def:0.1'}\n" +
               "apply plugin: 'java'\n";

      SimpleGroovyParser parser = new SimpleGroovyParser(source);
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
   public void nestedSourceTest()
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

      SimpleGroovyParser parser = new SimpleGroovyParser(source);
      assertEquals(1, parser.getInvocationsWithClosure().size());

      InvocationWithClosure subprojects = parser.getInvocationsWithClosure().get(0);
      assertEquals("subprojects", subprojects.getMethodName());
      assertEquals(2, subprojects.getInternalMapInvocations().size());
      assertEquals(1, subprojects.getInternalInvocations().size());

      InvocationWithClosure dependencies = subprojects.getInternalInvocations().get(0);
      assertEquals(1, dependencies.getInternalStringInvocations().size());

      InvocationWithString compile = dependencies.getInternalStringInvocations().get(0);
      assertEquals("compile", compile.getMethodName());
      assertEquals("group:artifact:1.0.0", compile.getString());
   }
}
