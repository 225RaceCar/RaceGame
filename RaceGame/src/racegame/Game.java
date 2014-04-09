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

    /**
     * The space rocket with which player will have to land.
     */
    private Car playerCar;
    
    /**
     * Landing area on which rocket will have to land.
     */
    private LandingArea landingArea;
    
    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;
    
    /**
     * Red border of the frame. It is used when player crash the rocket.
     */
    private BufferedImage redBorderImg;
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
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
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        playerCar = new Car();
        landingArea  = new LandingArea();
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/MoonGame/resources/images/stardust.png");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL redBorderImgUrl = this.getClass().getResource("/MoonGame/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
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
        // Move the rocket
        playerCar.Update();
        
        // Checks where the player rocket is. Is it still in the space or is it raceWin or crashed?
        // First we check bottom y coordinate of the rocket if is it near the landing area.
        if(playerCar.y + playerCar.rocketImgHeight - 10 < landingArea.y)
        {
            // Here we check if the rocket is over landing area.
            if((playerCar.x > landingArea.x) && (playerCar.x < landingArea.x + landingArea.landingAreaImgWidth - playerCar.rocketImgWidth))
            {
                // Here we check if the rocket speed isn't too high.
                if(playerCar.speedY <= playerCar.topLandingSpeed)
                    playerCar.raceWin = true;
                else
                    playerCar.crashed = true;
            }
            else
                playerCar.crashed = true;
                
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
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
        
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
        
        if(playerCar.raceWin)
        {
            g2d.drawString("You have successfully raceWin!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
            g2d.drawString("You have raceWin in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
        }
        else
        {
            g2d.setColor(Color.red);
            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
    }
}
