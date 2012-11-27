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
package com.googlecode.sarasvati.load.properties;

import java.beans.PropertyDescriptor;

import com.googlecode.sarasvati.load.SarasvatiLoadException;

public class BasePropertyMutator implements PropertyMutator
{
  protected PropertyDescriptor propertyDescriptor;
  protected Object target;

  @Override
  public void setPropertyDescriptor (final PropertyDescriptor propertyDescriptor)
  {
    this.propertyDescriptor = propertyDescriptor;
  }

  @Override
  public void setTarget (final Object target)
  {
    this.target = target;
  }

  @Override
  public Object getCurrentValue ()
  {
    try
    {
      return propertyDescriptor.getReadMethod().invoke( target );
    }
    catch (Exception e)
    {
      throw new SarasvatiLoadException( "Unabled to read property " + propertyDescriptor.getName(), e );
    }
  }

  @Override
  public void setFromText (final String text)
  {
    throw new UnsupportedOperationException( "Writes are not supported to the property " + propertyDescriptor.getName() + " on class " + target.getClass().getName() );
  }

  @Override
  public void setValue (final Object value)
  {
    try
    {
      propertyDescriptor.getWriteMethod().invoke( target, value );
    }
    catch (Exception e)
    {
      throw new SarasvatiLoadException( "Unabled to read property " + propertyDescriptor.getName(), e );
    }
  }
}
