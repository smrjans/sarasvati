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
package com.googlecode.sarasvati.event;

/**
 * Container for a set of EventActionTypes.
 *
 * @author Paul Lorenz
 */
public class EventActions
{
  private int mask = 0;

  public EventActions (final EventActionType...responseTypes)
  {
    if ( responseTypes != null )
    {
      for ( EventActionType type : responseTypes )
      {
        mask |= type.getMask();
      }
    }
  }

  public boolean isEventTypeIncluded (final EventActionType type)
  {
    return (mask & type.getMask()) != 0;
  }

  public EventActions compose (final EventActions eventActions)
  {
    if ( eventActions != null )
    {
      this.mask |= eventActions.mask;
    }
    return this;
  }
}
