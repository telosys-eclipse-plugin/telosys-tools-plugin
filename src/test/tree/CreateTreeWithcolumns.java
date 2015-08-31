package test.tree;

/*
 * Create a Tree with columns
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class CreateTreeWithcolumns {
  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Tree tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    tree.setHeaderVisible(true);
    TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
    column1.setText("Column 1");
    column1.setWidth(200);
    TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
    column2.setText("Column 2");
    column2.setWidth(200);
    TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
    column3.setText("Column 3");
    column3.setWidth(200);
    for (int i = 0; i < 4; i++) {
      TreeItem item = new TreeItem(tree, SWT.NONE);
      item.setText(new String[] { "item " + i, "abc", "defghi" });
      for (int j = 0; j < 4; j++) {
        TreeItem subItem = new TreeItem(item, SWT.NONE);
        subItem
            .setText(new String[] { "subitem " + j, "jklmnop",
                "qrs" });
        for (int k = 0; k < 4; k++) {
          TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
          subsubItem.setText(new String[] { "subsubitem " + k, "tuv",
              "wxyz" });
        }
      }
    }
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}