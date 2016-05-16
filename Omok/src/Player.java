import java.io.Serializable;

/**
 * Description: This class assigns the users game record 
 * Author: Dennis Situ
 * Last Updated: May 5, 2016
 */

public class Player implements Serializable {
	/**
	 * This gives the statistics which is saved in a file.
	 */
	private static final long serialVersionUID = 1L;
	String name = "";
	int win = 0;
	int loss = 0;
	int total = 0;
	int winStreak = 0;
	
}
