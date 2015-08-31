package org.telosys.tools.eclipse.plugin.commons.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.telosys.tools.generator.target.TargetDefinition;

public class ListenerForTableToolTip implements Listener { //, KeyListener {

	private final Table table;

	private Shell tip = null;

	//private Label label = null; //

	private TableItem currentTableItem = null;
	private Point     currentPoint = null ;

	//---------------------------------------------------------------------------------------------
	public ListenerForTableToolTip(Table table) {
		super();
		this.table = table;
	}
	
	//---------------------------------------------------------------------------------------------
	private void log(String msg) {
		System.out.println("["+this.getClass().getSimpleName()+"] : " + msg);
	}
	//---------------------------------------------------------------------------------------------
	private void logEvent(Event event) {
		String msg = "???" ;
		switch(event.type) {
		case SWT.Dispose: msg = "Dispose" ; break ;
		case SWT.MouseMove: msg = "MouseMove" ; break ;
		case SWT.MouseHover: msg = "MouseHover" ; break ;
		case SWT.MouseDown: msg = "MouseDown" ; break ;
		case SWT.MouseUp: msg = "MouseUp" ; break ;
		}
		System.out.println("["+this.getClass().getSimpleName()+"] - Event " + msg + " : " + event );
	}

	//---------------------------------------------------------------------------------------------
	/***
	@Override
	public void handleEvent(Event event) {

		final Shell shell = table.getShell();
		final Display display = shell.getDisplay();

		switch (event.type) {

		case SWT.Dispose:
		case SWT.KeyDown:
		case SWT.MouseMove: {
			if (tip == null)
				break;
			tip.dispose();
			tip = null;
			label = null;
			break;
		}

		case SWT.MouseHover: {
			TableItem item = table.getItem(new Point(event.x, event.y));
			if (item != null) {
				if (tip != null && !tip.isDisposed())
					tip.dispose();
				tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
				tip.setLayout(new FillLayout());
				label = new Label(tip, SWT.NONE);
				label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
				label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
//				label.setData("_TABLEITEM", item);
				label.setText("tooltip " + item.getText());
//				label.addListener(SWT.MouseExit, labelListener);
//				label.addListener(SWT.MouseDown, labelListener);
				Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Rectangle rect = item.getBounds(0);
				Point pt = table.toDisplay(rect.x, rect.y);
				tip.setBounds(pt.x, pt.y, size.x, size.y);
				tip.setVisible(true);
			}
		}
		}

	}
	***/
	
	//---------------------------------------------------------------------------------------------
//	//@Override
//	public void handleEvent(Event event) {
//
//		logEvent(event);
//		TableItem tableItem = getTableItem(event);
//		
////		String itemText = "(no item)" ;
////		if ( tableItem != null ) {
////			itemText = tableItem.getText() ;
////		}		
////		switch (event.type) {
////		case SWT.Dispose:
////		//case SWT.KeyDown:
////		case SWT.MouseMove: 
////		case SWT.MouseHover: 
////			log("handleEvent " + event.type + " : item '" + itemText + "' " 
////					+ ( tableItem != currentTableItem ? "different" : "same item")  );
////		}
//		
//		switch (event.type) {
//
//		case SWT.Dispose: // When the user closes the windows -> dispose / table widget  
//		case SWT.MouseMove: // Each time the mouse move over the table (even without table item)
//		case SWT.MouseDoubleClick: 
//		case SWT.MouseUp: 
////		case SWT.KeyDown: 
////		case SWT.HardKeyDown: 
////			if ( tableItem != currentTableItem ) {
////				// Not on the same item
////				disposeToolTip(); 
////			}
//			disposeToolTip(); 
//			break;
//
//		case SWT.MouseHover: // When the mouse is over a 'table item' and stay a little time on it
//		//case SWT.MouseDown: 
//			if ( tableItem != null ) {
//				if ( tableItem != currentTableItem ) {
//					// Not on the same item
//					showToolTip(tableItem);
//				}
//				else if ( tip == null ) {
//					// not yet shown
//					showToolTip(tableItem);
//				}
//			}
//			break;
//		}
//		currentTableItem = tableItem ;
//	}
//	
	//---------------------------------------------------------------------------------------------
	@Override
	public void handleEvent(Event event) {

		logEvent(event);
		TableItem tableItem = getTableItem(event);
		if ( event.type == SWT.MouseDown ) {
			if ( tableItem != null ) {
//				if ( tableItem != currentTableItem ) {
//					// Not on the same item
//					showToolTip(event, tableItem);
//				}
//				else if ( tip == null ) {
//					// not yet shown
//					showToolTip(event, tableItem);
//				}
				log("table location     : " + table.getLocation()  );
				log("table size         : " + table.getSize()  );
				log("table header hight : " + table.getHeaderHeight()  );
				if (   event.x > 30 
					&& event.x < ( table.getSize().x - 80 ) 
					&& event.y > ( table.getHeaderHeight() + 3) ) {
					showToolTip(event, tableItem);
				}
			}
		}
		else if ( event.type == SWT.MouseUp ) {
			disposeToolTip(); 
		}
		else if ( event.type == SWT.MouseMove ) {
			if ( tableItem == null || tableItem != currentTableItem ) {
				disposeToolTip(); 
			}
			else {
				if ( deltaXY(event, 2) )disposeToolTip(); 
			}
		}
//		else {
//			disposeToolTip(); 
//		}
	}
	private boolean deltaXY(Event event, int deltaMax) {
		int delta ;
		delta = currentPoint.x - event.x ;
		if ( delta > deltaMax || delta < -deltaMax ) return true; 
		delta = currentPoint.y - event.y ;
		if ( delta > deltaMax || delta < -deltaMax ) return true; 
		return false ;
	}
	//---------------------------------------------------------------------------------------------
	/**
	 * Returns the TableItem for the current mouse position <br>
	 * or null if the mouse is not on a TableItem
	 * @param event
	 * @return
	 */
	private TableItem getTableItem(Event event) {
		TableItem tableItem = table.getItem(new Point(event.x, event.y));
		log("getTableItem : " + tableItem );
		return tableItem ;
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Shows the ToolTip for the given TableItem
	 * @param tableItem
	 */
	private void showToolTip(Event event, TableItem tableItem) {
		log("showToolTip()");

		if ( tableItem == null ) {
			return ;
		}
		
		if (tip != null && !tip.isDisposed()) {
			tip.dispose();
		}
		
		currentTableItem = tableItem ;
		currentPoint = new Point(event.x, event.y);
		
		//--- Info to be displayed
		String info = "( no target information )" ;
		Object data = tableItem.getData();
		if ( data != null ) {
			if ( data instanceof TargetDefinition ) {
				TargetDefinition targetDefinition = (TargetDefinition) data ;
				info = 
					targetDefinition.getFolder() +
					"   |   " +
					targetDefinition.getFile() ;
			}
		}

		//--- Tool-tip colors
		final Shell shell = table.getShell();
		final Display display = shell.getDisplay();
		Color foregroundColor = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND) ;
		Color backgroundColor = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND) ;
		
