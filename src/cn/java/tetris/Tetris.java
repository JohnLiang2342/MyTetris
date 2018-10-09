package cn.java.tetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author John Liang
 * 
 * Tetris类  俄罗斯方块类
 * 属性：
 * game_state: 存储游戏的当前状态
 * current_one: 当前方块
 * next_one: 下一个方块
 * wall: 底部堆积的墙，用二维数组实现
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
	private Tetromino current_one = Tetromino.randomOne();
	private Tetromino next_one = Tetromino.randomOne();
	//20行10列
	private Cell[][] wall = new Cell[20][10];
	int score_pool[] = { 0, 1, 2, 5, 10 };
	String show_state[] = {"[P]pause","[C]continue","[Enter]replay"};
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
					hard_drop();
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
					current_one = Tetromino.randomOne();
					next_one = Tetromino.randomOne();
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
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			if(game_state==PLAYING) {
				if(can_drop()) {
					current_one.move_down();
				}else {
					land_to_wall();
					destroy_line();
					current_one = next_one;
					next_one = Tetromino.random_one();
				}
			}
			
		}
		
		
		
	}
	
	
	
	public void hard_drop() {
		// TODO Auto-generated method stub
		
	}



	public void rotate() {
		// TODO Auto-generated method stub
		
	}



	public void move_right() {
		// TODO Auto-generated method stub
		
	}



	public void move_left() {
		// TODO Auto-generated method stub
		
	}



	public void soft_drop() {
		// TODO Auto-generated method stub
		
	}





















	public static void main(String[] args) {
		//创建窗口
		JFrame frame = new JFrame("");
		//创建界面
		Tetris panel = new Tetris();
		//嵌入
		frame.add(panel);
		//窗口大小
		frame.setSize(535, 580);
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
