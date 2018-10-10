package cn.java.tetris;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.RowFilter;

/**
 * @author John Liang
 * 
 * Tetris类  俄罗斯方块类
 * 属性：
 * game_state: 存储游戏的当前状态
 * current_one: 当前方块
 * next_one: 下一个方块
 * wall: 墙，用二维数组实现
 * score_pool[]: 分数池，单次消除一行得1分，两行得2分，三行得5分，四行得10分，最多四行
 * show_stete[]: 显示提示信息
 * total_score: 总得分
 * total_line: 总消除行数
 * CELL_SIZE: 每格长度
 * 
 * 方法：
 * start():游戏逻辑
 * 
 * 
 * 
 */
public class Tetris extends JPanel {
	
	//三个常量表示游戏状态
	private static final int PLAYING = 0;
	private static final int PAUSE = 1;
	private static final int GAMEOVER = 2;
	
	private int game_state;
	private Tetromino current_one = Tetromino.random_one();
	private Tetromino next_one = Tetromino.random_one();
	//20行10列
	private Cell[][] wall = new Cell[20][10];
	int score_pool[] = { 0, 1, 2, 5, 10 };
	String show_state[] = {"[P]暂停","[C]继续","[Enter]重玩"};
	private int total_score = 0;
	private int total_line = 0;
	private static final int CELL_SIZE = 26;
	
