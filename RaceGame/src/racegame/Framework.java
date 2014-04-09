package racegame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    
    
    
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    
    
    
    
    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    
    
    
    
    
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 16;
    
    
    
    
    
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    
    
    
    
    //create a new enumerated whatever that Framework uses to call game states
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    
    
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    
    
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    
    

    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    
    /**
     * Image for menu.
     */
    private BufferedImage moonLanderMenuImg;
    
    
    public Framework ()
    {
        
        // call constructor of extended Canvas object
        super();
        
        //set the game state
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            
            //start th game thread and call gameloop to control the game
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {
        
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {
        try
        {
            URL moonLanderMenuImgUrl = this.getClass().getResource("/MoonGame/resources/images/stardust.png");
            moonLanderMenuImg = ImageIO.read(moonLanderMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //GAME_UPDATE_PERIOD is a specific unit of time for controlling the game updating to check logic and everything
     
    private void GameLoop(){
        // This two variables are used in VISUALIZING state of the game. 
        //We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true){
            
            //get the time from the system at the start of the game
            beginTime = System.nanoTime();
            
            //switch uses the ganeState enum to decide what to do
            //since this is called in gameLoop is checked w/ update()
            switch (gameState){
                //is the player playing the game
                case PLAYING:
                    
                    //set the time as the game goes on
                    gameTime += System.nanoTime() - lastTime;
                    
                    //*******************************************************************************************
                    //while PLAYING is true call UpdateGame from game.java
                    //this method essentially powers the whole game
                    game.UpdateGame(gameTime, mousePosition());
                    
                    //get the time at the end of this loop
                    lastTime = System.nanoTime();
                    
                    break;
                
                //these don't have to do anything with gameloop
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                    
                //loads resources for the FrameworkClass
                case STARTING:
                    // Sets variables and objects in this class
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();

                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                    
                    
                //used to set the game window    
                //THIS IS THE FIRST GAME STATE CALLED
                case VISUALIZING:
                    
                    //the game needs to wait a second before loading
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        //
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            //*********************************************************
            //end state load and all that
            
            //now that we know what state we are in, repaint the canvas
            repaint();
            
            // Here we calculate the time that defines for how long we should put thread to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put 
            //thread to sleep for 10 millisecond so that some other thread 
            //can do some work.
            if (timeLeft < 10) {
                timeLeft = 10; //set a minimum
            }
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        
        //Draw will be called by the game class, depending on gamestate
        //draw will draw the necessary items
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition(), gameTime);
            break;
            case MAIN_MENU:
                g2d.drawImage(moonLanderMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);
                g2d.setFont(new Font("gill.otf", Font.PLAIN, 13));
                g2d.drawString("Use w a d keys to controle the rocket.", frameWidth / 2 - 117, frameHeight / 2);
                g2d.drawString("Press any key to start the game.", frameWidth / 2 - 100, frameHeight / 2 + 30);
                
                //not this may be key to showing standings
                g2d.drawString("Is there anybody out there...", 7, frameHeight - 5);
            break;
                
            case OPTIONS:
                //...
            break;
                
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
        }
    }
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        
        //all the magic happens
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
                //the user can press any key to start the game, when a key
                //any key is released the game starts
                newGame();
            break;
            case GAMEOVER:
                //if the user hits one of these keys, start a new game
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
        }
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
}
