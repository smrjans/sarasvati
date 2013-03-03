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

    Copyright 2009 Paul Lorenz/Vincent Kirsch
 */
package com.googlecode.sarasvati.load.definition;

import java.util.List;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.load.GraphFactory;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.ProcessDefinitionTranslator;

/**
 * A ProcessDefinition is the result of the translation of a certain external source containing
 * the definition of a process. That external source must contain necessary information to create
 * an {@link Graph}, and that information is translated into an ProcessDefinition through a
 * {@link ProcessDefinitionTranslator}. This ProcessDefinition will then in its turn be used
 * by the {@link GraphLoader} to create an {@link Graph} through the {@link GraphFactory}.
 * The default kind of external source is an XML file containing &lt;process-definition&gt; tags.
 */
public interface ProcessDefinition
{
  /**
   * @return The process definition name
   */
  String getName ();

  /**
   * @return A message digest/hash of the process definition contents.
   */
  String getMessageDigest ();

  /**
   * @return Any custom data for the process definition
   */
  CustomDefinition getCustom();

  /**
   * @return A list of the nodes defined in the process definition
   */
  List<? extends NodeDefinition> getNodes ();

  /**
   * @return A list of the externals defined in the process definition.
   */
  List<? extends ExternalDefinition> getExternals ();

  /**
   * @return A list of custom xml elements of the process.
   */
  List<Object> getCustomProcessData();
}