	//图片资源
	public static BufferedImage T;
	public static BufferedImage I;
	public static BufferedImage O;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage S;
	public static BufferedImage Z;
	public static BufferedImage background;
	public static BufferedImage game_over;
	//静态资源的加载一般都在静态块中即static块中加载资源，在本例中加载资源所需要使用的IO类是ImageIO类。所用的方法为该类中的静态方法ImageIO.read()方法
	static {
		//ImageIO.read()默认可能会抛出IOException异常，因此在加载图片资源时需要使用异常机制尝试捕获和处理异常。使用trycatch语句将静态资源加载语句封装在一起
		try {
			T = ImageIO.read(Tetris.class.getResource("T.png"));
			I = ImageIO.read(Tetris.class.getResource("I.png"));
			O = ImageIO.read(Tetris.class.getResource("O.png"));
			J = ImageIO.read(Tetris.class.getResource("J.png"));
			L = ImageIO.read(Tetris.class.getResource("L.png"));
			S = ImageIO.read(Tetris.class.getResource("S.png"));
			Z = ImageIO.read(Tetris.class.getResource("Z.png"));
			background = ImageIO.read(Tetris.class.getResource("tetris.png"));
			game_over = ImageIO.read(Tetris.class.getResource("game-over.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	//主要逻辑
	public void start() {
		
		game_state = PLAYING;
		
		//设置键盘监听事件
		KeyListener kl = new KeyAdapter() {
			//键盘按下
			@Override
			public void keyPressed(KeyEvent e) {
				
				int code = e.getKeyCode();
				switch(code) {
				case KeyEvent.VK_DOWN:
					soft_drop();
					break;
				case KeyEvent.VK_LEFT:
					move_left();
					break;
				case KeyEvent.VK_RIGHT:
					move_right();
					break;
				case KeyEvent.VK_UP:
					rotate();
					break;
				case KeyEvent.VK_SPACE:
					fast_drop();
					break;
				case KeyEvent.VK_P:
					if(game_state==PLAYING) {
						game_state = PAUSE;
					}
					break;
				case KeyEvent.VK_C:
					if(game_state==PAUSE) {
						game_state = PLAYING;
					}
					break;
				case KeyEvent.VK_ENTER:
					game_state = PLAYING;
					wall = new Cell[20][10];
					current_one = Tetromino.random_one();
					next_one = Tetromino.random_one();
					total_score = 0;
					total_line = 0;
					break;		
				}
				repaint();
			}
		};
		this.addKeyListener(kl);
		this.requestFocus();
		
		while(true) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			if(game_state==PLAYING) {
				if(can_drop()) {
					current_one.move_down();			
				}else {
					land_to_wall();
					destroy_line();
					if(!is_gameover()) {
						current_one = next_one;
						next_one = Tetromino.random_one();
					}else {
						game_state = GAMEOVER;
					}
				}
				repaint();
			}
		}
	}
	
	
	/**
	 * @return
	 * 判断游戏是否结束
	 * 获取下一个方块的位置信息
	 * 遍历cell
	 * 若对应wall中不为null，则代表位置已占用
	 */
	private boolean is_gameover() {
		Cell cells[] = next_one.cells;
		for(Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if(wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 消除行
	 */
	private void destroy_line() {
		//统计消除行数
		int lines = 0;
		Cell cells[] = current_one.cells;
		for(Cell c : cells) {
			int row = c.getRow();
			//每拿一个cell都向下循环至底层，原始cell的最高层不变，不会被墙的下落后果影响 
			while(row<20) {
				if(is_full_line(row)) {
					//记录已满行数
					lines++;
					//消除效果：新建有一个数组覆盖当前元素已满数组
					wall[row] = new Cell[10];
					for(int i = row;i > 0;i--) {
						//墙下落效果：将消除行的上行数组里从索引为0的元素开始, 复制到消除行里的索引为0的位置, 复制的元素个数为10个，循环“墙高度”次						
						System.arraycopy(wall[i - 1], 0, wall[i], 0, 10);
					}
					wall[0] = new Cell[10];
				}
				row++;
			}
		}
		total_score += score_pool[lines];
		total_line += lines;
	}

	
	
	/**
	 * @param row
	 * @return
	 * 判断是行数是否已满
	 * 
	 */
	private boolean is_full_line(int row) {
		Cell line[] = wall[row];
		for(Cell c : line) {
			if(c == null) {
				return false;
			}
		}
		return true;
	}

	
	
	/**
	 * 将方块位置信息存入wall中
	 */
	private void land_to_wall() {
		Cell cells[] = current_one.cells;
		for(Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			wall[row][col] = c;
		}
	}

	
	
	/**
	 * @return
	 * 判断是否可以下落
	 */
	private boolean can_drop() {
		Cell cells[] = current_one.cells;
		//获取每个元素的行号和列号 判断： 只要有一个元素的下一行上有方块 或者只要有一个元素到达最后一行， 就不能再下落了
		for(Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if(row == 19) {
				return false;
			}
			if(wall[row+1][col]!=null) {
				return false;
			}
		}
		return true;
	}

	
	
	public void paint(Graphics g) {
		//背景
		g.drawImage(background, 0, 0, null);
		//移动画笔
		g.translate(15, 15);
		//绘制墙
		paint_wall(g);
		//绘制当前方块
		paint_corrent_one(g);
		//绘制下一个方块
		paint_next_one(g);
		paint_score(g);
		paint_state(g);
	}
	
	
	
	/**
	 * @param g
	 * 绘制界面右边的游戏状态
	 */
	private void paint_state(Graphics g) {
		if(game_state == PLAYING) {
			g.drawString(show_state[0], 285, 265);
		}
		if(game_state == GAMEOVER) {
			g.drawImage(Tetris.game_over,0,0,null);
			g.drawString(show_state[2], 285, 265);
		}
		if(game_state == PAUSE) {
			g.drawString(show_state[1], 285, 265);
		}
	}

	
	
	/**
	 * @param g
	 * 绘制分数
	 */
	private void paint_score(Graphics g) {
		g.setFont(new Font("微软雅黑", Font.PLAIN, 26));
		g.drawString("得分：" + total_score, 285, 165);
		g.drawString("消除行数：" + total_line, 285, 215);
	}

	
	
	/**
	 * @param g
	 * 在界面右边信息栏绘制下一个方块
	 */
	private void paint_next_one(Graphics g) {
		Cell cells[] = next_one.cells;
		for(Cell c : cells) {
			int x = c.getCol()*CELL_SIZE + 260;
			int y = c.getRow()*CELL_SIZE + 26;
			g.drawImage(c.getImage(),x,y,null);
		}
		
	}

	
	
	/**
	 * @param g
	 * 绘制当前方块
	 */
	private void paint_corrent_one(Graphics g) {
		Cell cells[] = current_one.cells;
		for(Cell c : cells) {
			int x = c.getCol()*CELL_SIZE;
			int y = c.getRow()*CELL_SIZE;
			g.drawImage(c.getImage(),x,y,null);
		}
		
	}

	
	
	/**
	 * @param g
	 * 绘制wall
	 */
	private void paint_wall(Graphics g) {
		for(int i=0;i<20;i++) {
			for(int j=0;j<10;j++) {
				int x = j*CELL_SIZE;
				int y = i*CELL_SIZE;
				//获取当前格
				Cell cell = wall[i][j];
				//若当前位置无占用，则绘制墙
				if(cell==null) {
					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
					//若有占用，则绘制方块
				}else {
					g.drawImage(cell.getImage(),x,y,null);
				}
			}
		}
	}

	
	
	/**
	 * 快速下落
	 */
	public void fast_drop() {
		if(!is_gameover()) {
			for(;;) {
				if(can_drop()) {
					current_one.move_down();
				}else {
					break;
				}
			}
			land_to_wall();
			destroy_line();
			current_one = next_one;
			next_one = Tetromino.random_one();
		}else {
			game_state = GAMEOVER;
		}
		
	}
	
	
	
	/**
	 * 缓慢下落
	 */
	public void soft_drop() {
		if(!is_gameover()) {
			if(can_drop()) {
				current_one.move_down();
			}else {
				land_to_wall();
				destroy_line();
				current_one = next_one;
				next_one = Tetromino.random_one();
			}	
		}else {
			game_state = GAMEOVER;
		}
		
	}


	
	/**
	 * 旋转
	 */
	public void rotate() {
		current_one.turn_right();
		if(out_of_wall() || coincide()) {
			current_one.turn_left();
		}
		
	}



	/**
	 * 方块向右移动
	 */
	public void move_right() {
		current_one.move_right();
		if(out_of_wall() || coincide()) {
			current_one.move_left();
		}
	}

	
	
	/**
	 * 方块向左移动
	 */
	public void move_left() {
		current_one.move_left();
		if(out_of_wall() || coincide()) {
			current_one.move_right();
		}
	}


	
	/**
	 * @return
	 * 判断方块移动或旋转后是否与wall重合
	 */
	private boolean coincide() {
		Cell cells[] = current_one.cells;
		for(Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if(wall[row][col]!=null) {
				return true;
			}
		}
		return false;
	}


	
	/**
	 * @return
	 * 判断方块是否移出左右边界
	 */
	private boolean out_of_wall() {
		Cell cell[] = current_one.cells;
		for(Cell c : cell) {
			int row = c.getRow();
			int col = c.getCol();
			if(col < 0 || col > 9 || row < 0 || row >19) {
				return true;
			}
		}
		return false;
	}




	public static void main(String[] args) {
		//创建窗口
		JFrame frame = new JFrame("俄罗斯方块");
		//创建界面
		Tetris panel = new Tetris();
		//嵌入
		frame.add(panel);
		//窗口大小
		frame.setSize(530, 585);
		//固定大小
		frame.setResizable(false);
		//设置窗口居中
		frame.setLocationRelativeTo(null);
		//设置为可见
		frame.setVisible(true);
		//设置窗口关闭，即程序终止
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//主逻辑
		panel.start();
		
		
		
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
