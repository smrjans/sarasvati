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
package com.googlecode.sarasvati.join;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.join.lang.JoinLangEnv;
import com.googlecode.sarasvati.join.lang.JoinLangExpr;

/**
 * Implements a join strategy that evaluates the join parameter
 * in the target node as a statement of JoinLang. The result
 * of the evaluation determines if the guard is satisfied or not.
 * <p>
 *
 * @author Paul Lorenz
 */
public class JoinLangJoinStrategy implements JoinStrategy
{
  @Override
  public JoinResult performJoin (final Engine engine, final ArcToken token)
  {
    Node targetNode = token.getArc().getEndNode();
    JoinLangExpr expr = JoinLangCompiler.compile( targetNode.getJoinParam() );
    JoinLangEnv env = engine.newJoinLangEnv( token );
    return expr.performJoin( env );
  }
}
