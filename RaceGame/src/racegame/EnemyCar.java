package racegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The space car with which player will have to land.
 * 
 * @author www.gametutorial.net
 */

public class EnemyCar {
    
    //to be used in object creation
    private Random random;
 
    //x coordinate
    public int x;
    
    //y coordinate
    public int y;
    
    //is race over?
    public boolean raceWin;
    
    /**
     * Has car crashed?
     */
    public boolean crashed;
        
    //accelerating speed 
    private int speedAccelerating;
    
    
    
    /**
     * Stopping/Falling speed of the car. Falling speed because, the gravity pulls the car down to the moon.
     */
    private int speedStopping;
    
    /**
     * Maximum speed that car can have without having a crash when landing.
     */
    public int topLandingSpeed;
    
    /**
     * How fast and to which direction car is moving on x coordinate?
     */
    private int speedX;
    /**
     * How fast and to which direction car is moving on y coordinate?
     */
    public int speedY;
            
    
    /**
     * Image of the car in air.
     */
    private BufferedImage carImg;
    /**
     * Image of the car when raceWin.
     */
    private BufferedImage carWonImg;
    /**
     * Image of the car when crashed.
     */
    private BufferedImage carCrashedImg;
    /**
     * Image of the car fire.
     */
    private BufferedImage carFireImg;
    
    /**
     * Width of car.
     */
    public int carImgWidth;
    /**
     * Height of car.
     */
    public int carImgHeight;
    
    
    public EnemyCar()
    {
        //load resources
        Initialize();
        LoadContent();
        
        // Now that we have carImgWidth we set starting x coordinate.
        x = random.nextInt(Framework.frameWidth - carImgWidth);
    }
    
    
    private void Initialize()
    {
        //random int to use when setting the car
        random = new Random();
        
        //this is also used to restart the game, but it does the same thing
        ResetPlayer();
        
        //used for control
        speedAccelerating = 2;
        speedStopping = 1;
        
        //used for "crossing the finish line"
        topLandingSpeed = 2;
    }
    
    
    //load car resources
    private void LoadContent()
    {
        try
        {
            URL carImgUrl = this.getClass().getResource("/MoonGame/resources/images/car.png");
            carImg = ImageIO.read(carImgUrl);
            carImgWidth = carImg.getWidth();
            carImgHeight = carImg.getHeight();
            
            URL carWonImgUrl = this.getClass().getResource("/MoonGame/resources/images/car.png");
            carWonImg = ImageIO.read(carWonImgUrl);
            
            URL carCrashedImgUrl = this.getClass().getResource("/MoonGame/resources/images/car.png");
            carCrashedImg = ImageIO.read(carCrashedImgUrl);
            
//            URL carFireImgUrl = this.getClass().getResource("/MoonGame/resources/images/car_fire.png");
//            carFireImg = ImageIO.read(carFireImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //set variables for a new game
    public void ResetPlayer()
    {
        raceWin = false;
        crashed = false;
        
        //place the car on the canvas
        x = random.nextInt(Framework.frameWidth - carImgWidth);
        y = (int)(Framework.frameHeight * 0.90);;
        
        speedX = 0;
        speedY = 0;
    }
    
    
    /**
     * Here we move the car.
     */
    
    public void Update()
    {
        
        //the following all increase the speed as a key is pressed
        // Calculating speed for moving up 
        if(Canvas.keyboardKeyState(KeyEvent.VK_W))
            speedY -= speedAccelerating;
        else
            //speedY += speedStopping;
            
        // Calculating speed for moving down.
        if(Canvas.keyboardKeyState(KeyEvent.VK_S))
            speedY += speedAccelerating;
        else
            //speedY += speedStopping;
        
        // Calculating speed for moving or stopping to the left.
        if(Canvas.keyboardKeyState(KeyEvent.VK_A))
            speedX -= speedAccelerating;
        else if(speedX < 0)
            speedX += speedStopping;
        
        // Calculating speed for moving or stopping to the right.
        if(Canvas.keyboardKeyState(KeyEvent.VK_D))
            speedX += speedAccelerating;
        
        //if no keys are being press slow the car
        else if(speedX > 0)
            speedX -= speedStopping;
        
        // Moves the car.
        x += speedX;
        y += speedY;
    }
    
    public void Draw(Graphics2D g2d)
    {
        
        //*******************************************
        //text coordinates
        g2d.setColor(Color.white);
        g2d.drawString("Car coordinates: " + x + " : " + y, 5, 15);
        
        // If the car is raceWin.
        
        //image for winnging
        if(raceWin)
        {
            g2d.drawImage(carWonImg, x, y, null);
        }
        // If the car is crashed.
        else if(crashed)
        {
            g2d.drawImage(carCrashedImg, x, y + carImgHeight - carCrashedImg.getHeight(), null);
        }
        // If the car is still in the space.
        else
        {
            // If player hold down a W key we draw car fire.
            
            //removed because it isn't a car car
//            if(Canvas.keyboardKeyState(KeyEvent.VK_W))
//                g2d.drawImage(carFireImg, x + 12, y + 66, null);
         g2d.drawImage(carImg, x, y, null);
        }
    }
    
}
