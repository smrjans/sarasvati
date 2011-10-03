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

    Copyright 2008-2009 Paul Lorenz
*/
package com.googlecode.sarasvati.editor.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.layout.LayoutFactory.ConnectionWidgetLayoutAlignment;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.action.ArcPropertiesAction;
import com.googlecode.sarasvati.editor.action.ArcSelectAction;
import com.googlecode.sarasvati.editor.action.ConnectAction;
import com.googlecode.sarasvati.editor.action.GraphMemberMoveAction;
import com.googlecode.sarasvati.editor.action.GraphMemberPropertiesAction;
import com.googlecode.sarasvati.editor.action.GraphMemberSelectAction;
import com.googlecode.sarasvati.editor.action.MoveTrackAction;
import com.googlecode.sarasvati.editor.action.ObjectSceneListenerAdapter;
import com.googlecode.sarasvati.editor.action.SceneAddExternalAction;
import com.googlecode.sarasvati.editor.action.SceneAddNodeAction;
import com.googlecode.sarasvati.editor.command.AddArcCommand;
import com.googlecode.sarasvati.editor.command.AddExternalCommand;
import com.googlecode.sarasvati.editor.command.AddNodeCommand;
import com.googlecode.sarasvati.editor.command.Command;
import com.googlecode.sarasvati.editor.command.CommandStack;
import com.googlecode.sarasvati.editor.command.DeleteArcCommand;
import com.googlecode.sarasvati.editor.command.DeleteExternalCommand;
import com.googlecode.sarasvati.editor.command.DeleteNodeCommand;
import com.googlecode.sarasvati.editor.command.MoveGraphMemberCommand;
import com.googlecode.sarasvati.editor.command.MultiCommand;
import com.googlecode.sarasvati.editor.command.MultiDeleteCommand;
import com.googlecode.sarasvati.editor.command.PasteCommand;
import com.googlecode.sarasvati.editor.dialog.DialogFactory;
import com.googlecode.sarasvati.editor.xml.XmlEditorProperties;
import com.googlecode.sarasvati.visual.common.GraphSceneImpl;
import com.googlecode.sarasvati.visual.common.NodeDrawConfig;
import com.googlecode.sarasvati.visual.common.PathTrackingConnectionWidget;
import com.googlecode.sarasvati.visual.graph.GraphLayoutNode;
import com.googlecode.sarasvati.visual.icon.NodeIcons;
import com.googlecode.sarasvati.visual.icon.OvalNodeIcon;

public class EditorScene extends GraphSceneImpl<EditorGraphMember<?>, EditorArc>
{
  protected static final Font ARC_LABEL_FONT = Font.decode( "serif bold 11" );

  protected final CommandStack commandStack;
  protected final EditorGraph graph;

  private final WidgetAction singleMoveAction = new MoveTrackAction( ActionFactory.createAlignWithMoveAction( mainLayer, intrLayer, null ) );

  @SuppressWarnings("synthetic-access")
  private final WidgetAction multiMoveAction = ActionFactory.createMoveAction( null, new MultiMoveProvider() );

  private final GraphMemberMoveAction moveAction = new GraphMemberMoveAction( singleMoveAction );

  private final WidgetAction connectAction = new ConnectAction( ActionFactory.createConnectAction( intrLayer, new SceneConnectProvider() ) );
  private final WidgetAction reconnectAction = ActionFactory.createReconnectAction( new SceneReconnectProvider() );

  private final WidgetAction graphMemberPopupMenuAction = ActionFactory.createPopupMenuAction( new GraphMemberPopupMenuProvider() );
  private final WidgetAction graphMemberPropertiesAction = new GraphMemberPropertiesAction();
  private final WidgetAction arcPropertiesAction = new ArcPropertiesAction();

  private final WidgetAction arcSelectAction = new ArcSelectAction( createSelectAction() );
  private final WidgetAction graphMemberSelectAction = new GraphMemberSelectAction( createSelectAction() );

  private boolean offsetAddedNodes = false;

