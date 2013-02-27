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
package com.googlecode.sarasvati.env;

import java.nio.ByteBuffer;

/**
 * Attribute converter for String <--> Integer
 *
 * @author Paul Lorenz
 */
public final class LongArrayAttributeConverter implements AttributeConverter
{
  @Override
  public String objectToString (final Object object)
  {
    if (object == null)
    {
      return null;
    }
    
    final long[] array = (long[]) object;
    
    final ByteBuffer byteBuffer = ByteBuffer.allocate(4 + (8 * array.length));
    byteBuffer.putInt(array.length);
    for (final long value : array)
    {
      byteBuffer.putLong(value);
    }
    
    return Base64.encode(byteBuffer.array());
  }
  
  /**
   * Converts the given string to an Integer
   *
   * @see com.googlecode.sarasvati.env.AttributeConverter#stringToObject(java.lang.String, java.lang.Class)
   */
  @Override
  public Object stringToObject (final String string, final Class<?> object)
  {
    if (string == null)
    {
      return null;
    }
   
    final byte[] bytes = Base64.decode(string);
    final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    final int length = byteBuffer.getInt();
    
    final long[] array = new long[length];
    
    for (int i = 0; i < array.length; i++)
    {
      array[i] = byteBuffer.getLong();
    }
    
    return array;
  }
}