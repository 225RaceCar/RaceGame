package racegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Actual game.
 * 
 */

public class Game {

    //set the FIRST course position place x and y
    //subsequent courses will be placed by the for loop, multiplies by i
    private double x = 0.20;
    private double y = 0.90;
    
    
    //the car class
    private Car playerCar;
    private EnemyCar enemyCar;
    
    
    private BufferedImage bg;
    private MovingBackground movingBg;
    
    //the course, last part should be the finish line
    private ArrayList<Course> courseList = new ArrayList<Course>();
    
    //background for the game
    private BufferedImage backgroundImg;
    
    //image overlayed on bg for when the game is over
    private BufferedImage redBorderImg;
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            
            //run the thread for game class
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
    //this is where the objects used in the game are actually defined
    //they are stored in the Game init thread
    private void Initialize()
    {
        playerCar = new Car();
        enemyCar = new EnemyCar();
        
        CreateCourse();
            
        
        
        movingBg = new MovingBackground();
        
    }
    
    
    
    
    //when game is initialized the course is created
    private void CreateCourse(){
        
        for(int i = 0; i < 30; i++)
        {
            
            //create course object
            Course segment = new Course(x, y - (i * 0.035));
            
            //add that course to the CourseList
            courseList.add(segment);
        }
        
    }
    
    
    
    
    
    
    
    
    
    
   //load global resources like the bg and text for the standings
    private void LoadContent()
    {
        try
        {
            //load bg image
            URL bgUrl = this.getClass().getResource("/MoonGame/resources/images/stardust.png");
            bg = ImageIO.read(bgUrl);
            
            //loading background image
            //load bg image
            URL backgroundImageUrl = this.getClass().getResource("/MoonGame/resources/images/stardust.png");
            backgroundImg = ImageIO.read(bgUrl);

            //load bg image
            URL redBorderImgUrl = this.getClass().getResource("/MoonGame/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        }
        
        //if images not found ooooooooops
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        movingBg.Initialize(bg, -1, Framework.frameWidth / 10);
    }
    
    
    //resets variables in car
    public void RestartGame(){
        playerCar.ResetPlayer();
        
        //clear list
        courseList.clear();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Update game logic.
     * 
     * THIS IS WHERE THE CAR NEEDS TO HIT SOMETHING TO WIN
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        // Move the car
        playerCar.Update();
        movingBg.Update();
        
        for(int i = 0; i < courseList.size(); i++){
            courseList.get(i).Update();
        }
        
        // Checks where the player car is. Is it still in the space or is it raceWin or crashed?
        
        
        
        //AS LONG AS the Y of car is less than Y of the last course, finish line
        //has not been crossed
        
        //carImgHeight can be removed
        if(playerCar.y + playerCar.carImgHeight < courseList.get(courseList.size() - 1).y)
        {

            // Here we check if the car speed isn't too high.
            if(playerCar.speedY >= playerCar.topLandingSpeed)
               playerCar.raceWin = true;
            else
               playerCar.crashed = true;
            
            //change the gamestate, on next loop gameover will occur
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }
    
    /**
     * Draw the game to the screen.
     * remember that override in Framework drawing earlier
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition){
        
        //draws according to Framework canvas size 
        //g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        //draw moving bg???????
        movingBg.Draw(g2d);
        
        
    
        // Draws the course objects
        for(int i = 0; i < courseList.size(); i++)
        {
            courseList.get(i).Draw(g2d);
        }
        
        //draw the cares
        playerCar.Draw(g2d);
        enemyCar.Draw(g2d);
        
        
    }
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     * @param gameTime Game time in nanoseconds.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        Draw(g2d, mousePosition);
        
        //draws the text to restart
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
        
        //draw text to say whether you won or lost
        if(playerCar.raceWin)
        {
            g2d.drawString("You have successfully raceWin!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
            g2d.drawString("You have raceWin in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
        }
        else
        {
            g2d.setColor(Color.red);
            g2d.drawString("You have crashed the car!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
    }
}
