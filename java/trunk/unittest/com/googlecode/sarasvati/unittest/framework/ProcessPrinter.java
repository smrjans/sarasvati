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
package com.googlecode.sarasvati.unittest.framework;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.util.SvUtil;

public class ProcessPrinter
{
  public static String toString(final NodeToken token)
  {
    return "[" + SvUtil.getShortClassName(token) +
           " id=" + token.getId() +
           " guardAction=" + token.getGuardAction() +
           " execType=" + token.getExecutionType() +
           " complete=" + token.isComplete() +
           " nodeName=" + token.getNode().getName() +
           "]";
  }

  public static String toString(final ArcToken token)
  {
    return "[" + SvUtil.getShortClassName(token.getClass()) +
           " execType=" + token.getExecutionType() +
           " pending=" + token.isPending() +
           " complete=" + token.isComplete() +
           " child=" + (token.isComplete() ? token.getChildToken().getId() : "--") +
           "]";
  }

  public static void print (final GraphProcess p)
  {
    System.out.println("Process: " + p.getGraph().getName());
    for (NodeToken t : p.getNodeTokens())
    {
      System.out.println(toString(t));
      for (ArcToken a : t.getChildTokens())
      {
        System.out.println("\t" + toString(a));
      }
    }
    System.out.println();
  }
}
