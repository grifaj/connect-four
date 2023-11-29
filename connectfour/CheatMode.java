package connectfour;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.FileWriter;

public class CheatMode {

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

		//BufferedWriter writer = new BufferedWriter(new FileWriter("\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\cheatData"));
		int count =0;
		for (int a = 0; a < 6; a++) {
			while (true) {
				String next = null;
				try {
					next = set[a].readLine();
					count++;
				} catch (Exception e) {
					break;
				}
				if (next == null) {
					break;
				}
				String[] test = next.split(" ");
				long[] ques = convert(test[0]);
				int ans = Integer.parseInt(test[1]);
				if(ans>=1) {
					ans = 1;
				}else if(ans<=-1) {
					ans = -1;
				}
				System.out.println(ques[0]+","+ques[1]+":"+ans);
				//writer.write(ques[0]+","+ques[1]+":"+ans+"\n");
				
			}

		}
		System.out.println(count);
	}
}
