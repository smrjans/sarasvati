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
package com.googlecode.sarasvati.visual.process;

import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;

/**
 * Represents a node in the history of process execution.
 * May either refer to a token and the node that token points
 * to, or just a node, if execution has either not reached,
 * or bypassed the given node.
 *
 * @author Paul Lorenz
 */
public interface VisualProcessNode
{
  Node getNode ();

  NodeToken getToken ();
}
