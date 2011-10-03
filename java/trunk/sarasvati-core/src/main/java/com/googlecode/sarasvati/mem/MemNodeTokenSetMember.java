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

package com.googlecode.sarasvati.mem;

import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.TokenSetMemberEnvAdapter;

public class MemNodeTokenSetMember extends MemTokenSetMember<MemNodeToken> implements NodeTokenSetMember
{
  protected Env env;

  public MemNodeTokenSetMember (final TokenSet tokenSet, final MemNodeToken token, final int memberIndex)
  {
    super( tokenSet, token, memberIndex );
  }

  @Override
  public Env getEnv ()
  {
    if ( env == null )
    {
      env = new TokenSetMemberEnvAdapter( getTokenSet().getMemberEnv(), memberIndex );
    }
    return env;
  }
}