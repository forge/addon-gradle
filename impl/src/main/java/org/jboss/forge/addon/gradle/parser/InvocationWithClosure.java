/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents invocations of a method which takes closure as a parameter.
 * 
 * @author Adam Wyłuda
 */
public class InvocationWithClosure extends SourceCodeElement
{

   private final String methodName;
   private final List<InvocationWithClosure> internalInvocations;
   private final List<InvocationWithString> stringInvocations;
   private final List<InvocationWithMap> mapInvocations;

   public InvocationWithClosure(String methodName, int lineNumber, int columnNumber, int lastLineNumber,
            int lastColumnNumber)
   {
      this(methodName,
               new ArrayList<InvocationWithClosure>(),
               new ArrayList<InvocationWithString>(),
               new ArrayList<InvocationWithMap>(),
               lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
   }

   public InvocationWithClosure(String methodName,
            List<InvocationWithClosure> internalInvocations,
            List<InvocationWithString> stringInvocations,
            List<InvocationWithMap> mapInvocations,
            int lineNumber, int columnNumber, int lastLineNumber, int lastColumnNumber)
   {
      super(lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      this.methodName = methodName;
      this.internalInvocations = internalInvocations;
      this.stringInvocations = stringInvocations;
      this.mapInvocations = mapInvocations;
   }

   public String getMethodName()
   {
      return methodName;
   }

   public List<InvocationWithClosure> getInternalInvocations()
   {
      return internalInvocations;
   }

   public List<InvocationWithString> getInternalStringInvocations()
   {
      return stringInvocations;
   }

   public List<InvocationWithMap> getInternalMapInvocations()
   {
      return mapInvocations;
   }
}
