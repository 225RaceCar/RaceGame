/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gamegui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Ian
 */
public class GameGUI extends Canvas{

    /**
     * @param args the command line arguments
     */
    
    /** The strategy that allows us to use accelerate page flipping */
    private BufferStrategy strategy;
    
    /** True if the game is currently "running", i.e. the game loop is looping */
    private boolean gameRunning = true;
    
    
    //game constructor
    public GameGUI() {
    //create a JFrame to display the game
    JFrame container = new JFrame("Race Car Game");
        
    // get hold the content of the frame and set up the resolution of the game
    JPanel panel = (JPanel) container.getContentPane();
    panel.setPreferredSize(new Dimension(800,600));
    panel.setLayout(null);
        
    // setup our canvas size and put it into the content of the frame
    setBounds(0,0,800,600);
    panel.add(this);
    
    // Tell AWT not to bother repainting our canvas since we're
    // going to do that our self in accelerated mode
    setIgnoreRepaint(true);
        
    //center the window in the middle of the screen
    container.setLocation(400, 300);
    
    
    // finally make the window visible 
    container.pack();
    container.setResizable(false);
    container.setVisible(true);
    
    // add a listener to respond to the user closing the window. If they
    // do we'd like to exit the game
    container.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
            System.exit(0);
			}
		});
    
    // add a key input system (defined below) to our canvas
    // so we can respond to key pressed
    
    //addKeyListener(new KeyInputHandler());
		
    // request the focus so key events come to us
    requestFocus();

    // create the buffering strategy which will allow AWT
    // to manage our accelerated graphics
    createBufferStrategy(2);
    strategy = getBufferStrategy();
		
    // initialise the entities in our game so there's something
    // to see at startup
    
    //initEntities();
	
    } //end the consstructor

   
    
    
    
    public void gameLoop(){
        
        long lastLoopTime = System.currentTimeMillis();
        
        while(gameRunning){
        // work out how long its been since the last update, this
	// will be used to calculate how far the entities should
	// move this loop
	long delta = System.currentTimeMillis() - lastLoopTime;
	lastLoopTime = System.currentTimeMillis();
        
        // Get hold of a graphics context for the accelerated 
	// surface and blank it out
	Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
	g.setColor(Color.white);
	g.fillRect(0,0,800,600);
        
        
        
        // finally, we've completed drawing so clear up the graphics
	// and flip the buffer over
	g.dispose();
	strategy.show();

	// finally pause for a bit. Note: this should run us at about
	// 100 fps but on windows this might vary each loop due to
	// a bad implementation of timer
	try { Thread.sleep(10); } catch (Exception e) {}
    }
    }
    
    
    public static void main(String argv[]) {
		GameGUI g =new GameGUI();

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
		g.gameLoop();
	}
}
