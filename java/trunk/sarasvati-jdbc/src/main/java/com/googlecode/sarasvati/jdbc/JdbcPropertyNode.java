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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.googlecode.sarasvati.jdbc.action.DatabaseAction;
import com.googlecode.sarasvati.jdbc.dialect.DatabaseDialect;

public class JdbcPropertyNode extends JdbcNode
{
  protected Map<String, String> attrMap = new HashMap<String, String>();

  public String getProperty (final String key)
  {
    return attrMap.get( key );
  }

  public void setProperty (final String key, final String value)
  {
    attrMap.put( key, value );
  }

  public void importProperties (final Map<String, String> properties)
  {
    if ( properties != null )
    {
      for ( Entry<String,String> entry : properties.entrySet() )
      {
        setProperty( entry.getKey(), entry.getValue() );
      }
    }
  }

  @Override
  public void afterCreate (final JdbcEngine engine)
  {
    DatabaseDialect dialect = engine.getDatabaseDialect();
    for ( Entry<String, String> entry : attrMap.entrySet() )
    {
      DatabaseAction stmt = dialect.newNodePropertyInsertAction( this, entry.getKey(), entry.getValue() );
      stmt.execute( engine );
    }
  }

  @Override
  public void afterLoad (JdbcEngine engine)
  {
    DatabaseDialect dialect = engine.getDatabaseDialect();
    dialect.newNodePropertiesLoadAction( this ).execute( engine );
  }
}