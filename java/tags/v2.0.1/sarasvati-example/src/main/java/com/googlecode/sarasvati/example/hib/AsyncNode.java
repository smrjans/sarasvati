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
@DiscriminatorValue( "async" )
@NodeType("Node which completes asynchronously")
public class AsyncNode extends HibNode
{
  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    engine.completeAsynchronous( token, Arc.DEFAULT_ARC );
  }
}