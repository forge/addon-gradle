/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;
import org.gradle.jarjar.com.google.common.base.Optional;
import org.gradle.jarjar.com.google.common.base.Preconditions;
import org.gradle.jarjar.com.google.common.collect.Lists;

/**
 * This is a minimal groovy parser necessary to obtain information about gradle project. It can create method invocation
 * tree from given source, as gradle build configuration is invocation oriented.
 * 
 * @author Adam Wyłuda
 */
public class SimpleGroovyParser
{
   private static class PreInvocationWithClosure
   {
      public String methodName;
      public int lineNumber;
      public int columnNumber;
      public int lastLineNumber;
      public int lastColumnNumber;
      public List<InvocationWithClosure> invocationWithClosureList = Lists.newArrayList();
      public List<InvocationWithMap> invocationWithMapList = Lists.newArrayList();
      public List<InvocationWithString> invocationWithStringList = Lists.newArrayList();

      public InvocationWithClosure create()
      {
         return new InvocationWithClosure(methodName, invocationWithClosureList, invocationWithStringList,
                  invocationWithMapList,
                  lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      }
   }

   private final InvocationWithClosure root;
   private final List<InvocationWithClosure> invocationWithClosureList;
   private final List<InvocationWithMap> invocationWithMapList;
   private final List<InvocationWithString> invocationWithStringList;

   private SimpleGroovyParser(String source)
   {
      root = createInvocationWithClosureRoot(source);
      invocationWithClosureList = root.getInternalInvocations();
      invocationWithMapList = root.getInternalMapInvocations();
      invocationWithStringList = root.getInternalStringInvocations();
   }

   public static SimpleGroovyParser fromSource(String source)
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

   public Optional<InvocationWithClosure> invocationWithClosureByName(String name)
   {
      return root.invocationWithClosureByName(name);
   }

   public Optional<InvocationWithString> invocationWithStringByName(String name)
   {
      return root.invocationWithStringByName(name);
   }

   public Optional<InvocationWithMap> invocationWithMapByName(String name)
   {
      return root.invocationWithMapByName(name);
   }
   
   public List<InvocationWithClosure> allInvocationsAtPath(String... path)
   {
      Preconditions.checkArgument(path.length > 0, "Path must have at least one element");
      List<InvocationWithClosure> list = Lists.newArrayList();
      allInvocationsAtPath(list, root, path);
      return list;
   }
   
   private void allInvocationsAtPath(List<InvocationWithClosure> list, InvocationWithClosure invocation, String... path)
   {
      if (path.length == 0)
      {
         list.add(invocation);
         return;
      }
      String name = path[0];
      for (InvocationWithClosure subinvocation : invocation.getInternalInvocations())
      {
         if (subinvocation.getMethodName().equals(name))
         {
            allInvocationsAtPath(list, subinvocation, Arrays.copyOfRange(path, 1, path.length));
         }
      }
   }

   static InvocationWithClosure createInvocationWithClosureRoot(String source)
   {
      BlockStatement sourceBlockStatement = parseSource(source);
      PreInvocationWithClosure root = new PreInvocationWithClosure();
      fillInvocationFromStatement(sourceBlockStatement, root);
      return root.create();
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
   static void fillInvocationFromStatement(BlockStatement blockStatement, PreInvocationWithClosure node)
   {
      for (Statement statement : blockStatement.getStatements())
      {
         processStatement(statement, node);
      }
   }

   static void processStatement(Statement statement, PreInvocationWithClosure node)
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

   static void processMethodCallExpression(Expression expression, PreInvocationWithClosure node)
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

   static void processArgumentListExpression(ArgumentListExpression argumentsExpression, PreInvocationWithClosure node,
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
         node.invocationWithStringList.add(invocation);
      }
      
      // If argument is closure
      else if (argumentExpression instanceof ClosureExpression)
      {
         BlockStatement blockStatement = (BlockStatement) ((ClosureExpression) argumentExpression).getCode();
         
         PreInvocationWithClosure invocation = new PreInvocationWithClosure();
         invocation.methodName = methodName;
         invocation.lineNumber = lineNumber;
         invocation.columnNumber = columnNumber;
         invocation.lastLineNumber = lastLineNumber;
         invocation.lastColumnNumber = lastColumnNumber;
         
         fillInvocationFromStatement(blockStatement, invocation);
         node.invocationWithClosureList.add(invocation.create());
      }
   }

   static void processTupleExpression(TupleExpression argumentsExpression, PreInvocationWithClosure node,
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
            PreInvocationWithClosure node,
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
      node.invocationWithMapList.add(invocation);
   }
}
