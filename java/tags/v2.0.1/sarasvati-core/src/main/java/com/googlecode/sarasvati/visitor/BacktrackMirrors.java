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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.visitor;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ExecutionType;

public class BacktrackMirrors
{
  protected Map<ArcToken, ArcToken> map          = new HashMap<ArcToken, ArcToken> ();

  public void addVisited (final ArcToken token)
  {
    if ( !map.containsKey( token ) )
    {
      map.put( token, null );
    }
  }

  public boolean hasMirror (final ArcToken token)
  {
    return map.containsKey( token );
  }

  public void findMirror (final ArcToken token)
  {
    if ( token.getExecutionType() == ExecutionType.ForwardBacktracked )
    {
      findForwardBacktrackedMirror( token );
    }
    else
    {
      findBacktrackMirror( token );
    }
  }

  private void findBacktrackMirror (final ArcToken token)
  {
    for ( ArcToken parent : token.getParentToken().getParentTokens() )
    {
      if ( isMirror( token, parent ) )
      {
        map.put( token, parent );
        tieToEndOfBacktrackChain( token );
        return;
      }

      if ( parent.getExecutionType() == ExecutionType.Backtracked )
      {
        ArcToken mirror = map.get( parent );
        if ( mirror != null )
        {
          for ( ArcToken mirrorParent : mirror.getParentToken().getParentTokens() )
          {
            if ( isMirror( token, mirrorParent ) )
            {
              map.put( token, mirrorParent );
              tieToEndOfBacktrackChain( token );
              return;
            }
          }
        }
      }
    }
  }

  private void findForwardBacktrackedMirror (final ArcToken token)
  {
    if ( token.getParentToken().getExecutionType() != ExecutionType.ForwardBacktracked )
    {
      return;
    }

    for ( ArcToken parent : token.getParentToken().getParentTokens() )
    {
      if ( parent.getExecutionType() == ExecutionType.Backtracked &&
           token.getArc().equals( parent.getArc() ) )
      {
        map.put( token, parent );
        tieToEndOfBacktrackChain( token );
        return;
      }
    }
  }

  private boolean isMirror (final ArcToken token, final ArcToken candidate)
  {
    return ( candidate.getExecutionType() == ExecutionType.ForwardBacktracked ||
             candidate.getExecutionType() == ExecutionType.UTurnBacktracked ) &&
           token.getArc().equals( candidate.getArc() ) &&
           map.containsKey( candidate );
  }

  private void tieToEndOfBacktrackChain (final ArcToken token)
  {
    ArcToken lookup = map.get( token );
    ArcToken result = lookup;
    while ( lookup != null )
    {
      lookup = map.get( lookup );
      if ( lookup != null )
      {
        result = lookup;
      }
    }

    map.put( token, result );
  }

  public ArcToken getMirror (final ArcToken token)
  {
    ArcToken result = map.get( token );
    return result == null ? token : result;
  }
}