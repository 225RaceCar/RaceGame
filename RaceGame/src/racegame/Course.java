/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package racegame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ian
 */
public class Course {
    
    //total size of the course
    int totalCourseLength;
    
    //x and y coordinates
    int x, y;
    
    /**
     * Image of landing area.
     */
    private BufferedImage courseImg;
    
    //actual boundary of course in gui
    public int courseWidth;
    
    public Course() {
        initialize();
        loadContent();
    }
    
    private void initialize(){
         // X coordinate of the landing area is at 23% frame width.
        x = (int)(Framework.frameWidth * 0.20);
        // Y coordinate of the landing area is at 80% frame height.
        y = (int)(Framework.frameHeight * 0.80);
    }
    
    private void loadContent()
    {
        try
        {
            URL landingAreaImgUrl = this.getClass().getResource("/MoonGame/resources/images/track.png");
            courseImg = ImageIO.read(landingAreaImgUrl);
            courseWidth = courseImg.getWidth();
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
