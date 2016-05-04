import java.util.Scanner;

public class Main {

	static Scanner keyb = new Scanner(System.in);

	static char[][] gameBoard = null;
	static boolean playerTurn = true;
	static final int BOARDSIZE = 15;

	public static void main(String[] args) {
		// printIntro();
		// printPlayerStat();
		setupBoard();
		do {
			placePiece();
			printBoard(gameBoard);
		} while (!isGameWon());
		System.out.println("Win");
	}

	public static void startGame() {
		boolean gameStart = false;
		do {
			try {
				String numberStart = keyb.next();
				if (numberStart.equalsIgnoreCase("p")) {
					gameStart = true;
				} else {
					System.out.println("Invalid Command!");
				}
			} catch (Exception e) {
				System.out.println("Invalid Command!");
			}
		} while (!gameStart);
	}

	// Set Player name and stats
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

	// Prints introduction of game
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

	// Creates new array making the board
	public static void setupBoard() {
		gameBoard = new char[BOARDSIZE][BOARDSIZE]; // Declare new array of BOARDSIZE x BOARDSIZE
		for (int i = 0; i < gameBoard.length; i++) { // Loop row up by one
			for (int j = 0; j < gameBoard[i].length; j++) { // In each column, loop the column up by one
				gameBoard[i][j] = '-'; // Insert '-' in each index
			}
		}
		printBoard(gameBoard); // Prints the board to the console
	}

	// Prints board to console
	public static void printBoard(char[][] data) {
		System.out.println("A B C D E F G H I J K L M N O");
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[row].length; col++) {
				System.out.print(data[row][col] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
		if (playerTurn) {
			System.out.println("Player " + 1 + "'s Turn");
		} else {
			System.out.println("Player " + 2 + "'s Turn");
		}
	}

	// Get letter coordinate from player
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

	// Get number coordinate from player
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

	// Returns a piece as X or O and returns whether valid move or not
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

	// Determine 5 in a row (Win Condition).
	public static boolean isGameWon() {
		// Determine horizontal 5 in a row.
		for (int col = 0; col <= gameBoard.length - 5; col++) { // loop through the column
			for (int row = 0; row <= gameBoard.length - 5; row++) { // loop through the row
				char letter = gameBoard[col][row]; // Assign the character chosen to letter
				if (gameBoard[col][row] != '-') { // Check to see if index is X or O
					boolean isWin = true;
					for (int i = 1; i < 5; i++) { // Counter up to 5
						if (gameBoard[col][row + i] != letter) { // Checks if next index equals letter assigned
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
		for (int col = 0; col <= gameBoard.length - 5; col++) { // loop through the row
			for (int row = 0; row <= gameBoard.length - 5; row++) { // loop through the column
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
		// Determine diagonally 5 in a row.
		for (int col = 0; col <= gameBoard.length - 5; col++) { // loop through the row
			for (int row = 0; row <= gameBoard.length - 5; row++) { // loop through the column
				char letter = gameBoard[col][row]; // Assign the character chosen to letter
				if (gameBoard[col][row] != '-') { // Check to see if index is X or O
					boolean isWin = true;
					for (int i = 1; i < 5; i++) { // Counter up to 5
						if (gameBoard[col + i][row + i] != letter)  { // Checks if next index equals letter assigned
							isWin = false;
							break;
						} else if (gameBoard[col - i][row + i] != letter) {
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