  public EditorScene (final EditorGraph graph)
  {
    this.graph = graph;
    this.commandStack = new CommandStack();

    getActions().addAction( SceneAddNodeAction.INSTANCE );
    getActions().addAction( SceneAddExternalAction.INSTANCE );
    getActions().addAction( ActionFactory.createRectangularSelectAction( this, this.mainLayer ) );
    getActions().addAction( ActionFactory.createPopupMenuAction( new EditorPopupMenuProvider() ) );

    addObjectSceneListener( new ObjectSceneListenerAdapter()
    {
      @Override
      public void selectionChanged (final ObjectSceneEvent event,
                                    final Set<Object> previousSelection,
                                    final Set<Object> newSelection)
      {
        setGraphMemberSelection( previousSelection, false );
        setGraphMemberSelection( newSelection, true );
        GraphEditor.getInstance().updateCutCopyPaste( EditorScene.this );
      }
    },
    ObjectSceneEventType.OBJECT_SELECTION_CHANGED );

    for ( EditorGraphMember<?> member : graph.getNodes() )
    {
      addNode( member );
    }

    for ( EditorGraphMember<?> member : graph.getExternals() )
    {
      addNode( member );
    }

    for ( EditorArc arc : graph.getArcs() )
    {
      addEdge( arc );
      setEdgeSource( arc, arc.getStart() );
      setEdgeTarget( arc, arc.getEnd() );
    }

    offsetAddedNodes = true;
  }

  public void setGraphMemberSelection (final Set<Object> selection, final boolean selected)
  {
    int count = 0;
    for ( Object obj : selection )
    {
      if ( obj instanceof EditorGraphMember<?> )
      {
        ((EditorGraphMember<?>)obj).setSelected( selected );
        count++;
      }
    }

    if ( selected && count > 1 )
    {
      moveAction.setAction( multiMoveAction );
    }
    else
    {
      moveAction.setAction( singleMoveAction );
    }
  }

  public CommandStack getCommandStack ()
  {
    return commandStack;
  }

  public EditorGraph getGraph ()
  {
    return graph;
  }

  public void editCut ()
  {
    Clipboard.getInstance().setContents( getSelectedObjects() );
    removeSelected( getSelectedObjects(), "Cut" );
  }

  public void editCopy ()
  {
    Clipboard.getInstance().setContents( getSelectedObjects() );
    GraphEditor.getInstance().updateCutCopyPaste( this );
  }

  public void editDelete ()
  {
    removeSelected( getSelectedObjects(), "Delete" );
  }

  public void editPaste (final Point location)
  {
    int xOffset = Integer.MAX_VALUE;
    int yOffset = Integer.MAX_VALUE;

    Set<?> clipboard = Clipboard.getInstance().getContents();

    for ( Object o : clipboard )
    {
      if ( o instanceof EditorGraphMember<?> )
      {
        EditorGraphMember<?> member = (EditorGraphMember<?>)o;
        if ( member.getX() < xOffset )
        {
          xOffset = member.getX();
        }
        if (member.getY() < yOffset )
        {
          yOffset = member.getY();
        }
      }
    }

    xOffset = -xOffset + location.x;
    yOffset = -yOffset + location.y;

    Map<EditorGraphMember<?>, EditorGraphMember<?>> oldNewMap = new HashMap<EditorGraphMember<?>, EditorGraphMember<?>>();
    List<EditorArc> arcs = new LinkedList<EditorArc>();

    List<Command> commands = new ArrayList<Command>( clipboard.size() );

    Set<String> currentNodeNames = null;
    Set<String> currentExternalNames = null;

    for ( Object o : clipboard )
    {
      if ( o instanceof EditorNode )
      {
        if ( currentNodeNames == null )
        {
          currentNodeNames = graph.getCurrentNodeNames();
        }
        EditorNode node = (EditorNode)o;
        EditorNode newNode = new EditorNode( node.getState().copy( currentNodeNames ) );
        Point origin = new Point( node.getX() + xOffset, node.getY() + yOffset );
        oldNewMap.put( node, newNode );
        commands.add( new AddNodeCommand( this, origin, newNode ) );
        currentNodeNames.add( newNode.getName() );
      }
      else if ( o instanceof EditorExternal )
      {
        if ( currentExternalNames == null )
        {
          currentExternalNames = graph.getCurrentNodeNames();
        }
        EditorExternal external = (EditorExternal)o;
        EditorExternal newExternal = new EditorExternal( external.getState().copy( currentExternalNames ) );
        Point origin = new Point( external.getX() + xOffset, external.getY() + yOffset );
        oldNewMap.put( external, newExternal );
        commands.add( new AddExternalCommand( this, origin, newExternal ) );
        currentExternalNames.add( newExternal.getName() );
      }
      else if ( o instanceof EditorArc )
      {
        EditorArc arc = (EditorArc)o;
        if ( clipboard.contains( arc.getStart() ) &&
             clipboard.contains( arc.getEnd() ) )
        {
          arcs.add( arc );
        }
      }
    }

    for ( EditorArc arc : arcs )
    {
      EditorGraphMember<?> start = oldNewMap.get( arc.getStart() );
      EditorGraphMember<?> end = oldNewMap.get( arc.getEnd() );
      EditorArc newArc = new EditorArc( arc.getState(), start, end );
      commands.add( new AddArcCommand( this, newArc ) );
    }

    if ( !commands.isEmpty() )
    {
      CommandStack.pushAndPerform( new PasteCommand( this, commands ) );
    }
  }

