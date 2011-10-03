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
package com.googlecode.sarasvati.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import com.googlecode.sarasvati.editor.action.ArcSelectAction;
import com.googlecode.sarasvati.editor.action.ConnectAction;
import com.googlecode.sarasvati.editor.action.GraphMemberMoveAction;
import com.googlecode.sarasvati.editor.action.GraphMemberSelectAction;
import com.googlecode.sarasvati.editor.action.SceneAddExternalAction;
import com.googlecode.sarasvati.editor.action.SceneAddNodeAction;
import com.googlecode.sarasvati.editor.command.CommandStack;
import com.googlecode.sarasvati.editor.dialog.DialogFactory;
import com.googlecode.sarasvati.editor.menu.CloseAction;
import com.googlecode.sarasvati.editor.menu.ConvertLibraryAction;
import com.googlecode.sarasvati.editor.menu.CopyAction;
import com.googlecode.sarasvati.editor.menu.CutAction;
import com.googlecode.sarasvati.editor.menu.DeleteAction;
import com.googlecode.sarasvati.editor.menu.ExitAction;
import com.googlecode.sarasvati.editor.menu.ExportPreferencesAction;
import com.googlecode.sarasvati.editor.menu.ImportPreferencesAction;
import com.googlecode.sarasvati.editor.menu.NewGraphAction;
import com.googlecode.sarasvati.editor.menu.OpenAction;
import com.googlecode.sarasvati.editor.menu.OpenFromLibraryAction;
import com.googlecode.sarasvati.editor.menu.PasteAction;
import com.googlecode.sarasvati.editor.menu.PreferencesAction;
import com.googlecode.sarasvati.editor.menu.RedoAction;
import com.googlecode.sarasvati.editor.menu.SaveAction;
import com.googlecode.sarasvati.editor.menu.UndoAction;
import com.googlecode.sarasvati.editor.model.Clipboard;
import com.googlecode.sarasvati.editor.model.EditorGraph;
import com.googlecode.sarasvati.editor.model.EditorGraphFactory;
import com.googlecode.sarasvati.editor.model.EditorPreferences;
import com.googlecode.sarasvati.editor.model.EditorScene;
import com.googlecode.sarasvati.editor.model.GraphState;
import com.googlecode.sarasvati.editor.model.Library;
import com.googlecode.sarasvati.editor.model.ValidationResults;
import com.googlecode.sarasvati.editor.xml.EditorXmlLoader;
import com.googlecode.sarasvati.editor.xml.XmlEditorProperties;
import com.googlecode.sarasvati.load.SarasvatiLoadException;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.xml.XmlLoader;

public class GraphEditor
{
  private static final String GRAPH_NAME_KEY = "graphName";
  private static final GraphEditor INSTANCE = new GraphEditor ();

  private enum SaveResult
  {
    SaveCanceled( true ),
    SaveFailed( true ),
    SaveNotWanted( false ),
    SaveSucceeded( false );

    private boolean abortExit;

    private SaveResult (final boolean abortExit)
    {
      this.abortExit = abortExit;
    }

    public boolean isAbortExit ()
    {
      return abortExit;
    }
  }

  public static GraphEditor getInstance ()
  {
    return INSTANCE;
  }

  protected XmlLoader   xmlLoader;
  protected EditorXmlLoader editorXmlLoader;
  protected JFrame      mainWindow;
  protected JPanel      mainPanel;
  protected JToolBar    toolBar;
  protected JTabbedPane tabPane;

  protected JToggleButton moveButton;
  protected JToggleButton editArcsButton;
  protected JToggleButton addNodesButton;
  protected JToggleButton addExternalsButton;

  protected SaveAction    saveAction;
  protected SaveAction    saveAsAction;
  protected CloseAction   closeAction;

  protected DeleteAction  deleteAction;
  protected CutAction     cutAction;
  protected CopyAction    copyAction;
  protected PasteAction   pasteAction;
  protected UndoAction    undoAction;
  protected RedoAction    redoAction;

  protected EditorMode  mode;
  protected File        lastFile;

  private GraphEditor () throws SarasvatiLoadException
  {
    xmlLoader = new XmlLoader();
    editorXmlLoader = new EditorXmlLoader();
  }

  public JFrame getMainWindow ()
  {
    return mainWindow;
  }

