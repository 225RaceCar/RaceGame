package racegame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Landing area where rocket will land.
 *
 * 
 */
public class Course {

    //x and y coordinates
    public int x;
    public int y;

    private double speed = 1;

    //different kinds of track image
    private BufferedImage trackImg0;
    private BufferedImage trackImg1;
    private BufferedImage trackImg2;
    private BufferedImage trackImg3;
    
    private BufferedImage trackImgEnd;
    
    
    //the "current"tracl image
    private BufferedImage trackImg;

    //width of race car
    public int courseImgWidth;

    //constructo calls initialize and loadContent
    public Course(int x, int y, int img) {
        Initialize(x, y);
        LoadContent(img);
    }
    
    //set the values as needed
    private void Initialize(int x, int y) {
        // X and Y coordinate of track passed from game.java
        this.x = x;
        this.y = y;
    }

    //load content
    private void LoadContent(int img) {
        try {
            if (img == 0) {
                URL trackImgUrl0 = this.getClass().getResource("/MoonGame/resources/images/track0.png");
                trackImg = ImageIO.read(trackImgUrl0);
            } else if (img == 1) {
                URL trackImgUrl1 = this.getClass().getResource("/MoonGame/resources/images/track1.png");
                trackImg = ImageIO.read(trackImgUrl1);
            } else if (img == 2) {
                URL trackImgUrl2 = this.getClass().getResource("/MoonGame/resources/images/track2.png");
                trackImg = ImageIO.read(trackImgUrl2);
            } else if (img == 3) {
                URL trackImgUrl3 = this.getClass().getResource("/MoonGame/resources/images/track3.png");
                trackImg = ImageIO.read(trackImgUrl3);
            } else if (img == 4){
                URL trackImgUrlEnd = this.getClass().getResource("/MoonGame/resources/images/track0end.png");
                trackImg = ImageIO.read(trackImgUrlEnd);
            }

            //get widht of the track
            //courseImgWidth = trackImg.getWidth();
        } catch (IOException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    //when Update is called the game will move the course depending on the 
    //car position
    public void Update(int speedY, int y) {
        if(speedY <= -4 || y <= 350){
            speed = speedY;
            this.y -= -50;
        } else if(speedY <= -3){
            speed = speedY;
            this.y -= -19;
        } else if(speedY < 0){
            speed = speedY;
            this.y -= -10;
        } else if(y >= 600){
            speed = speedY * 2;
            this.y +=speed;
        }
        
        
        
        
    }
    
    
    public int getY(){
        return y;
    }
    
    
    

    public void changeColor() {
        this.trackImg = trackImg1;

    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(trackImg, x, y, null);
    }

}
