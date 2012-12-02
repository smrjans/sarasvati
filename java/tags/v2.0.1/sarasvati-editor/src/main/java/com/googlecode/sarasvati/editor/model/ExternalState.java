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

import java.util.Map;
import java.util.Set;

public class ExternalState extends GraphMemberState
{
  protected final String graphName;

  public ExternalState (final String name,
                        final String graphName,
                        final Map<String,String> customProperties,
                        final boolean isNew)
  {
    super( name, customProperties, isNew );
    this.graphName = graphName;
  }

  public String getGraphName ()
  {
    return graphName;
  }

  public ExternalState copy (final Set<String> currentNames)
  {
    return new ExternalState( getNextUniqueName( currentNames ), graphName, getCustomProperties(), true );
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((graphName == null) ? 0 : graphName.hashCode());
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals( obj ))
    {
      return false;
    }
    if (!(obj instanceof ExternalState))
    {
      return false;
    }
    ExternalState other = (ExternalState) obj;
    if (graphName == null)
    {
      if (other.graphName != null)
      {
        return false;
      }
    } else if (!graphName.equals( other.graphName ))
    {
      return false;
    }
    return true;
  }
}
