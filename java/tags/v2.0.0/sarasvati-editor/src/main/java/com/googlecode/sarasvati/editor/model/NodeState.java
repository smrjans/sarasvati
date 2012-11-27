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

package com.googlecode.sarasvati.editor.model;

import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.JoinType;

public class NodeState extends GraphMemberState
{
  private final String type;
  private final JoinType joinType;
  private final String joinParam;
  private final boolean isStart;
  private final String  guard;

  public NodeState (final String name,
                    final String type,
                    final JoinType joinType,
                    final String joinParam,
                    final boolean isStart,
                    final String guard,
                    final Map<String, String> customProperties,
                    final boolean isNew)
  {
    super( name, customProperties, isNew );
    this.type = type;
    this.joinType = joinType;
    this.joinParam = joinParam == null ? null : joinParam.trim();
    this.isStart = isStart;
    this.guard = guard == null ? null : guard.trim();
  }

  public String getType ()
  {
    return type;
  }

  public EditorNodeType getEditorNodeType ()
  {
    EditorNodeType editorNodeType = EditorPreferences.getInstance().getTypeByName( type );
    return editorNodeType == null ? EditorPreferences.getInstance().getTypeByName( "node" ) : editorNodeType;
  }

  public JoinType getJoinType ()
  {
    return joinType;
  }

  public String getJoinParam ()
  {
    return joinParam;
  }

  public boolean isStart ()
  {
    return isStart;
  }

  public String getGuard ()
  {
    return guard;
  }

  public NodeState copy (final Set<String> currentNames)
  {
    return new NodeState( getNextUniqueName( currentNames ), type, joinType, joinParam, isStart, guard, getCustomProperties(), true );
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((guard == null) ? 0 : guard.hashCode());
    result = prime * result + (isStart ? 1231 : 1237);
    result = prime * result + ((joinParam == null) ? 0 : joinParam.hashCode());
    result = prime * result + ((joinType == null) ? 0 : joinType.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals( obj ))
    {
      return false;
    }
    if (!(obj instanceof NodeState))
    {
      return false;
    }
    NodeState other = (NodeState) obj;
    if (guard == null)
    {
      if (other.guard != null)
      {
        return false;
      }
    } else if (!guard.equals( other.guard ))
    {
      return false;
    }
    if (isStart != other.isStart)
    {
      return false;
    }
    if (joinParam == null)
    {
      if (other.joinParam != null)
      {
        return false;
      }
    } else if (!joinParam.equals( other.joinParam ))
    {
      return false;
    }
    if (joinType == null)
    {
      if (other.joinType != null)
      {
        return false;
      }
    } else if (!joinType.equals( other.joinType ))
    {
      return false;
    }
    if (type == null)
    {
      if (other.type != null)
      {
        return false;
      }
    } else if (!type.equals( other.type ))
    {
      return false;
    }
    return true;
  }
}