  public boolean isOffsetAddedNodes ()
  {
    return offsetAddedNodes;
  }

  public void setOffsetAddedNodes (final boolean offsetAddedNodes)
  {
    this.offsetAddedNodes = offsetAddedNodes;
  }

  public void nodeTypesUpdated ()
  {
    for ( EditorNode node : graph.getNodes() )
    {
      node.stateChanged();
    }
  }

  public void removeSelected (final Set<?> selected, final String action)
  {
    List<Command> commands = new ArrayList<Command>( selected.size() );

    Set<EditorArc> arcs = new HashSet<EditorArc>();

    for ( Object obj : getSelectedObjects() )
    {
      if ( obj instanceof EditorNode )
      {
        commands.add( new DeleteNodeCommand( action, this, (EditorNode)obj ) );
      }
      else if ( obj instanceof EditorExternal )
      {
        commands.add( new DeleteExternalCommand( action, this, (EditorExternal)obj ) );
      }
      else if ( obj instanceof EditorArc )
      {
        EditorArc arc = (EditorArc)obj;
        commands.add( new DeleteArcCommand( this, arc ) );
        arcs.add( arc );
      }
    }

    for ( EditorArc arc : graph.getArcs() )
    {
      if ( !arcs.contains( arc ) &&
           (selected.contains( arc.getStart() ) ||
            selected.contains( arc.getEnd() ) ) )
      {
        commands.add( new DeleteArcCommand( this, arc ) );
        arcs.add( arc );
      }
    }

    Collections.sort( commands );

    CommandStack.pushAndPerform( new MultiDeleteCommand( action, this, commands ) );
  }

  @Override
  protected PathTrackingConnectionWidget attachEdgeWidget (final EditorArc arc)
  {
    final PathTrackingConnectionWidget widget = super.attachEdgeWidget( arc );
    widget.setEndPointShape (PointShape.SQUARE_FILLED_BIG);
    widget.getActions().addAction( createObjectHoverAction() );
    widget.getActions().addAction( arcSelectAction );
    widget.getActions().addAction( reconnectAction );
    widget.getActions().addAction( arcPropertiesAction );

    final LabelWidget arcLabel = new LabelWidget( this, arc.getState().getLabel() );
    arcLabel.setFont( ARC_LABEL_FONT );
    arcLabel.setForeground( Color.BLUE );
    arcLabel.setOpaque( true );
    widget.addChild( arcLabel );
    widget.setConstraint( arcLabel, ConnectionWidgetLayoutAlignment.CENTER_SOURCE, 20 );

    arc.addListener( new ModelListener<EditorArc>()
    {
      @Override
      public void modelChanged (final EditorArc modelInstance)
      {
        arcLabel.setLabel( modelInstance.getState().getLabel() );
      }
    });

    return widget;
  }

