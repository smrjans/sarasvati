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

package com.googlecode.sarasvati.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.impl.BaseProcessDefinitionResolver;

public class DefaultFileXmlProcessDefinitionResolver extends BaseProcessDefinitionResolver<File>
{
  private static final String DEFAULT_SUFFIX = ".wf.xml";

  public DefaultFileXmlProcessDefinitionResolver (final XmlLoader loader, final File basePath)
  {
    this( loader, basePath, null );
  }

  public DefaultFileXmlProcessDefinitionResolver (final XmlLoader loader, final File basePath, final String suffix)
  {
    super( loader, getFilesRepository( basePath, suffix ));
  }

  private static Map<String, File> getFilesRepository (final File basePath, final String suffix)
  {
    String suf = suffix == null? DEFAULT_SUFFIX : suffix;

    Map<String, File> map = new HashMap<String, File>();

    if ( basePath.isDirectory() )
    {
      for ( String fileName : basePath.list() )
      {
        if ( fileName.endsWith( suf ) )
        {
          map.put( fileName.substring( 0, fileName.length() - suf.length() ), new File( basePath, fileName ) );
        }
        else
        {
          map.put( fileName, new File( fileName ) );
        }
      }
    }
    else
    {
      if ( basePath.getName().endsWith( suf ) )
      {
        map.put( basePath.getName().substring( 0, basePath.getName().length() - suf.length() ), basePath );
      }
      else
      {
        map.put( basePath.getName(), basePath );
      }
    }

    return map;
  }
}