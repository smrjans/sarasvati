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

public class ArcState
{
  private final String externalStart;
  private final String externalEnd;
  private final String label;

  public ArcState (final String label, final String externalStart, final String externalEnd)
  {
    this.externalStart = externalStart;
    this.externalEnd = externalEnd;
    this.label = label;
  }

  public String getExternalStart ()
  {
    return externalStart;
  }

  public String getExternalEnd ()
  {
    return externalEnd;
  }

  public String getLabel ()
  {
    return label;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( externalEnd == null )
        ? 0 : externalEnd.hashCode() );
    result = prime * result + ( ( externalStart == null )
        ? 0 : externalStart.hashCode() );
    result = prime * result + ( ( label == null )
        ? 0 : label.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof ArcState ) ) return false;
    ArcState other = (ArcState)obj;
    if ( externalEnd == null )
    {
      if ( other.externalEnd != null ) return false;
    }
    else if ( !externalEnd.equals( other.externalEnd ) ) return false;
    if ( externalStart == null )
    {
      if ( other.externalStart != null ) return false;
    }
    else if ( !externalStart.equals( other.externalStart ) ) return false;
    if ( label == null )
    {
      if ( other.label != null ) return false;
    }
    else if ( !label.equals( other.label ) ) return false;
    return true;
  }
}
