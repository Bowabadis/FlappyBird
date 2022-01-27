import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game extends JPanel implements ActionListener {
    //game Alterations
    int width = 600;
    int height = 600;
    int delay = 20;
    int plrSize = 25, constX = plrSize*3;
    int startPipe = width+25;
    Color plrColor = new Color(252, 211, 3);
    //jump variables
    double jumpVar1 = 1.31;
    //gravity variables
    double gravVar1 = 1.4;
    double gravVar2 = 0.35;
    //game Variables
    int floorheight = 7; //divides height by floor height to get the height of the floor
    int realHeight = height-(height/floorheight);
    double plrY;
    int pipeX;
    double jumpIteration = 0, gravityIteration = 0;
    int numPipes = 0, pipeMoveSpeed = 3;
    Timer timer;
    Random random;
    int score = 0, highscore = 0, previousScore = 0;
    int pipeNum = 0, scoreNum = 0;
    boolean isrunning = false, isjumping = false, startMenu, hasStart = false, gameOver = false;
    ArrayList<Rectangle> pipes = new ArrayList<>();


    Game(){
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(88, 232, 237));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        timer = new Timer(delay, this);
        startMenu = true;
    }
    public void startGame(){
        random = new Random();
        plrY = realHeight/2; //middle of sky
        newPipe();
        isrunning = true;
        timer.start();
    }
    public void newPipe(){
        Rectangle fPipe = new Rectangle();
        Rectangle fPipe2 = new Rectangle();
        NewPipe newPipe = new NewPipe(realHeight, score);
        fPipe.setBounds(startPipe,newPipe.pipeBound[0]-500,50,500);
        pipes.add(fPipe);
        fPipe2.setBounds(startPipe,newPipe.pipeBound[1],50,500);
        pipes.add(fPipe2);
    }
    public void movePipes(){
       for(int i =0; i<pipes.size(); i++){
           pipes.get(i).x -= pipeMoveSpeed;
       }
      pipes.trimToSize();
       if(pipes.get(scoreNum).x < constX-20){ //gives score after pipe passes by a player
            score += 1;
            scoreNum += 2;
       }
       if(pipes.get(pipeNum*2).x < 350){ //generates a set of pipes
           newPipe();
           pipeNum += 1;
       }
    }
    public void gravity(){
        if(!isjumping){
            plrY += Math.pow(gravVar1, gravityIteration);
            gravityIteration += gravVar2;
        }
        if(isjumping){
            gravityIteration = 0;
        }
    }
    public void jump(){
        if(isjumping) {
            plrY -= Math.pow(jumpVar1, jumpIteration / 2);
            jumpIteration--;
        }
        if(jumpIteration <=0){
            isjumping = false;
        }
    }


    public void checkGameOver(){
        if(plrY>height){
            isrunning = false;
            gameOver = true;
        }
        for(int i =0; i<pipes.size(); i++){
            if(pipes.get(i).intersects(constX,plrY,plrSize,plrSize)){
                isrunning = false;
                gameOver = true;
            }
        }
        if(gameOver && !isrunning){
            timer.stop();
            pipes.clear();
            pipeNum = 0;
            scoreNum = 0;
            if(score>highscore){
                highscore = score;
            }
            previousScore = score;
            score = 0;
        }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(61, 245, 64));
        g.fillRect(0,height-(height/floorheight),width,height/floorheight);
        if(isrunning){
            draw(g);
        }else if(startMenu){
            startMenu(g);
        }else if(gameOver){
            gameOverMenu(g);
        }
    }
    public void startMenu(Graphics g){
        g.setColor(plrColor);
        g.fillOval(constX, realHeight/2, plrSize,plrSize);
        //start text
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD, 28));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("PRESS ANY KEY TO START", (width-metrics.stringWidth("PRESS ANY KEY TO START"))/2, height/2);
        g.fillRect(((width-metrics.stringWidth("PRESS ANY KEY TO START"))/2)-53, (height/2)-15, 50,10);
        g.fillRect(((width-metrics.stringWidth("PRESS ANY KEY TO START"))/2)+376, (height/2)-15, 50,10);
    }
    public void gameOverMenu(Graphics g){
        g.setColor(plrColor);
        g.fillOval(constX, realHeight/2, plrSize,plrSize);
        //start text
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD, 28));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("PRESS ANY KEY TO START", (width-metrics.stringWidth("PRESS ANY KEY TO START"))/2, height/2);
        g.fillRect(((width-metrics.stringWidth("PRESS ANY KEY TO START"))/2)-53, (height/2)-15, 50,10);
        g.fillRect(((width-metrics.stringWidth("PRESS ANY KEY TO START"))/2)+376, (height/2)-15, 50,10);
        g.setColor(Color.red);
        g.drawString("GAME OVER", (width-metrics.stringWidth("GAMEOVER"))/2, (height/2)-40);
        g.setColor(Color.white);
        g.drawString("HIGHSCORE "+highscore, (width-metrics.stringWidth("HIGHSCORE "+highscore))/2, 30);
        g.drawString("SCORE "+previousScore, (width-metrics.stringWidth("SCORE "+previousScore))/2, height-30);
    }
    public void draw(Graphics g){
        g.setColor(plrColor);
        g.fillOval(constX, (int)plrY, plrSize,plrSize);
        //drawing pipes
        g.setColor(Color.black);
        for(int i = 0; i<pipes.size(); i++){
            g.fillRect(pipes.get(i).x,pipes.get(i).y,pipes.get(i).width,pipes.get(i).height);
        }
        //score text
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD, 15));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("SCORE "+score, (width-metrics.stringWidth("SCORE "+score))/2, height-10);
        g.drawString("HIGHSCORE "+highscore, (width-metrics.stringWidth("HIGHSCORE "+highscore))/2, 20);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(isrunning) {
            jump();
            gravity();
            movePipes();
            checkGameOver();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            if(!isrunning && (startMenu || gameOver)){
                isrunning = true;
                startMenu = false;
                hasStart = true;
                startGame();
            }
            if(isrunning) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_W:
                        if (!isjumping) {
                            isjumping = true;
                            jumpIteration = 20;
                            jump();
                        }
                        break;
                }
            }
        }
    }
}
