package racegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {

    //the car class
    private Car playerCar;
    
    //the finish line
    private LandingArea landingArea;
    
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
        
        landingArea  = new LandingArea();
            
        
    }
    
   //load global resources like the bg and text for the standings
    private void LoadContent()
    {
        try
        {
            //load bg image
            URL backgroundImgUrl = this.getClass().getResource("/MoonGame/resources/images/stardust.png");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            //load bg image
            URL redBorderImgUrl = this.getClass().getResource("/MoonGame/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        }
        
        //if images not found ooooooooops
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //resets variables in car
    public void RestartGame()
    {
        playerCar.ResetPlayer();
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
        
        // Checks where the player car is. Is it still in the space or is it raceWin or crashed?
        // First we check bottom y coordinate of the car if is it near the landing area.
        
        //if the position of the car is LESS than the landing ara y coordinate
        //AS LONG AS the Y of car is less than Y of landingArea, finish line
        //has not bee crossed
        if(playerCar.y + playerCar.carImgHeight < landingArea.y)
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
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        
        //draws according to Framework canvas size 
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        //draw everything we need
        landingArea.Draw(g2d);
        playerCar.Draw(g2d);
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
