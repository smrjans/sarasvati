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

    Copyright 2008-2009, 2012 Paul Lorenz
*/

package com.googlecode.sarasvati.hib;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.Query;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Where;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.ProcessState;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.event.CachingExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventQueue;
import com.googlecode.sarasvati.event.InitialExecutionEventQueue;
import com.googlecode.sarasvati.impl.MapEnv;

@Entity
@Table (name="wf_process")
@org.hibernate.annotations.Table( appliesTo="wf_process", indexes={@Index(name="wf_process_idx", columnNames={"graph_id", "state"} )} )
public class HibGraphProcess implements GraphProcess
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long id;

  @ForeignKey(name="FK_process_graph")
  @ManyToOne (fetch=FetchType.LAZY)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  @JoinColumn( name="graph_id")
  protected HibGraph            graph;

  protected ProcessState state;

  @ForeignKey(name="FK_process_parent")
  @ManyToOne(fetch = FetchType.LAZY, targetEntity=HibNodeToken.class)
  @JoinColumn (name = "parent_token_id", nullable=true)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected NodeToken parentToken;

  @Column (name="create_date", updatable=false)
  @Temporal (TemporalType.TIMESTAMP)
  protected Date createDate;

  @Version
  protected Integer version;

  @OneToMany (mappedBy="process",
              targetEntity=HibNodeToken.class,
              fetch=FetchType.LAZY,
              cascade=CascadeType.REMOVE)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<NodeToken> nodeTokens;

  @OneToMany (mappedBy="process", targetEntity=HibArcToken.class, fetch=FetchType.LAZY)
  @Where (clause="complete_date is null and pending='N'")
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<ArcToken>  activeArcTokens;

  @OneToMany (mappedBy="process", targetEntity=HibNodeToken.class, fetch=FetchType.LAZY)
  @Where (clause="complete_date is null")
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<NodeToken> activeNodeTokens;

  @OneToMany (mappedBy="process", targetEntity=HibArcToken.class, fetch=FetchType.LAZY)
  @Where (clause="complete_date is null and pending='Y'")
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected List<ArcToken>  executionQueue;

  @OneToMany (mappedBy="process", fetch=FetchType.LAZY)
  @Cascade( { org.hibernate.annotations.CascadeType.LOCK, org.hibernate.annotations.CascadeType.DELETE } )
  protected List<HibProcessListener>  listeners;

  @ForeignKey(name="FK_process_attr")
  @ElementCollection(targetClass=String.class)
  @JoinTable( name="wf_process_attr", joinColumns={@JoinColumn( name="process_id")})
  @MapKeyColumn(name="name")
  @Column( name="value")
  @Cascade( org.hibernate.annotations.CascadeType.DELETE )
  protected Map<String, String> attrMap;

  @Transient
  protected Env env = null;

  @Transient
  protected ExecutionEventQueue eventQueue = new InitialExecutionEventQueue()
  {
    @Override
    protected ExecutionEventQueue init ()
    {
      CachingExecutionEventQueue newEventQueue = CachingExecutionEventQueue.newArrayListInstance();
      newEventQueue.initFromPersisted( getListeners() );
      eventQueue = newEventQueue;
      return eventQueue;
    }
  };

  public HibGraphProcess () { /* Default constructor for Hibernate */ }

  public HibGraphProcess (final HibGraph graph)
  {
    this.graph = graph;
    this.nodeTokens = new HashSet<NodeToken>();
    this.activeArcTokens = new HashSet<ArcToken>();
    this.activeNodeTokens = new HashSet<NodeToken>();
    this.executionQueue = new LinkedList<ArcToken>();
    this.listeners = new LinkedList<HibProcessListener>();
    this.state = ProcessState.Created;
    this.createDate = new Date();
    this.attrMap = new HashMap<String, String>();
  }

  public Long getId ()
  {
    return id;
  }

  public void setId (final Long id)
  {
    this.id = id;
  }

  @Override
  public HibGraph getGraph ()
  {
    return graph;
  }

  public void setGraph (final HibGraph graph)
  {
    this.graph = graph;
  }

  @Override
  public Set<NodeToken> getNodeTokens ()
  {
    return nodeTokens;
  }

  public void setNodeTokens (final Set<NodeToken> nodeTokens)
  {
    this.nodeTokens = nodeTokens;
  }

  @Override
  public Set<ArcToken> getActiveArcTokens ()
  {
    return activeArcTokens;
  }

  public void setActiveArcTokens (final Set<ArcToken> activeArcTokens)
  {
    this.activeArcTokens = activeArcTokens;
  }

  @Override
  public Set<NodeToken> getActiveNodeTokens ()
  {
    return activeNodeTokens;
  }

  public void setActiveNodeTokens (final Set<NodeToken> activeNodeTokens)
  {
    this.activeNodeTokens = activeNodeTokens;
  }

  public List<ArcToken> getExecutionQueue ()
  {
    return executionQueue;
  }

  public void setExecutionQueue (final List<ArcToken> executionQueue)
  {
    this.executionQueue = executionQueue;
  }

  @Override
  public ArcToken dequeueArcTokenForExecution ()
  {
    return executionQueue.remove( 0 );
  }

  @Override
  public void enqueueArcTokenForExecution (final ArcToken token)
  {
    executionQueue.add( token );
  }

  @Override
  public boolean isArcTokenQueueEmpty ()
  {
    return executionQueue.isEmpty();
  }

  public List<HibProcessListener> getListeners ()
  {
    return listeners;
  }

  public void setListeners (final List<HibProcessListener> listeners)
  {
    this.listeners = listeners;
  }

  public void setEnv (final Env env)
  {
    this.env = env;
  }

  @Override
  public NodeToken getParentToken ()
  {
    return parentToken;
  }

  public void setParentToken (final NodeToken parentToken)
  {
    this.parentToken = parentToken;
  }

  public Date getCreateDate ()
  {
    return createDate;
  }

  public void setCreateDate (final Date createDate)
  {
    this.createDate = createDate;
  }

  public Integer getVersion ()
  {
    return version;
  }

  public void setVersion (final Integer version)
  {
    this.version = version;
  }

  public Map<String, String> getAttrMap ()
  {
    return attrMap;
  }

  public void setAttrMap (final Map<String, String> attrMap)
  {
    this.attrMap = attrMap;
  }

  @Override
  public Env getEnv ()
  {
    if (env == null)
    {
      env = new MapEnv( getAttrMap() );
    }
    return env;
  }

  @Override
  public void addNodeToken (final NodeToken token)
  {
    nodeTokens.add( token );
  }

  @Override
  public void addActiveArcToken (final ArcToken token)
  {
    activeArcTokens.add( token );
  }

  @Override
  public void removeActiveArcToken (final ArcToken token)
  {
    activeArcTokens.remove( token );
  }

  @Override
  public void addActiveNodeToken (final NodeToken token)
  {
    activeNodeTokens.add( token );
  }

  @Override
  public void removeActiveNodeToken (final NodeToken token)
  {
    activeNodeTokens.remove( token );
  }

  @Override
  public ProcessState getState ()
  {
    return state;
  }

  @Override
  public void setState (final ProcessState state)
  {
    this.state = state;
  }

  @Override
  public boolean isCanceled ()
  {
    return state == ProcessState.PendingCancel || state == ProcessState.Canceled;
  }

  @Override
  public boolean isComplete ()
  {
    return state == ProcessState.PendingCompletion || state == ProcessState.Completed;
  }

  @Override
  public boolean isExecuting ()
  {
    return state == ProcessState.Executing;
  }

  @Override
  public boolean hasActiveTokens ()
  {
    return !activeArcTokens.isEmpty() || !activeNodeTokens.isEmpty();
  }

  @Override
  public ExecutionEventQueue getEventQueue ()
  {
    return eventQueue;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NodeToken> getTokensOnNode (final Node node, final Engine engine)
  {
    HibEngine hibEngine = (HibEngine)engine;
    String hql = "from HibNodeToken where nodeRef = :nodeRef and process=:process";
    Query query = hibEngine.getSession().createQuery( hql );
    query.setEntity( "nodeRef", node );
    query.setEntity( "process", this );
    return query.list();
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
    if ( !( obj instanceof HibGraphProcess ) ) return false;
    final HibGraphProcess other = (HibGraphProcess)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}