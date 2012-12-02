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
package com.googlecode.sarasvati.example.mem;

import java.util.List;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.load.GraphValidatorAdapter;
import com.googlecode.sarasvati.load.SarasvatiLoadException;
import com.googlecode.sarasvati.load.definition.NodeDefinition;

public class MemExampleGraphValidator extends GraphValidatorAdapter
{
  @Override
  public void validateNodeDefinition (final NodeDefinition nd)
    throws SarasvatiLoadException
  {
    if ( nd.getGuard() != null && !nd.getGuard().isEmpty() )
    {
      boolean isGuardValid = true;

      // ... validation logic

      if ( !isGuardValid )
      {
        throw new SarasvatiLoadException( "The guard defined for node " +
                                          nd.getName()  +
                                          " failed validation." );
      }
    }
  }

  @Override
  public void validateGraph (final Graph graph)
    throws SarasvatiLoadException
  {
    List<Node> startNodes = graph.getStartNodes();
    if ( startNodes.size() != 1 ||
        !"validate-order".equals( startNodes.get( 0 ).getType() ) )
    {
      throw new SarasvatiLoadException( "Process definition " + graph.getName() +
                                        " does not start with validte order node. " +
                                        "Policy dictates that all " +
                                        "workflows must start with a " +
                                        "validate order node" );
    }
  }
}
