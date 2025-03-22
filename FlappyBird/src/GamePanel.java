import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener,KeyListener {
	int width=360;
	int height=640;
	Clip clip;
	URL soundURL[]=new URL[5];
	
	//Images
	Image backgroundImg;
	Image topPipeimg;
	Image bottomPipeimg;
	Image flappyBird;
	
	//Bird
	int birdX=width/8;
	int birdY=height/2;
	int birdwidth=34;
	int birdheight=24;
	class Bird{
		int x=birdX;
		int y=birdY;
		int width=birdwidth;
		int height=birdheight;
		Image img;
		Bird(Image img){
			this.img=img;
		}
		
	}
	class Pipe{
		int x=pipeX;
		int y=pipeY;
		int width=pipeWidth;
		int height=pipeHeight;
		Image img;
		boolean passed=false;
		Pipe(Image topPipe){
			this.img=topPipe;
		}
		
	}
	
	
	int pipeX=width;
	int pipeY=0;
	int pipeWidth=64;
	int pipeHeight=512;
	
	
	
	Bird bird;
	int velocityX=-4;
	int velocityY=0;
	int gravity=1;
	
	
	ArrayList<Pipe> pipes;
	Random random=new Random();
	
	
	
	Timer gameloop;
	Timer placePipeTimer;
	
	boolean gameOver=false;
	double score=0;
	
	GamePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		
		setFocusable(true);
		addKeyListener(this);
		setPreferredSize(new Dimension(width,height));
		setBackground(Color.BLUE);
		backgroundImg=new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
		flappyBird=new ImageIcon(getClass().getResource("flappybird.png")).getImage();
		bottomPipeimg=new ImageIcon(getClass().getResource("bottompipe.png")).getImage();
		topPipeimg=new ImageIcon(getClass().getResource("toppipe.png")).getImage();
		soundURL[0]=getClass().getResource("gamemusic.wav");
		AudioInputStream ais=AudioSystem.getAudioInputStream(soundURL[0]);
		clip=AudioSystem.getClip();
		clip.open(ais);
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		
		
		bird=new Bird(flappyBird);
		pipes=new ArrayList<Pipe>();
		
		placePipeTimer=new Timer(1500,new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				placePipes();
				
			}
			
		});
		placePipeTimer.start();
		
		gameloop=new Timer(1000/60,this);
		gameloop.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		g.drawImage(backgroundImg,0,0,width,height,null);
		
		g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
		
		for (int i=0;i<pipes.size();i++) {
			Pipe pipe=pipes.get(i);
			g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial",Font.ITALIC,32));
		if(gameOver) {
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Arial",Font.ITALIC,32));
			g.drawString("GameOver:"+String.valueOf((int)score),width/2-80,height/2);
		}
		else {
			g.drawString("Score:"+String.valueOf((int)score),10,35);
		}
	}
	public void move() {
		velocityY+=gravity;
		bird.y+=velocityY;
		bird.y=Math.max(bird.y,0); 
		
		
		for (int i=0;i<pipes.size();i++) {
			Pipe pipe=pipes.get(i);
			pipe.x+=velocityX;
			if(!pipe.passed && bird.x>pipe.x+pipe.width) {
				pipe.passed=true;
				score+=0.5;
				
			}
			if (collision(bird,pipe)) {
				gameOver=true; 
			}
		}
		
		if(bird.y>height) {
			gameOver=true;
		}
	}
	
	public void placePipes() {
		int randomPipeY=(int) (pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
		int openingSpace=height/4;
		
		
		Pipe topPipe=new Pipe(topPipeimg);
		topPipe.y=randomPipeY;
		pipes.add(topPipe);
		
		Pipe bottomPipe=new Pipe(bottomPipeimg);
		bottomPipe.y=topPipe.y+pipeHeight+openingSpace;
		pipes.add(bottomPipe);
		
	}
	
	public boolean collision(Bird a,Pipe b) {
		return a.x<b.x+b.width && a.x+a.width>b.x && a.y<b.y+b.height&& a.y+a.height>b.y;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		move();
		repaint();
		if(gameOver) {
			clip.stop();
			placePipeTimer.stop();
			gameloop.stop();
			
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			velocityY=-9;
			if(gameOver) {
				bird.y=birdY;
				velocityY=0;
				pipes.clear();
				score=0;
				gameOver=false;
				gameloop.start();
				placePipeTimer.start();
				clip.start();
				clip.loop(clip.LOOP_CONTINUOUSLY);
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
