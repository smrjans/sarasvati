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

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.rubric.lang;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;

public class ErrorReportingRubricParser extends RubricParser
{
  private final ErrorReporter errorReporter;

  public ErrorReportingRubricParser (final TokenStream input, final RecognizerSharedState state, final ErrorReporter errorReporter)
  {
    super( input, state );
    this.errorReporter = errorReporter;
  }

  public ErrorReportingRubricParser (final TokenStream input, final ErrorReporter errorReporter)
  {
    super( input );
    this.errorReporter = errorReporter;
  }

  @Override
  public String getErrorMessage (final RecognitionException re, final String[] arg1)
  {
    String message = super.getErrorMessage( re, arg1 );
    return errorReporter.reportError( re, message );
  }

  @Override
  public void emitErrorMessage( final String arg0 )
  {
    // do nothing
  }
}
