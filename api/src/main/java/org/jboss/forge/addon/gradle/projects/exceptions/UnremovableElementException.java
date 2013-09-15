/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.exceptions;

/**
 * Thrown when it's not possible to remove specified source code declaration.
 * 
 * @author Adam Wyłuda
 */
public class UnremovableElementException extends RuntimeException
{
   private static final long serialVersionUID = 3676296264423815416L;
   
   public UnremovableElementException()
   {
   }
   
   public UnremovableElementException(String message)
   {
      super(message);
   }
}
