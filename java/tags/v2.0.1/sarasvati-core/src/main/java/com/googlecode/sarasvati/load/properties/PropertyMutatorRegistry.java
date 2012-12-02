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
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.env.AttributeConverter;
import com.googlecode.sarasvati.env.AttributeConverters;

public class PropertyMutatorRegistry
{
  private static final Map<Class<?>, Class<?>> mutatorCache = new HashMap<Class<?>, Class<?>>();


  public static void registerPropertyMutator (final Class<?> targetClass, final Class<?> mutatorClass)
  {
    mutatorCache.put( targetClass, mutatorClass );
  }

  public static PropertyMutator getMutator (final PropertyDescriptor pd, final Object obj, final PropertyMutator defaultMutator)
  {
    Class<?> mutatorClass = mutatorCache.get( pd.getPropertyType() );

    PropertyMutator mutator = null;

    if ( mutatorClass != null )
    {
      try
      {
        mutator = (PropertyMutator)mutatorClass.newInstance();
      }
      catch (Throwable t)
      {
        mutator = null; // use the default property mutator
      }
    }
    else
    {
      AttributeConverter converter = AttributeConverters.getConverterForType( pd.getPropertyType() );
      if ( converter != null )
      {
        mutator = new AttributeConverterPropertyMutator( converter, pd.getPropertyType() );
      }
    }

    mutator = mutator != null ? mutator : defaultMutator;
    mutator.setPropertyDescriptor( pd );
    mutator.setTarget( obj );

    return mutator;
  }
}
