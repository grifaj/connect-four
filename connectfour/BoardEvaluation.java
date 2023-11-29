package connectfour;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Map;

public class BoardEvaluation {

	static int w = 7; // width
	static int h = 6; // height
	static int[] order = { 3, 2, 4, 1, 5, 0, 6 };
	static int maxSize = 8000000;
	static HashMap<String, Integer> map = new HashMap<String, Integer>();// eight ply moves
	static LinkedHashMap<String, int[]> cache = new LinkedHashMap<String, int[]>();
	static HashMap<String, Integer> cheatData = new HashMap<String, Integer>();
	static long start;
	static int maxDepth;

	public static int negmax(long[] board, int play, int alpha, int beta, int depth) {
		long mask = board[0];
		long position = board[1];
		// System.out.println("ply "+Long.bitCount(mask));

		//if (depth > maxDepth) {
		//	return 0;
		//}

		//if (((System.nanoTime() - start) / 1000000000.0) > 3) {
		//	return 0;
		//}

		if (Long.bitCount(mask) == h * w) {// if a draw
			return 0;
		}

		for (int a = 0; a < w; a++) {// for each column, can it be played and is that then a win
			int col = order[a];
			if (playableCol(board, col) && winNextTurn(board, col)) {
				return 1;
			}
		}

		if (Long.bitCount(mask) == 8) {
			try {
				int saved = map.get(mask + "," + position);
				return saved;
			} catch (Exception e) {
				return 0;// shouldn't really do that
			}

		}

		// see if upper bound is already found
		int max;
		int min;
		try {
			int[] value = cache.get(mask + "," + position);
			max = value[1];
			min = value[0];
		} catch (Exception e) {
			max = 1;// score if we win
			min = -1;
		}
		if (alpha < min) {
			alpha = min;
		}

		if (beta > max) {
			beta = max;
			if (alpha >= beta) {// best score possible is lower than current best score
				return beta;
			}
		}

		for (int a = 0; a < w; a++) {// look through all possible moves
			int col = order[a];
			if (playableCol(board, col)) {
				// update board with move in that column
				//printBoard(addMove(board, col),1-play);
				int score = -negmax(addMove(board, col), (1 - play), -beta, -alpha, depth + 1);
				if (score >= beta) {
					return score;
				}
				if (score > alpha) {
					alpha = score;
				}
			}
		}

		int[] temp = { alpha, beta };
		cache.put(mask + "," + position, temp);
		return alpha;
	}

	public static boolean winNextTurn(long[] board, int col) {
		long mask = board[0];
		long position = board[1];

		long enemy = position ^ mask;
		mask = mask | (mask + (1L << (col * 7)));// add move to board
		position = mask ^ enemy;
		long[] temp = { mask, position };

		return checkForWin(temp);
	}

	public static long[] addMove(long[] board, int col) {// returns board of next players turn
		long mask = board[0];
		long position = board[1];

		long enemy = position ^ mask;
		mask = mask | (mask + (1L << (col * 7)));
		long[] ret = { mask, enemy };
		return ret;
	}

	// only checks for win for current player
	public static boolean checkForWin(long[] board) {
		long position = board[1];
		// Horizontal
		if (((position & (position >> 7)) & ((position & (position >> 7)) >> 14)) > 0) {
			return true;
		}
		// diagonal \
		if (((position & (position >> 6)) & ((position & (position >> 6)) >> 12)) > 0) {
			return true;
		}
		// diagonal /
		if (((position & (position >> 8)) & ((position & (position >> 8)) >> 16)) > 0) {
			return true;
		}
		// vertical
		if (((position & (position >> 1)) & ((position & (position >> 1)) >> 2)) > 0) {
			return true;
		}
		return false;
	}

	public static boolean playableCol(long[] board, int col) {
		long mask = board[0];
		long top = Long.parseLong("100000", 2);
		top = top << 7 * col;

		return ((top & mask) == 0);

	}

	public static void createMaps() throws Exception {
		// create hashmap of 8 ply moves
		String line;
		File file = new File("connectfour/moveSet");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(":");
			map.put(parts[0], Integer.parseInt(parts[1]));
		}
		reader.close();

