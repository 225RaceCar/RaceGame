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
    private BufferedImage trackImg0;
    private BufferedImage trackImg1;
    private BufferedImage trackImg2;
    private BufferedImage trackImg3;

    private BufferedImage trackImg;

    /**
     * Width of landing area.
     */
    public int trackImgWidth;

    public Course(double x, double y, int img) {
        Initialize(x, y);
        LoadContent(img);
    }

    private void Initialize(double x, double y) {
        // X and Y coordinate of track passed from game.java
        this.x = (int) (Framework.frameWidth * x);
        this.y = (int) (Framework.frameHeight * y);
    }

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
            }

            //get widht of the track
            //trackImgWidth = trackImg.getWidth();
        } catch (IOException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Update() {
        y += speed;
    }

    public void changeColor() {
        this.trackImg = trackImg1;

    }

    public void Draw(Graphics2D g2d) {
        //this.Update();
        g2d.drawImage(trackImg, x, y, null);
    }

}
