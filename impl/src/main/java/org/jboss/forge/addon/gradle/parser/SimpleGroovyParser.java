/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a minimal groovy parser necessary to obtain information about gradle project. It can create method invocation
 * tree from given source, as gradle build configuration is invocation oriented.
 * 
 * @author Adam Wyłuda
 */
public class SimpleGroovyParser
{

   private final List<InvocationWithClosure> invocationWithClosureList;
   private final List<InvocationWithMap> invocationWithMapList;
   private final List<InvocationWithString> invocationWithStringList;

   SimpleGroovyParser(String source)
   {
      InvocationWithClosure root = createInvocationWithClosureRoot(source);
      invocationWithClosureList = root.getInternalInvocations();
      invocationWithMapList = root.getInternalMapInvocations();
      invocationWithStringList = root.getInternalStringInvocations();
   }

   public SimpleGroovyParser fromSource(String source)
   {
      return new SimpleGroovyParser(source);
   }

   public List<InvocationWithClosure> getInvocationsWithClosure()
   {
      return invocationWithClosureList;
   }

   public List<InvocationWithMap> getInvocationsWithMap()
   {
      return invocationWithMapList;
   }

   public List<InvocationWithString> getInvocationsWithString()
   {
      return invocationWithStringList;
   }

   static InvocationWithClosure createInvocationWithClosureRoot(String source)
   {
      BlockStatement sourceBlockStatement = parseSource(source);
      InvocationWithClosure root = new InvocationWithClosure("", 0, 0, 0, 0);
      fillInvocationFromStatement(sourceBlockStatement, root);
      return root;
   }

   static BlockStatement parseSource(String source)
   {
      SourceUnit sourceUnit = SourceUnit.create("script", source);
      sourceUnit.parse();
      sourceUnit.nextPhase();
      sourceUnit.convert();
      ModuleNode moduleNode = sourceUnit.getAST();
      return moduleNode.getStatementBlock();
   }

   /**
    * Goes through blockStatement recursively to create InvocationWithClosure tree.
    */
   static void fillInvocationFromStatement(BlockStatement blockStatement, InvocationWithClosure node)
   {
      for (Statement statement : blockStatement.getStatements())
      {
         processStatement(statement, node);
      }
   }

   static void processStatement(Statement statement, InvocationWithClosure node)
   {
      // If statement is an expression like function call
      if (statement instanceof ExpressionStatement)
      {
         Expression expression = ((ExpressionStatement) statement).getExpression();
         // If expression is method call
         if (expression instanceof MethodCallExpression)
         {
            processMethodCallExpression(expression, node);
         }
      }
   }

   static void processMethodCallExpression(Expression expression, InvocationWithClosure node)
   {
      String methodName = ((MethodCallExpression) expression).getMethodAsString();
      int lineNumber = expression.getLineNumber();
      int columnNumber = expression.getColumnNumber();
      int lastLineNumber = expression.getLastLineNumber();
      int lastColumnNumber = expression.getLastColumnNumber();
      Expression argumentsExpression = ((MethodCallExpression) expression).getArguments();
      // In case argument expression is string constant or closure
      if (argumentsExpression instanceof ArgumentListExpression &&
               ((ArgumentListExpression) argumentsExpression).getExpressions().size() == 1)
      {
         processArgumentListExpression((ArgumentListExpression) argumentsExpression, node,
                  methodName, lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      }
      // If argument expression is TupleExpression then it may be a map
      else if (argumentsExpression instanceof TupleExpression &&
               ((TupleExpression) argumentsExpression).getExpressions().size() == 1)
      {
         processTupleExpression((TupleExpression) argumentsExpression, node,
                  methodName, lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      }
   }

   static void processArgumentListExpression(ArgumentListExpression argumentsExpression, InvocationWithClosure node,
            String methodName, int lineNumber, int columnNumber,
            int lastLineNumber, int lastColumnNumber)
   {
      Expression argumentExpression = ((ArgumentListExpression) argumentsExpression).getExpressions().get(0);
      // If argument is string constant
      if (argumentExpression instanceof ConstantExpression)
      {
         String string = ((ConstantExpression) argumentExpression).getValue().toString();
         InvocationWithString invocation = new InvocationWithString(methodName, string, lineNumber, columnNumber,
                  lastLineNumber, lastColumnNumber);
         node.getInternalStringInvocations().add(invocation);
      }
      // If argument is closure
      else if (argumentExpression instanceof ClosureExpression)
      {
         BlockStatement blockStatement = (BlockStatement) ((ClosureExpression) argumentExpression).getCode();
         InvocationWithClosure invocation = new InvocationWithClosure(methodName, lineNumber, columnNumber,
                  lastLineNumber, lastColumnNumber);
         fillInvocationFromStatement(blockStatement, invocation);
         node.getInternalInvocations().add(invocation);
      }
   }

   static void processTupleExpression(TupleExpression argumentsExpression, InvocationWithClosure node,
            String methodName, int lineNumber, int columnNumber,
            int lastLineNumber, int lastColumnNumber)
   {
      Expression argumentExpression = ((TupleExpression) argumentsExpression).getExpressions().get(0);
      // In case argument expression is a map
      if (argumentExpression instanceof NamedArgumentListExpression)
      {
         processNamedArgumentListExpression((NamedArgumentListExpression) argumentExpression, node,
                  methodName, lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      }
   }

   static void processNamedArgumentListExpression(NamedArgumentListExpression argumentListExpression,
            InvocationWithClosure node,
            String methodName, int lineNumber, int columnNumber,
            int lastLineNumber, int lastColumnNumber)
   {
      Map<String, String> parameters = new HashMap<String, String>();
      for (MapEntryExpression mapEntryExpression : argumentListExpression.getMapEntryExpressions())
      {
         Expression keyExpression = mapEntryExpression.getKeyExpression();
         Expression valueExpression = mapEntryExpression.getValueExpression();
         if (keyExpression instanceof ConstantExpression &&
                  valueExpression instanceof ConstantExpression)
         {
            String key = ((ConstantExpression) keyExpression).getValue().toString();
            String value = ((ConstantExpression) valueExpression).getValue().toString();
            parameters.put(key, value);
         }
      }
      InvocationWithMap invocation = new InvocationWithMap(methodName, parameters,
               lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      node.getInternalMapInvocations().add(invocation);
   }
}
