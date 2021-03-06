/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

/**
 * @author Adam Wyłuda
 */
public abstract class SourceCodeElement
{
   private final String code;
   private final int lineNumber;
   private final int columnNumber;
   private final int lastLineNumber;
   private final int lastColumnNumber;

   public SourceCodeElement(String code, int lineNumber, int columnNumber, int lastLineNumber, int lastColumnNumber)
   {
      this.code = code;
      this.lineNumber = lineNumber;
      this.columnNumber = columnNumber;
      this.lastLineNumber = lastLineNumber;
      this.lastColumnNumber = lastColumnNumber;
   }
   
   public String getCode()
   {
      return code;
   }

   public int getLineNumber()
   {
      return lineNumber;
   }

   public int getColumnNumber()
   {
      return columnNumber;
   }

   public int getLastLineNumber()
   {
      return lastLineNumber;
   }

   public int getLastColumnNumber()
   {
      return lastColumnNumber;
   }
   
   @Override
   public String toString()
   {
      return getCode();
   }
}
