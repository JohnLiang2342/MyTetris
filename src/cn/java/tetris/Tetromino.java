package cn.java.tetris;

public class Tetromino {
	
	/**
	 *每个形状四个格子
	 *protected修饰符，以被继承类调用，也可以被自己类里的函数调用，但不能被其他的类调用
	 */
	protected Cell[] cells = new Cell[4];
	
	
	
	/**随机生成一个四格方块*/
	public static Tetromino random_one() {
		Tetromino t = null;
		int num = (int)(Math.random()*7);
		switch(num) {
		case 0:t = new I();break;
		case 1:t = new J();break;
		case 2:t = new L();break;
		case 3:t = new O();break;
		case 4:t = new S();break;
		case 5:t = new T();break;
		case 6:t = new Z();break;	
		}
		return t;
	}
	
	
	public void move_down() {
		for(Cell c : cells ) {
			c.moveDown();
		}
		
	}
	
	public void move_left() {
		for(Cell c : cells) {
			c.moveLeft();	
		}
	}
	
	public void move_right() {
		for(Cell c : cells) {
			c.moveRight();
		}
	}
	
	
	/**
	 * iRow,iCol 中心点
	 * nRow,nCol 旋转后
	 */
	public void turn_right() {
		Cell center = cells[0];
		int iRow = center.getRow();
		int iCol = center.getCol();
		for(Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			int nRow = iRow-iCol+col;
			int nCol = iRow+iCol-row;
			c.setRow(nRow);
			c.setCol(nCol);
		}
	}


	public void turn_left() {
		Cell center = cells[0];
		int iRow = center.getRow();
		int iCol = center.getCol();
		for(Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			int nRow = iRow+iCol-col;
			int nCol = iCol-iRow+row;
			c.setRow(nRow);
			c.setCol(nCol);
		}
		
		
	}
	
	

}