  public EditorMode getMode ()
  {
    return mode;
  }

  public XmlLoader getXmlLoader ()
  {
    return xmlLoader;
  }

  public EditorXmlLoader getEditorXmlLoader ()
  {
    return editorXmlLoader;
  }

  public void setMode (final EditorMode mode)
  {
    if ( this.mode == mode )
    {
      return;
    }

    GraphMemberMoveAction.setEnabled( false );
    SceneAddNodeAction.setEnabled( false );
    SceneAddExternalAction.setEnabled( false );
    ConnectAction.setEnabled( false );
    ArcSelectAction.setEnabled( false );
    GraphMemberSelectAction.setEnabled( false );

    moveButton.setSelected( false );
    addNodesButton.setSelected( false );
    addExternalsButton.setSelected( false );
    editArcsButton.setSelected( false );

    if ( mode == EditorMode.AddNode )
    {
      SceneAddNodeAction.setEnabled( true );
      addNodesButton.setSelected( true );
    }
    else if ( mode == EditorMode.AddExternal )
    {
      SceneAddExternalAction.setEnabled( true );
      addExternalsButton.setSelected( true );
    }
    else if ( mode == EditorMode.EditArcs )
    {
      ConnectAction.setEnabled( true );
      ArcSelectAction.setEnabled( true );
      editArcsButton.setSelected( true );

    }
    else if ( mode == EditorMode.Move )
    {
      GraphMemberMoveAction.setEnabled( true );
      GraphMemberSelectAction.setEnabled( true );
      ArcSelectAction.setEnabled( true );
      moveButton.setSelected( true );
    }

    this.mode = mode;
  }

  public File getLastFile ()
  {
    return lastFile;
  }

  public void setLastFile (final File lastFile)
  {
    this.lastFile = lastFile;
  }

