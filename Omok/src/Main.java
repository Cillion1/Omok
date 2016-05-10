/**
 * Description: This class runs our game
 * Author: Dennis Situ
 * Last Updated: May 10, 2016
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Main {

	static Player player;
	static String fileName = "";
	static Scanner keyb = new Scanner(System.in);

	static char[][] gameBoard = null;
	static boolean playerTurn = true;
	static final int BOARDSIZE = 15;
	static int turnCount = 0;

	public static void main(String[] args) {
		// Prints the introduction of the game
		// printIntro(); 
		boolean restartGame = false; // Variable that restarts the game or not
		do {
			// Output player stats and show the board
			printPlayerStat();
			setupBoard();
			// Play until the game is won
			do {
				placePiece();
				printBoard(gameBoard);
			} while (!isGameWon() || !isGameTied());
			
			if (!isGameTied()) {
				if (!playerTurn) { // Prints and update status if Player One won.
					System.out.println("Player 1 Wins");
				} else if (playerTurn) { // Prints and update status if Player One won.
					System.out.println("Player 2 Wins");
				}
				System.out.println("The game is a tie!");
			}
			System.out.println("(P)lay Again");
			System.out.println("(Q)uit");
			boolean quitGame = false;
			do {
				String userDecision = keyb.next();
				if (userDecision.equalsIgnoreCase("Q")) {
					quitGame = true;
				} else if (userDecision.equalsIgnoreCase("P")) {
					restartGame = true;
					quitGame = true;
				} else {
					System.out.println("Invalid Command");
				}
			} while (!quitGame);
			playerTurn = true;
		} while (restartGame);
	}
	
	/**
	 * Get input from the user and check to see if the letter assigned is inputed
	 */
	public static void startGame() {
		boolean gameStart = false;
		do {
			String numberStart = keyb.next();
			if (numberStart.equalsIgnoreCase("p")) { // Checks to see if 'p' is the input
				gameStart = true;
			} else {
				System.out.println("Invalid Command. Please type the correct letter to continue."); // Prints out if input was anything but 'p'.
			}
		} while (!gameStart);
	}

	/**
	 * This prints out the players record from the Player class
	 */
	public static void printPlayerStat() {
		loadPlayerData();
		player.run();
		savePlayerData();
	}

	/**
	 * Prints out the title introduction screen.
	 */
	public static void printIntro() {
		System.out.println("Omok!");
		System.out.println("");
		System.out.println("Instructions:");
		System.out.println("");
		System.out
				.println("To play omok, each player takes turn to place pieces on the board. "
						+ "Getting 5 pieces in a line either vertically, horizontally, or diagonally will achieve a win.");
		System.out.println("");
		System.out.println("(P)lay");
		System.out.println("");
		System.out.println("Enter your choice: ");
		startGame();
	}

	/**
	 * Creates a new array and inserts '-' to all the indexes
	 * and prints the board out after
	 */
	public static void setupBoard() {
		gameBoard = new char[BOARDSIZE][BOARDSIZE]; // Declare new array of BOARDSIZE x BOARDSIZE
		for (int i = 0; i < gameBoard.length; i++) { // Loop row up by one
			for (int j = 0; j < gameBoard[i].length; j++) { // In each column, loop the column up by one
				gameBoard[i][j] = '-'; // Insert '-' in each index
			}
		}
		printBoard(gameBoard); // Prints the board to the console
	}

	/**
	 * Prints the board out and switch players each input
	 * @param data
	 */
	public static void printBoard(char[][] data) {
		System.out.println("A B C D E F G H I J K L M N O");
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[row].length; col++) {
				System.out.print(data[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println("");
		isWhoTurn();
	}
	
	/**
	 * Switch from player one to two and vice versa
	 */
	public static void isWhoTurn() {
		if (!isGameWon()) {
			if (playerTurn) {
				System.out.println("Player " + 1 + "'s Turn");
				turnCount++;
			} else {
				System.out.println("Player " + 2 + "'s Turn");
			}
		}
	}
	
	/**
	 * Grabs a letter and return the letter as a integer
	 * @return
	 */
	public static int getLetterPosition() {
		int letterNumber = 0;
		int positionLength = 0;
		if (turnCount <= 3) {
			System.out.println("Type the position in the format “X Y” (replace X with a letter and Y with a number... eg. A 3).");
		}
		do {
			String position = keyb.next();
			positionLength = position.length();
			char c = position.toLowerCase().charAt(0);
			letterNumber = c - 'a';
			if (letterNumber > BOARDSIZE || letterNumber < 0
					|| positionLength > 1) {
				System.out.println("Invalid Move! Must be a letter before P.");
			}
		} while (letterNumber > BOARDSIZE || letterNumber < 0
				|| positionLength > 1);
		return letterNumber;
	}

	/**
	 * Grabs an integer and returns it
	 * @return
	 */
	public static int getNumberPosition() {
		int number = 0;
		do {
			try {
				String position = keyb.next();
				number = Integer.parseInt(position);
				if (number <= 0 || number > BOARDSIZE) {
					System.out.println("Invalid Move! Must be a positive number less than " + BOARDSIZE + ".");
				}
			} catch (Exception e) {
				System.out.println("Invalid Move! Not a possible coordinate.");
			}
		} while (number > BOARDSIZE || number <= 0);
		return number;
	}

	/**
	 * Inserts two numbers on the 2D array and assign it as X or O
	 */
	public static void placePiece() {
		// Assign numbers from user
		boolean positionNotTaken = false;
		do {
			int x = getLetterPosition();
			int y = getNumberPosition() - 1;
			// Check on if player has already played in position
			if (gameBoard[y][x] == '-') {
				if (playerTurn) {
					gameBoard[y][x] = 'X';
					playerTurn = false;
					positionNotTaken = true;
				} else {
					gameBoard[y][x] = 'X'; // Change to O later
					playerTurn = true;
					positionNotTaken = true;
				}
			} else {
				System.out.println("Invalid Move! Position is taken.");
				System.out.println("");
			}
		} while (!positionNotTaken);
	}

	/**
	 * Checks if 5 X's or 5 O's are on the board
	 * and if the game is won.
	 * @return true;
	 */
	public static boolean isGameWon() {
		// Determine horizontal 5 in a row.
		for (int col = 0; col <= gameBoard.length - 1; col++) { 	// loop through the column
			for (int row = 0; row <= gameBoard.length - 5; row++) { // loop through the row
				char letter = gameBoard[col][row]; 					// Assign the character chosen to letter
				if (gameBoard[col][row] != '-') { 					// Check to see if index is X or O
					boolean isWin = true;
					for (int i = 1; i < 5; i++) { 					// Counter up to 5
						if (gameBoard[col][row + i] != letter) { 	// Checks if next index equals letter assigned
							isWin = false; //
							break;
						}
					}
					if (isWin) {
						return true;
					}
				}
			}
		}
		// Determine vertically 5 in a row.
		for (int col = 0; col <= gameBoard.length - 5 ; col++) { // loop through the column
			for (int row = 0; row <= gameBoard.length - 1; row++) { // loop through the row
				char letter = gameBoard[col][row]; // Assign the character chosen to letter
				if (gameBoard[col][row] != '-') { // Check to see if index is X or O
					boolean isWin = true;
					for (int i = 1; i < 5; i++) { // Counter up to 5
						if (gameBoard[col + i][row] != letter) { // Checks if next index equals letter assigned
							isWin = false;
							break;
						}
					}
					if (isWin) {
						return true;
					}
				}
			}
		}
		/*// Check top-right to bottom-left.
		for (int col = 4; col <= gameBoard.length - 5; col++) { // loop through the row
			for (int row = 4; row <= gameBoard.length - 5; row++) { // loop through the column
				char letter = gameBoard[col][row]; // Assign the character chosen to letter
				if (gameBoard[col][row] != '-') { // Check to see if index is X or O
					boolean isWin = true;
					for (int i = 1; i < 5; i++) { // Counter up to 5
						if (gameBoard[col - i][row - i] != letter)  { // Checks if next index equals letter assigned
							isWin = false;
							break;
						}
					}
					if (isWin) {
						return true;
					}
				}
			}
		}
		// Check top-left to bottom-right
		for (int col = 4; col <= gameBoard.length - 5; col++) { // loop through the row
			for (int row = 4; row <= gameBoard.length - 5; row++) { // loop through the column
				char letter = gameBoard[col][row]; // Assign the character chosen to letter
				if (gameBoard[col][row] != '-') { // Check to see if index is X or O
					boolean isWin = true;
					for (int i = 1; i < 5; i++) { // Counter up to 5
						if (gameBoard[col - i][row + i] != letter)  { // Checks if next index equals letter assigned
							isWin = false;
							break;
						}
					}
					if (isWin) {
						return true;
					}
				}
			}
		}*/
		return false;
	}
	
	public static boolean isGameTied() {
		for (int col = 0; col < gameBoard.length; col++) {
			for (int row = 0; row < gameBoard.length; row++) {				
				if (gameBoard[col][row] != '-') {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void loadPlayerData() {
		System.out.println("Loading Data...");
		FileInputStream streamIn = null;
		ObjectInputStream objectinputstream = null;
		try {
			System.out.println("Username: ");
			String userOpen = keyb.next();
			fileName = userOpen;
			streamIn = new FileInputStream(fileName);
			objectinputstream = new ObjectInputStream(streamIn);
			// Since the game object contains references
			// to all the other objects, reading it in
			// also reads in all the other objects!
			player = (Player) objectinputstream.readObject();
			if (player == null) {
				System.out.println("No player found, Creating new player.");
				player = new Player();
			} else {
				System.out.println("Loaded! Welcome back " + player.name + "!");
			}
		} catch (Exception e) {
			System.out.println("No player found, Creating new player.");
			player = new Player();
			
		} finally {
			try {
				objectinputstream.close();
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Save the user's game to the save game file
	 */
	public static void savePlayerData() {
		System.out.println("Saving player...");
		FileOutputStream fout;
		ObjectOutputStream oos = null;
		try {
			// Ask user for filename save
			fout = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fout);
			// Since the game object contains references
			// to all the other objects, writing it
			// also writes all the other objects!
			oos.writeObject(player);
			System.out.println("Saved!");
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		try {
			oos.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
}
