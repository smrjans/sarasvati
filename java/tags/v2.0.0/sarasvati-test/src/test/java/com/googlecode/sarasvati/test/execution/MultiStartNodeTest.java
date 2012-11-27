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
package com.googlecode.sarasvati.test.execution;

import org.junit.Test;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class MultiStartNodeTest extends ExecutionTest
{
  @Test public void testLinear () throws Exception
  {
    final RubricPredicate predicate = new RubricPredicate()
    {      
      @Override
      public boolean eval(final Engine engine, final NodeToken token)
      {
        return "bar".equals(token.getProcess().getEnv().getAttribute("foo"));
      }
    };
    
    DefaultRubricFunctionRepository.getGlobalInstance().registerPredicate("isFooBar", predicate);
    MapEnv env = new MapEnv();
    env.setAttribute("foo", "bar");
    GraphProcess p = startProcess("multi-start-node", env);

    String state = "[1 nodeA C F]" +
    		           "  (C F 3)" +
    		           "[2 nodeB C F]" +
    		           "[3 nodeC C F]";
    TestProcess.validate( p, state );
  }
}
