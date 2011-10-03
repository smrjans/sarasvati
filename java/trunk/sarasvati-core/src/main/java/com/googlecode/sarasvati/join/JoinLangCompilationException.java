/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.join;

import org.antlr.runtime.RecognitionException;

public class JoinLangCompilationException extends JoinLangException
{
  private static final long serialVersionUID = 1L;

  protected RecognitionException re;

  public JoinLangCompilationException (final RecognitionException re, final String message)
  {
    super( message );
    this.re = re;
  }

  @Override
  public RecognitionException getRecognitionException ()
  {
    return re;
  }
}
