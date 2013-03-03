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
package com.googlecode.sarasvati.editor.panel;

import javax.swing.table.AbstractTableModel;

import com.googlecode.sarasvati.editor.model.EditorNodeType;
import com.googlecode.sarasvati.editor.model.EditorNodeTypeAttribute;

public class EditorNodeTypeTableModel extends AbstractTableModel
{
  private static final long serialVersionUID = 1L;

  private static final Class<?>[] types = new Class<?> [] { String.class, String.class, Boolean.class };
  private static final String[] columns = new String[] { "Name" , "Default Value", "Use CDATA" };

  private EditorNodeType nodeType;

  public EditorNodeType getNodeType ()
  {
    return nodeType;
  }

  public void setNodeType (final EditorNodeType nodeType)
  {
    this.nodeType = nodeType;
    fireTableDataChanged();
  }

  @Override
  public int getColumnCount ()
  {
    return 3;
  }

  @Override
  public int getRowCount ()
  {
    return nodeType == null ? 0 : nodeType.getAttributes().size();
  }

  @Override
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    EditorNodeTypeAttribute nodeTypeAttr = nodeType.getAttributes().get( rowIndex );
    switch ( columnIndex )
    {
      case 0 :
        return nodeTypeAttr.getName();
      case 1 :
        return nodeTypeAttr.getDefaultValue();
      case 2 :
        return nodeTypeAttr.isUseCDATA();
    }

    return null;
  }

  @Override
  public Class<?> getColumnClass (final int columnIndex)
  {
    return types[ columnIndex ];
  }

  @Override
  public String getColumnName (final int column)
  {
    return columns[ column ];
  }

  public void addAttribute ()
  {
    int index = nodeType.getAttributes().size();
    nodeType.getAttributes().add( new EditorNodeTypeAttribute( "", "", false ) );
    fireTableRowsInserted( index, index );
  }

  public void removeAttribute (final int index)
  {
    nodeType.getAttributes().remove( index );
    fireTableRowsDeleted( index, index );
  }

  @Override
  public void setValueAt (final Object value, final int rowIndex, final int columnIndex)
  {
    EditorNodeTypeAttribute nodeTypeAttr = nodeType.getAttributes().get( rowIndex );
    switch ( columnIndex )
    {
      case 0 :
        nodeTypeAttr.setName( (String)value );
        break;
      case 1 :
        nodeTypeAttr.setDefaultValue( (String)value );
        break;
      case 2 :
        nodeTypeAttr.setUseCDATA( (Boolean)value );
        break;
    }
  }

  @Override
  public boolean isCellEditable (final int rowIndex, final int columnIndex)
  {
    return true;
  }
}
