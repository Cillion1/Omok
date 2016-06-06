/**
 * Description: This class runs our game
 * Author: Dennis Situ
 * Last Updated: June 3, 2016
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
	static int turnCount = 1;
	static boolean forfeit = false;

	public static void main(String[] args) {
		runGame();
	}
	
	/**
	 * This method runs the game and restarts or quits the game.
	 */
	public static void runGame() {
		printIntro();
		boolean restartGame = false;
		// Loop is done to have the game restart back to the state before the game starts.
		do {
			printPlayerStat();
			setupBoard();
			// Place pieces and show board after until one player wins or game is tied
			do {
				if (!forfeit) {
					placePiece();
					printBoard(gameBoard);
				}
			} while (!isGameDone());
			// Prints out the following message depending on how game is won.
			if (isGameTied()) {
				System.out.println("The game is a tie!");
			} else if (!playerTurn) {
				if (forfeit) {
					System.out.println(playerTwo.name + " forfeits!");
				}
				System.out.println(playerOne.name + " Wins!");
			} else if (playerTurn) {
				if (forfeit) {
					System.out.println(playerOne.name + " forfeits!");
				}
				System.out.println(playerTwo.name + " Wins!");
			}
			// Updates the player stats and prints out the option to play again or not
			updatePlayerStats();
			System.out.println("");
			System.out.println("(P)lay Again");
			System.out.println("(Q)uit");
			boolean quitGame = false;
			// Asks for user input and checks to see if P or Q is clicked
			do {
				String userDecision = keyb.next();
				// If 'q' is pressed, quitGame only becomes true allowing it to escape both loops as restartGame is false still. 
				// If 'p' is pressed, quitGame and restartGame becomes both true allowing it to escape the quitGame loop, but get sent back
				// to the beginning
				if (userDecision.equalsIgnoreCase("Q")) {
					quitGame = true;
				} else if (userDecision.equalsIgnoreCase("P")) {
					restartGame = true;
					quitGame = true;
				} else {
					System.out.println("Invalid Command");
				}
			} while (!quitGame);
			// Assigns game setup back to normal
			playerTurn = true;
			turnCount = 1;
			forfeit = false;
		} while (restartGame);
	}
	
	/**
	 * This updates the player stats after the game is finished and saves the players data to a file.
	 */
	public static void updatePlayerStats() {
		// Stats are added depending on how game is finished
		if (!playerTurn) {
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
		} else if (playerTurn) {
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
		} else if (isGameTied()) {
			playerOne.total++;
			playerOne.winStreak++;
			fileName = playerOne.name;
			savePlayerOneData();
			
			playerTwo.total++;
			playerTwo.winStreak = 0;
			fileName = playerTwo.name;
			savePlayerTwoData();
		}
	}

	/**
	 * Get input from the user and check to see if the letter assigned is
	 * inputed
	 */
	public static void startGamePrompt() {
		System.out.println("(P)lay");
		System.out.println("");
		System.out.println("Enter your choice: ");
		boolean gameStart = false;
		do {
			// Grabs input and checks to see if the input is 'p' or not
			String numberStart = keyb.next();
			if (numberStart.equalsIgnoreCase("p")) {
				gameStart = true;
			} else {
				System.out.println("Invalid Command. Please type the correct letter to continue.");
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
		startGamePrompt();
	}

	/**
	 * Prints out the title introduction screen.
	 */
	public static void printIntro() {
		System.out.println("Omok!");
		System.out.println("");
		System.out.println("Instructions:");
		System.out.println("");
		System.out.println("To play omok, each player take turns to place pieces on the board. "
						+ "Getting 5 pieces in a line either vertically, horizontally, or diagonally will achieve a win.");
		System.out.println("");
		startGamePrompt();
	}

	/**
	 * Creates a new array and inserts '-' to all the indexes and prints the
	 * board out after
	 */
	public static void setupBoard() {
		// Declare new array of BOARDSIZE x BOARDSIZE and inserts '-' in each index
		gameBoard = new char[BOARDSIZE][BOARDSIZE]; 
		for (int i = 0; i < gameBoard.length; i++) { 
			for (int j = 0; j < gameBoard[i].length; j++) {
				gameBoard[i][j] = '-';
			}
		}
		// This prints the board to the console
		printBoard(gameBoard);
	}

	/**
	 * Prints the board out and switch the player on every input
	 * 
	 * @param char[][] The array holding the board
	 */
	public static void printBoard(char[][] data) {
		// Prints out the x-axis and y-axis reference along with printing the board
		System.out.println("   A B C D E F G H I J K L M N O");
		for (int row = 0; row < data.length; row++) {
			// Prints out the numbers on the side
			if (row >= 9) {
				System.out.print(row + 1 + " ");
			} else {
				System.out.print(" " + (row + 1) + " ");
			}
			for (int col = 0; col < data[row].length; col++) {
				System.out.print(data[row][col] + " ");
			}
			if (row >= 9) {
				System.out.print(row + 1 + " ");
			} else {
				System.out.print((row + 1) + " ");
			}
			System.out.println();
		}
		System.out.println("   A B C D E F G H I J K L M N O");
		System.out.println("");
		switchPlayer();
	}

	/**
	 * Switch from player one to two and vice versa
	 */
	public static void switchPlayer() {
		if (!isGameWon()) {
			if (!forfeit) {
				System.out.println("Turn " + turnCount);
				if (playerTurn) {
					System.out.println(playerOne.name + "'s Turn.");
				} else if (!playerTurn) {
					System.out.println(playerTwo.name + "'s Turn.");
				}
				if (turnCount <= 1) {
					System.out.println("Type the position in the format “X Y” (replace X with a letter and Y with a number... eg. A 3). Type 'FF' to forfeit the game.");
				}
			}
		}
	}

	/**
	 * Grabs a letter and return the letter as a integer and breaks out of loop if user forfeits
	 * 
	 * @return int Column number
	 */
	public static int getLetterPosition() {
		int letterNumber = 0;
		int positionLength = 0;
		// Grabs user input and converts a letter to a number and checks if it is valid
		do {
			String position = keyb.next();
			if (position.equalsIgnoreCase("FF")) {
				forfeit = true;
				break;
			}
			positionLength = position.length();
			char c = position.toLowerCase().charAt(0);
			letterNumber = c - 'a';
			if (letterNumber > BOARDSIZE - 1 || letterNumber < 0
					|| positionLength > 1) {
				System.out.println("Invalid Move! Must be a letter before P.");
			}
		} while (letterNumber > BOARDSIZE - 1 || letterNumber < 0
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
		if (!forfeit) {
			// Grabs user input and checks to see if it is a valid number for the board
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
		}
		return number;
	}

	/**
	 * Inserts two numbers on the 2D array and assign it as X or O
	 */
	public static void placePiece() {
		boolean positionNotTaken = false;
		do {
			// Calls both methods and uses both return values to assign to gameBoard[][]
			int x = getLetterPosition();
			int y = getNumberPosition() - 1;
			System.out.println("");
			if (forfeit) {
				break;
			}
			// Check on if player has already played in position
			if (gameBoard[y][x] == '-') {
				// Assign 'X' or 'O' depending on player one or two
				if (playerTurn) {
					gameBoard[y][x] = 'X';
					playerTurn = false;
				} else {
					gameBoard[y][x] = 'O';
					playerTurn = true;
					turnCount++;
				}
				positionNotTaken = true;
			} else {
				System.out.println("Invalid Move! Position is taken.");
			}
		} while (!positionNotTaken);
	}

	/**
	 * Checks if 5 X's or 5 O's are on the board and if the game is won.
	 * 
	 * @return boolean true if there is a win. False if there is no win
	 */
	public static boolean isGameWon() {
		// Determine horizontally 5 in a row.
		for (int col = 0; col <= gameBoard.length - 1; col++) {
			for (int row = 0; row <= gameBoard.length - 5; row++) {
				// Save current char after placePiece has finished
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					// Check to see if the row ahead up to 5 is not equal to letter
					for (int i = 1; i < 5; i++) {
						if (gameBoard[col][row + i] != letter) {
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
		// Determine vertically 5 in a row.
		for (int col = 0; col <= gameBoard.length - 5; col++) {
			for (int row = 0; row <= gameBoard.length - 1; row++) {
				// Save current char after placePiece has finished
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					// Check to see if the column ahead up to 5 is not equal to letter
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
		// Determine top-right to bottom-left 5 in a row
		for (int col = 0; col <= gameBoard.length - 5; col++) {
			for (int row = 4; row <= gameBoard.length - 1; row++) {
				// Save current char after placePiece has finished
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					// Check to see if the column down one and row left one is not equal to letter 5 times in a row.
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
		// Determine top-left to bottom-right 5 in a row.
		for (int col = 0; col <= gameBoard.length - 5; col++) {
			for (int row = 0; row <= gameBoard.length - 5; row++) {
				// Save current char after placePiece has finished
				char letter = gameBoard[col][row];
				if (gameBoard[col][row] != '-') {
					boolean isWin = true;
					// Check to see if the column down one and row right one is not equal to letter 5 times in a row.
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
	 * @return boolean false if the game is tied. True if game is not tied.
	 */
	public static boolean isGameTied() {
		for (int col = 0; col < gameBoard.length; col++) {
			for (int row = 0; row < gameBoard.length; row++) {
				// If a '-' still exists on the board, return false, otherwise return true
				if (gameBoard[col][row] == '-') {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This checks to see if the game is done or not
	 * @return true if the game is tied or won by someone. False if it is not.
	 */
	public static boolean isGameDone() {
		if (isGameWon()) {
			return true;
		} else if (isGameTied()) {
			return true;
		} else if (forfeit) {
			return true;
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
			// Attempts to load a file based on the name user types
			streamIn = new FileInputStream(fileName);
			objectinputstream = new ObjectInputStream(streamIn);
			// Reads the file from the player
			playerOne = (Player) objectinputstream.readObject();
			// Creates new player if file cannot be found
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
				System.out.println("");
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
		String name;
		System.out.println("Player 2's Name: ");
		do {
			String userName = keyb.next();
			name = userName;
			System.out.println("Loading Data...");
			// Check to see if second player name is not the same as first player
			if (!isNameChecked(name)) {
				System.out.println("The name has been already taken by player one. Please choose another name.");
			}
		} while (!isNameChecked(name));
		// Assign their name as the filename
		fileName = name;
		try {
			// Attempts to load a file based on the name user types
			streamIn = new FileInputStream(fileName);
			objectinputstream = new ObjectInputStream(streamIn);
			// Reads the file from the player
			playerTwo = (Player) objectinputstream.readObject();
			// Creates new player if file cannot be found
			if (playerTwo == null) {
				System.out.println("No player found, Creating new player.");
				playerTwo = new Player();
				playerTwo.name = name;
			} else {
				System.out.println("Loaded!");
			}
		} catch (Exception e) {
			System.out.println("No player found, Creating new player.");
			playerTwo = new Player();
			playerTwo.name = name;
		} finally {
			// Closes input stream
			try {
				objectinputstream.close();
			} catch (Exception e) {
				System.out.println("");
			}
		}
		runP2();
	}
	
	/**
	 * This checks if the player two name is the same as the player one name
	 * 
	 * @param name The player two name
	 * @return true if player two's name is not the same as player one. False if both names are the same
	 */
	public static boolean isNameChecked(String name) {
		if (name.equalsIgnoreCase(playerOne.name)) {
			return false;
		}
		return true;
	}

	/**
	 * This save player 1's stats to the file assigned.
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
			// Closes Object Output Stream
			oos.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	/**
	 * This saves player 2's stats to the file assigned.
	 */
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
			// Closes Object Output Stream
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
