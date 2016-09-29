package Tests;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jogamp.common.util.InterruptSource.Thread;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import Transition;

import java.util.Map;
import java.util.Random;

public class TextureSwapTest implements GLEventListener {
  private Random generator;
  private int swap = 1;
  private BufferedImage[] values;
  private BufferedImage backingImage;
  private int backingImgWidth;
  private int backingImgHeight;
  private Texture CurrentTexture;
  private Animator a;
  public int numCalls;
  public long startTime;
  private JFrame frame; //testing
  public static final int TILE_LENGTH = 1000;
  public static final int VIEWPORT_LENGTH = 400;
  public static final int OVERLAP_LENGTH = 450;
  public static final int SPEED = 1; // no.of pixels moved in each call to display, leads to movement speed of roughly 500 pixels/sec
  
  private Map<String, BufferedImage> subImages;
  private int tileX = 0;
  private int tileY = 0;
  private int xPos = 0;
  private int yPos = 0;
  
  private Transition t;
  private Tiler tiler;
  @Override
  public void display(GLAutoDrawable drawable) {
	GL2 gl = drawable.getGL().getGL2();
	BufferedImage randomValue;
	System.out.println(swap);
	randomValue = values[1];
	if (swap == 1){
		randomValue = values[0];
		swap = -1;
	}
	else{
		randomValue = values[1];
		swap = 1;

	}
	
	System.out.println(randomValue);
	
	CurrentTexture = AWTTextureIO.newTexture(gl.getGLProfile(), randomValue, false);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    gl.glEnable(GL.GL_TEXTURE_2D);
    CurrentTexture.enable(gl);
    CurrentTexture.bind(gl);
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(1, 1); // top right
    gl.glVertex3f(1.0f, 1.0f, 0);
    gl.glTexCoord2f(0, 1); // top left
    gl.glVertex3f(-1.0f, 1.0f, 0);
    gl.glTexCoord2f(0, 0); // bot left
    gl.glVertex3f(-1.0f, -1.0f, 0);
    gl.glTexCoord2f(1, 0); //bot right
    gl.glVertex3f(1.0f, -1.0f, 0);

    gl.glEnd();
    

  }

  

  @Override
  public void dispose(GLAutoDrawable arg0) {
    //method body
  }

  @Override
  public void init(GLAutoDrawable arg0) {
	System.out.println("init() called");
	generator = new Random();
	
    GL2 gl = arg0.getGL().getGL2();
    try {
      File f = new File("Periodic_table_large.png");
      boolean exists = f.exists(); 
      System.out.println(exists);
      BufferedImage bufferedImage = ImageIO.read(f);
      backingImage = bufferedImage;
      backingImgWidth = backingImage.getWidth();
      BufferedImage aa = bufferedImage.getSubimage(200, 200, 400, 400);
      BufferedImage bb = bufferedImage.getSubimage(0, 0, 400, 400);

//      tiler = new GridCreateTest(backingImage, TILE_LENGTH, VIEWPORT_LENGTH, OVERLAP_LENGTH); // Precomputing of tiles is complete
//      subImages = tiler.makeGrid(); // map of tile coordinates to subimages
      values = new BufferedImage[2];
      values[0] = aa;
      values[1] = bb;
      BufferedImage randomValue = values[0];
//      t = new Transition(OVERLAP_LENGTH, VIEWPORT_LENGTH, TILE_LENGTH);
//      String startTileKey = GridCreateTest.coordinateConverter(new int[]{0,0, TILE_LENGTH, TILE_LENGTH});
//      BufferedImage startImage = subImages.get(startTileKey);
      CurrentTexture = AWTTextureIO.newTexture(gl.getGLProfile(), randomValue, false);
      CurrentTexture.enable(gl);
      CurrentTexture.bind(gl);
      a = new Animator(arg0);
      a.start();
      numCalls = 0;
      startTime = System.currentTimeMillis();
    }
    catch (IOException exc) {
      exc.printStackTrace();
      System.exit(1);
    }
    // method body
  }

  @Override
  public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
    // method body
  }
  
  public static void main(String[] args) {

    //getting the capabilities object of GL2 profile        
    final GLProfile profile = GLProfile.get(GLProfile.GL2);
    GLCapabilities capabilities = new GLCapabilities(profile);

    // The canvas
    final GLCanvas glcanvas = new GLCanvas(capabilities);
    Line l = new Line();
    TextureSwapTest t = new TextureSwapTest();
    
    glcanvas.addGLEventListener(l);
    glcanvas.addGLEventListener(t);
    glcanvas.setSize(VIEWPORT_LENGTH, VIEWPORT_LENGTH);

    //creating frame
    final Frame frame = new Frame ("straight Line");

    
    //adding canvas to frame
    frame.add(glcanvas);
    frame.setSize(400,400);
    //frame.setSize(400, 600);
    frame.setVisible(true);
    //Animator animator = new Animator(glcanvas);
    
      // by default, an AWT Frame doesn't do anything when you click
      // the close button; this bit of code will terminate the program when
      // the window is asked to close
    

  }//end of main



}//end of classimport javax.media.opengl.GL2;
