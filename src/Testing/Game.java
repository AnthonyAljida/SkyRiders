
package Testing;

import javax.swing.*;

import Testing.Renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class Game implements ActionListener, MouseListener, KeyListener{
	
	public static final int width = 1200;
	public static final int height = 800;
	public static Game flappybird;
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList <Rectangle> columns;
	public Random rand;
	public int ticks;
	public int ymotion;
	public boolean GameOver;
	public boolean started;
	public int score = 0;
	
		public Game() 
		{
			JFrame jframe = new JFrame();
			renderer = new Renderer();
			Timer timer = new Timer(20, this);
			rand = new Random();
		
			jframe.setVisible(true); //creates a frame
			jframe.setTitle("Flappy Bird"); //sets the title of the frame to Flappy Bird
			jframe.setSize(width, height); //sets the X.dimension and Y.dimension of the frame
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exits out of the frame completely
			jframe.setResizable(false); //prevents the frame from being resized
			jframe.add(renderer);
			jframe.addMouseListener(this);
			jframe.addKeyListener(this);
			bird = new Rectangle(width/2 - 10, height/2 - 10, 20, 20);
			columns = new ArrayList<Rectangle>();
			
			addcolumn(true);
			addcolumn(true);
			addcolumn(true);
			addcolumn(true);
			
			timer.start();
		
		}
		
		public void addcolumn(boolean start)
		{
			int space = 300;
			int widthC = 100;
			int heightC = 50 + rand.nextInt(300);
			
			if(start)
			{
			columns.add(new Rectangle(width + widthC + columns.size() * 300, height - heightC - 120, widthC, heightC));
			columns.add(new Rectangle(width + widthC + (columns.size() - 1) * 300, 0, widthC, height - heightC - space));
			}
			else
			{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, height - heightC - 120, widthC, heightC));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0 , widthC, height - heightC - space));
			}
			
		}
			
			
			
		
		
		public void paintcolumn(Graphics g, Rectangle column)
		{
			
			g.setColor(Color.green.darker().darker());
			g.fillRect(column.x,column.y,column.width,column.height);
		
		}
		
		
		public void jump()
		{
			if(GameOver)
			{
				bird = new Rectangle(width/2 - 10, height/2 - 10, 20, 20);
				columns.clear();
				ymotion = 0;
				score = 0;
				
				addcolumn(true);
				addcolumn(true);
				addcolumn(true);
				addcolumn(true);
				GameOver = false;
			}
			
			if(!started)
			{
			
				started = true;
			}
			else if(!GameOver)
			{
				if(ymotion > 0)
				{
					ymotion = 0;
				}
				ymotion -= 10;
			}
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			int speed = 10;
			ticks++;
			
			if(started)
			{
			
			
				for(int i = 0; i<columns.size(); i++)
				{
				
					Rectangle column = columns.get(i);
					column.x -= speed;
	
				}
		
				if(ticks % 2 == 0 && ymotion < 15)
				{
					ymotion+=3;
				}
			
				for(int i = 0; i<columns.size(); i++)
				{
					
				Rectangle column = columns.get(i);
				
					if(column.x + column.width < 0)
					{
						columns.remove(column);
					
						if(column.y == 0)
						{
							addcolumn(false);
						}
					}
			
				}
			
			
			bird.y += ymotion;
			
			for(Rectangle column : columns)
			{
				if(column.y == 0 && bird.x + bird.width/2 > column.x + column.width/2 - 10 && bird.x + bird.width/2 < column.x + column.width /2 + 10)
				{
					score++;
				}
				
				if(column.intersects(bird))
				{
					GameOver = true;
					
					if(bird.x <= column.x)
					{
						bird.x = column.x - bird.width;
					}
					else 
					{
						if(column.y != 0)
						{
							bird.y = column.y - bird.height;
						}
						else if(bird.y< column.height)
						{
							bird.y = column.height;
						}
					}
														
				}
			}
			
			if(bird.y > height - 120 || bird.y < 0)
			{
				GameOver = true;
			}
			
			if(bird.y + ymotion >= height - 120)
			{
				bird.y = height - 120 - bird.height;
				GameOver = true;
			}
			
			}
			renderer.repaint();
		}
		public void repaint(Graphics g) 
		{
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, width, height);
			
			g.setColor(Color.YELLOW);
			g.fillRect(0, height - 120, width, 120);
			
			g.setColor(Color.green);
			g.fillRect(0, height - 120, width, 30);
			
			
			g.setColor(Color.red);
			g.fillRect(bird.x, bird.y, bird.width, bird.height);
			
			for(Rectangle column : columns)
			{
				
				paintcolumn(g, column);
			}
			
			g.setColor(Color.white);
			g.setFont(new Font("Ampersand", 1, 100));
			
			if(!started)
			{
				g.drawString("Click To Start!", 250, height/2 - 50);
			}
		
			if(GameOver)
			{
				g.drawString("Game Over!", 325, height/2 - 50);
			}
			
			if(!GameOver && started)
			{
				g.drawString(String.valueOf(score), width/2 - 25, 100);
			}
		}
	
	
	public static void main(String[] args) {
		
		 flappybird = new Game();
		
	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
		
	}

	  
		
}
