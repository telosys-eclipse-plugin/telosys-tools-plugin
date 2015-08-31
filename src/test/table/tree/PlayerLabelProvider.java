package test.table.tree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import test.table.tree.data.Player;
import test.table.tree.data.Team;

/**
 * This class provides the labels for PlayerTable
 */

//class PlayerLabelProvider implements ITableLabelProvider 
class PlayerLabelProvider implements ILabelProvider 
{
  // Image to display if the player led his team
  private Image ball;

  // Constructs a PlayerLabelProvider
  public PlayerLabelProvider() {
    // Create the image
    try {
      ball = new Image(null, new FileInputStream("images/ball.png"));
    } catch (FileNotFoundException e) {
      // Swallow it
    }
  }

  /**
   * Gets the image for the specified column
   * 
   * @param arg0
   *            the player
   * @param arg1
   *            the column
   * @return Image
   */
  public Image getPlayerColumnImage(Object arg0, int arg1) {
    Player player = (Player) arg0;
    Image image = null;
    switch (arg1) {
    // A player can't lead team in first name or last name
    case PlayerConst.COLUMN_POINTS:
    case PlayerConst.COLUMN_REBOUNDS:
    case PlayerConst.COLUMN_ASSISTS:
      if (player.ledTeam(arg1))
        // Set the image
        image = ball;
      break;
    }
    return image;
  }
  
  /**
   * Gets the image for the specified column
   * 
   * @param arg0
   *            the player or team
   * @param arg1
   *            the column
   * @return Image
   */
  public Image getColumnImage(Object arg0, int arg1) {
    // Teams have no image
    if (arg0 instanceof Player)
      return getPlayerColumnImage(arg0, arg1);
    return null;
  }  

  /**
   * Gets the text for the specified column
   * 
   * @param arg0
   *            the player
   * @param arg1
   *            the column
   * @return String
   */
  public String getPlayerColumnText(Object arg0, int arg1) {
    Player player = (Player) arg0;
    String text = "";
    switch (arg1) {
    case PlayerConst.COLUMN_FIRST_NAME:
      text = player.getFirstName();
      break;
    case PlayerConst.COLUMN_LAST_NAME:
      text = player.getLastName();
      break;
    case PlayerConst.COLUMN_POINTS:
      text = String.valueOf(player.getPoints());
      break;
    case PlayerConst.COLUMN_REBOUNDS:
      text = String.valueOf(player.getRebounds());
      break;
    case PlayerConst.COLUMN_ASSISTS:
      text = String.valueOf(player.getAssists());
      break;
    }
    return text;
  }
  
  /**
   * Gets the text for the specified column
   * 
   * @param arg0
   *            the player or team
   * @param arg1
   *            the column
   * @return String
   */
  public String getColumnText(Object arg0, int arg1) {
    if (arg0 instanceof Player)
      return getPlayerColumnText(arg0, arg1);
    Team team = (Team) arg0;
    return arg1 == 0 ? team.getYear() + " " + team.getName() : "";
  }

  /**
   * Adds a listener
   * 
   * @param arg0
   *            the listener
   */
  public void addListener(ILabelProviderListener arg0) {
    // Throw it away
  }

  /**
   * Dispose any created resources
   */
  public void dispose() {
    // Dispose the image
    if (ball != null)
      ball.dispose();
  }

  /**
   * Returns whether the specified property, if changed, would affect the
   * label
   * 
   * @param arg0
   *            the player
   * @param arg1
   *            the property
   * @return boolean
   */
  public boolean isLabelProperty(Object arg0, String arg1) {
    return false;
  }

  /**
   * Removes the specified listener
   * 
   * @param arg0
   *            the listener
   */
  public void removeListener(ILabelProviderListener arg0) {
    // Do nothing
  }

public Image getImage(Object element) {
	// Auto-generated method stub
	return null;
}

public String getText(Object element) {
	// Auto-generated method stub
	return null;
}
}