  protected void setup ()
  {
    mainWindow = new JFrame( "Sarasvati Graph Editor" );
    mainWindow.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
    mainWindow.addWindowListener( new WindowAdapter()
    {
      @Override
      public void windowClosing (final WindowEvent e)
      {
        exitRequested();
      }
    });

    mainWindow.setMinimumSize( new Dimension( 800, 600 ) );
    mainWindow.setJMenuBar( createMenu() );

    mainWindow.setVisible( true );

    DialogFactory.setFrame( mainWindow );

    toolBar = new JToolBar( "Tools" );
    toolBar.setFloatable( true );

    moveButton = new JToggleButton( "Move" );
    moveButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed (final ActionEvent e)
      {
        setMode( EditorMode.Move );
      }
    });

    toolBar.add( moveButton );

    editArcsButton = new JToggleButton( "Edit Arcs" );
    editArcsButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed (final ActionEvent e)
      {
        setMode( EditorMode.EditArcs );
      }
    });

    toolBar.add( editArcsButton );

    addNodesButton = new JToggleButton( "Add Nodes" );
    addNodesButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed (final ActionEvent e)
      {
        setMode( EditorMode.AddNode );
      }
    });

    toolBar.add( addNodesButton );

    addExternalsButton = new JToggleButton( "Add Externals" );
    addExternalsButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed (final ActionEvent e)
      {
        setMode( EditorMode.AddExternal );
      }
    });

    toolBar.add( addExternalsButton );

    final JButton autoLayoutButton = new JButton( "Auto-Layout" );
    autoLayoutButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed (final ActionEvent e)
      {
        getCurrentScene().autoLayout();
      }
    });

    toolBar.add( autoLayoutButton );

    tabPane = new JTabbedPane( SwingConstants.TOP );
    tabPane.setTabLayoutPolicy( JTabbedPane.WRAP_TAB_LAYOUT );

    tabPane.addChangeListener( new ChangeListener()
    {
      @Override
      public void stateChanged (final ChangeEvent e)
      {
        tabSelectionChanged();
      }
    });

    mainPanel = new JPanel ();
    mainPanel.setLayout( new BorderLayout() );

    mainPanel.add( toolBar, BorderLayout.PAGE_START );
    mainPanel.add( tabPane, BorderLayout.CENTER );

    mainWindow.setContentPane( mainPanel );

    setupModeKeys();

    createNewProcessDefinition();
    tabSelectionChanged();
    setMode( EditorMode.Move );

    final EditorPreferences prefs = EditorPreferences.getInstance();
    prefs.loadPreferences();

    if ( prefs.isFirstRun() )
    {
      DialogFactory.showInfo( "This is the first time the Sarasvati editor has been run. \n" +
                              "You may wish to configure a process definition library and custom node types" );

      final JDialog dialog = DialogFactory.newPreferencesDialog();
      dialog.setVisible( true );
    }
  }

  protected JMenuBar createMenu ()
  {
    final JMenuBar menuBar = new JMenuBar();

    final JMenu fileMenu = new JMenu( "File" );
    fileMenu.setMnemonic( KeyEvent.VK_F );

    saveAction = new SaveAction( false );
    saveAsAction = new SaveAction( true );
    closeAction = new CloseAction();

    fileMenu.add( new JMenuItem( new NewGraphAction() ) );
    fileMenu.add( new JMenuItem( new OpenAction() ) );
    fileMenu.add( new JMenuItem( new OpenFromLibraryAction() ) );
    fileMenu.add( new JMenuItem( saveAsAction ) );
    fileMenu.add( new JMenuItem( saveAction ) );
    fileMenu.addSeparator();
    fileMenu.add( closeAction );
    fileMenu.addSeparator();
    fileMenu.add( new JMenuItem( new ExitAction() ) );

    final JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );

    deleteAction = new DeleteAction();
    cutAction    = new CutAction();
    copyAction   = new CopyAction();
    pasteAction  = new PasteAction();
    undoAction   = new UndoAction();
    redoAction   = new RedoAction();

    editMenu.add( new JMenuItem( deleteAction ) );
    editMenu.add( new JMenuItem( cutAction ) );
    editMenu.add( new JMenuItem( copyAction ) );
    editMenu.add( new JMenuItem( pasteAction ) );
    editMenu.addSeparator();
    editMenu.add( new JMenuItem( undoAction ) );
    editMenu.add( new JMenuItem( redoAction ) );
    editMenu.addSeparator();
    editMenu.add( new JMenuItem( new PreferencesAction() ) );

    final JMenu toolsMenu = new JMenu( "Tools" );
    toolsMenu.setMnemonic( KeyEvent.VK_T );

    toolsMenu.add( new JMenuItem( new ExportPreferencesAction() ) );
    toolsMenu.add( new JMenuItem( new ImportPreferencesAction() ) );
    toolsMenu.add( new JMenuItem( new ConvertLibraryAction() ) );

    menuBar.add( fileMenu );
    menuBar.add( editMenu );
    menuBar.add( toolsMenu );

    return menuBar;
  }

  public void createNewProcessDefinition ()
  {
    final JScrollPane scrollPane = new JScrollPane();

    final EditorScene scene = new EditorScene( new EditorGraph( new GraphState() ) );
    scrollPane.setViewportView( scene.createView() );
    scrollPane.putClientProperty( "scene", scene );
    scrollPane.putClientProperty( GRAPH_NAME_KEY, "Untitled" );
    addTab( "Untitled", scrollPane );
  }

  private void addTab (final String name, final JComponent component)
  {
    tabPane.addTab( name, component );
    tabPane.setTabComponentAt( tabPane.getTabCount() - 1, new TabComponent( tabPane, name ) );
    tabPane.setSelectedComponent( component );
  }

  public void giveFocusOrOpen (final String name, final File path)
  {
    for ( final Component child : tabPane.getComponents() )
    {
      final JComponent pane = (JComponent)child;
      final String title = (String)pane.getClientProperty( GRAPH_NAME_KEY );
      if ( SvUtil.equals( title, name ) )
      {
        tabPane.setSelectedComponent( pane );
        return;
      }
    }

    openProcessDefinition( path );
  }

  public void openProcessDefinition (final File processDefinitionFile)
  {
    if ( getCurrentScene() != null &&
         getCurrentScene().getGraph().getFile() == null &&
         CommandStack.getCurrent().isEmpty() )
    {
      closeCurrentTab();
    }

    try
    {
      final ProcessDefinition xmlProcDef = xmlLoader.translate( processDefinitionFile );

      final File editorPropsFile = new File( processDefinitionFile.getParentFile(), xmlProcDef.getName() + ".editor.xml" );
      XmlEditorProperties xmlEditorProps = null;
      if ( editorPropsFile.exists() && editorPropsFile.canRead() )
      {
        xmlEditorProps = editorXmlLoader.loadEditorProperties( editorPropsFile );
      }
      final EditorGraph graph = EditorGraphFactory.loadFromXml( xmlProcDef, xmlEditorProps );
      graph.setFile( processDefinitionFile );
      final EditorScene scene = new EditorScene( graph );

      final JScrollPane scrollPane = new JScrollPane();
      scrollPane.setViewportView( scene.createView() );
      addTab( graph.getName(), scrollPane );

      scrollPane.putClientProperty( "scene", scene );
      scrollPane.putClientProperty( GRAPH_NAME_KEY, graph.getName() );
      tabSelectionChanged();

      Library.getInstance().updateIfNotExists(xmlProcDef, xmlEditorProps, processDefinitionFile);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
      JOptionPane.showMessageDialog( mainWindow, e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE );
    }
  }

  public void setupModeKeys ()
  {
    final EditorKeyListener keyListener = new EditorKeyListener();
    final KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    keyboardFocusManager.addKeyEventDispatcher( new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent (final KeyEvent event)
      {
        final Window window = keyboardFocusManager.getActiveWindow();

        if ( window == mainWindow )
        {
          if ( event.getID() == KeyEvent.KEY_PRESSED )
          {
            keyListener.keyPressed( event );
          }
          else if ( event.getID() == KeyEvent.KEY_RELEASED )
          {
            keyListener.keyReleased( event );
          }
        }
        return false;
      }
    });
  }

  public SaveResult saveRequested (final boolean isSaveAs)
  {
    final EditorScene scene = getCurrentScene();

    if ( scene == null )
    {
      return null;
    }

    final EditorGraph graph = scene.getGraph();

    final ValidationResults results = graph.validateGraph();

    StringBuilder buf = new StringBuilder ();
    for ( final String msg : results.getErrors() )
    {
      buf.append( "ERROR: " + msg );
      buf.append( "\n" );
    }
    for ( final String msg : results.getWarnings() )
    {
      buf.append( "WARNING: " + msg );
      buf.append( "\n" );
    }
    for ( final String msg : results.getInfos() )
    {
      buf.append( "WARNING: " + msg );
      buf.append( "\n" );
    }

    if ( results.hasErrors() )
    {
      JOptionPane.showMessageDialog( GraphEditor.getInstance().getMainWindow(),
                                     buf.toString(),
                                     "Invalid Process Definition",
                                     JOptionPane.ERROR_MESSAGE );
      return SaveResult.SaveFailed;
    }

    if ( results.hasWarnings() )
    {
      buf.append( "\nYour process definition triggered validation warnings. Do you still wish to save?" );
      if ( JOptionPane.OK_OPTION !=
             JOptionPane.showConfirmDialog( mainWindow,
                                            buf.toString(),
                                            "Warning",
                                            JOptionPane.WARNING_MESSAGE ) )
      {
        return SaveResult.SaveFailed;
      }

      buf = new StringBuilder();
    }

    if ( isSaveAs || scene.getGraph().getFile() == null )
    {
      File basePath = Library.getInstance().getBasePath();

      if ( basePath == null )
      {
        basePath = getLastFile();
      }

      final JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory( basePath );
      fileChooser.setFileFilter( new FileFilter()
      {
        @Override
        public String getDescription ()
        {
          return "Process Definitions";
        }

        @Override
        public boolean accept (final File f)
        {
          return f.isDirectory() || f.getName().endsWith( ".wf.xml" );
        }
      });

      final int retVal = fileChooser.showSaveDialog( mainWindow );

      if ( retVal == JFileChooser.APPROVE_OPTION )
      {
        setLastFile( fileChooser.getSelectedFile() );
        return saveProcessDefinition( graph, fileChooser.getSelectedFile(), buf.toString() );
      }

      return SaveResult.SaveCanceled;
    }

    return saveProcessDefinition( graph, graph.getFile(), buf.toString() );
  }

  private File getEditorPropertiesFile (final File graphFile)
  {
    String name = graphFile.getName();
    final int firstDot = name.indexOf( '.' );

    if ( firstDot > 0 )
    {
      name = name.substring( 0, firstDot );
    }

    return new File( graphFile.getParentFile(), name + ".editor.xml" );
  }

  /**
   * Loads the editor properties associated with the given graph definition file
   *
   * @param graphFile The file containing the graph definition
   * @return The {@link XmlEditorProperties} associated with the given graph definition file
   */
  public XmlEditorProperties loadEditorProperties (final File graphFile)
  {
    return getEditorXmlLoader().loadEditorProperties( getEditorPropertiesFile( graphFile ) );
  }

  public SaveResult saveProcessDefinition (final EditorGraph graph,
                                           final File outputFile,
                                           final String infoMessages)
  {
    File saveFile = null;

    String name = outputFile.getName();

    final int firstDot = name.indexOf( '.' );

    if ( firstDot > 0 )
    {
      name = name.substring( 0, firstDot );
      saveFile = outputFile;
    }
    else
    {
      saveFile = new File( outputFile.getParentFile(), name + ".wf.xml" );
    }

    graph.setName( name );
    final JComponent c = (JComponent)tabPane.getSelectedComponent();
    c.putClientProperty( GRAPH_NAME_KEY, name );
    updateTabTitle( tabPane.getSelectedIndex(), name );

    try
    {
      final EditorGraphFactory.XmlSaveData saveData = EditorGraphFactory.exportToXml( graph );
      xmlLoader.saveProcessDefinition( saveData.getXmlProcDef(), saveFile );
      editorXmlLoader.saveEditorProperties( saveData.getXmlEditorProps(), new File( saveFile.getParentFile(), name + ".editor.xml" ) );
      graph.setFile( saveFile );
      CommandStack.markSaved();

      JOptionPane.showMessageDialog( mainWindow,
                                     infoMessages + "\nProcess definition successfully saved to: '" + saveFile.getPath() + "'",
                                     "Save", JOptionPane.INFORMATION_MESSAGE );

      Library.getInstance().update( saveData.getXmlProcDef(), saveData.getXmlEditorProps(), saveFile );

      return SaveResult.SaveSucceeded;
    }
    catch ( final Exception e )
    {
      e.printStackTrace();
      JOptionPane.showMessageDialog( mainWindow, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE );
      return SaveResult.SaveFailed;
    }
  }

  public EditorScene getCurrentScene ()
  {
    final JComponent c = (JComponent)tabPane.getSelectedComponent();
    return c != null ? (EditorScene)c.getClientProperty( "scene" ) : null;
  }

  public void tabSelectionChanged ()
  {
    final EditorScene current = getCurrentScene();
    CommandStack.setCurrent( current == null ? null : current.getCommandStack() );
    updateMenu();
  }

  public void updateMenu ()
  {
    final CommandStack currentCommandStack = CommandStack.getCurrent();

    if ( currentCommandStack != null )
    {
      undoAction.setEnabled( currentCommandStack.canUndo() );
      redoAction.setEnabled( currentCommandStack.canRedo() );
      closeAction.setEnabled( true );
    }
    else
    {
      undoAction.setEnabled( false );
      redoAction.setEnabled( false );
      saveAction.setEnabled( false );
      saveAsAction.setEnabled( false );
      closeAction.setEnabled( false );
    }

    if ( undoAction.isEnabled() && currentCommandStack != null )
    {
      undoAction.setName( "Undo: " + currentCommandStack.getUndoName() );
    }
    else
    {
      undoAction.setName( "Undo" );
    }

    if ( redoAction.isEnabled() && currentCommandStack != null )
    {
      redoAction.setName( "Redo: " + currentCommandStack.getRedoName() );
    }
    else
    {
      redoAction.setName( "Redo" );
    }

    if ( currentCommandStack != null )
    {
      final boolean isUnSaved = !currentCommandStack.isSaved();
      saveAction.setEnabled( isUnSaved );
      saveAsAction.setEnabled( true );

      final JComponent c = (JComponent)tabPane.getSelectedComponent();
      final String title = (String)c.getClientProperty( GRAPH_NAME_KEY );
      if ( isUnSaved )
      {
        updateTabTitle( tabPane.getSelectedIndex(), "*" + title );
      }
      else
      {
        updateTabTitle( tabPane.getSelectedIndex(), title );
      }
    }

    updateCutCopyPaste();
  }

  public void updateCutCopyPaste ()
  {
    final EditorScene scene = getCurrentScene();
    if (scene == null )
    {
      deleteAction.setEnabled( false );
      cutAction.setEnabled( false );
      copyAction.setEnabled( false );
      pasteAction.setEnabled( false );
    }
    else
    {
      updateCutCopyPaste( scene );
    }
  }

  public void updateCutCopyPaste (final EditorScene scene)
  {
    final boolean hasSelection = !scene.getSelectedObjects().isEmpty();
    deleteAction.setEnabled( hasSelection );
    cutAction.setEnabled( hasSelection );
    copyAction.setEnabled( hasSelection );
    pasteAction.setEnabled( Clipboard.getInstance().isClipboardPasteable() );
  }

  private void updateTabTitle (final int index, final String label)
  {
    tabPane.setTitleAt( index, label );
    final TabComponent tabComp = (TabComponent) tabPane.getTabComponentAt( tabPane.getSelectedIndex() );
    if ( tabComp != null )
    {
      tabComp.setLabelText( label );
    }
  }

  public void exitRequested ()
  {
    if ( tabPane.getSelectedIndex() > 0 )
    {
      if ( closeCurrentTab().isAbortExit() )
      {
        return;
      }
    }

    while ( tabPane.getTabCount() > 0 )
    {
      tabPane.setSelectedIndex( 0 );
      tabSelectionChanged();
      if ( closeCurrentTab().isAbortExit() )
      {
        return;
      }
    }

    System.exit( 0 );
  }

  public void closeTab (final int index)
  {
    final Component previous = tabPane.getSelectedComponent();
    final boolean returnToPrev = tabPane.getSelectedIndex() != index;

    if ( returnToPrev )
    {
      tabPane.setSelectedIndex( index );
    }

    final SaveResult result = closeCurrentTab();

    if ( returnToPrev && !result.isAbortExit() )
    {
      tabPane.setSelectedComponent( previous );
    }
  }

  public SaveResult closeCurrentTab ()
  {
    if ( !CommandStack.getCurrent().isSaved() )
    {
      final JComponent c = (JComponent)tabPane.getSelectedComponent();
      final String title = (String)c.getClientProperty( GRAPH_NAME_KEY );
      final int result =
        JOptionPane.showConfirmDialog( mainWindow, "Process definition '" + title + "' has unsaved changes. " +
                                                    "Do you wish to save your work before exiting?" );
      if ( JOptionPane.YES_OPTION == result )
      {
        final SaveResult saveResult = saveRequested( false );
        if ( saveResult == SaveResult.SaveSucceeded )
        {
          tabPane.remove( tabPane.getSelectedIndex() );
        }
        return saveResult;
      }
      else if ( JOptionPane.NO_OPTION == result )
      {
        tabPane.remove( tabPane.getSelectedIndex() );
        return SaveResult.SaveNotWanted;
      }

      return SaveResult.SaveCanceled;
    }

    tabPane.remove( tabPane.getSelectedIndex() );
    return SaveResult.SaveNotWanted;
  }

  public static void main( final String[] args )
  {
    SwingUtilities.invokeLater( new Runnable()
    {
      @Override public void run()
      {
        GraphEditor.getInstance().setup();
      }
    });
  }

  public void editPaste ()
  {
    final EditorScene scene = getCurrentScene();
    if ( scene != null )
    {
      final Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
      SwingUtilities.convertPointFromScreen( mouseLocation, scene.getView() );
      scene.editPaste( mouseLocation );
    }
  }

  public void editCopy ()
  {
    final EditorScene scene = getCurrentScene();
    if ( scene != null )
    {
      scene.editCopy();
    }
  }

  public void editCut ()
  {
    final EditorScene scene = getCurrentScene();
    if ( scene != null )
    {
      scene.editCut();
    }
  }

  public void editDelete ()
  {
    final EditorScene scene = getCurrentScene();
    if ( scene != null )
    {
      scene.editDelete();
    }
  }

  public void nodeTypesChanged ()
  {
    for ( final Component component : tabPane.getComponents() )
    {
      if ( component instanceof JComponent )
      {
        final JComponent jComponent = (JComponent)component;
        final EditorScene scene = (EditorScene)jComponent.getClientProperty( "scene" );
        if ( scene != null )
        {
          scene.nodeTypesUpdated();
        }
      }
    }
  }
}