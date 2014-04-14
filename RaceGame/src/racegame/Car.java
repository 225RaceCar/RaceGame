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

//the car the player will drive
public class Car {

    //to be used in object creation
    private Random random;

    //x & y coordinates
    public int x;
    public int y;
    
    //car top speed
    public double TOP_SPEED;
    

    //is race over?
    public boolean raceWin;

    //has car crashed
    public boolean crashed;

    //accelerating speed 
    private int speedAccelerating;

    //sets the speed to counter movement
    private double speedStopping;
    public int topLandingSpeed;

    //how fast and in what direction is the car moving
    private double speedX;
    public double speedY;

    //car images
    private BufferedImage carImg;
    private BufferedImage carWonImg;
    private BufferedImage carCrashedImg;

    //image of the car tire
    private BufferedImage carTireImg;

    //size of car image
    public int carImgWidth;
    public int carImgHeight;

    public Car() {
        //load resources
        Initialize();
        LoadContent();

        //will set the starting coordinate
        x = (int) (Framework.frameWidth * 0.20);
    }

    private void Initialize() {
        //random int to use when setting the car
        random = new Random();

        //this is also used to restart the game, but it does the same thing
        ResetPlayer();

        //used for control
        speedAccelerating = 2;
        speedStopping = 1;

        //used for "crossing the finish line"
        topLandingSpeed = 2;
    
        //set the top speed
        TOP_SPEED = 6;
    }

    //load car resources
    private void LoadContent() {
        try {
            //set the car image and its size
            URL carImgUrl = this.getClass().getResource("/raceresources/resources/images/car.png");
            carImg = ImageIO.read(carImgUrl);
            carImgWidth = carImg.getWidth();
            carImgHeight = carImg.getHeight();

            //set and image for when the race is won
            URL carWonImgUrl = this.getClass().getResource("/raceresources/resources/images/car.png");
            carWonImg = ImageIO.read(carWonImgUrl);
            
            //set and image for when the car is crashed
            URL carCrashedImgUrl = this.getClass().getResource("/raceresources/resources/images/car.png");
            carCrashedImg = ImageIO.read(carCrashedImgUrl);

        } catch (IOException ex) {
            //i'm not totally sure how this works but it is some kind of fancy error reporting
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //set variables for a new game
    public void ResetPlayer() {
        
        //booleans to control win and lose conditions
        raceWin = false;
        crashed = false;

        //place the car on the canvas
        x = (int) (Framework.frameWidth * 0.20);
        y = (int) (Framework.frameHeight * 0.70);

        //speed of the car
        speedX = 0;
        speedY = 0;
    }

    
    
    
    
    
    
    //Update gets called constantly and tells the cnavas where to draw thw car
    public void Update() {

        //the following all increase the speed as a key is pressed
        // Calculating speed for moving up 
        if (Canvas.keyboardKeyState(KeyEvent.VK_W)) {
            if (speedY >= TOP_SPEED * - 0.5) {
                speedY -= speedAccelerating;
            } else {
                speedY += speedStopping;
            }
        }

        // Calculating speed for moving down.
        if (Canvas.keyboardKeyState(KeyEvent.VK_S)) {
            if (speedY <= TOP_SPEED * 0.5) {
                speedY += speedAccelerating;
            } else {
                speedY -= speedStopping;
            }
        }
            // Calculating speed for moving or stopping to the left.
        if (Canvas.keyboardKeyState(KeyEvent.VK_A)) {
            speedX -= speedAccelerating;
        } else if (speedX < 0) {
            speedX += speedStopping;
        }

        
        // Calculating speed for moving or stopping to the right.
        if (Canvas.keyboardKeyState(KeyEvent.VK_D)) {
            speedX += speedAccelerating;
        } //if no keys are being press slow the car
        else if (speedX > 0) {
            speedX -= speedStopping;
        }

        // Moves the car.
        x += speedX;
        y += speedY;
    }

    public void Draw(Graphics2D g2d) {

        //*******************************************
        //text coordinates
        g2d.setColor(Color.white);
        g2d.drawString("Car coordinates: " + x + " : " + y, 5, 15);

        g2d.drawString("Frame size, height " + Framework.frameHeight + ", width  " + Framework.frameWidth, 5, 30);
        g2d.drawString("Car coordinates: " + x + " : " + y, 5, 50);
        
        g2d.drawString("The car speed is currently: " + speedY, 5, 65);

        // If the car is raceWin.
        //image for winnging
        if (raceWin) {
            g2d.drawImage(carWonImg, x, y, null);
        } // If the car is crashed.
        else if (crashed) {
            g2d.drawImage(carCrashedImg, x, y + carImgHeight - carCrashedImg.getHeight(), null);
        } // If the car is still in the space.
        else {
            // If player hold down a W key we draw car fire.

            //removed because it isn't a car car
//            if(Canvas.keyboardKeyState(KeyEvent.VK_W))
//                g2d.drawImage(carFireImg, x + 12, y + 66, null);
            g2d.drawImage(carImg, x, y, null);
        }
    }
    
    public double getSpeedY(){
        return speedY;
    }

    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }

}
