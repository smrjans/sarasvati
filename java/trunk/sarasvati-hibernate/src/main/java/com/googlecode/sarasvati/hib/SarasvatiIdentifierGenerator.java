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

    Copyright 2010 Paul Lorenz
*/
package com.googlecode.sarasvati.hib;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

public class SarasvatiIdentifierGenerator implements IdentifierGenerator, Configurable
{
  private IdentifierGenerator generator = null;

  @Override
  public Serializable generate(final SessionImplementor session, final Object object)
      throws HibernateException
  {
    return generator.generate(session, object);
  }

  @Override
  public void configure(final Type type,
                        final Properties properties,
                        final Dialect dialect)
    throws MappingException
  {
    if (dialect.supportsIdentityColumns())
    {
      generator = new IdentityGenerator();
    }
    else
    {
      final SequenceGenerator seqGenerator = new SequenceGenerator();
      seqGenerator.configure(type, properties, dialect);
      generator = seqGenerator;
    }
  }
}