  protected Icon getIconForMember (final EditorGraphMember<?> graphMember)
  {
    if ( graphMember instanceof EditorExternal )
    {
      EditorExternal external = (EditorExternal)graphMember;
      return new OvalNodeIcon( external.getState().getName(),
                               NodeDrawConfig.NODE_BG_DISCARDED,
                               false,
                               external.isSelected() );
    }

    EditorNode node = (EditorNode)graphMember;
    NodeState state = node.getState();
    EditorNodeType type = state.getEditorNodeType();

    return NodeIcons.newInstance( type.getNodeIconType(),
                                  state.getName(),
                                  type.getIconColor(),
                                  state.getJoinType() != JoinType.OR,
                                  node.isSelected() );
  }

  @Override
  protected Widget widgetForNode (final EditorGraphMember<?> graphMember)
  {
    final Icon icon = getIconForMember( graphMember );

    final JLabel label = new JLabel( icon );
    final ComponentWidget widget = new ComponentWidget( this, label );

    if ( offsetAddedNodes )
    {
      int xOffset = icon.getIconWidth() >> 1;
      int yOffset = icon.getIconHeight() >> 1;

      graphMember.setX( graphMember.getX() - xOffset );
      graphMember.setY( graphMember.getY() - yOffset );
    }

    widget.setPreferredLocation( graphMember.getOrigin() );

    widget.getActions().addAction( graphMemberSelectAction );
    widget.getActions().addAction( graphMemberPopupMenuAction );
    widget.getActions().addAction( graphMemberPropertiesAction );
    widget.getActions().addAction( moveAction );
    widget.getActions().addAction( connectAction );

    graphMember.addListener( new ModelListener<EditorGraphMember<?>> ()
    {
      @Override
      public void modelChanged (final EditorGraphMember<?> modelInstance)
      {
        label.setIcon( getIconForMember( graphMember ) );
      }
    });

    return widget;
  }

  public class SceneConnectProvider implements ConnectProvider
  {
    private EditorGraphMember<?> source = null;
    private EditorGraphMember<?> target = null;

    @Override
    public boolean isSourceWidget (final Widget sourceWidget)
    {
      Object object = findObject (sourceWidget);
      source = isNode (object) ? (EditorGraphMember<?>) object : null;
      return source != null;
    }

    @Override
    public ConnectorState isTargetWidget (final Widget sourceWidget, final Widget targetWidget)
    {
      Object object = findObject (targetWidget);
      target = isNode (object) ? (EditorGraphMember<?>) object : null;
      if (target != null)
      {
        return ConnectorState.ACCEPT;
      }
      return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver (final Scene scene)
    {
      return false;
    }

    @Override
    public Widget resolveTargetWidget (final Scene scene, final Point sceneLocation)
    {
      return null;
    }

    @Override
    public void createConnection (final Widget sourceWidget, final Widget targetWidget)
    {
      String arcLabel = sourceWidget == targetWidget ? EditorPreferences.getInstance().getDefalutSelfArcsLabel() : null;

      String startNode = null;
      String endNode   = null;

      if ( source.isExternal() )
      {
        XmlEditorProperties editorProps = source.asExternal().getEditorProperties();
        if ( editorProps != null )
        {
          startNode = editorProps.getDefaultNodeForOutgoingArcs();
        }
      }

      if ( target.isExternal() )
      {
        XmlEditorProperties editorProps = target.asExternal().getEditorProperties();
        if ( editorProps != null )
        {
          endNode = editorProps.getDefaultNodeForIncomingArcs();
        }
      }

      CommandStack.addArc( EditorScene.this, new EditorArc( new ArcState( arcLabel, startNode, endNode ), source, target ) );
    }
  }

  public class SceneReconnectProvider implements ReconnectProvider
  {
    private EditorArc arc;
    private EditorGraphMember<?> originalNode;
    private EditorGraphMember<?> replacementNode;

    @Override
    public void reconnectingStarted (final ConnectionWidget connectionWidget,
                                     final boolean reconnectingSource)
    {
      // does nothing
    }

    @Override
    public void reconnectingFinished (final ConnectionWidget connectionWidget,
                                      final boolean reconnectingSource)
    {
      // does nothing
    }

    @Override
    public boolean isSourceReconnectable (final ConnectionWidget connectionWidget)
    {
      Object object = findObject (connectionWidget);
      arc = isEdge (object) ? (EditorArc) object : null;
      originalNode = arc != null ? getEdgeSource (arc) : null;
      return originalNode != null;
    }

