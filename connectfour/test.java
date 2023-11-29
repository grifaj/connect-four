package connectfour;

import java.util.Arrays;

public class test {

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

	public static long[] c(String s) {
		String mask = "";
		String position = "";
		String[] x  = s.split(",");
		for(int a = 41;a>=0;a--) {
			if((a+1)%6 ==0) {
				mask+="0";
				position+="0";
			}
			if(!x[a].equals("b")) {
				mask+="1";
				if(x[a].equals("x")) {
					position+="1";
				}else {
					position+="0";
				}
			}
			else {
				mask+="0";
				position+="0";
			}
		} 
		
		long[] ret = { Long.parseLong(mask, 2), Long.parseLong(position, 2) };
		return (ret);
	}

	public static void main(String[] args) {

		String[][] bruh = { { ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", "." },
				{ ".", ".", ".", ".", ".", "." }, { ".", ".", ".", "0", "1", "0" }, { ".", ".", ".", ".", ".", "." },
				{ ".", ".", ".", ".", ".", "." }, { ".", ".", ".", "1", "0", "1" }, };
		//Connect4.printBoard(bruh);
		//System.out.println(Arrays.toString(convert(bruh,1)));
		/*
		String a= "x,o,o,x,x,b,b,b,b,b,b,b,b,b,b,b,b,b,o,o,b,b,b,b,b,b,b,b,b,b,x,b,b,b,b,b,b,b,b,b,b,b,loss";
		String b = "b,b,b,b,b,b,x,b,b,b,b,b,b,b,b,b,b,b,o,o,b,b,b,b,b,b,b,b,b,b,b,b,b,b,b,b,x,o,o,x,x,b,loss";
		BoardEvaluation.printBoard(c(a), 1);
		BoardEvaluation.printBoard(c(b), 1);
		String out = "";
		for(int x  = 7;x>0;x--) {
			System.out.println(a.substring((x-1)*12,x*12));
			out = out + a.substring((x-1)*12,x*12);
		}
		out = out+b.substring(84,b.length());
		System.out.println(out);
		System.out.println(b);
		
		//long[] temp = {136339441860737l,109951162793984l};
		long[] temp = {4432674684959l,268435481};
		BoardEvaluation.printBoard(temp, 1);
		*/
		int play =1;
		long[] before = BoardEvaluation.convert(bruh, play);
		System.out.println(Arrays.toString(before));
		BoardEvaluation.printBoard(before, play);
		long[] after = BoardEvaluation.addMove(before, 0);
		BoardEvaluation.printBoard(after, 1-play);
		System.out.println(Arrays.toString(after));

		
	

	}

}
