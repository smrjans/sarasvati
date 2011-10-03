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

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.googlecode.sarasvati.visual.icon.NodeIconType;

public class EditorNodeType
{
  private String name;
  private boolean allowNonSpecifiedAttributes;
  private final List<EditorNodeTypeAttribute> attributes;
  private NodeIconType nodeIconType;
  private Color iconColor;

  public EditorNodeType (final String name,
                         final boolean allowNonSpecifiedAttributes,
                         final NodeIconType nodeIconType,
                         final Color iconColor)
  {
    this.name = name;
    this.allowNonSpecifiedAttributes = allowNonSpecifiedAttributes;
    this.attributes = new LinkedList<EditorNodeTypeAttribute>();
    this.nodeIconType = nodeIconType;
    this.iconColor = iconColor;
  }

  public EditorNodeType (final String name,
                         final boolean allowNonSpecifiedAttributes,
                         final NodeIconType nodeIconType,
                         final Color iconColor,
                         final List<EditorNodeTypeAttribute> attributes)
  {
    this.name = name;
    this.allowNonSpecifiedAttributes = allowNonSpecifiedAttributes;
    this.nodeIconType = nodeIconType;
    this.iconColor = iconColor;
    this.attributes = attributes;
  }

  /**
   * @return the name
   */
  public String getName ()
  {
    return name;
  }

  /**
   * @return the allowNonSpecifiedAttributes
   */
  public boolean isAllowNonSpecifiedAttributes ()
  {
    return allowNonSpecifiedAttributes;
  }

  /**
   * @return the attributes
   */
  public List<EditorNodeTypeAttribute> getAttributes ()
  {
    return attributes;
  }

  @Override
  public String toString ()
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
   * @return the nodeIconType
   */
  public NodeIconType getNodeIconType ()
  {
    return nodeIconType;
  }

  /**
   * @param nodeIconType the nodeIconType to set
   */
  public void setNodeIconType (final NodeIconType nodeIconType)
  {
    this.nodeIconType = nodeIconType;
  }

  /**
   * @return the color
   */
  public Color getIconColor ()
  {
    return iconColor;
  }

  /**
   * @param iconColor the color to set
   */
  public void setIconColor (final Color iconColor)
  {
    this.iconColor = iconColor;
  }

  /**
   * @param allowNonSpecifiedAttributes the allowNonSpecifiedAttributes to set
   */
  public void setAllowNonSpecifiedAttributes (final boolean allowNonSpecifiedAttributes)
  {
    this.allowNonSpecifiedAttributes = allowNonSpecifiedAttributes;
  }

  public Set<String> getCDataTypes ()
  {
    Set<String> set = new HashSet<String>();

    for ( EditorNodeTypeAttribute attr : attributes )
    {
      if ( attr.isUseCDATA() )
      {
        set.add( attr.getName() );
      }
    }
    return set;
  }

  public EditorNodeType copy ()
  {
    List<EditorNodeTypeAttribute> attributesCopy = new LinkedList<EditorNodeTypeAttribute>();
    for ( EditorNodeTypeAttribute attr : attributes )
    {
      attributesCopy.add( attr.copy() );
    }
    return new EditorNodeType( name, allowNonSpecifiedAttributes, nodeIconType, iconColor, attributesCopy );
  }
}
