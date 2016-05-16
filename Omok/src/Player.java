/**
 * Description: This class assigns the users game record 
 * Author: Dennis Situ
 * Last Updated: May 5, 2016
 */

public class Player {
	String name = "";
	int win = 0;
	int loss = 0;
	int total = 0;
	int winStreak = 0;
	
	public void run() {
		System.out.println("Player: " + name);
		System.out.println("Wins: " + win);
		System.out.println("Loss " + loss);
		System.out.println("Total Games: " + total);
		System.out.println("Win Streak " + winStreak);
	}
}
