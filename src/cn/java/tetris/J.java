package cn.java.tetris;

/**
 * @author Administrator
 * 父类属性：protected Cell[] cells = new Cell[4];
 */
public class J extends Tetromino {
	
	public J() {
		cells[0] = new Cell(0,4,Tetris.J);
		cells[1] = new Cell(0,3,Tetris.J);
		cells[2] = new Cell(0,5,Tetris.J);
		cells[3] = new Cell(1,5,Tetris.J);	
	}

}
