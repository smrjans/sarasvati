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
package com.googlecode.sarasvati.hib;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.ForeignKey;

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

@Entity
@Table(name="wf_node_token")
public class HibNodeToken implements NodeToken
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long    id;

  @ForeignKey(name="FK_nodetok_process")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "process_id", nullable=false)
  protected HibGraphProcess process;

  @ForeignKey(name="FK_nodetok_ref")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_ref_id", nullable=false)
  protected HibNodeRef nodeRef;

  @ForeignKey(name="FK_nodetok_attr_node")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "attr_set_id", nullable=true)
  protected HibNodeToken attrSetToken;

  @ForeignKey(name="FK_nodetok_attr")
  @CollectionOfElements
  @JoinTable( name="wf_token_attr", joinColumns={@JoinColumn( name="attr_set_id")})
  @org.hibernate.annotations.MapKey( columns={@Column(name="name")})
  @Column( name="value")
  @Cascade( org.hibernate.annotations.CascadeType.DELETE )
  protected Map<String, String> attrMap;

  @ForeignKey(name="FK_nodetok_parent", inverseName="FK_nodetok_child")
  @OneToMany( fetch=FetchType.LAZY, targetEntity=HibArcToken.class, cascade=CascadeType.ALL, mappedBy="childToken" )
  protected List<ArcToken> parentTokens;

  @OneToMany( mappedBy="parentToken", targetEntity=HibArcToken.class, fetch=FetchType.LAZY, cascade=CascadeType.REMOVE )
  protected List<ArcToken> childTokens;

  @Temporal(TemporalType.TIMESTAMP)
  @Column (name="create_date", updatable = false)
  protected Date    createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column (name="complete_date")
  protected Date    completeDate;

  @Column (name="guard_action")
  protected GuardAction guardAction;

  @Column (name="execution_type")
  protected ExecutionType executionType;

  @Transient
  protected Map<String,Object> transientAttributes = new HashMap<String, Object>();

  @Transient
  protected Env env = null;

  @Transient
  protected Env fullEnv = null;

  @OneToMany (mappedBy="token", targetEntity=HibNodeTokenSetMember.class, fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<NodeTokenSetMember> tokenSetMemberships;

  public HibNodeToken () { /* Default constructor for Hibernate */ }

  public HibNodeToken (final HibGraphProcess process,
                       final HibNodeRef nodeRef,
                       final HibNodeToken attrSetToken,
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
    this.tokenSetMemberships = new HashSet<NodeTokenSetMember>();
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  public void setId (final Long id)
  {
    this.id = id;
  }

  @Override
  public HibGraphProcess getProcess ()
  {
    return process;
  }

  public void setProcess (final HibGraphProcess process)
  {
    this.process = process;
  }

  @Override
  public HibNodeRef getNode ()
  {
    return nodeRef;
  }

  public void setNodeRef (final HibNodeRef nodeRef)
  {
    this.nodeRef = nodeRef;
  }

  @Override
  public GuardAction getGuardAction ()
  {
    return guardAction;
  }

  @Override
  public void setGuardAction (final GuardAction action)
  {
    this.guardAction = action;
  }

  public HibNodeToken getAttrSetToken()
  {
    return attrSetToken;
  }

  public void setAttrSetToken( final HibNodeToken attrSetToken )
  {
    this.attrSetToken = attrSetToken;
  }

  public Map<String, String> getAttrMap()
  {
    return attrMap;
  }

  public void setAttrMap( final Map<String, String> attrMap )
  {
    this.attrMap = attrMap;
  }

  @Override
  public List<ArcToken> getParentTokens ()
  {
    return parentTokens;
  }

  public void setParentTokens (final List<ArcToken> parentTokens)
  {
    this.parentTokens = parentTokens;
  }

  @Override
  public List<ArcToken> getChildTokens()
  {
    return childTokens;
  }

  public void setChildTokens (final List<ArcToken> childTokens)
  {
    this.childTokens = childTokens;
  }

  @Override
  public Date getCreateDate ()
  {
    return createDate;
  }

  public void setCreateDate (final Date createDate)
  {
    this.createDate = createDate;
  }

  @Override
  public Date getCompleteDate ()
  {
    return completeDate;
  }

  public void setCompleteDate (final Date completeDate)
  {
    this.completeDate = completeDate;
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
  public Set<NodeTokenSetMember> getTokenSetMemberships ()
  {
    return tokenSetMemberships;
  }

  public void setTokenSetMemberships (final Set<NodeTokenSetMember> tokenSetMemberships)
  {
    this.tokenSetMemberships = tokenSetMemberships;
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
      return HibNodeToken.this.equals( attrSetToken );
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
        attrSetToken = HibNodeToken.this;
      }
    }

    @Override
    public void removeAttribute (final String name)
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
    public boolean hasAttribute (final String name)
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
    public Iterable<String> getAttributeNames ()
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
    public Iterable<String> getTransientAttributeNames ()
    {
      return transientAttributes.keySet();
    }

    @Override
    public void importEnv (final Env copyEnv)
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
    if ( !( obj instanceof HibNodeToken ) ) return false;
    final HibNodeToken other = (HibNodeToken)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }

  @Override
  public String toString ()
  {
    return "[HibNodeToken id=" + id + " guardAction=" + guardAction + " execType=" + executionType + " complete=" + isComplete() + "]";
  }
}