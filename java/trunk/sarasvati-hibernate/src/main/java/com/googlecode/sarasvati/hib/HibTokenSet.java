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

package com.googlecode.sarasvati.hib;

import java.util.HashMap;
import java.util.HashSet;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.env.TokenSetMemberEnv;
import com.googlecode.sarasvati.impl.MapEnv;

@Entity
@Table(name="wf_token_set")
public class HibTokenSet implements TokenSet
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="sequence_generator")
  @SequenceGenerator(name="sequence_generator", sequenceName="wf_token_set_seq")
  protected Long    id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "process_id", nullable=false)
  protected HibGraphProcess process;

  @Column(name="name", nullable=false)
  protected String name;

  @Column(name="max_member_index", nullable=false)
  protected int maxMemberIndex;

  @OneToMany (fetch=FetchType.LAZY,
              mappedBy="tokenSet",
              orphanRemoval=true,
              cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE } )
  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
  protected Set<HibTokenSetMemberAttribute>     memberAttributes;

  @ForeignKey(name="FK_token_set_attr")
  @ElementCollection(targetClass=String.class)
  @JoinTable( name="wf_token_set_attr", joinColumns={@JoinColumn(name="token_set_id", nullable=false)})
  @MapKeyColumn(name="name")
  @Column( name="value")
  @Cascade( org.hibernate.annotations.CascadeType.DELETE )
  protected Map<String, String> attrMap;

  @OneToMany (mappedBy="tokenSet",
      targetEntity=HibArcTokenSetMember.class,
      fetch=FetchType.LAZY,
      cascade=CascadeType.REMOVE)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<ArcTokenSetMember> arcTokenSetMembers;

  @OneToMany (mappedBy="tokenSet",
      targetEntity=HibNodeTokenSetMember.class,
      fetch=FetchType.LAZY,
      cascade=CascadeType.REMOVE)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<NodeTokenSetMember> nodeTokenSetMembers;

  @Transient
  protected Env env;

  @Transient
  protected TokenSetMemberEnv memberEnv;

  @Transient
  protected Set<ArcToken> activeArcTokens;

  @Transient
  protected Set<NodeToken> activeNodeTokens;

  protected HibTokenSet ()
  {
    /* default constructor for hibernate */
  }

  public HibTokenSet (final HibGraphProcess process,
                      final String name,
                      final int maxMemberIndex)
  {
    this.process = process;
    this.name = name;
    this.maxMemberIndex = maxMemberIndex;
    this.attrMap = new HashMap<String, String>();
    this.memberAttributes = new HashSet<HibTokenSetMemberAttribute>();
    this.activeArcTokens = new HashSet<ArcToken>();
    this.activeNodeTokens = new HashSet<NodeToken>();
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
  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
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
  public int getMaxMemberIndex ()
  {
    return maxMemberIndex;
  }

  public void setMaxMemberIndex (final int maxMemberIndex)
  {
    this.maxMemberIndex = maxMemberIndex;
  }

  public Map<String, String> getAttrMap ()
  {
    return attrMap;
  }

  public void setAttrMap (final Map<String, String> attrMap)
  {
    this.attrMap = attrMap;
  }


  public Set<ArcTokenSetMember> getArcTokenSetMembers ()
  {
    return arcTokenSetMembers;
  }

  public void setArcTokenSetMembers (final Set<ArcTokenSetMember> arcTokenSetMembers)
  {
    this.arcTokenSetMembers = arcTokenSetMembers;
  }

  public Set<NodeTokenSetMember> getNodeTokenSetMembers ()
  {
    return nodeTokenSetMembers;
  }

  public void setNodeTokenSetMembers (final Set<NodeTokenSetMember> nodeTokenSetMembers)
  {
    this.nodeTokenSetMembers = nodeTokenSetMembers;
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
  public TokenSetMemberEnv getMemberEnv ()
  {
    if (memberEnv == null)
    {
      memberEnv = new HibTokenSetMemberEnv( this );
    }
    return memberEnv;
  }

  public Set<HibTokenSetMemberAttribute> getMemberAttributes ()
  {
    return memberAttributes;
  }

  public void setMemberAttributes (final Set<HibTokenSetMemberAttribute> memberAttributes)
  {
    this.memberAttributes = memberAttributes;
  }

  @Override
  public Set<ArcToken> getActiveArcTokens (final Engine engine)
  {
    if ( activeArcTokens == null )
    {
      activeArcTokens = new HashSet<ArcToken>( ((HibEngine)engine).getActiveArcTokens( this ) );
    }
    return activeArcTokens;
  }

  @Override
  public Set<NodeToken> getActiveNodeTokens (final Engine engine)
  {
    if ( activeNodeTokens == null )
    {
      activeNodeTokens = new HashSet<NodeToken>( ((HibEngine)engine).getActiveNodeTokens( this ) );
    }
    return activeNodeTokens;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof HibTokenSet))
      return false;
    HibTokenSet other = (HibTokenSet) obj;
    if (id == null)
    {
      if (other.getId() != null)
        return false;
    } else if (!id.equals( other.getId() ))
      return false;
    return true;
  }
}