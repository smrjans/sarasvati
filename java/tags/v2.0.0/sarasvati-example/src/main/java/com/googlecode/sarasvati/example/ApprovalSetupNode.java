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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.MapEnv;

/**
 * Example node to generate approvals
 *
 * @author Paul Lorenz
 */
public class ApprovalSetupNode extends CustomNode
{
  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    Env initialEnv = new MapEnv();
    initialEnv.setAttribute( "access", 1 );

    Map<String,List<?>> initialMemberEnv = new HashMap<String, List<?>>();
    initialMemberEnv.put( "group", Arrays.asList( new String[] { "Operations Approval", "Business Approval", "Vendor Approval" } ) );

    engine.completeWithNewTokenSet( token, Arc.DEFAULT_ARC, "approvals", 3, true, initialEnv, initialMemberEnv );
  }
}