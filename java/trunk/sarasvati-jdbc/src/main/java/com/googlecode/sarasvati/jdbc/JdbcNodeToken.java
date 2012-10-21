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
package com.googlecode.sarasvati.jdbc;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.AttributeConverters;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.NestedEnv;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.visitor.TokenVisitor;

public class JdbcNodeToken implements NodeToken, JdbcObject
{
  protected Long    id;

  protected JdbcGraphProcess process;

  protected JdbcNodeRef nodeRef;

  protected JdbcNodeToken attrSetToken;

  protected Map<String, String> attrMap;

  protected List<ArcToken> parentTokens;

  protected List<ArcToken> childTokens;

  protected Date    createDate;
  protected Date    completeDate;
  protected GuardAction guardAction;
  protected ExecutionType executionType;

  protected Map<String,Object> transientAttributes = new HashMap<String, Object>();

  protected Env env = null;
  protected Env fullEnv = null;

  public JdbcNodeToken (final JdbcGraphProcess process,
                        final JdbcNodeRef nodeRef,
                        final JdbcNodeToken attrSetToken,
                        final ExecutionType executionType,
                        final Map<String,String> attrMap,
                        final List<ArcToken> parentTokens,
                        final Map<String,Object> transientAttributes)
  {
    this.process       = process;
    this.nodeRef       = nodeRef;
    this.executionType = executionType;
    this.attrSetToken  = attrMap.isEmpty() ? attrSetToken : this;
    this.attrMap       = attrMap;
    this.parentTokens  = parentTokens;
    this.childTokens   = new LinkedList<ArcToken>();
    this.createDate    = new Date();

    this.transientAttributes = transientAttributes;
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  @Override
  public void setId (final Long id)
  {
    this.id = id;
  }

  @Override
  public JdbcGraphProcess getProcess ()
  {
    return process;
  }

  @Override
  public JdbcNodeRef getNode ()
  {
    return nodeRef;
  }

  @Override
  public GuardAction getGuardAction ()
  {
    return guardAction;
  }

  public JdbcNodeToken getAttrSetToken ()
  {
    return attrSetToken;
  }

  public Map<String, String> getAttrMap ()
  {
    return attrMap;
  }

  @Override
  public List<ArcToken> getParentTokens ()
  {
    return parentTokens;
  }

  @Override
  public List<ArcToken> getChildTokens()
  {
    return childTokens;
  }

  @Override
  public Date getCreateDate ()
  {
    return createDate;
  }

  @Override
  public Date getCompleteDate ()
  {
    return completeDate;
  }

  @Override
  public boolean isComplete ()
  {
    return completeDate != null;
  }

  @Override
  public void markComplete ()
  {
    this.completeDate = new Date();
  }

  @Override
  public void setGuardAction (final GuardAction action)
  {
    this.guardAction = action;
  }

  @Override
  public ExecutionType getExecutionType ()
  {
    return executionType;
  }

  @Override
  public void markBacktracked ()
  {
    executionType = executionType.getCorrespondingBacktracked( isComplete() );
  }

  @Override
  public void accept (final TokenVisitor visitor)
  {
    visitor.visit( this );
  }

  @Override
  public Env getFullEnv ()
  {
    if ( fullEnv == null )
    {
      fullEnv = new NestedEnv( getEnv(), process.getEnv() );
    }
    return fullEnv;
  }

  @Override
  public Env getEnv ()
  {
    if ( env == null )
    {
      env = new HibTokenEnv();
    }
    return env;
  }

  private class HibTokenEnv implements Env
  {
    public HibTokenEnv()
    {
      // Default constructor
    }

    private boolean isAttributeSetLocal ()
    {
      return JdbcNodeToken.this.equals( attrSetToken );
    }

    @Override
    public String getAttribute (final String name)
    {
      if ( attrSetToken == null )
      {
        return null;
      }
      else if ( !isAttributeSetLocal() )
      {
        return attrSetToken.getEnv().getAttribute( name );
      }
      else
      {
        return attrMap.get( name );
      }
    }

    @Override
    public <T> T getAttribute (final String name,
                               final Class<T> type)
    {
      String value = getAttribute( name );
      return AttributeConverters.stringToObject( value, type );
    }

    @Override
    public <T> T getAttribute (final String name,
                               final Class<T> type,
                               final T defaultValue)
    {
      String value = getAttribute( name );
      return AttributeConverters.stringToObject( value, type, defaultValue );
    }

    protected void copyOnWrite ()
    {
      if ( !isAttributeSetLocal() )
      {
        if ( attrSetToken != null )
        {
          attrMap.putAll( attrSetToken.getAttrMap() );
        }
        attrSetToken = JdbcNodeToken.this;
      }
    }

    @Override
    public void removeAttribute( final String name )
    {
      copyOnWrite();
      attrMap.remove( name );
    }

    @Override
    public void setAttribute (final String name,
                              final String value )
    {
      copyOnWrite();
      attrMap.put( name, value );
    }

    @Override
    public void setAttribute (final String name,
                              final Object value )
    {
      setAttribute( name, AttributeConverters.objectToString( value ) );
    }

    @Override
    public boolean hasAttribute( final String name )
    {
      if ( attrSetToken == null )
      {
        return false;
      }
      else if ( !isAttributeSetLocal() )
      {
        return attrSetToken.getEnv().hasAttribute( name );
      }
      else
      {
        return attrMap.containsKey( name );
      }
    }

    @Override
    public Iterable<String> getAttributeNames()
    {
      if ( attrSetToken == null )
      {
        return Collections.emptyList();
      }
      else if ( !isAttributeSetLocal() )
      {
        return attrSetToken.getEnv().getAttributeNames();
      }
      else
      {
        return attrMap.keySet();
      }
    }

    @Override
    public void setTransientAttribute (final String name, final Object value)
    {
      transientAttributes.put( name, value );
    }

    @Override
    public Object getTransientAttribute (final String name)
    {
      return transientAttributes.get( name );
    }

    @Override
    public boolean hasTransientAttribute (final String name)
    {
      return transientAttributes.containsKey( name );
    }

    @Override
    public void removeTransientAttribute (final String name)
    {
      transientAttributes.remove( name );
    }

    @Override
    public Iterable<String> getTransientAttributeNames()
    {
      return transientAttributes.keySet();
    }

    @Override
    public void importEnv(final Env copyEnv)
    {
      for ( String name : copyEnv.getAttributeNames() )
      {
        setAttribute( name, copyEnv.getAttribute( name ) );
      }

      for ( String name : copyEnv.getTransientAttributeNames() )
      {
        setTransientAttribute( name, copyEnv.getTransientAttribute( name ) );
      }
    }
  }

  @Override
  public boolean isMutable ()
  {
    return true;
  }

  @Override
  public Set<NodeTokenSetMember> getTokenSetMemberships ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TokenSet getTokenSet (final String name)
  {
    return SvUtil.getTokenSet( this, name );
  }

  @Override
  public NodeTokenSetMember getTokenSetMember (final String name)
  {
    return (NodeTokenSetMember)SvUtil.getTokenSetMember( this, name );
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof JdbcNodeToken ) ) return false;
    final JdbcNodeToken other = (JdbcNodeToken)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "[JdbcNodeToken id=" + id + " action=" + guardAction + "]";
  }
}