    @Override
    public boolean isTargetReconnectable (final ConnectionWidget connectionWidget)
    {
      Object object = findObject (connectionWidget);
      arc = isEdge (object) ? (EditorArc) object : null;
      originalNode = arc != null ? getEdgeTarget (arc) : null;
      return originalNode != null;
    }

    @Override
    public ConnectorState isReplacementWidget (final ConnectionWidget connectionWidget,
                                               final Widget replacementWidget,
                                               final boolean reconnectingSource)
    {
      Object object = findObject (replacementWidget);
      replacementNode = isNode (object) ? (EditorGraphMember<?>) object : null;
      if (replacementNode != null)
      {
        return ConnectorState.ACCEPT;
      }
      return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomReplacementWidgetResolver (final Scene scene)
    {
        return false;
    }

    @Override
    public Widget resolveReplacementWidget (final Scene scene, final Point sceneLocation)
    {
        return null;
    }

    @Override
    public void reconnect (final ConnectionWidget connectionWidget,
                           final Widget replacementWidget,
                           final boolean reconnectingSource)
    {
      if (replacementWidget == null)
      {
        CommandStack.deleteArc( EditorScene.this, arc );
      }
      else if (reconnectingSource)
      {
        CommandStack.updateArc( EditorScene.this, arc, true, replacementNode );
      }
      else
      {
        CommandStack.updateArc( EditorScene.this, arc, false, replacementNode );
      }
    }
  }

  public void autoLayout ()
  {
    EditorGraphLayoutTree layoutTree = new EditorGraphLayoutTree( graph );

    List<Command> commands = new LinkedList<Command>();

    for ( EditorGraphMember<?> member : graph.getNodes() )
    {
      GraphLayoutNode<?> layoutNode = layoutTree.getTreeNode( member );
      Point newOrigin = new Point( layoutNode.getOriginX(), layoutNode.getOriginY() );
      commands.add( new MoveGraphMemberCommand( this, member, member.getOrigin(), newOrigin ) );
    }

    for ( EditorGraphMember<?> member : graph.getExternals() )
    {
      GraphLayoutNode<?> layoutNode = layoutTree.getTreeNode( member );
      Point newOrigin = new Point( layoutNode.getOriginX(), layoutNode.getOriginY() );
      commands.add( new MoveGraphMemberCommand( this, member, member.getOrigin(), newOrigin ) );
    }

    CommandStack.pushAndPerform( new MultiCommand( "Auto-Layout", this, commands ) );
  }

  public class EditorSelectProvider implements SelectProvider
  {
    @Override
    public boolean isAimingAllowed (final Widget widget,
                                    final Point localLocation,
                                    final boolean invertSelection)
    {
      return true;
    }

    @Override
    public boolean isSelectionAllowed (final Widget widget,
                                       final Point localLocation,
                                       final boolean invertSelection)
    {
      return true;
    }

    @Override
    public void select (final Widget widget,
                        final Point localLocation,
                        final boolean invertSelection)
    {
      if ( invertSelection )
      {
        System.out.println( "Deselected: " + findObject( widget ) );
      }
      else
      {
        System.out.println( "Selected: " + findObject( widget  ) );
      }
    }
  }

  private class MultiMoveProvider implements MoveProvider
  {
    private Map<Widget, Point> originals = new HashMap<Widget, Point>();
    private Point              original;

    @Override
    public void movementStarted (final Widget widget)
    {
      Object object = findObject( widget );
      if ( isNode( object ) )
      {
        for ( Object o : getSelectedObjects() )
        {
          if ( isNode( o ) )
          {
            Widget w = findWidget( o );
            if ( w != null ) originals.put( w, w.getPreferredLocation() );
          }
        }
      }
      else
      {
        originals.put( widget, widget.getPreferredLocation() );
      }
    }

    @Override
    public void movementFinished (final Widget widget)
    {
      List<Command> commands = new ArrayList<Command>( originals.size() );
      for ( Map.Entry<Widget, Point> entry : originals.entrySet() )
      {
        Point startLocation = new Point( entry.getValue() );
        Point endLocation   = new Point( entry.getKey().getLocation() );

        EditorGraphMember<?> member = (EditorGraphMember<?>) findObject( entry.getKey() );
        member.setOrigin( endLocation );
        commands.add( new MoveGraphMemberCommand( EditorScene.this, member, startLocation, endLocation ) );
      }

      commandStack.pushCommand( new MultiCommand( "Move", EditorScene.this, commands ) );

      originals.clear();
      original = null;
    }

