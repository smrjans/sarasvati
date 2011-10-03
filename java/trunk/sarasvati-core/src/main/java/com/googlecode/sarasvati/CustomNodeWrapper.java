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

package com.googlecode.sarasvati;

import com.googlecode.sarasvati.mem.MemNode;

/**
 * This interface represents a Node which will provide an adapter
 * for instances of {@link CustomNode}, bridging the gap between
 * a backend specific {@link Node} implementation, such as
 * {@link HibNode} or {@link MemNode}, and the backend agnostic
 * {@link CustomNode}.
 *
 * @author Paul Lorenz
 */
public interface CustomNodeWrapper extends Node
{
  /**
   * Returns the {@link CustomNode} being wrapped.
   *
   * @param engine The current engine, which may be required to load the CustomNode
   * @return The {@link CustomNode} being wrapped.
   */
  CustomNode getCustomNode (Engine engine);

  /**
   * Returns the default adapter for the current Engine.
   *
   * @param <T> The adapter
   * @param clazz The adapter type being requested.
   *
   * @return An adapter of the type being requested or null, if none is available.
   */
  <T> T getDefaultAdaptor (Class<T> clazz);

  /**
   * Evaluates the guard using the default strategy for the given
   * Node/Engine combination.
   *
   * @param engine The engine executing the current process
   * @param token The token which triggered the guard evaluation.
   *
   * @return A GuardResult based on the guard defined for the Node.
   */
  GuardResult defaultGuard (Engine engine, NodeToken token);
}
