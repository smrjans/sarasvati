package com.googlecode.sarasvati.example.snippets;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;

public class CompleteWithCheckSiblings
{
  public List<NodeToken> getSiblings(final NodeToken token)
  {
    final List<NodeToken> result = new LinkedList<NodeToken>();
    for (final ArcToken arcParent : token.getParentTokens())
    {
      final NodeToken parent = arcParent.getParentToken();
      for (final ArcToken siblingArcs : parent.getChildTokens())
      {
        final NodeToken child = siblingArcs.getChildToken();
        if (!token.equals(child))
        {
          result.add(child);
        }
      }
    }
    return result;
  }
  
  public void complete(final Engine engine, final NodeToken token, final String arcName)
  {
    if ("back".equals(arcName))
    {
      for (final NodeToken sibling : getSiblings(token))
      {
        if (sibling.isComplete())
        {
          throw new RuntimeException("Can not go back to start if sibling tasks are complete");
        }
      }
    }
    engine.complete(token, arcName);    
  }
}
