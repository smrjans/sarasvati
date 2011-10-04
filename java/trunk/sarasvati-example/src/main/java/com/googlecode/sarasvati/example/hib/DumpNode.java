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
package com.googlecode.sarasvati.example.hib;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.annotations.NodeType;
import com.googlecode.sarasvati.hib.HibNode;

@Entity
@DiscriminatorValue( "dump" )
@NodeType( "Print out a message when executed" )
public class DumpNode extends HibNode
{
  public static boolean doPrint = true;

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    if( doPrint )
    {
      System.out.println( "Accepted into: " + getName() );
    }

    if ( token.getProcess().getParentToken() != null )
    {
      engine.complete( token, Arc.DEFAULT_ARC );
    }
    else
    {
      engine.completeAsynchronous( token, Arc.DEFAULT_ARC );
    }
  }
}