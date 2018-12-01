/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gradle.internal.impldep.com.google.common.base.Optional;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableList;
import org.gradle.internal.impldep.com.google.common.collect.Maps;


/**
 * Represents invocations of a method which takes closure as a parameter.
 * 
 * @author Adam Wyłuda
 */
public class InvocationWithClosure extends SourceCodeElement
{
   private final String methodName;
   private final String stringParameter;
   private final Map<String, String> mapParameter;
   
   private final List<InvocationWithClosure> internalInvocations;
   private final List<InvocationWithString> stringInvocations;
   private final List<InvocationWithMap> mapInvocations;
   private final List<VariableAssignment> variableAssignments;

   private final Map<String, InvocationWithClosure> internalInvocationMap = Maps.newHashMap();
   private final Map<String, InvocationWithString> stringInvocationMap = Maps.newHashMap();
   private final Map<String, InvocationWithMap> mapInvocationMap = Maps.newHashMap();
   private final Map<String, VariableAssignment> variableAssignmentMap = Maps.newHashMap();

   public InvocationWithClosure(String code, String methodName,  String stringParameter, Map<String, String> mapParameter,
            int lineNumber, int columnNumber, int lastLineNumber, int lastColumnNumber)
   {
      this(code,
               methodName, stringParameter, mapParameter,
               new ArrayList<InvocationWithClosure>(),
               new ArrayList<InvocationWithString>(),
               new ArrayList<InvocationWithMap>(),
               new ArrayList<VariableAssignment>(),
               lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
   }

   public InvocationWithClosure(String code,
            String methodName, String stringParameter, Map<String, String> mapParameter,
            List<InvocationWithClosure> internalInvocations,
            List<InvocationWithString> stringInvocations,
            List<InvocationWithMap> mapInvocations,
            List<VariableAssignment> variableAssignments,
            int lineNumber, int columnNumber, int lastLineNumber, int lastColumnNumber)
   {
      super(code, lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      
      this.methodName = methodName;
      this.stringParameter = stringParameter;
      this.mapParameter = mapParameter;
      
      this.internalInvocations = ImmutableList.<InvocationWithClosure> copyOf(internalInvocations);
      this.stringInvocations = ImmutableList.<InvocationWithString> copyOf(stringInvocations);
      this.mapInvocations = ImmutableList.<InvocationWithMap> copyOf(mapInvocations);
      this.variableAssignments = variableAssignments;

      // Filling indexes
      for (InvocationWithClosure invocation : internalInvocations)
      {
         internalInvocationMap.put(invocation.getMethodName(), invocation);
      }
      for (InvocationWithString invocation : stringInvocations)
      {
         stringInvocationMap.put(invocation.getMethodName(), invocation);
      }
      for (InvocationWithMap invocation : mapInvocations)
      {
         mapInvocationMap.put(invocation.getMethodName(), invocation);
      }
      for (VariableAssignment assignment : variableAssignments)
      {
         variableAssignmentMap.put(assignment.getVariable(), assignment);
      }
   }

   public String getMethodName()
   {
      return methodName;
   }
   
   public String getStringParameter()
   {
      return stringParameter;
   }
   
   public Map<String, String> getMapParameter()
   {
      return mapParameter;
   }

   public List<InvocationWithClosure> getInvocationsWithClosure()
   {
      return internalInvocations;
   }

   public List<InvocationWithString> getInvocationsWithString()
   {
      return stringInvocations;
   }

   public List<InvocationWithMap> getInvocationsWithMap()
   {
      return mapInvocations;
   }

   public List<VariableAssignment> getVariableAssignments()
   {
      return variableAssignments;
   }

   public Optional<InvocationWithClosure> invocationWithClosureByName(String name)
   {
      return Optional.fromNullable(internalInvocationMap.get(name));
   }

   public Optional<InvocationWithString> invocationWithStringByName(String name)
   {
      return Optional.fromNullable(stringInvocationMap.get(name));
   }

   public Optional<InvocationWithMap> invocationWithMapByName(String name)
   {
      return Optional.fromNullable(mapInvocationMap.get(name));
   }

   public Optional<VariableAssignment> variableAssignmentByName(String name)
   {
      return Optional.fromNullable(variableAssignmentMap.get(name));
   }
}
