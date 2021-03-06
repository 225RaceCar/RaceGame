package racegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Actual game.
 *
 */
public class Game {

    

    Random random = new Random();
    
    int[] points;

    //the car class
    private Car playerCar;
    private EnemyCar enemyCar;

    //variables to move the background
    private BufferedImage bg;
    private MovingBackground movingBg;

    //not used
    private int[] courseListList = new int[4];

    //the course, last part should be the finish line
    private ArrayList<Course> courseList = new ArrayList<Course>();

    //background for the game
    private BufferedImage backgroundImg;

    //image overlayed on bg for when the game is over
    private BufferedImage redBorderImg;
    
    //to test which points have been passed
    boolean[] passedPoint;
    private int[] passedPointLoc;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override

            //run the thread for game class
            public void run() {
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
    private void Initialize() {
        //initialize cars
        playerCar = new Car();
        enemyCar = new EnemyCar();

        //create the course
        CreateCourse();

        //create the background
        movingBg = new MovingBackground();

    }
    
    
    




    //set the FIRST course position place x and y
    //subsequent courses will be placed by the for loop, multiplies by i
    private int x = Framework.frameWidth / 3;
    private int y = 600;







    //when game is initialized the course is created
    //this holds the logic and places the course
    private void CreateCourse() {

        //these will help set and control logic for courese
        points = new int[4];
        passedPoint = new boolean[4];
        passedPointLoc = new int[4];
        
        for (int i = 0; i < points.length; i++) {
            points[i] = random.nextInt(1500 + 200);
            passedPoint[i] = false;
        }

        for (int j = 0; j < points.length; j++) {
            
            //used to reset the x after finish line is made 
            x += 125;
            
            //creates the four segments of th course
            for (int i = 0; i < points[j]; i++) {
                
                //create course object
                Course segment = new Course(x, y, j);

                //add that course to the CourseList
                courseList.add(segment);
                y -= 1;
                
                
            }
            
            //we need four indexes of section "finish lines"
            passedPointLoc[j] = courseList.size();
            
            //this will set a end of point 
            //first make room to set the point
            y -= 40;
            x -= 125;
            Course segment = new Course(x, y, 4);
            courseList.add(segment);
            
            //make room for the finish line
            y -= 40;
        }
        System.out.println(courseList.size());
        //courseList.get(6).changeColor();
    }

    





//load global resources like the bg and text for the standings
    private void LoadContent() {
        try {
            //load bg image
            URL bgUrl = this.getClass().getResource("/raceresources/resources/images/stardust.png");
            bg = ImageIO.read(bgUrl);

            //loading background image
            //load bg image
            URL backgroundImageUrl = this.getClass().getResource("/raceresources/resources/images/stardust.png");
            backgroundImg = ImageIO.read(bgUrl);

            //load bg image
            URL redBorderImgUrl = this.getClass().getResource("/raceresources/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        } //if images not found ooooooooops
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

        movingBg.Initialize(bg, 4, 0);
    }

    //resets variables in car
    public void RestartGame() {
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
    public void UpdateGame(long gameTime, Point mousePosition) {
        // Move the car
        playerCar.Update();
        movingBg.Update();
        enemyCar.Update(playerCar.getSpeedY(), passedPoint[2]);

        for (int i = 0; i < courseList.size(); i++) {
            courseList.get(i).Update(playerCar.getSpeedY(), playerCar.getY());
        }

        // Checks where the player car is. Is it still in the space or is it raceWin or crashed?
        //AS LONG AS the Y of car is less than Y of the last course, finish line
        //has not been crossed
        //carImgHeight can be removed
        if (playerCar.y < courseList.get(courseList.size() - 1).y) {

            playerCar.raceWin = true;
            //change the gamestate, on next loop gameover will occur
            System.out.println(courseList.size());
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }

    /**
     * Draw the game to the screen. remember that override in Framework drawing
     * earlier
     *
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition) {

        //draws according to Framework canvas size 
        //g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        //draw moving bg???????
        movingBg.Draw(g2d);

        // Draws the course objects
        for (int i = 0; i < courseList.size(); i++) {
            courseList.get(i).Draw(g2d);
        }
        
        if(playerCar.getY() < courseList.get(passedPointLoc[0]).getY()){
            //drawpoint passed
            g2d.setColor(Color.white);
            g2d.drawString("Passed Point A!!!", 5, 175);
            passedPoint[0] = true;
        } 
        
        if(playerCar.getY() < courseList.get(passedPointLoc[1]).getY()){
            //drawpoint passed
            g2d.setColor(Color.white);
            g2d.drawString("Passed Point B!!!", 5, 200);
            passedPoint[1] = true;
        } 
        
        if(playerCar.getY() < courseList.get(passedPointLoc[2]).getY()){
            //drawpoint passed
            g2d.setColor(Color.white);
            g2d.drawString("Passed Point C!!!", 5, 225);
            passedPoint[2] = true;
        } 
        
        //draw the cars
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
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime) {
        Draw(g2d, mousePosition);

        //draws the text to restart
        //g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);

        //draw text to say whether you won or lost
        if (playerCar.raceWin) {
            g2d.drawString("You have successfully won the race!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
            g2d.drawString("You have won in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
        } else {
            g2d.setColor(Color.red);
            g2d.drawString("You have crashed the car!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
    }
    
    
    public boolean getPassedPoint(int i){
        return passedPoint[i];
    }
}
