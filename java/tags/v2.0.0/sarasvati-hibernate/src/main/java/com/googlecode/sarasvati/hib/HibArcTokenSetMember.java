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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.googlecode.sarasvati.ArcTokenSetMember;

@Entity
@Table(name="wf_token_set_arcmem")
public class HibArcTokenSetMember implements ArcTokenSetMember
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="sequence_generator")
  @SequenceGenerator(name="sequence_generator", sequenceName="wf_token_set_arcmem_seq")
  protected Long    id;

  @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
  @JoinColumn(name = "token_set_id", nullable=false)
  protected HibTokenSet tokenSet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "token_id", nullable=false)
  protected HibArcToken token;

  @Column(name="member_index", nullable=false)
  protected int memberIndex;

  protected HibArcTokenSetMember ()
  {
    /* default constructor for hibernate */
  }

  public HibArcTokenSetMember (final HibTokenSet tokenSet, final HibArcToken token, final int memberIndex)
  {
    this.tokenSet = tokenSet;
    this.token = token;
    this.memberIndex = memberIndex;
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
  public HibTokenSet getTokenSet ()
  {
    return tokenSet;
  }

  public void setTokenSet (final HibTokenSet tokenSet)
  {
    this.tokenSet = tokenSet;
  }

  @Override
  public HibArcToken getToken ()
  {
    return token;
  }

  public void setToken (final HibArcToken token)
  {
    this.token = token;
  }

  @Override
  public int getMemberIndex ()
  {
    return memberIndex;
  }

  public void setMemberIndex (final int memberIndex)
  {
    this.memberIndex = memberIndex;
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
    if ( !( obj instanceof HibArcTokenSetMember ) ) return false;
    HibArcTokenSetMember other = (HibArcTokenSetMember)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}
