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
package com.googlecode.sarasvati.example;

public enum TaskState
{
  Open( 0 ),
  Completed( 1 ),
  Rejected( 2 ),
  Cancelled( 3 );

  private int id;

  private TaskState (final int id)
  {
    this.id = id;
  }

  public int getId ()
  {
    return id;
  }

  public static TaskState getById (final int id)
  {
    for ( TaskState state : TaskState.values() )
    {
      if ( state.getId() == id )
      {
        return state;
      }
    }

    return null;
  }
}
