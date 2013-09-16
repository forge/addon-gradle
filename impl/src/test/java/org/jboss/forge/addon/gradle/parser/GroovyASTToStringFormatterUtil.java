/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.parser;

import java.util.Scanner;

/**
 * Simple util for Groovy elements toString() formatting, useful for debugging purposes.
 * 
 * @author Adam Wyłuda
 */
public class GroovyASTToStringFormatterUtil
{
   public static void main(String... args)
   {
      @SuppressWarnings("resource")
      String input = new Scanner(System.in).nextLine();
      
      boolean inString = false;
      int indentation = 0;
      
      for (char c : input.toCharArray())
      {
         if (c == '\'')
         {
            inString = !inString;
         }
         else if (!inString && (c == ']' || c == '}'))
         {
            System.out.println();
            indentation--;
            System.out.print(indent(indentation));
         }
         
         System.out.print(c);
         
         if (!inString && (c == '[' || c == '{'))
         {
            System.out.println();
            indentation++;
            System.out.print(indent(indentation));
         }
      }
   }
   
   static String indent(int times)
   {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < times; i++)
      {
         sb.append("   ");
      }
      return sb.toString();
   }
}
