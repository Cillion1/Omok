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

	static Player playerOne = new Player();
	static Player playerTwo = new Player();
	static String fileName = "";
	static Scanner keyb = new Scanner(System.in);

	static char[][] gameBoard = null;
	static boolean playerTurn = true;
	static final int BOARDSIZE = 15;
	static int turnCount = 0;

	public static void main(String[] args) {
		printIntro();
		runGame();
	}
	
	/**
	 * This runs the game and restarts the game when it is done.
	 */
	public static void runGame() {
		boolean restartGame = false;
		do {
			printPlayerStat();
			setupBoard();
			do {
				placePiece();
				printBoard(gameBoard);
			} while (!isGameWon());

			if (!isGameTied()) {
				System.out.println("The game is a tie!");
			} else {
				updatePlayerStats();
			}
			System.out.println("");
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
	 * This updates the player stats after the game is finished.
	 */
	public static void updatePlayerStats() {
		if (!playerTurn) {
			System.out.println("Player 1 Wins");
			playerOne.win++;
			playerOne.total++;
			playerOne.winStreak++;
			fileName = playerOne.name;
			savePlayerOneData();

			playerTwo.loss++;
			playerTwo.total++;
			playerTwo.winStreak = 0;
			fileName = playerTwo.name;
			savePlayerTwoData();
		} else if (playerTurn) { // Prints and update status if Player one won.
			System.out.println("Player 2 Wins");
			playerTwo.win++;
			playerTwo.total++;
			playerTwo.winStreak++;
			fileName = playerTwo.name;
			savePlayerTwoData();

			playerOne.loss++;
			playerOne.total++;
			playerOne.winStreak = 0;
			fileName = playerOne.name;
			savePlayerOneData();
		}
	}

	/**
	 * Get input from the user and check to see if the letter assigned is
	 * inputed
	 */
	public static void startGame() {
		boolean gameStart = false;
		do {
			String numberStart = keyb.next();
			if (numberStart.equalsIgnoreCase("p")) {
				gameStart = true;
			} else {
				System.out
						.println("Invalid Command. Please type the correct letter to continue.");
			}
		} while (!gameStart);
	}

	/**
	 * This prints out the players record from the Player class
	 */
	public static void printPlayerStat() {
		loadPlayerOneData();
		savePlayerOneData();
		System.out.println("");
		loadPlayerTwoData();
		savePlayerOneData();
		savePlayerTwoData();
		System.out.println("");
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
	 * Creates a new array and inserts '-' to all the indexes and prints the
	 * board out after
	 */
	public static void setupBoard() {
		gameBoard = new char[BOARDSIZE][BOARDSIZE]; // Declare new array of
													// BOARDSIZE x BOARDSIZE
		for (int i = 0; i < gameBoard.length; i++) { // Loop row up by one
			for (int j = 0; j < gameBoard[i].length; j++) { // In each column,
															// loop the column
															// up by one
				gameBoard[i][j] = '-'; // Insert '-' in each index
			}
		}
		printBoard(gameBoard); // Prints the board to the console
	}

	/**
	 * Prints the board out and switch players each input
	 * 
	 * @param char[][] The array holding the board
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
		switchPlayer();
	}

	/**
	 * Switch from player one to two and vice versa
	 */
	public static void switchPlayer() {
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
	 * 
	 * @return int Column number
	 */
	public static int getLetterPosition() {
		int letterNumber = 0;
		int positionLength = 0;
		if (turnCount <= 3) {
			System.out
					.println("Type the position in the format “X Y” (replace X with a letter and Y with a number... eg. A 3).");
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
	 * 
	 * @return int
	 */
	public static int getNumberPosition() {
		int number = 0;
		do {
			try {
				String position = keyb.next();
				number = Integer.parseInt(position);
				if (number <= 0 || number > BOARDSIZE) {
					System.out
							.println("Invalid Move! Must be a positive number less than "
									+ BOARDSIZE + ".");
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
			System.out.println(gameBoard[y][x]);
			if (gameBoard[y][x] == '-') {
				if (playerTurn) {
					gameBoard[y][x] = 'X';
					playerTurn = false;
					positionNotTaken = true;
				} else {
					gameBoard[y][x] = 'O'; // Change to O later
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
	 * Checks if 5 X's or 5 O's are on the board and if the game is won.
	 * 
	 * @return boolean true if there is a win. False if there is no win
	 */
	public static boolean isGameWon() {
		// Determine horizontal 5 in a row.
		for (int col = 0; col <= gameBoard.length - 1; col++) {
			for (int row = 0; row <= gameBoard.length - 5; row++) {
				// Assign X or O in letter.
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					for (int i = 1; i < 5; i++) {
						if (gameBoard[col][row + i] != letter) {
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
		for (int col = 0; col <= gameBoard.length - 5; col++) {
			for (int row = 0; row <= gameBoard.length - 1; row++) {
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					for (int i = 1; i < 5; i++) {
						if (gameBoard[col + i][row] != letter) {
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
		// Determine top-right to bottom-left (Works)
		for (int col = 0; col <= gameBoard.length - 5; col++) {
			for (int row = 4; row <= gameBoard.length - 1; row++) {
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					for (int i = 1; i < 5; i++) {
						if (gameBoard[col + i][row - i] != letter) {
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
		// Determine top-left to bottom-right (\)
		for (int col = 0; col <= gameBoard.length - 5; col++) {
			for (int row = 0; row <= gameBoard.length - 5; row++) {
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					for (int i = 1; i < 5; i++) {
						if (gameBoard[col + i][row + i] != letter) {
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

	/**
	 * Checks to see if there are no more possible placements on the board.
	 * 
	 * @return boolean true if the game is tied. False if game is not tied.
	 */
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

	/**
	 * This loads player 1's file if it is there and creates a new one if it does
	 * not.
	 */
	public static void loadPlayerOneData() {
		FileInputStream streamIn = null;
		ObjectInputStream objectinputstream = null;
		System.out.println("Player 1's Name: ");
		String userName = keyb.next();
		System.out.println("Loading Data...");
		// Assign their name as the filename
		fileName = userName;
		try {
			streamIn = new FileInputStream(fileName);
			objectinputstream = new ObjectInputStream(streamIn);
			// Reads the file from the player
			playerOne = (Player) objectinputstream.readObject();
			// Checks to see if player exists or not and creates a new player if
			// it does not exist
			if (playerOne == null) {
				System.out.println("No player found, Creating new player.");
				playerOne = new Player();
				playerOne.name = userName;
			} else {
				System.out.println("Loaded!");
			}
		} catch (Exception e) {
			System.out.println("No player found, Creating new player.");
			playerOne = new Player();
			playerOne.name = userName;
		} finally {
			try {
				objectinputstream.close();
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
		}
		runP1();
	}

	/**
	 * This loads player 2's file if it is there and creates a new one if it does
	 * not.
	 */
	public static void loadPlayerTwoData() {
		FileInputStream streamIn = null;
		ObjectInputStream objectinputstream = null;
		System.out.println("Player 2's Name: ");
		String userName = keyb.next();
		System.out.println("Loading Data...");
		// Assign their name as the filename
		fileName = userName;
		try {
			streamIn = new FileInputStream(fileName);
			objectinputstream = new ObjectInputStream(streamIn);
			// Reads the file from the player
			playerTwo = (Player) objectinputstream.readObject();
			// Checks to see if player exists or not and creates a new player if
			// it does not exist
			if (playerTwo == null) {
				System.out.println("No player found, Creating new player.");
				playerTwo = new Player();
				playerTwo.name = userName;
			} else {
				System.out.println("Loaded!");
			}
		} catch (Exception e) {
			System.out.println("No player found, Creating new player.");
			playerTwo = new Player();
			playerTwo.name = userName;
		} finally {
			try {
				objectinputstream.close();
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
			}
		}
		runP2();
	}

	/**
	 * This save player 1's stats to the file assign.
	 */
	public static void savePlayerOneData() {
		FileOutputStream fout;
		ObjectOutputStream oos = null;
		try {
			// Creates the file from file name
			fout = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fout);
			// Writes the data to the file
			oos.writeObject(playerOne);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		try {
			oos.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	public static void savePlayerTwoData() {
		FileOutputStream fout;
		ObjectOutputStream oos = null;
		try {
			// Creates the file from file name
			fout = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fout);
			// Writes the data to the file
			oos.writeObject(playerTwo);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		try {
			oos.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	/**
	 * This prints out the stats for player 1
	 */
	public static void runP1() {
		System.out.println("Player: " + playerOne.name);
		System.out.println("Wins: " + playerOne.win);
		System.out.println("Loss " + playerOne.loss);
		System.out.println("Total Games: " + playerOne.total);
		System.out.println("Win Streak " + playerOne.winStreak);
	}

	/**
	 * This prints out the stats for player 2
	 */
	public static void runP2() {
		System.out.println("Player: " + playerTwo.name);
		System.out.println("Wins: " + playerTwo.win);
		System.out.println("Loss " + playerTwo.loss);
		System.out.println("Total Games: " + playerTwo.total);
		System.out.println("Win Streak " + playerTwo.winStreak);
	}
}
