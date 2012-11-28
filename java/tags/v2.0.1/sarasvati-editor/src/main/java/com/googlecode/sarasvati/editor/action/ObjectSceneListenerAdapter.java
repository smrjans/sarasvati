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
package com.googlecode.sarasvati.editor.action;

import java.util.Set;

import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;

public class ObjectSceneListenerAdapter implements ObjectSceneListener
{

  @Override
  public void focusChanged (final ObjectSceneEvent event,
                            final Object previousFocusedObject,
                            final Object newFocusedObject)
  {
    // does nothing by default
  }

  @Override
  public void highlightingChanged (final ObjectSceneEvent event,
                                   final Set<Object> previousHighlighting,
                                   final Set<Object> newHighlighting)
  {
    // does nothing by default
  }

  @Override
  public void hoverChanged (final ObjectSceneEvent event,
                            final Object previousHoveredObject,
                            final Object newHoveredObject)
  {
    // does nothing by default
  }

  @Override
  public void objectAdded (final ObjectSceneEvent event,
                           final Object addedObject)
  {
    // does nothing by default
  }

  @Override
  public void objectRemoved (final ObjectSceneEvent event,
                             final Object removedObject)
  {
    // does nothing by default
  }

  @Override
  public void objectStateChanged (final ObjectSceneEvent event,
                                  final Object changedObject,
                                  final ObjectState previousState,
                                  final ObjectState newState)
  {
    // does nothing by default
  }

  @Override
  public void selectionChanged (final ObjectSceneEvent event,
                                final Set<Object> previousSelection,
                                final Set<Object> newSelection)
  {
    // does nothing by default
  }
}
