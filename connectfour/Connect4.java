package connectfour;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;
//import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Connect4 implements ActionListener, ChangeListener {
	JFrame frame;
	JLabel diffLabel;
	JButton onePlayer, twoPlayer, saveGame, loadGame, selfPlay, mainMenu, playStart, initialQuit, gamePlay;
	JPanel menu, start, game, gameMenu;
	static Cell[] place;
	Cell[] startBoard;
	JCheckBox teacher;
	static JSlider diff;
	JRadioButton goFirst, goSecond;
	ButtonGroup group;
	int h, w, turn;
	int gameType = -1;
	boolean continued = false;
	boolean gameStarted = false;
	boolean applicationStart = false;
	boolean readyToStart = false;

	// red =1 yellow =0

	public static void main(String[] args) {
		new Connect4();

	}

	public void gameStart() {
		gameStarted = true;
		// either one or two players include bot for one player

		gameMenu = new JPanel();
		gameMenu.setLayout(new BoxLayout(gameMenu, BoxLayout.Y_AXIS));
		gameMenu.setBorder(BorderFactory.createLineBorder(Color.black));

		// save/load game
		saveGame = new JButton("save game");
		saveGame.addActionListener(this);
		loadGame = new JButton("load game");
		loadGame.addActionListener(this);
		gamePlay = new JButton("start");
		gamePlay.addActionListener(this);

		mainMenu = new JButton("back to main menu");
		mainMenu.addActionListener(this);

		if (gameType == 1) {
			// difficulty slider
			diff = new JSlider(JSlider.HORIZONTAL, 1, 42, 20); // max depth set here
			diff.addChangeListener(this);
			diff.setMajorTickSpacing(5);
			diff.setMinorTickSpacing(1);
			diff.setPaintTicks(true);

			// first/second selector
			goFirst = new JRadioButton("first");
			goFirst.setSelected(true);
			goFirst.addActionListener(this);
			goSecond = new JRadioButton("second");
			goSecond.addActionListener(this);
			group = new ButtonGroup();
			group.add(goFirst);
			group.add(goSecond);

			diffLabel = new JLabel("current difficulty: " + diff.getValue());

			gameMenu.add(diffLabel);
			gameMenu.add(diff);
			gameMenu.add(goFirst);
			gameMenu.add(goSecond);

		}
		gameMenu.add(mainMenu);
		gameMenu.add(teacher);
		gameMenu.add(saveGame);
		gameMenu.add(loadGame);
		gameMenu.add(gamePlay);

		frame.remove(menu);
		frame.add(gameMenu, BorderLayout.WEST);

		JPanel game = createBoard();
		frame.add(game, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.repaint();
	}

	public void initialScreen() {
		// frame setup
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		h = screenSize.height;
		w = screenSize.width;
		frame = new JFrame("connect four");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(w / 2, h / 2);
		frame.setLayout(new BorderLayout());

		// change initial screen to a main menu
		start = new JPanel(new GridBagLayout());
		// start.setBorder(BorderFactory.createLineBorder(Color.green));

		// play and quit buttons
		playStart = new JButton("play");
		playStart.addActionListener(this);
		initialQuit = new JButton("quit");
		initialQuit.addActionListener(this);

		start.add(playStart);
		start.add(initialQuit);
		start.add(createBoard());

		frame.add(start, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.repaint();

	}

	public Connect4() {
		initialScreen();// creates start
		Cell.party(false);

		// play game at the start random moves
		while (!this.applicationStart) {
			counterPlacement(ThreadLocalRandom.current().nextInt(0, 7));
			winStatment();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		clearField();

		while (true) {
			gameType = -1;
			// wait for player choice for game
			while (gameType == -1) {
				System.out.print("");
			}
			gameStart();
			disableBoard();
			while (!winStatment() && gameStarted) {
				if (readyToStart) {
					enableBoard();
					if (gameType == 1) {
						if (turn % 2 == 0 && goSecond.isSelected()) {
							disableBoard();
							botMove();
							enableBoard();
						}
						if (turn % 2 != 0 && goFirst.isSelected()) {
							disableBoard();
							botMove();
							enableBoard();
						}
					}else if(gameType==0) {
						botMove();
					}
				}
			}
			readyToStart = false;

		}
	}

	// shown after clicking play
	public void menuStart() {
		frame.remove(start);

		// side menu buttons
		twoPlayer = new JButton("two player");
		twoPlayer.addActionListener(this);
		onePlayer = new JButton("one player");
		onePlayer.addActionListener(this);
		selfPlay = new JButton("zero player");
		selfPlay.addActionListener(this);

		// menu panel setup
		menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.setBorder(BorderFactory.createLineBorder(Color.black));

		teacher = new JCheckBox("party time");
		teacher.addActionListener(this);

		menu.add(new JLabel("Menu"));
		menu.add(onePlayer);
		menu.add(twoPlayer);
		menu.add(selfPlay);

		frame.add(menu, BorderLayout.WEST);
		Cell.party(false);
		frame.repaint();
		frame.setVisible(true);
	}

	public JPanel createBoard() {
		turn = 0;
		// local player game panel setup
		JPanel game = new JPanel(new BorderLayout());
		// game.setBorder(BorderFactory.createLineBorder(Color.yellow));

		place = new Cell[7 * 6];
		JPanel board = new JPanel(new GridLayout(6, 7, 0, 0));// set board to empty if new game
		for (int a = 0; a < 7 * 6; a++) {
			place[a] = new Cell();
			if (continued) {
				// set up board from save file
				if (field[a % 7][a / 7].equals("1")) {// [6][5] //00,10,20,30...01,11,21
					place[a].updateCell(CellState.P2);
				} else if (field[a % 7][a / 7].equals("0")) {
					place[a].updateCell(CellState.P1);
				}

			}
			place[a].addActionListener(this);
			board.add(place[a]);
		}
		JPanel cage = new JPanel(); // put board inside so it doesn't spread out
		// board.setBorder(BorderFactory.createLineBorder(Color.green));
		cage.add(board);
		// cage.setBorder(BorderFactory.createLineBorder(Color.red));

		game.add(cage, BorderLayout.CENTER);
		this.game = game;
		return game;
	}

	public void disableBoard() {
		for (int a = 0; a < 6 * 7; a++) {
			place[a].setEnabled(false);
		}
	}

	public void enableBoard() {
		for (int a = 0; a < 6 * 7; a++) {
			place[a].setEnabled(true);
		}
	}

	public static String[][] field = { { ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." },
			{ ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." },
			{ ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." }, };

	public static void clearField() {
		// clear the hidden board
		for (int a = 0; a < 7; a++) {
			for (int b = 0; b < 6; b++) {
				field[a][b] = ".";
			}
		}
		// clear the visual board
		for (int a = 0; a < 7 * 6; a++) {
			place[a].updateCell(CellState.EMPTY);

		}
	}

	public boolean winStatment() {
		// sets winner, if there is one, to current player
		String msg = "yellow";
		if (turn % 2 == 0) {
			msg = "red";
		}
		// check for win
		String message = "";
		int state = checkForWin(field);
		//String[] s = { "no win", "win", "draw" };
		// System.out.println("game state: " + s[state]);
		if (state == 1) {
			System.out.println(msg + " wins");
			message = msg + " wins";
		} else if (state == 2) {
			System.out.println("its a draw");
			message = "its a draw";
		}

		// pop up for restart, quit

		if (state != 0) {
			if (applicationStart) {
				Object[] ob = { "Restart", "Quit" };
				int choice = JOptionPane.showOptionDialog(null, message, "Game over", 0, 1, null, ob, null);
				System.out.println("choice " + ob[choice]);
				if (choice == 1) {
					// remove game, clear both boards
					frame.remove(game);
					frame.repaint();
					frame.remove(gameMenu);
					clearField();
					menuStart();
				} else {// restart
					// remove everything then add back board
					frame.remove(game);
					frame.repaint();
					clearField();
					gameStart();

				}
			} else {
				clearField();
				frame.remove(game);
				// start.add(createBoard());
			}
			return true;

		}
		return false;
	}

	public void counterPlacement(int col) {// puts counter on both boards
		// put piece on hidden field

		int row = 5;
		for (int a = 5; a >= 0; a--) {
			if (field[col][a].equals(".")) {
				if (turn % 2 == 0) {
					field[col][a] = "1";
				} else {
					field[col][a] = "0";
				}
				break;
			} else {
				row--;
				// will be to -1 if col is full, if so discount move and
				// ask again
			}
		}
		// check for full col
		if (row == -1) {
			// System.out.println("col " + col + " full");

		} else {
			// if col not full:
			if (turn % 2 == 0) {
				place[col + (row * 7)].updateCell(CellState.P2);
			} else {
				place[col + (row * 7)].updateCell(CellState.P1);
			}

			turn++;
			frame.repaint();
			// check for a win
			// winStatment();
		}

	}

	public static int checkForWin(String[][] f) {// 1 for a win ,0 for no win, 2
													// for draw
		String id = "1";
		String e = "0";
		boolean space = false;
		// Vertical
		for (int col = 0; col < 7; col++) {
			for (int row = 5; row >= 3; row--) {
				if (f[col][row].equals(id) && f[col][row - 1].equals(id) && f[col][row - 2].equals(id)
						&& f[col][row - 3].equals(id)) {
					// System.out.println("win by verticle");
					return 1;
				}
				if (f[col][row].equals(e) && f[col][row - 1].equals(e) && f[col][row - 2].equals(e)
						&& f[col][row - 3].equals(e)) {
					// System.out.println("win by verticle");
					return 1;
				}

			}
		}
		// Horizontal
		for (int row = 5; row >= 0; row--) {
			for (int col = 0; col < 4; col++) {
				if (f[col][row].equals(id) && f[col + 1][row].equals(id) && f[col + 2][row].equals(id)
						&& f[col + 3][row].equals(id)) {
					// System.out.println("win by horizontal");
					return 1;

				}
				if (f[col][row].equals(e) && f[col + 1][row].equals(e) && f[col + 2][row].equals(e)
						&& f[col + 3][row].equals(e)) {
					// System.out.println("win by horizontal");
					return 1;

				}

			}
		}
		// >
		// diagonal /
		for (int row = 5; row >= 3; row--) {
			for (int col = 0; col < 4; col++) {
				if (f[col][row].equals(id) && f[col + 1][row - 1].equals(id) && f[col + 2][row - 2].equals(id)
						&& f[col + 3][row - 3].equals(id)) {
					// System.out.println("win by diagonal");
					return 1;
				}
				if (f[col][row].equals(e) && f[col + 1][row - 1].equals(e) && f[col + 2][row - 2].equals(e)
						&& f[col + 3][row - 3].equals(e)) {
					// System.out.println("win by diagonal");
					return 1;
				}
			}

		}
		// <
		// \
		for (int row = 5; row >= 3; row--) {
			for (int col = 6; col >= 3; col--) {
				if (f[col][row].equals(id) && f[col - 1][row - 1].equals(id) && f[col - 2][row - 2].equals(id)
						&& f[col - 3][row - 3].equals(id)) {
					// System.out.println("win by diagonal");
					return 1;
				}
				if (f[col][row].equals(e) && f[col - 1][row - 1].equals(e) && f[col - 2][row - 2].equals(e)
						&& f[col - 3][row - 3].equals(e)) {
					// System.out.println("win by diagonal");
					return 1;
				}
			}

		}
		for (int col = 0; col < 7; col++) {
			for (int row = 0; row < 6; row++) {
				if (f[col][row].equals(".")) {
					space = true;
				}
			}
		}
		if (space == false) {
			return 2;
		}
		return 0;
	}

	public static void printBoard(String[][] field) {
		for (int a = 0; a < 6; a++) {
			for (int b = 0; b < 7; b++) {
				System.out.print(field[b][a] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void botMove() {

		try {
			BoardEvaluation.createMaps();
		} catch (Exception e1) {
			System.out.println("maps");
			e1.printStackTrace();
		}
		counterPlacement(BoardEvaluation.bestMove(BoardEvaluation.convert(field, 0), 0));

	}

	public static String[][] convertBack(long[] board, int play) {
		String[][] field = { { ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." },
				{ ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." },
				{ ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." }, };

		String p = "" + play;
		String e = "" + (1 - play);

		String player = Long.toBinaryString(board[1]);
		while (player.length() < 49) {
			player = "0" + player;
		}
		// System.out.println(player);
		for (int a = 0; a < (player).length(); a++) {
			if (player.substring(a, a + 1).equals("1")) {
				field[6 - (a / 7)][(a % 7) - 1] = p;

			}
		}
		String enemy = Long.toBinaryString(board[1] ^ board[0]);
		while (enemy.length() < 49) {
			enemy = "0" + enemy;
		}
		// System.out.println(enemy);
		for (int a = 0; a < (enemy).length(); a++) {
			if (enemy.substring(a, a + 1).equals("1")) {
				field[6 - (a / 7)][(a % 7) - 1] = e;

			}
		}

		// printBoard(field);
		return field;
	}

	public void actionPerformed(ActionEvent e) {// contains all buttons

		// initial buttons
		// start
		if (e.getSource() == playStart) {
			System.out.println("play clicked");
			this.applicationStart = true;
			menuStart();
		}
		// quit
		if (e.getSource() == initialQuit) {
			frame.dispose();
		}

		// menu buttons
		if (e.getSource() == onePlayer) {
			gameType = 1;
			System.out.println("game type: one player");
		}

		if (e.getSource() == twoPlayer) {
			gameType = 2;
			System.out.println("game type: two players");

		}

		// party time
		if (e.getSource() == teacher) {
			Cell.party(teacher.isSelected());
			for (int a = 0; a < 42; a++) {
				place[a].updateCell(place[a].getCs());
			}
		}

		if (e.getSource() == selfPlay) {
			gameType = 0;
			System.out.println("game type: zero player");

		}

		// back to main menu
		if (e.getSource() == mainMenu) {
			gameStarted = false;
			frame.remove(gameMenu);
			frame.remove(game);
			clearField();
			menuStart();
		}

		if (e.getSource() == gamePlay) {
			readyToStart = true;
		}

		if (e.getSource() == saveGame) {
			String saveName = (String) JOptionPane.showInputDialog(frame, "input name of save file:", "save game",
					JOptionPane.PLAIN_MESSAGE, null, null, null);
			PrintWriter writer;
			try {
				writer = new PrintWriter(saveName, "UTF-8");
				int play = (turn % 2 == 0) ? 1 : 0;
				writer.println(BoardEvaluation.convert(field, play)[0]);
				writer.println(BoardEvaluation.convert(field, play)[1]);
				writer.println(gameType);
				writer.close();

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

		if (e.getSource() == loadGame) {
			String fileName = (String) JOptionPane.showInputDialog(frame, "input name of save file:", "load file",
					JOptionPane.PLAIN_MESSAGE, null, null, null);
			File file = new File("\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\" + fileName);
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				long[] board = { Long.parseLong(br.readLine()), Long.parseLong(br.readLine()) };
				gameType = Integer.parseInt(br.readLine());
				field = convertBack(board, 1);
				frame.repaint();
				br.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			continued = true;
			gameStart();

		}

		// counter cells
		if (gameStarted) {
			for (int a = 0; a < 6 * 7; a++) {
				if (e.getSource() == place[a]) {
					System.out.println("cell " + a + " clicked");
					counterPlacement(a % 7);

					// game has started disable first/second
					goSecond.setEnabled(false);
					goFirst.setEnabled(false);

				}
			}
		}

	}

	// actionListner for difficulty slider
	public void stateChanged(ChangeEvent e) {
		diffLabel.setText("current difficulty: " + diff.getValue());

	}

}
