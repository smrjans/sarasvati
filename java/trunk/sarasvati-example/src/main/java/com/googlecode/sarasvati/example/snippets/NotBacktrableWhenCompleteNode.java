package com.googlecode.sarasvati.example.snippets;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.hib.HibNode;

public class NotBacktrableWhenCompleteNode extends HibNode 
{  
  @Override
  public boolean isBacktrackable(Engine engine, NodeToken token)
  {    
    return !token.isComplete();
  }  
}
