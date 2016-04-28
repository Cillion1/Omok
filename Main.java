import java.util.Scanner;

public class Main {

	static Scanner keyb = new Scanner(System.in);

	static char[][] boardPlacement = null;
	static boolean playerTurn = true;
	static boolean test = true;

	public static void main(String[] args) {
		// printIntro();
		// startGame();
		// printPlayerStat();
			boardSetup();
		do {
			placePiece();
			printBoard(boardPlacement);
		} while(test);
	}

	public static void startGame() {
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
	
	// Set Player name and stats
	public static void printPlayerStat() { 
		System.out.println("Type Player 1�s username: ");
		String setPlayerOne = keyb.next();
		Player playerOne = new Player();
		playerOne.name = setPlayerOne;
		System.out.println("Player: " + playerOne.name);
		System.out.println("Wins: " + playerOne.win);
		System.out.println("Loss: " + playerOne.loss);
		System.out.println("Total Games: " + playerOne.total);
		System.out.println("Win Streak: " + playerOne.winStreak);
		System.out.println("");
		System.out.println("Type Player 2�s username: ");
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
		System.out.println("Play (S)");
		System.out.println("");
		System.out.println("Enter your choice: ");
	}

	// Creates new array making the board
	public static void boardSetup() {
		boardPlacement = new char[15][15]; // Declare new array of 15x15
		for (int i = 0; i < boardPlacement.length; i++) { // Loop row up by one
			for (int j = 0; j < boardPlacement[i].length; j++) { // In each column, loop the column up by one
                boardPlacement[i][j] = '-'; // Insert '-' in each index
			}
		}
		printBoard(boardPlacement); // Prints the board to the console
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
			if (letterNumber > 15 || letterNumber < 0 || positionLength > 1) {
				System.out.println("Invalid Move! Must be a letter before P.");
			}
		} while (letterNumber > 15 || letterNumber < 0 || positionLength > 1);
		return letterNumber;
	}

	// Get number coordinate from player
	public static int getNumberPosition() {
		int number = 0;
		do {
			try {
				String position = keyb.next();
				number = Integer.parseInt(position);
				if (number <= 0 || number > 15) {
					System.out.println("Invalid Move! Must be a positive number less than 15.");
				}
			} catch (Exception e) {
				System.out.println("Invalid Move! Not a possible coordinate.");
			}
		} while (number > 15 || number <= 0);
		return number;
	}

	// Returns a piece as X or O and returns whether valid move or not
	public static boolean placePiece() {
		// Assign numbers from user
		int x = getLetterPosition();
		int y = getNumberPosition() - 1;
		// Check on if player has already played in position
		if (boardPlacement[y][x] == '-') {
			if (playerTurn) {
				boardPlacement[y][x] = 'X';
				playerTurn = false;
			} else {
				boardPlacement[y][x] = 'O';
				playerTurn = true;
			}
			return true;
		} else {
			System.out.println("Invalid Move! Position is taken.");
			System.out.println("");
			return false;
		}
	}
}