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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Clipboard
{
  private static Clipboard INSTANCE = new Clipboard();

  public static Clipboard getInstance ()
  {
    return INSTANCE;
  }

  private Set<Object> clipboard = new HashSet<Object> ();
  private boolean clipboardPasteable = false;

  public void setContents (final Collection<?> contents)
  {
    clipboard.clear();
    clipboard.addAll( contents );
    calculateClipboardPasteable();
  }

  private void calculateClipboardPasteable ()
  {
    for ( Object o : clipboard )
    {
      if ( o instanceof EditorGraphMember<?> )
      {
        clipboardPasteable = true;
        return;
      }
    }

    clipboardPasteable = false;
  }

  public boolean isClipboardPasteable ()
  {
    return clipboardPasteable;
  }

  public Set<?> getContents ()
  {
    return clipboard;
  }
}