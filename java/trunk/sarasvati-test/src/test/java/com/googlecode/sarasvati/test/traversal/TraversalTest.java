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

    Copyright 2008, 2012 Paul Lorenz
*/
package com.googlecode.sarasvati.test.traversal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.Token;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.visitor.TokenTraversals;
import com.googlecode.sarasvati.visitor.TokenVisitorAdaptor;

public class TraversalTest extends ExecutionTest
{
  public static class BreadthFirstTestVisitor extends TokenVisitorAdaptor
  {
    final Map<Token, Integer> levelMap = new HashMap<Token, Integer>();
    int currentNodeLevel = 1;

    @Override
    public void visit (final NodeToken token)
    {
      int level = 0;
      if (!token.getParentTokens().isEmpty())
      {
        level = Integer.MAX_VALUE;
        for (final ArcToken parent : token.getParentTokens())
        {
          Integer parentLevel = levelMap.get(parent.getParentToken());
          level = Math.min(level, parentLevel == null ? Integer.MAX_VALUE : parentLevel);
        }
      }
      
      level++;
      levelMap.put(token, level);
      
      if (level != currentNodeLevel)
      {
        if (level == currentNodeLevel + 1)
        {
          currentNodeLevel++;
        }
        else
        {
          throw new RuntimeException("Current level is " + currentNodeLevel + " hit node at level " + level);
        }
      }
    }    
  }
  
  public static class DepthFirstTestVisitor extends TokenVisitorAdaptor
  {
    final Set<Token> doneSet = new HashSet<Token>();
    final LinkedList<Token> stack = new LinkedList<Token>();
    final Map<Token, Integer> levelMap = new HashMap<Token, Integer>();
    int currentNodeLevel = 1;
    
    @Override
    public void visit (final NodeToken token)
    {
      int level = 0;
      
      if (!token.getParentTokens().isEmpty())
      {
        boolean parentFoundInStack = false;
        level = Integer.MAX_VALUE;
        for (final ArcToken parent : token.getParentTokens())
        {
          Integer parentLevel = levelMap.get(parent.getParentToken());
          level = Math.min(level, parentLevel == null ? Integer.MAX_VALUE : parentLevel);
          
          parentFoundInStack |= stack.contains(parent.getParentToken());
        }
        
        if (!parentFoundInStack)
        {
          throw new RuntimeException("No parent found in stack. Not depth first!");
        }
      }
      
      level++;
      levelMap.put(token, level);
      
      if (level == currentNodeLevel)
      {
        if (!stack.isEmpty())
        {
          doneSet.add(stack.removeLast());
        }
        stack.add(token);
      }
      else if (level > currentNodeLevel)
      {
        stack.add(token);
      }
      else if (level < currentNodeLevel)
      {
        int diff = currentNodeLevel - level;
        for (int i = 0; i < diff; i++)
        {
          Token stackToken = stack.removeLast();
          if (doneSet.contains(stackToken))
          {
            throw new RuntimeException("Rencountered tree section that was already traversed!");
          }
          doneSet.add(stackToken);
        }
        stack.add(token);
      }
      
      currentNodeLevel = level;
    }    
  }
  
  public NodeToken executeTraversal () throws Exception
  {
    Graph g = ensureLoaded( "traversal" );
    GraphProcess p = startProcess( g );

    NodeToken tokenA = null;

    for (NodeToken t : p.getNodeTokens() )
    {
      if ( t.getNode().isStart() )
      {
        tokenA = t;
        break;
      }
    }

    Assert.assertNotNull(  "No token found for start node", tokenA );

    return tokenA;
  }

  @Test 
  public void testBreadthFirst () throws Exception
  {
    NodeToken tokenA = executeTraversal();
    TokenTraversals.traverseChildrenBreadthFirst( tokenA, new BreadthFirstTestVisitor() );
  }

  @Test 
  public void testDepthFirst () throws Exception
  {
    NodeToken tokenA = executeTraversal();
    TokenTraversals.traverseChildrenDepthFirst( tokenA, new DepthFirstTestVisitor() );
  }
  
  @Test(expected=RuntimeException.class)
  public void testBreadthFirstWithDepthFirstTester() throws Exception
  {
    NodeToken tokenA = executeTraversal();
    TokenTraversals.traverseChildrenBreadthFirst( tokenA, new DepthFirstTestVisitor());
  }

  @Test(expected=RuntimeException.class) 
  public void testDepthFirstWithBreadthFirstTester() throws Exception
  {
    NodeToken tokenA = executeTraversal();
    TokenTraversals.traverseChildrenDepthFirst( tokenA, new BreadthFirstTestVisitor());
  }
}