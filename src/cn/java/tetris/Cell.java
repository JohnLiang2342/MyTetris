package cn.java.tetris;

import java.awt.image.BufferedImage;

public class Cell {
	//行
	private int row;
	//列 column
	private int col;
	private BufferedImage image;
	
	public Cell(int row,int col,BufferedImage image) {
		this.row = row;
		this.col = col;
		this.image = image;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	
	public void moveDown() {
		row++;
	}
	public void moveLeft() {
		col--;
	}
	public void moceRight() {
		col++;
	}

}