    @Override
    public Point getOriginalLocation (final Widget widget)
    {
      original = widget.getPreferredLocation();
      return original;
    }

    @Override
    public void setNewLocation (final Widget widget, final Point location)
    {
      int dx = location.x - original.x;
      int dy = location.y - original.y;
      for ( Map.Entry<Widget, Point> entry : originals.entrySet() )
      {
        Point point = entry.getValue();
        entry.getKey().setPreferredLocation( new Point( point.x + dx, point.y + dy ) );
      }
    }
  }

  protected class EditorPopupMenuProvider implements PopupMenuProvider
  {
    @Override
    public JPopupMenu getPopupMenu (final Widget widget,
                                    final Point localLocation)
    {
      JPopupMenu menu = new JPopupMenu();
      menu.setLocation( localLocation );

      Action addNodeAction = new AbstractAction( "Add Node" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          setOffsetAddedNodes( false );
          CommandStack.addNode( EditorScene.this, localLocation );
          setOffsetAddedNodes( true );
        }
      };

      Action addExternalAction = new AbstractAction( "Add External" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          setOffsetAddedNodes( false );
          CommandStack.addExternal( EditorScene.this, localLocation );
          setOffsetAddedNodes( true );
        }
      };

      Action pasteAction = new AbstractAction( "Paste" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          editPaste( localLocation );
        }
      };

      Action editPropertiesAction = new AbstractAction( "Properties" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          JDialog dialog = DialogFactory.newGraphPropertiesDialog( graph );
          dialog.setLocation( widget.convertLocalToScene( localLocation ) );
          dialog.setVisible( true );
        }
      };

      pasteAction.setEnabled( Clipboard.getInstance().isClipboardPasteable() );

      menu.add( addNodeAction );
      menu.add( addExternalAction );
      menu.add( pasteAction );
      menu.add( editPropertiesAction );
      return menu;
    }
  }

  protected class GraphMemberPopupMenuProvider implements PopupMenuProvider
  {
    @Override
    public JPopupMenu getPopupMenu (final Widget widget,
                                    final Point localLocation)
    {
      JPopupMenu menu = new JPopupMenu();
      menu.setLocation( localLocation );

      final EditorGraphMember<?> graphMember = (EditorGraphMember< ? >)findObject( widget );

      String type = null;

      if ( graphMember instanceof EditorNode )
      {
        type = "Node";
      }
      else if ( graphMember instanceof EditorExternal )
      {
        type = "External";
      }

      Action deleteAction = new AbstractAction( "Delete" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          editDelete();
        }
      };

      Action cutAction = new AbstractAction( "Cut" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          editCut();
        }
      };

      Action copyAction = new AbstractAction( "Copy " )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          editCopy();
        }
      };

      Action editPropertiesAction = new AbstractAction( "Edit " + type + " Properties" )
      {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed (final ActionEvent e)
        {
          JDialog dialog = DialogFactory.newGraphMemberPropertiesDialog( graphMember );
          dialog.setLocation( widget.convertLocalToScene( localLocation ) );
          dialog.setVisible( true );
        }
      };

      menu.add( deleteAction );
      menu.add( cutAction );
      menu.add( copyAction );
      menu.add( editPropertiesAction );

      if ( graphMember instanceof EditorExternal )
      {
        EditorExternal external = (EditorExternal)graphMember;
        final Library library = Library.getInstance();
        final LibraryEntry entry = library.getEntry( external.getState().getGraphName() );

        Action openExternal = new AbstractAction( "Open External" )
        {
          private static final long serialVersionUID = 1L;

          @Override
          public void actionPerformed (final ActionEvent e)
          {
            GraphEditor.getInstance().giveFocusOrOpen( entry.getName(), entry.getPath() );
          }
        };

        if ( entry == null )
        {
          openExternal.setEnabled( false );
        }

        menu.add( openExternal );
      }

      return menu;
    }
  }

}