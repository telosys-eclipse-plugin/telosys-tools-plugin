package test.table.tree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import test.table.tree.data.PlayerTableModel;

/**
 * This class demonstrates TableTreeViewer.
 */
/*
 * TableTreeViewer : Deprecated. As of 3.1 use TreeViewer instead 
 * TableTree : Deprecated. As of 3.1 use Tree, TreeItem and TreeColumn
 */

public class PlayerTableTree extends ApplicationWindow {
  // The TableTreeViewer
  //private TableTreeViewer ttv;
  private TreeViewer ttv;

  /**
   * PlayerTableTree constructor
   */
  public PlayerTableTree() {
    super(null);
  }

  /**
   * Runs the application
   */
  public void run() {
    // Don't return from open() until window closes
    setBlockOnOpen(true);

    // Open the main window
    open();

    // Dispose the display
    Display.getCurrent().dispose();
  }

  /**
   * Configures the shell
   * 
   * @param shell
   *            the shell
   */
  protected void configureShell(Shell shell) {
    super.configureShell(shell);
    shell.setText("Team Tree");
  }

  /**
   * Creates the main window's contents
   * 
   * @param parent
   *            the main window
   * @return Control
   */
  protected Control createContents(Composite parent) {
    // Create the table viewer to display the players
    //ttv = new TableTreeViewer(parent);
	//ttv.getTableTree().setLayoutData(new GridData(GridData.FILL_BOTH));
    ttv = new TreeViewer(parent);
    Tree tree = ttv.getTree();
    tree.setLayoutData(new GridData(GridData.FILL_BOTH));

    // Set the content and label providers
    ttv.setContentProvider(new PlayerTreeContentProvider());
    ttv.setLabelProvider(new PlayerLabelProvider());
    ttv.setInput(new PlayerTableModel());

    // Set up the table
//    Table table = ttv.getTableTree().getTable();
//    TableTree tableTree = ttv.getTableTree();
    
    new TreeColumn(tree, SWT.LEFT).setText("First Name");
    new TreeColumn(tree, SWT.RIGHT).setText("Last Name");
    new TreeColumn(tree, SWT.RIGHT).setText("Last");
    new TreeColumn(tree, SWT.RIGHT).setText("Rebounds");
    new TreeColumn(tree, SWT.RIGHT).setText("Assists");
    
    //new TableColumn(table, SWT.LEFT).setText("First Name");
//    new TableColumn(table, SWT.LEFT).setText("Last Name");
//    new TableColumn(table, SWT.RIGHT).setText("Last");
//    new TableColumn(table, SWT.RIGHT).setText("Rebounds");
//    new TableColumn(table, SWT.RIGHT).setText("Assists");

    // Expand everything
    ttv.expandAll();

    // Pack the columns
//    for (int i = 0, n = table.getColumnCount(); i < n; i++) {
//      table.getColumn(i).pack();
//    }
    for (int i = 0, n = tree.getColumnCount(); i < n; i++) {
        tree.getColumn(i).pack();
      }

    // Turn on the header and the lines
//    table.setHeaderVisible(true);
//    table.setLinesVisible(true);
    tree.setHeaderVisible(true);
    tree.setLinesVisible(true);

    // Pack the window
    parent.pack();

    // Scroll to top
    //ttv.reveal(ttv.getElementAt(0));
    ttv.reveal(tree.getItem(0));
    
//    return ttv.getTableTree();
    return tree ;
  }

  /**
   * The application entry point
   * 
   * @param args
   *            the command line arguments
   */
  public static void main(String[] args) {
    new PlayerTableTree().run();
  }
}
