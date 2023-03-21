import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class Main extends JFrame{

    
    ArrayList<Monster> monsters = new ArrayList<>();
    
    public Main(){
        this.add(new GamePanel());
		//this.setTitle("Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
      

    }
   
    public static void main(String[] args) {
        int number_of_monsters = Integer.parseInt(args[0]);
		System.out.println(number_of_monsters);
		Main m = new Main();
		
		Main.Monster [] monsters = new Main.Monster[number_of_monsters];
		
		Random r = new Random();
		
		for(int i=0;i<number_of_monsters;i++)
		{
			monsters[i] = m.new Monster(Math.abs(r.nextInt()%500),Math.abs(r.nextInt()%500));		
		}
		
		for(int i=0;i<number_of_monsters;i++)
			monsters[i].start();
		
		try {
			for(int i=0;i<number_of_monsters;i++)
				monsters[i].join();
		} 
        catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }

    public class Monster extends Thread implements ActionListener{
        int x;
        int y;
        //Thread T = new Thread();
        Timer timer;
        Random random;

        public Monster(int abs, int abs2) {
            x = abs;
            y = abs2;
            monsters.add(this);
            
        }
        /*
        public void start() {
        }
        public void join() {
        }
        */
        public void changeCor(){
            random = new Random();
            timer = new Timer(1000,this);
            timer.start();
            int r =  random.nextInt((4 - 1) + 1) + 1;
            switch(r){
                case 1:
                    if(x+10 < 500){x = x + 10;}
                    else{changeCor();}
                break;
                    
                case 2:
                    if(x-10 > 0){ x = x - 10;}
                    else{changeCor();}
                    
                break;

                case 3:
                    if(y+10 < 500){ y = y + 10;}
                    else{changeCor();}
                break;

                case 4:
                    if(y-10 > 0){y = y + 10;}
                    else{changeCor();}
                break;

            }

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            
        }
    }

    public class GamePanel extends JPanel implements ActionListener{

        static final int SCREEN_WIDTH = 500;
	    static final int SCREEN_HEIGHT = 500;
	    static final int UNIT_SIZE = 20;
        static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
        final int x[] = new int[GAME_UNITS];
	    final int y[] = new int[GAME_UNITS];
        boolean running = false;
        boolean alive = true;
        char direction;
        Random random;
        int x1 = 250;
        int y1 = 250;
        Timer timer;

        GamePanel(){
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
            this.setBackground(Color.white);
            this.setFocusable(true);
            this.addKeyListener((KeyListener) new MyKeyAdapter());
            startGame();
        }
        public void startGame() {
            
            running = true;
            timer = new Timer(1000,this);
            timer.start();
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }
        public void draw(Graphics g) {
		
            if(alive) {
                /*
                for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
                    g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                    g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
                }
                */
                move();
                checkBlue();
                if(running){
                g.setColor(Color.GREEN);
                g.fillRect(x1, y1, UNIT_SIZE, UNIT_SIZE);
                }
            
                g.setColor(Color.BLUE);
                for (int i = 0;i<monsters.size();i++) {
                    g.fillRect(monsters.get(i).x,monsters.get(i).x , UNIT_SIZE, UNIT_SIZE);
                    monsters.get(i).changeCor();
                    //System.out.println(monsters.get(i).x + " " + monsters.get(i).y);
                    
                }
                
            }
            else {
                gameOver(g);
                
            }
        } 
        public void move(){
            checkBlue();
            switch(direction) {
            case 'U':
                if(y1 > 0)
                    y1 = y1 - UNIT_SIZE/2;
                    direction = 1;
                    
                break;
            case 'D':
                if(y1 < SCREEN_HEIGHT)
                    y1 = y1 + UNIT_SIZE/2;
                    direction = 1;
                    
                break;
            case 'L':
                if(x1 > 0)
                    x1 = x1 - UNIT_SIZE/2;
                    direction = 1;
                    
                break;
            case 'R':
                if(x1 < SCREEN_WIDTH)
                    x1 = x1 + UNIT_SIZE/2;
                    direction = 1;
                    
                break;
            }
            for (int i = 0;i<monsters.size();i++) {
               
                monsters.get(i).changeCor();
                
            }
            
        }  
        public void checkBlue() {
            for(int i = 0;i<monsters.size();i++){
                if( Math.abs(x1 -monsters.get(i).x) < 20  && Math.abs(y1 - monsters.get(i).y) <20 ) {
                    
                    alive = false;
                    
                    //running = true;
                }
              
            }    
        } 
        public void checkCollisions() {
            checkBlue();
            
            //check if head touches left border
            if((x1 < 0) || x1 > SCREEN_WIDTH-20) {
                running = false;
            }
            //check if head touches right border
            else{
                running = true;
            }
            //check if head touches top border
            if(y1 < 0 || y1 > SCREEN_HEIGHT-20) {
                running = false;
            }
            //check if head touches bottom border
            else {
                running = true;
            }
             
            if(!running) {
                timer.stop();
            }
            
        }

        public void gameOver(Graphics g) {
            System.exit(0);
           
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(alive) {
                //move();
                checkBlue();//ghost check
                checkCollisions();//sınırlar
            }
            
            repaint();
        }

        public class MyKeyAdapter extends KeyAdapter{
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    
                        direction = 'L';
                    
                    break;
                case KeyEvent.VK_D:
                    
                        direction = 'R';
                    
                    break;
                case KeyEvent.VK_W:
                    
                        direction = 'U';
                    
                    break;
                case KeyEvent.VK_S:
                    
                        direction = 'D';
                    
                    break;
                }
            }
        }

        

    }
        


    
   
   
}



