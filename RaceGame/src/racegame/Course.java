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
 * @author www.gametutorial.net
 */

public class Course {
    
    /**
     * X coordinate of the landing area.
     */
    public int x;
    /**
     * Y coordinate of the landing area.
     */
    public int y;
    
    
    
    
    private double speed = 1;
    
    
    /**
     * Image of landing area.
     */
    private BufferedImage landingAreaImg;
    
    /**
     * Width of landing area.
     */
    public int landingAreaImgWidth;
    
    
    public Course(double x, double y)
    {
        Initialize(x, y);
        LoadContent();
    }
    
    
    private void Initialize(double x, double y)
    {   
        // X coordinate of the landing area is at 23% frame width.
        this.x = (int)(Framework.frameWidth * x);
        // Y coordinate of the landing area is at 20% frame height.
        this.y = (int)(Framework.frameHeight * y);
    }
    
    private void LoadContent()
    {
        try
        {
            URL landingAreaImgUrl = this.getClass().getResource("/MoonGame/resources/images/track.png");
            landingAreaImg = ImageIO.read(landingAreaImgUrl);
            landingAreaImgWidth = landingAreaImg.getWidth();
        }
        catch (IOException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void Update(){
        
        y += speed;
    }
    
    
    
    
    
    
    
    
    
    
    public void Draw(Graphics2D g2d)
    {
        this.Update();
        g2d.drawImage(landingAreaImg, x, y, null);
    }
    
}
