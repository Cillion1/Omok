import java.util.Scanner;

public class Main {

	static Scanner keyb = new Scanner(System.in);

	static char[][] gameBoard = null;
	static boolean playerTurn = true;
	static boolean test = true;
	static final int BOARDSIZE = 15;

	public static void main(String[] args) {
		printIntro();
		startGame();
		// printPlayerStat();
			boardSetup();
		do {
			placePiece();
			printBoard(gameBoard);
		} while(isGameWon());
		System.out.println("Win");
	}

	public static void startGame() {
		char gameStart = 0;
		do {
			try {
				String numberStart = keyb.next();
				if (numberStart.equalsIgnoreCase("p")) {
					gameStart = 1;
				} else {
					System.out.println("Invalid Command!");
				}
			} catch (Exception e) {
				System.out.println("Invalid Command!");
			}
		} while (gameStart != 1);
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
		System.out.println("Type S to start.");
		char gameStart = 0;
		do {
			try {
				String numberStart = keyb.next();
				if (numberStart.equalsIgnoreCase("s")) {
					gameStart = 1;
				} else {
					System.out.println("Invalid Command!");
				}
			} catch (Exception e) {
				System.out.println("Invalid Command!");
			}
		} while (gameStart != 1);
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
	}

	// Creates new array making the board
	public static void boardSetup() {
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
			if (letterNumber > BOARDSIZE || letterNumber < 0 || positionLength > 1) {
				System.out.println("Invalid Move! Must be a letter before P.");
			}
		} while (letterNumber > BOARDSIZE || letterNumber < 0 || positionLength > 1);
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
					System.out.println("Invalid Move! Must be a positive number less than " + BOARDSIZE + ".");
				}
			} catch (Exception e) {
				System.out.println("Invalid Move! Not a possible coordinate.");
			}
		} while (number > BOARDSIZE || number <= 0);
		return number;
	}

	// Returns a piece as X or O and returns whether valid move or not
	public static boolean placePiece() {
		// Assign numbers from user
		int x = getLetterPosition();
		int y = getNumberPosition() - 1;
		// Check on if player has already played in position
		if (gameBoard[y][x] == '-') {
			if (playerTurn) {
				gameBoard[y][x] = 'X';
				playerTurn = false;
			} else {
				gameBoard[y][x] = 'O';
				playerTurn = true;
			}
			return true;
		} else {
			System.out.println("Invalid Move! Position is taken.");
			System.out.println("");
			return false;
		}
	}
	
	// Determine 5 in a row (Win Condition).
	public static boolean isGameWon() {
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard.length; col++) {
				for (int i = 0; i < 5; i++) {
					// Count the row up to 5 and check if X is placed in each of the indexes
					if (gameBoard[row + i][col] == 'X' && i == 5) {
						return true;
						
					}
				}
			}
		}
		return false;
	}
}
