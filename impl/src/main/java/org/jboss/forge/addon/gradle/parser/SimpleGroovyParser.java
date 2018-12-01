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
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;
import org.gradle.internal.impldep.com.google.common.base.Optional;
import org.gradle.internal.impldep.com.google.common.base.Preconditions;
import org.gradle.internal.impldep.com.google.common.collect.Lists;
import org.gradle.internal.impldep.com.google.common.collect.Maps;

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
      public int lineNumber = 1;
      public int columnNumber = 1;
      public int lastLineNumber = 1;
      public int lastColumnNumber = 1;

      public String methodName;
      public String stringParameter;
      public Map<String, String> mapParameter;

      public List<InvocationWithClosure> invocationWithClosureList = Lists.newArrayList();
      public List<InvocationWithMap> invocationWithMapList = Lists.newArrayList();
      public List<InvocationWithString> invocationWithStringList = Lists.newArrayList();
      public List<VariableAssignment> variableAssignmentList = Lists.newArrayList();

      public InvocationWithClosure create(String source)
      {
         String code = source.substring(SourceUtil.positionInSource(source, lineNumber, columnNumber),
                  SourceUtil.positionInSource(source, lastLineNumber, lastColumnNumber));
         return new InvocationWithClosure(code, methodName, stringParameter, mapParameter,
                  invocationWithClosureList, invocationWithStringList,
                  invocationWithMapList, variableAssignmentList,
                  lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
      }
   }

   private final String source;
   private final InvocationWithClosure root;

   private SimpleGroovyParser(String source)
   {
      this.source = source;
      root = createInvocationWithClosureRoot(source);
   }

   public static SimpleGroovyParser fromSource(String source)
   {
      return new SimpleGroovyParser(source);
   }

   public List<InvocationWithClosure> getInvocationsWithClosure()
   {
      return root.getInvocationsWithClosure();
   }

   public List<InvocationWithMap> getInvocationsWithMap()
   {
      return root.getInvocationsWithMap();
   }

   public List<InvocationWithString> getInvocationsWithString()
   {
      return root.getInvocationsWithString();
   }

   public List<VariableAssignment> getVariableAssignments()
   {
      return root.getVariableAssignments();
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

   public Optional<VariableAssignment> variableAssignmentByName(String name)
   {
      return root.variableAssignmentByName(name);
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
      for (InvocationWithClosure subinvocation : invocation.getInvocationsWithClosure())
      {
         if (subinvocation.getMethodName().equals(name))
         {
            allInvocationsAtPath(list, subinvocation, Arrays.copyOfRange(path, 1, path.length));
         }
      }
   }

   private InvocationWithClosure createInvocationWithClosureRoot(String source)
   {
      BlockStatement sourceBlockStatement = parseSource(source);
      PreInvocationWithClosure root = new PreInvocationWithClosure();
      fillInvocationFromStatement(sourceBlockStatement, root);
      return root.create(source);
   }

   private BlockStatement parseSource(String source)
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
   private void fillInvocationFromStatement(BlockStatement blockStatement, PreInvocationWithClosure node)
   {
      for (Statement statement : blockStatement.getStatements())
      {
         processStatement(statement, node);
      }
   }

   private void processStatement(Statement statement, PreInvocationWithClosure node)
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

         // If expression is binary expression, which might be variable assignment
         if (expression instanceof BinaryExpression)
         {
            processBinaryExpression((BinaryExpression) expression, node);
         }
      }
   }

   private void processMethodCallExpression(Expression expression, PreInvocationWithClosure node)
   {
      String methodName = ((MethodCallExpression) expression).getMethodAsString();

      Expression argumentsExpression = ((MethodCallExpression) expression).getArguments();

      // In case argument expression is a (G)String constant or closure
      if (argumentsExpression instanceof ArgumentListExpression)
      {
         processArgumentListExpression(expression, (ArgumentListExpression) argumentsExpression, node,
                  methodName);
      }

      // If argument expression is a TupleExpression then it may be a map
      else if (argumentsExpression instanceof TupleExpression &&
               ((TupleExpression) argumentsExpression).getExpressions().size() == 1)
      {
         processTupleExpression(expression, (TupleExpression) argumentsExpression, node, methodName);
      }
   }

   private void processArgumentListExpression(Expression expression,
            ArgumentListExpression argumentsExpression,
            PreInvocationWithClosure node, String methodName)
   {
      // If it's single argument call
      if (argumentsExpression.getExpressions().size() == 1)
      {
         Expression argumentExpression = argumentsExpression.getExpressions().get(0);

         // If argument is a string constant
         if (isStringOrGString(argumentExpression))
         {
            String string = valueFromStringOrGString(argumentExpression);

            String code = source
                     .substring(
                              SourceUtil.positionInSource(source, expression.getLineNumber(), expression.getColumnNumber()),
                              SourceUtil.positionInSource(source, expression.getLastLineNumber(),
                                       expression.getLastColumnNumber()));
            InvocationWithString invocation = new InvocationWithString(code, methodName, string,
                     expression.getLineNumber(), expression.getColumnNumber(),
                     expression.getLastLineNumber(), expression.getLastColumnNumber());
            node.invocationWithStringList.add(invocation);
         }

         // If argument is a closure
         else if (argumentExpression instanceof ClosureExpression)
         {
            processClosureExpression(expression, (ClosureExpression) argumentExpression, node, methodName,
                     "", Maps.<String, String> newHashMap());
         }
      }
      // If it's two argument call
      else if (argumentsExpression.getExpressions().size() == 2)
      {
         Expression firstArgumentExpression = argumentsExpression.getExpressions().get(0);
         Expression secondArgumentExpression = argumentsExpression.getExpressions().get(1);
         
         String stringParameter = "";
         Map<String, String> mapParameter = Maps.newHashMap();
         
         if (isStringOrGString(firstArgumentExpression))
         {
            stringParameter = valueFromStringOrGString(firstArgumentExpression);
         }
         else if (firstArgumentExpression instanceof MapExpression)
         {
            mapParameter = mapFromMapEntryExpressions(((MapExpression) firstArgumentExpression).getMapEntryExpressions());
         }
         
         if (secondArgumentExpression instanceof ClosureExpression)
         {
            processClosureExpression(expression, (ClosureExpression) secondArgumentExpression, node, methodName,
                     stringParameter, mapParameter);
         }
      }
   }

   private void processClosureExpression(Expression expression,
            ClosureExpression closureExpression,
            PreInvocationWithClosure node, 
            String methodName, String stringParameter, Map<String, String> mapParameter)
   {
      BlockStatement blockStatement = (BlockStatement) (closureExpression).getCode();

      PreInvocationWithClosure invocation = new PreInvocationWithClosure();
      invocation.methodName = methodName;
      invocation.stringParameter = stringParameter;
      invocation.mapParameter = mapParameter;
      
      invocation.lineNumber = expression.getLineNumber();
      invocation.columnNumber = expression.getColumnNumber();
      invocation.lastLineNumber = expression.getLastLineNumber();
      invocation.lastColumnNumber = expression.getLastColumnNumber();

      fillInvocationFromStatement(blockStatement, invocation);
      node.invocationWithClosureList.add(invocation.create(source));
   }

   private void processTupleExpression(Expression expression,
            TupleExpression argumentsExpression, PreInvocationWithClosure node,
            String methodName)
   {
      Expression argumentExpression = argumentsExpression.getExpressions().get(0);

      // In case argument expression is a map
      if (argumentExpression instanceof NamedArgumentListExpression)
      {
         processNamedArgumentListExpression(expression, (NamedArgumentListExpression) argumentExpression, node,
                  methodName);
      }
   }

   private void processNamedArgumentListExpression(Expression expression,
            NamedArgumentListExpression argumentListExpression,
            PreInvocationWithClosure node, String methodName)
   {
      Map<String, String> parameters = mapFromMapEntryExpressions(argumentListExpression.getMapEntryExpressions());
      String code = source.substring(
               SourceUtil.positionInSource(source, expression.getLineNumber(), expression.getColumnNumber()),
               SourceUtil.positionInSource(source, expression.getLastLineNumber(), expression.getLastColumnNumber()));
      InvocationWithMap invocation = new InvocationWithMap(code, methodName, parameters,
               expression.getLineNumber(), expression.getColumnNumber(),
               expression.getLineNumber(), expression.getLastColumnNumber());
      node.invocationWithMapList.add(invocation);
   }

   private void processBinaryExpression(BinaryExpression expression, PreInvocationWithClosure node)
   {
      // This condition must be true to be string variable assignment
      // but not new variable declaration
      if (!(expression instanceof DeclarationExpression) &&
               (expression.getLeftExpression() instanceof VariableExpression ||
               expression.getLeftExpression() instanceof PropertyExpression) &&
               expression.getOperation().getText().toString().equals("=") &&
               isStringOrGString(expression.getRightExpression()))
      {
         String variable = expression.getLeftExpression().getText();
         String value = valueFromStringOrGString(expression.getRightExpression());

         int lineNumber = expression.getLineNumber();
         int columnNumber = expression.getColumnNumber();
         int lastLineNumber = expression.getLastLineNumber();
         int lastColumnNumber = expression.getLastColumnNumber();

         String code = source.substring(SourceUtil.positionInSource(source, lineNumber, columnNumber),
                  SourceUtil.positionInSource(source, lastLineNumber, lastColumnNumber));
         VariableAssignment variableAssignment =
                  new VariableAssignment(code, variable, value,
                           lineNumber, columnNumber, lastLineNumber, lastColumnNumber);
         node.variableAssignmentList.add(variableAssignment);
      }
   }

   private Map<String, String> mapFromMapEntryExpressions(List<MapEntryExpression> mapEntries)
   {
      Map<String, String> map = new HashMap<>();
      for (MapEntryExpression mapEntryExpression : mapEntries)
      {
         Expression keyExpression = mapEntryExpression.getKeyExpression();
         Expression valueExpression = mapEntryExpression.getValueExpression();
         if (keyExpression instanceof ConstantExpression &&
                  isStringOrGString(valueExpression))
         {
            String key = ((ConstantExpression) keyExpression).getValue().toString();
            String value = valueFromStringOrGString(valueExpression);
            map.put(key, value);
         }
      }
      return map;
   }

   private String valueFromStringOrGString(Expression expression)
   {
      if (isString(expression))
      {
         return (String) ((ConstantExpression) expression).getValue();
      }
      else if (isGString(expression))
      {
         return ((GStringExpression) expression).getText();
      }
      throw new IllegalArgumentException("Given expression is neither String nor GString expression");
   }

   private boolean isStringOrGString(Expression expression)
   {
      return isString(expression) || isGString(expression);
   }

   private boolean isString(Expression expression)
   {
      return expression instanceof ConstantExpression &&
               ((ConstantExpression) expression).getValue() instanceof String;
   }

   private boolean isGString(Expression expression)
   {
      return expression instanceof GStringExpression;
   }
}
