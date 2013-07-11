/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.Map;

import org.gradle.jarjar.com.google.common.collect.ImmutableMap;

/**
 * Represents invocations of a method which takes map as a parameter.
 * 
 * @author Adam Wyłuda
 */
public class InvocationWithMap extends SourceCodeElement
{

   private final String methodName;
   private final Map<String, String> parameters;

   public InvocationWithMap(String methodName, Map<String, String> parameters,
            int lineNumber, int columnNumber, int lastLineNumber, int lastColumnNumber)
   {
      super(lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      this.methodName = methodName;
      this.parameters = ImmutableMap.<String, String> copyOf(parameters);
   }

   public String getMethodName()
   {
      return methodName;
   }

   public Map<String, String> getParameters()
   {
      return parameters;
   }
}
