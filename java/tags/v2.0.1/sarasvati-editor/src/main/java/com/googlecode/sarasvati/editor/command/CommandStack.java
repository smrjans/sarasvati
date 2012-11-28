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
package com.googlecode.sarasvati.editor.command;

import java.awt.Point;
import java.util.LinkedList;

import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.model.ArcState;
import com.googlecode.sarasvati.editor.model.EditorArc;
import com.googlecode.sarasvati.editor.model.EditorExternal;
import com.googlecode.sarasvati.editor.model.EditorGraph;
import com.googlecode.sarasvati.editor.model.EditorGraphMember;
import com.googlecode.sarasvati.editor.model.EditorNode;
import com.googlecode.sarasvati.editor.model.EditorNodeType;
import com.googlecode.sarasvati.editor.model.EditorPreferences;
import com.googlecode.sarasvati.editor.model.EditorScene;
import com.googlecode.sarasvati.editor.model.ExternalState;
import com.googlecode.sarasvati.editor.model.GraphMemberState;
import com.googlecode.sarasvati.editor.model.GraphState;
import com.googlecode.sarasvati.editor.model.NodeState;

public class CommandStack
{
  private static CommandStack current = new CommandStack ();

  private final LinkedList<Command> commandStack = new LinkedList<Command>();
  private Command lastSavedCommand = null;

  private int currentIndex = -1;

  private int nodeCounter = 0;
  private int externalCounter = 0;

  public void pushCommand (final Command command)
  {
    currentIndex++;
    while ( currentIndex < commandStack.size() )
    {
      commandStack.removeLast();
    }
    commandStack.add( command );
    GraphEditor.getInstance().updateMenu();
  }

  public boolean canUndo ()
  {
    return currentIndex > -1;
  }

  public boolean canRedo ()
  {
    return currentIndex < ( commandStack.size() - 1 );
  }

  public void undo ()
  {
    if ( !canUndo() )
    {
      return;
    }

    Command command = commandStack.get( currentIndex );
    currentIndex--;
    command.undoAction();
    GraphEditor editor = GraphEditor.getInstance();
    editor.updateMenu();
    editor.getCurrentScene().validate();
  }

  public void redo ()
  {
    if ( !canRedo() )
    {
      return;
    }

    currentIndex++;
    Command command = commandStack.get( currentIndex );
    command.performAction();
    GraphEditor editor = GraphEditor.getInstance();
    editor.updateMenu();
    editor.getCurrentScene().validate();
  }

  public String getUndoName ()
  {
    return canUndo() ? commandStack.get( currentIndex ).getName() : "";
  }

  public String getRedoName ()
  {
    return canRedo() ? commandStack.get( currentIndex + 1 ).getName() : "";
  }

  public void saved ()
  {
    if ( currentIndex >= 0 )
    {
      lastSavedCommand = commandStack.get( currentIndex );
    }
    GraphEditor.getInstance().updateMenu();
  }

  public boolean isSaved ()
  {
    return commandStack.isEmpty() ||
           currentIndex < 0 ||
           ( currentIndex >= 0 && commandStack.get( currentIndex ) == lastSavedCommand);
  }

  public boolean isEmpty ()
  {
    return commandStack.isEmpty();
  }

  public static void markSaved ()
  {
    current.saved();
  }

  public static void graphMemberMoved (final EditorScene scene,
                                       final EditorGraphMember<?> member,
                                       final Point startLocation,
                                       final Point endLocation)
  {
    member.setOrigin( new Point( endLocation ) );
    current.pushCommand( new MoveGraphMemberCommand( scene, member, startLocation, endLocation ) );
  }

  public static void moveGraphMember (final EditorScene scene,
                                      final EditorGraphMember<?> member,
                                      final Point startLocation,
                                      final Point endLocation)
  {
    pushAndPerform( new MoveGraphMemberCommand( scene, member, startLocation, endLocation ) );
  }

  public static void addNode (final EditorScene scene,
                              final Point location)
  {
    current.nodeCounter++;
    EditorNodeType type = EditorPreferences.getInstance().getDefaultNodeType();
    NodeState state = new NodeState( "Node_" + current.nodeCounter,
                                     type == null ? "node" : type.getName(),
                                     JoinType.OR,
                                     null,
                                     false,
                                     null,
                                     null,
                                     true );
    EditorNode node = new EditorNode( state );

    pushAndPerform( new AddNodeCommand( scene, scene.convertLocalToScene( location ), node ) );
  }

  public static void addExternal (final EditorScene scene,
                                  final Point location)
  {
    current.externalCounter++;
    ExternalState state = new ExternalState( "External_" + current.externalCounter, "", null, true );
    EditorExternal external = new EditorExternal( state );

    pushAndPerform( new AddExternalCommand( scene, scene.convertLocalToScene( location ), external ) );
  }

  public static void addArc (final EditorScene scene,
                             final EditorArc arc)
  {
    pushAndPerform( new AddArcCommand( scene, arc ) );
  }

  public static void deleteArc (final EditorScene scene,
                                final EditorArc arc)
  {
    pushAndPerform( new DeleteArcCommand( scene, arc ) );
  }

  public static <T extends GraphMemberState> void editGraphMember (final EditorGraphMember<T> graphMember,
                                                                   final T newState)
  {
    pushAndPerform( new EditGraphMemberCommand<T>( graphMember, newState ) );
  }

  public static void editArc (final EditorArc arc,
                              final ArcState newState)
  {
    pushAndPerform( new EditArcCommand( arc, newState ) );
  }

  public static void editGraph (final EditorGraph graph,
                                final GraphState newState)
  {
    pushAndPerform( new EditGraphCommand( graph, newState ) );
  }

  public static void pushAndPerform (final Command command)
  {
    current.pushCommand( command );
    command.performAction();
    GraphEditor.getInstance().getCurrentScene().validate();
  }

  public static void updateArc (final EditorScene scene,
                                final EditorArc arc,
                                final boolean isSource,
                                final EditorGraphMember<?> newNode)
  {
    ChangeArcCommand command = new ChangeArcCommand( scene, arc, isSource, newNode );
    current.pushCommand( command );
    command.performAction();
  }

  public static CommandStack getCurrent ()
  {
    return current;
  }

  public static void setCurrent (final CommandStack stack)
  {
    current = stack;
  }
}