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
package com.googlecode.sarasvati.rubric.visitor;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.rubric.lang.RubricStmtResult;

/**
 * Class which collects the names of exit arcs used in a guard statement.
 *
 * @author Paul Lorenz
 */
public class ExitArcNameCollector extends RubricVisitorAdaptor
{
  private Set<String> exitArcs = new HashSet<String>();

  @Override
  public void visit (final RubricStmtResult resultStmt)
  {
    if ( resultStmt.getResult() instanceof GuardResult )
    {
      GuardResult response = (GuardResult)resultStmt.getResult();
      if ( response.getGuardAction() == GuardAction.SkipNode )
      {
        exitArcs.add( response.getExitArcForSkip() );
      }
    }
  }

  public Set<String> getExitArcNames ()
  {
    return exitArcs;
  }
}
