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
package com.googlecode.sarasvati.editor.model;

import java.io.File;
import java.lang.ref.SoftReference;

import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.xml.XmlEditorProperties;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.util.SvUtil;

public class LibraryEntry implements Comparable<LibraryEntry>
{
  protected String name;
  protected File path;
  protected SoftReference<ProcessDefinition> pdRef;
  protected SoftReference<XmlEditorProperties> editorRef;

  public LibraryEntry (final String name,
                       final File path )
  {
    this.name = name;
    this.path = path;
  }

  public LibraryEntry (final ProcessDefinition processDefinition,
                       final XmlEditorProperties editorProperties,
                       final File path )
  {
    this.name = processDefinition.getName();
    this.path = path;
    this.pdRef = new SoftReference<ProcessDefinition>( processDefinition );
    this.editorRef = new SoftReference<XmlEditorProperties>( editorProperties );
  }

  /**
   * @return the name
   */
  public String getName ()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName (final String name)
  {
    this.name = name;
  }

  /**
   * @return the path
   */
  public File getPath ()
  {
    return path;
  }

  /**
   * @param path the path to set
   */
  public void setPath (final File path)
  {
    this.path = path;
  }

  public ProcessDefinition getProcessDefinition ()
  {
    ProcessDefinition processDefinition = pdRef == null ? null : pdRef.get();

    if ( processDefinition == null )
    {
      processDefinition = GraphEditor.getInstance().getXmlLoader().translate( path );
      pdRef = new SoftReference<ProcessDefinition>( processDefinition );
    }

    return processDefinition;
  }

  public XmlEditorProperties getEditorProperties ()
  {
    XmlEditorProperties editorProperties = editorRef == null ? null : editorRef.get();

    if ( editorProperties == null )
    {
      editorProperties = GraphEditor.getInstance().loadEditorProperties( path );
      editorRef = new SoftReference<XmlEditorProperties>( editorProperties );
    }

    return editorProperties;
  }

  @Override
  public int compareTo (final LibraryEntry o)
  {
    if ( o == null )
    {
      return 1;
    }

    return SvUtil.compare( name, o.getName() );
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof LibraryEntry ) ) return false;
    LibraryEntry other = (LibraryEntry)obj;
    if ( name == null )
    {
      if ( other.name != null ) return false;
    }
    else if ( !name.equals( other.name ) ) return false;
    return true;
  }
}