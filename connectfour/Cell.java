package connectfour;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cell extends JButton {

	CellState cs;
	public CellState getCs() {
		return cs;
	}

	public void setCs(CellState cs) {
		this.cs = cs;
	}

	static String r;
	static String y;

	public Cell() {
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setContentAreaFilled(false);
		updateCell(CellState.EMPTY);
		
	}

	public void updateCell(CellState s) {
		this.cs = s;
		switch (s) {
		case EMPTY:
			this.setIcon(new ImageIcon("connectfour/images/emptyboardpiece.png"));
			this.setDisabledIcon(new ImageIcon("connectfour/images/emptyboardpiece.png"));
			break;
		case P1:
			this.setIcon(new ImageIcon(r));
			this.setDisabledIcon(new ImageIcon(r));
			break;
		case P2:
			this.setIcon(new ImageIcon(y));
			this.setDisabledIcon(new ImageIcon(y));
			break;
		}

	}
	
	public static void party(boolean p) {
		if(p) {
			r = "connectfour/images/ashboardpiece.png";
			y= "connectfour/images/dewbboardpiece.png";
		}
		else {
			r = "connectfour/images/redboardpiece.png";
			y= "connectfour/images/yellowboardpiece.png";
		}
	}

	
}
