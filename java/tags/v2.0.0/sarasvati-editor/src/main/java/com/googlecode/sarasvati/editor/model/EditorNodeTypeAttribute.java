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

public class EditorNodeTypeAttribute
{
  private String name;
  private String defaultValue;
  private boolean useCDATA;

  public EditorNodeTypeAttribute (final String name,
                                  final String defaultValue,
                                  final boolean useCDATA)
  {
    this.name = name;
    this.defaultValue = defaultValue;
    this.useCDATA = useCDATA;
  }

  /**
   * @return the name
   */
  public String getName ()
  {
    return name;
  }

  /**
   * @return the defaultValue
   */
  public String getDefaultValue ()
  {
    return defaultValue;
  }

  /**
   * @return the useCDATA
   */
  public boolean isUseCDATA ()
  {
    return useCDATA;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  public void setDefaultValue (final String defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public void setUseCDATA (final boolean useCDATA)
  {
    this.useCDATA = useCDATA;
  }

  public EditorNodeTypeAttribute copy ()
  {
    return new EditorNodeTypeAttribute( name, defaultValue, useCDATA );
  }
}
