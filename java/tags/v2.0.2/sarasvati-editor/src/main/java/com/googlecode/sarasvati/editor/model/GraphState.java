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
package com.googlecode.sarasvati.editor.model;


public class GraphState
{
  protected String defaultNodeForIncomingArcs;
  protected String defaultNodeForOutgoingArcs;

  public GraphState ()
  {
    // Constructor for new, empty, graph state
  }

  public GraphState (final String defaultNodeForIncomingArcs,
                     final String defaultNodeForOutgoingArcs)
  {
    this.defaultNodeForIncomingArcs = defaultNodeForIncomingArcs;
    this.defaultNodeForOutgoingArcs = defaultNodeForOutgoingArcs;
  }

  public String getDefaultNodeForIncomingArcs ()
  {
    return defaultNodeForIncomingArcs;
  }

  public String getDefaultNodeForOutgoingArcs ()
  {
    return defaultNodeForOutgoingArcs;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( defaultNodeForIncomingArcs == null )
        ? 0 : defaultNodeForIncomingArcs.hashCode() );
    result = prime * result + ( ( defaultNodeForOutgoingArcs == null )
        ? 0 : defaultNodeForOutgoingArcs.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof GraphState ) ) return false;
    final GraphState other = (GraphState)obj;
    if ( defaultNodeForIncomingArcs == null )
    {
      if ( other.defaultNodeForIncomingArcs != null ) return false;
    }
    else if ( !defaultNodeForIncomingArcs.equals( other.defaultNodeForIncomingArcs ) )
      return false;
    if ( defaultNodeForOutgoingArcs == null )
    {
      if ( other.defaultNodeForOutgoingArcs != null ) return false;
    }
    else if ( !defaultNodeForOutgoingArcs.equals( other.defaultNodeForOutgoingArcs ) )
      return false;
    return true;
  }
}
