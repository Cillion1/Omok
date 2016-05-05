/**
 * Description: This class runs our game
 * Author: Dennis
 * Last Updated: May 5, 2016
 */

import java.util.Scanner;

public class Main {

	static Scanner keyb = new Scanner(System.in);

	static char[][] gameBoard = null;
	static boolean playerTurn = true;
	static final int BOARDSIZE = 15;

	public static void main(String[] args) {
		// printIntro();
		boolean restartGame = false;
		do {
			// printPlayerStat();
			setupBoard();
			do {
				placePiece();
				printBoard(gameBoard);
			} while (!isGameWon());
			if (!playerTurn) {
				System.out.println("Player 1 Wins");
			} else if (playerTurn) {
				System.out.println("Player 2 Wins");
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
			try {
				String numberStart = keyb.next();
				if (numberStart.equalsIgnoreCase("p")) { // Checks to see if 'p' is the input
					gameStart = true;
				} else {
					System.out.println("Invalid Command!"); // Prints out if input was anything but 'p'.
				}
			} catch (Exception e) {
				System.out.println("Invalid Command!");
			}
		} while (!gameStart);
	}

	/**
	 * This prints out the players record from the Player class
	 */
	public static void printPlayerStat() {
		System.out.println("Type Player 1’s username: ");
		String setPlayerOne = keyb.next();
		Player playerOne = new Player();
		playerOne.name = setPlayerOne;
		System.out.println("Player: " + playerOne.name);
		System.out.println("Wins: " + playerOne.win);
		System.out.println("Loss: " + playerOne.loss);
		System.out.println("Total Games: " + playerOne.total);
		System.out.println("Win Streak: " + playerOne.winStreak);
		System.out.println("");
		System.out.println("Type Player 2’s username: ");
		String setPlayerTwo = keyb.next();
		Player playerTwo = new Player();
		playerTwo.name = setPlayerTwo;
		System.out.println("Player: " + playerTwo.name);
		System.out.println("Wins: " + playerTwo.win);
		System.out.println("Loss: " + playerTwo.loss);
		System.out.println("Total Games: " + playerOne.total);
		System.out.println("Win Streak: " + playerOne.winStreak);
		System.out.println("");
		System.out.println("Type P to start.");
		startGame();
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
		System.out.println("Type Position");
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
		// Check top-right to bottom-left.
		for (int col = 1; col <= gameBoard.length - 5; col++) { // loop through the row
			for (int row = 1; row <= gameBoard.length - 5; row++) { // loop through the column
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
		for (int col = 1; col <= gameBoard.length - 5; col++) { // loop through the row
			for (int row = 1; row <= gameBoard.length - 5; row++) { // loop through the column
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
		}
		return false;
	}
}
