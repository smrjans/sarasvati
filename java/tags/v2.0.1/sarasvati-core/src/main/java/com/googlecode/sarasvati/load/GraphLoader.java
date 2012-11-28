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
package com.googlecode.sarasvati.load;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;

/**
 * Interface for loading process definitions into a {@link GraphRepository}.
 * Should not be considered thread-safe.
 *
 * @author Paul Lorenz
 */
public interface GraphLoader<G extends Graph>
{
  /**
   * Loads the given process definition.
   *
   * <p>
   * Equivalent to loadDefinition( procDef, null )
   * <p>
   *
   * @see GraphLoader#loadDefinition(ProcessDefinition, String)
   */
  void loadDefinition (final ProcessDefinition procDef) throws SarasvatiLoadException;

  /**
   * Loads the given process definition, giving it the passed in custom identifier.
   *
   * @param procDef The process definition to load
   * @param customId The custom identifier to give the loaded process definition
   *
   * @throws SarasvatiLoadException Thrown if an error occurs during load, or the loader
   *                                has a validator which throws an exception.
   */
  void loadDefinition (final ProcessDefinition procDef, final String customId) throws SarasvatiLoadException;

  /**
   * Returns true if the graph has been loaded into the associated
   * {@link GraphRepository}, false otherwise.
   *
   * @param name The name of the graph being searched for
   *
   * @return true if the graph has been loaded into the associated
   * {@link GraphRepository}, false otherwise.
   */
  boolean isLoaded (String name);

  /**
   * Loads all new and changed process definitions under the given base directory. Uses
   * the default file name filter, which matches all files with the .wf.xml extension.
   * <p>
   * Equivalent to loadNewAndChanged( baseDir, null );
   * <p>
   * @see GraphLoader#loadNewAndChanged(File, FilenameFilter)
   */
  List<LoadResult> loadNewAndChanged (File baseDir) throws SarasvatiLoadException;

  /**
   * Loads all new and changed process definitions under the given base directory that match
   * the given file name filter.
   * <p>
   * This method works as follows:
   *   <ol>
   *     <li>
   *       It finds and parse all process definitions under the given base directory
   *       that match the given filename filter.
   *     </li>
   *     <li>
   *       A SHA-1 hash is calculated for each process definition. This hash is based
   *       on the sorted contents of the process definition. Changes in the ordering
   *       of the file and changes to whitespace outside of elements containing text
   *       will not result in a hash change.
   *     </li>
   *     <li>
   *       The process definitions are sorted by dependency, that for any given process
   *       definition, its dependencies will be inspected first.
   *     </li>
   *     <li>
   *       The sorted set of process definitions will be inspected.
   *       <ul>
   *         <li>
   *           If a process definition is new, i.e. it does not exist in the {@link GraphRepository},
   *           it will be marked for load
   *         </li>
   *         <li>
   *           If a process definition is updated, i.e. the SHA-1 hash does not match the value in
   *           {@link Graph#getCustomId()} for the new graph of the same name in the repository,
   *           it will be marked for load.
   *         </li>
   *         <li>
   *           If a dependency (referenced external) of a process definition is marked for load,
   *           the process definition will also be marked for load.
   *         </li>
   *       </ol>
   *       <li>
   *         All process definitions marked for load will then be loaded, in dependency order.
   *       </li>
   *   </ol>
   * <p>
   * @param baseDir The directory under which to recursively search for process definitions
   *
   * @param filter The filename filter to use to find process definition files. May be null,
   *               in which case the default filter is used. The default filter matches all
   *               files with the .wf.xml extension.
   *
   * @throws SarasvatiLoadException Thrown if an error occurs during the load or if the
   *                                {@link GraphValidator} throws an exception.
   * @return The list of load results, which indicate which process definitions have been loaded
   *         and the reason why they were loaded (new, updated, dependency changed).
   */
  List<LoadResult> loadNewAndChanged (File baseDir, FilenameFilter filter)
    throws SarasvatiLoadException;

  /**
   * Takes a {@link ProcessDefinitionTranslator}, applies it to the given source and loads the resulting
   * {@link ProcessDefinition}.
   * <p>
   * Equivalent to loadDefinition( translator, source, null )
   * <p>
   * @see GraphLoader#loadDefinition(ProcessDefinitionTranslator, Object, String)
   **/
  <T> void loadDefinition (ProcessDefinitionTranslator<T> translator, T source) throws SarasvatiLoadException;

  /**
   * Takes a {@link ProcessDefinitionTranslator}, applies it to the given source and loads the resulting
   * {@link ProcessDefinition}.
   *
   * @param <T> The type of input that the process definition translator takes.
   *
   * @param translator Class which takes an input and returns a {@link ProcessDefinition}
   * @param source The input for the translator (usually an XML file).
   * @param customId The custom ID to give the graph. Example usage is a SHA-1 hash of the process definition.
   *
   * @throws SarasvatiLoadException Thrown if an error occurs during the load or if the
   *                                {@link GraphValidator} throws an exception.
   */
  <T> void loadDefinition (ProcessDefinitionTranslator<T> translator, T source, String customId)
    throws SarasvatiLoadException;

  /**
   * Loads the named process definition. The given resolver is used to find the process definition
   * and any of its dependencies that have not yet been loaded. The process definition is only
   * and its dependencies are only loaded if they are new, have changed or dependencies have changed.
   *
   * @param name The name of the process definition to load
   * @param resolver The resolver to use in finding the process definition and its dependencies.
   *
   * @return The list of load results, which indicate which process definitions have been loaded
   *         and the reason why they were loaded (new, updated, dependency changed).
   *
   * @throws SarasvatiLoadException Thrown if an error occurs during the load or if the
   *                                {@link GraphValidator} throws an exception.
   */
  List<LoadResult> loadWithDependencies (String name, ProcessDefinitionResolver resolver) throws SarasvatiLoadException;

  /**
   * Loads the given XML process definition file.
   * <p>
   * Equivalent to loadDefinition( new XmlLoader(), file );
   *
   * @param file The xml file to load
   * @throws SarasvatiLoadException Thrown if an error occurs during the load or if the
   *                                {@link GraphValidator} throws an exception.
   */
  void load (final File file) throws SarasvatiLoadException;
}