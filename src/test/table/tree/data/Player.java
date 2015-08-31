package test.table.tree.data;


/**
 * This class represents a player
 */

public class Player {
  private Team team;

  private String lastName;

  private String firstName;

  private float points;

  private float rebounds;

  private float assists;

  /**
   * Constructs an empty Player
   */
  public Player() {
    this(null, null, 0.0f, 0.0f, 0.0f);
  }

  /**
   * Constructs a Player
   * 
   * @param firstName
   *            the first name
   * @param lastName
   *            the last name
   * @param points
   *            the points
   * @param rebounds
   *            the rebounds
   * @param assists
   *            the assists
   */
  public Player(String firstName, String lastName, float points,
      float rebounds, float assists) {
    setFirstName(firstName);
    setLastName(lastName);
    setPoints(points);
    setRebounds(rebounds);
    setAssists(assists);
  }

  /**
   * Sets the team for theo player
   * 
   * @param team
   *            the team
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /**
   * Gets the assists
   * 
   * @return float
   */
  public float getAssists() {
    return assists;
  }

  /**
   * Sets the assists
   * 
   * @param assists
   *            The assists to set.
   */
  public void setAssists(float assists) {
    this.assists = assists;
  }

  /**
   * Gets the first name
   * 
   * @return String
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name
   * 
   * @param firstName
   *            The firstName to set.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name
   * 
   * @return String
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name
   * 
   * @param lastName
   *            The lastName to set.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the points
   * 
   * @return float
   */
  public float getPoints() {
    return points;
  }

  /**
   * Sets the points
   * 
   * @param points
   *            The points to set.
   */
  public void setPoints(float points) {
    this.points = points;
  }

  /**
   * Gets the rebounds
   * 
   * @return float
   */
  public float getRebounds() {
    return rebounds;
  }

  /**
   * Sets the rebounds
   * 
   * @param rebounds
   *            The rebounds to set.
   */
  public void setRebounds(float rebounds) {
    this.rebounds = rebounds;
  }

  /**
   * Gets the team
   * 
   * @return Team
   */
  public Team getTeam() {
    return team;
  }

  /**
   * Returns whether this player led the team in the specified category
   * 
   * @param column
   *            the column (category)
   * @return boolean
   */
  public boolean ledTeam(int column) {
    return team.led(this, column);
  }
  
}