		// System.out.println("hashmap stored " + (System.nanoTime() - start) /
		// 1000000000.0);
		// start = System.nanoTime();


	}

	public static int bestMove(long[] board, int play) {
		try {
			//maxDepth = Connect4old.diff.getValue();
			maxDepth = Connect4.diff.getValue();
		} catch (Exception e) {
			maxDepth = 50;
		}
		cache = new LinkedHashMap<String, int[]>();
		int bestScore = -10;
		int bestCol = -1;
		start = System.nanoTime();
		// check to see if win on next turn
		for (int a = 0; a < w; a++) {
			int col = order[a];
			if (playableCol(board, col)) {
				long[] tBoard = { addMove(board, col)[0], addMove(board, col)[1] ^ addMove(board, col)[0] };
				// printBoard(tBoard,play);
				if (checkForWin(tBoard)) {
					// System.out.println("win next turn");
					return col;
				}
			}
		}
		// check to see if enemy can win if they had next turn and play in that column
		for (int a = 0; a < w; a++) {
			int col = order[a];
			if (playableCol(board, col)) {
				long[] enemyBoard = { board[0], board[1] ^ board[0] };
				long[] tBoard1 = { addMove(enemyBoard, col)[0],
						addMove(enemyBoard, col)[1] ^ addMove(enemyBoard, col)[0] };
				// printBoard(tBoard1,play);
				if (checkForWin(tBoard1)) {
					// System.out.println("loss next turn");
					return col;
				}
			}
		}

		for (int a = 0; a < w; a++) {
			int col = order[a];
			if (playableCol(board, col)) {
				// System.out.println("trying col " + col);
				int score = -negmax(addMove(board, col), 1 - play, -1, 1, 0);
				if (score == 1) {
					return col;
				}
				if (score > bestScore) {
					bestScore = score;
					bestCol = col;
				}

			}
		}

		return bestCol;
	}

	// can't use block move as shortcut
	public static int bestScore(long[] board, int play) {
		cache = new LinkedHashMap<String, int[]>();
		int bestScore = -1;
		// check to see if win on next turn
		for (int a = 0; a < w; a++) {
			int col = order[a];
			if (playableCol(board, col)) {
				long[] tBoard = { addMove(board, col)[0], addMove(board, col)[1] ^ addMove(board, col)[0] };
				// printBoard(tBoard,play);
				if (checkForWin(tBoard)) {
					return 1;
				}
			}
		}

		for (int a = 0; a < w; a++) {
			int col = order[a];
			if (playableCol(board, col)) {
				int score = -negmax(addMove(board, col), 1 - play, -1, 1, 0);
				if (score == 1) {
					return 1;
				}
				if (score > bestScore) {
					bestScore = score;
				}

			}
		}

		return bestScore;
	}

	public static long[] convert(String[][] f, int play) {

		String mask = "";
		String position = "";
		for (int b = 6; b >= 0; b--) {
			mask = mask + "0";
			position = position + "0";
			for (int a = 0; a < 6; a++) {
				if (f[b][a].equals(".")) {
					mask = mask + "0";
					position = position + "0";
				} else {
					mask = mask + "1";
					if (f[b][a].equals(play + "")) {
						position = position + "1";
					} else {
						position = position + "0";
					}
				}
			}
		}

		long[] ret = { Long.parseLong(mask, 2), Long.parseLong(position, 2) };
		return (ret);
	}

	public long[] convert(String s) {
		String mask = "";
		String position = "";
		String[] x = s.split(",");
		for (int a = 41; a >= 0; a--) {
			if ((a + 1) % 6 == 0) {
				mask += "0";
				position += "0";
			}
			if (!x[a].equals("b")) {
				mask += "1";
				if (x[a].equals("x")) {
					position += "1";
				} else {
					position += "0";
				}
			} else {
				mask += "0";
				position += "0";
			}
		}

		long[] ret = { Long.parseLong(mask, 2), Long.parseLong(position, 2) };
		return (ret);
	}

	public static void printBoard(long[] board, int play) {
		Connect4.printBoard(Connect4.convertBack(board, play));
	}

	public static void main(String[] args) throws Exception {
		/*
		String[][] bruh = { { ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." },
				{ ".", ".", "0", "1", "1", "1" }, { "1", "0", "1", "1", "1", "0" }, { ".", ".", ".", "1", "0", "0" },
				{ ".", ".", ".", "0", "0", "0" }, { ".", ".", ".", "0", "0", "1" }, };

		int play = 1;
		// Connect4.printBoard(bruh);
		long[] board = {277609240321951l, 220081105766931l};
		printBoard(board, play);

		start = System.nanoTime();
		createMaps();
		System.out.println(bestMove(board, play));
		System.out.println(bestScore(board, play));
		System.out.println("time " + (System.nanoTime() - start) / 1000000000.0);
		*/

	}

}
