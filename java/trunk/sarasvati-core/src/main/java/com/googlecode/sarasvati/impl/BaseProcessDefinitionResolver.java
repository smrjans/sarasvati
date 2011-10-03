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

    Copyright 2009 Vincent Kirsch
 */
package com.googlecode.sarasvati.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.load.ProcessDefinitionResolver;
import com.googlecode.sarasvati.load.ProcessDefinitionTranslator;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;

/**
 * This class is a generic implementation of the {@link ProcessDefinitionResolver} interface.
 * The goal of a {@link ProcessDefinitionResolver} is to map a given name to a {@link ProcessDefinition}.
 * The actual translation of a giver source into a {@link ProcessDefinition} is performed by a {@link ProcessDefinitionTranslator}.
 * This class will thus map the name with a source of type <T> from an internal repository, and then call the provided {@link ProcessDefinitionTranslator}
 * on that source, thus effectively returning the expected {@link ProcessDefinition}.
 *
 * @author vkirsch
 *
 * @param <T> the type of the source to translate into a {@link ProcessDefinition}
 * @see ProcessDefinitionTranslator
 */
public abstract class BaseProcessDefinitionResolver<T> implements ProcessDefinitionResolver
{
  protected ProcessDefinitionTranslator<T> translator;
  protected Map<String, T> sourcesRepository;

  /**
   * Build a new process definition resolver without a repository of sources to lookup.
   * Sources can be added later by calling {@link BaseProcessDefinitionResolver#addSource(Object)}
   *
   * @param translator The translator to use to translate a source of type <T> into a {@link ProcessDefinition}
   */
  public BaseProcessDefinitionResolver (final ProcessDefinitionTranslator<T> translator)
  {
    this.translator = translator;
    this.sourcesRepository = new HashMap<String, T> ();
  }

  /**
   * Build a process definition resolver that will resolve a name to a <T> that will then be translated to
   * a {@link ProcessDefinition} via the provided {@link ProcessDefinitionTranslator}.
   * The {@link ProcessDefinitionResolver#resolve(String)} method will match the name it is provided with a <T> via the result of a
   * call to the toString() method of each element of the provided list.
   *
   * @see ProcessDefinitionResolver#resolve(String)
   * @param translator The translator to use to translate a source of type <T> into a {@link ProcessDefinition}
   * @param sources The sources that will form the repository used to resolve a name into a {@link ProcessDefinition} via the translator
   */
  public BaseProcessDefinitionResolver (final ProcessDefinitionTranslator<T> translator, final List<T> sources)
  {
    this.translator = translator;

    sourcesRepository = new HashMap<String, T>();
    if ( sources != null )
    {
      for (T t : sources)
      {
        sourcesRepository.put( t.toString(), t );
      }
    }
  }

  /**
   * Build a process definition resolver that will resolve a name to a <T> that will then be translated to
   * a {@link ProcessDefinition} via the provided {@link ProcessDefinitionTranslator}.
   * The {@link ProcessDefinitionResolver#resolve(String)} method will match the name it is provided with a <T> via the provided map.
   *
   * @see ProcessDefinitionResolver#resolve(String)
   * @param translator The translator to use to translate a source of type <T> into a {@link ProcessDefinition}
   * @param map The sources repository used to resolve a name into a {@link ProcessDefinition} via the translator
   */
  public BaseProcessDefinitionResolver (final ProcessDefinitionTranslator<T> translator, final Map<String, T> map)
  {
    this.translator = translator;
    if ( map != null )
    {
      this.sourcesRepository = map;
    }
    else
    {
      this.sourcesRepository = new HashMap<String, T>();
    }
  }

  public void addSource (final T source)
  {
    addSource (source.toString(), source);
  }

  public void addSource (final String name, final T source)
  {
    String key = name == null? source.toString() : name;
    sourcesRepository.put ( key, source );
  }

  /**
   * @see com.googlecode.sarasvati.load.ProcessDefinitionResolver#resolve(java.lang.String)
   */
  @Override
  public ProcessDefinition resolve (final String name)
  {
    return sourcesRepository.get(name) == null ? null : translator.translate( sourcesRepository.get( name ) );
  }
}