		//--- Tool-tip creation
		tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = 8; // left/right
		fillLayout.marginHeight = 3; // top/bottom
		tip.setLayout(fillLayout);
		tip.setForeground(foregroundColor);
		tip.setBackground(backgroundColor);
		
		Label label = new Label(tip, SWT.NONE);
		label.setForeground(foregroundColor);
		label.setBackground(backgroundColor);		
		label.setText(info);

		//--- Tool-tip position
		Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = tableItem.getBounds(0);
		Point pt = table.toDisplay(rect.x, rect.y);
		tip.setBounds(pt.x+20, pt.y+20, size.x, size.y);
		tip.setVisible(true);
	}
	
//	private void showToolTip(TableItem tableItem) {
//		log("showToolTip()");
//
//		if ( tableItem == null ) return ;
//		
//		final Shell shell = table.getShell();
//		Label toolTipLabel = new Label(shell, SWT.BORDER);
//		table.getLayout().
//		
//		//--- Info to be displayed
//		String info = "( no target information )" ;
//		Object data = tableItem.getData();
//		if ( data != null ) {
//			if ( data instanceof TargetDefinition ) {
//				TargetDefinition targetDefinition = (TargetDefinition) data ;
//				info = 
//					targetDefinition.getFolder() +
//					"   |   " +
//					targetDefinition.getFile() ;
//			}
//		}
//		toolTipLabel.setText(info);
//		
//		final Display display = shell.getDisplay();
//		Color foregroundColor = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND) ;
//		Color backgroundColor = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND) ;
//		
//		toolTipLabel.setBackground(backgroundColor);
//		toolTipLabel.setForeground(foregroundColor);
//		
//		//--- Tool-tip position
////		Point size = toolTipLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
////		Rectangle rect = tableItem.getBounds(0);
////		Point pt = table.toDisplay(rect.x, rect.y);
////		toolTipLabel.setBounds(pt.x+20, pt.y+12, size.x, size.y);
//		toolTipLabel.setSize(200, 30);
//		toolTipLabel.setLocation(100, 100);
//		toolTipLabel.setVisible(true);
//
//	}
	//---------------------------------------------------------------------------------------------
	private void disposeToolTip() {
		log("disposeToolTip()");
		if ( tip != null ) {
			tip.dispose();
		}
		tip = null;
		//label = null;
	}

//	@Override
//	public void keyPressed(KeyEvent keyEvent) {
//		log("keyPressed : " + keyEvent);
//		disposeToolTip();
//		
//	}
//
//	@Override
//	public void keyReleased(KeyEvent keyEvent) {
//		log("keyReleased : " + keyEvent);
//		//disposeToolTip();		
//	}
}
