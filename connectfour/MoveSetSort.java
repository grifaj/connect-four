package connectfour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
//import java.io.FileWriter;
//import java.util.Arrays;

public class MoveSetSort {

	public static long[] convert(String s) {
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

	public static void main(String[] args) throws Exception {
		File file = new File("F:\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\TrompData");
		BufferedReader br = new BufferedReader(new FileReader(file));
		//BufferedWriter writer = new BufferedWriter(new FileWriter("\\PortableApps\\EclipsePortable\\Data\\workspace\\connectfour\\src\\connectfour\\moveSet"));
		while (true) {
			String next = null;
			try {
				next = br.readLine();
			} catch (Exception e) {
				break;
			}
			if(next==null) {
				break;
			}
			
			//convert both board and mirror image
			long[] conv = convert(next);
			//EvenFaster.printBoard(conv, 1);

			String endState;
			if (next.substring(84, next.length()).equals("win")) {
				endState = "1";
			} else if (next.substring(84, next.length()).equals("loss")) {
				endState = "-1";
			} else {
				endState = "0";
			}
			String line = conv[0] + "," + conv[1] + ":" + endState;
			
			//System.out.println(line);
			//writer.write(line);
			//System.out.println(next);
			
			//mirror
			String out = "";
			for(int x  = 7;x>0;x--) {
				//System.out.println(next.substring((x-1)*12,x*12));
				out = out + next.substring((x-1)*12,x*12);
			}
			out = out+next.substring(84,next.length());
			
			conv = convert(out);
			//EvenFaster.printBoard(conv, 1);
			
			line = conv[0] + "," + conv[1] + ":" + endState;
			
			//writer.write(line);
			//System.out.println(line);
			
			
		}
		br.close();
	}

}
