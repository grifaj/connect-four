package connectfour;

//import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class Benchmark {

	public static long[] convert(String board) {
		long[] temp = { 0, 0 };
		for (int a = 0; a < board.length(); a++) {
			temp = addMove(temp, Integer.parseInt(board.substring(a, a + 1)) - 1);
		}
		return temp;

	}

	public static long[] addMove(long[] board, int col) {// returns board of next players turn
		long mask = board[0];
		long position = board[1];

		long enemy = position ^ mask;
		mask = mask | (mask + (1L << (col * 7)));
		long[] ret = { mask, enemy };
		return ret;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader t1 = new BufferedReader(new FileReader(
				"\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\testData1"));
		BufferedReader t2 = new BufferedReader(new FileReader(
				"\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\testData2"));
		BufferedReader t3 = new BufferedReader(new FileReader(
				"\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\testData3"));
		BufferedReader t4 = new BufferedReader(new FileReader(
				"\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\testData4"));
		BufferedReader t5 = new BufferedReader(new FileReader(
				"\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\testData5"));
		BufferedReader t6 = new BufferedReader(new FileReader(
				"\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\testData6"));
		BufferedReader[] set = { t1, t2, t3, t4, t5, t6 };
		
		BoardEvaluation.createMaps();
		for (int a = 0; a < 1; a++) {
			int count = 0;
			long start = System.nanoTime();
			while (true) {
				String next = null;
				try {
					next = set[a].readLine();
				} catch (Exception e) {
					break;
				}
				if (next == null) {
					break;
				}
				String[] test = next.split(" ");
				long[] ques = convert(test[0]);
				int ans = Integer.parseInt(test[1]);
				//EvenFaster.printBoard(ques, 1);
				//System.out.println(ans);

				// change ans to weak solve
				if (ans <= -1) {
					ans = -1;
				} else if (ans >= 1) {
					ans = 1;
				}
				
				int guess = BoardEvaluation.bestScore(ques,1);
				System.out.println("assertEquals("+ans+",BoardEvaluation.bestScore(new long[] {"+Arrays.toString(ques)+"},1));");
				if (guess != ans) {
					System.out.println("wrong: "+guess+" answer: "+ans);
					BoardEvaluation.printBoard(ques, 1);
					System.out.println(Arrays.toString(ques));
				}
				count++;
				if(count  == 10) {
					break;
				}
			}
			System.out.println("test "+(a+1));
			System.out.println("total time: " + (System.nanoTime() - start) / 1000000000.0);
			System.out.println("time per case " + ((System.nanoTime() - start) / 1000000000.0) / count);
			
			/*
			test 1
			total time: 0.7337915
			time per case 0.06727680909090909
			test 2
			total time: 0.4900371
			time per case 0.04455671818181818
			test 3
			total time: 1709.0551913
			time per case 155.36866835454546
			test 4
			total time: 23373.5177414
			time per case 2124.8652633454544
			test 5
			total time: 13122.8771013
			time per case 1192.9888349545454

			 */

		}
	}

}
