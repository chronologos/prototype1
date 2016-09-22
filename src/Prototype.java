import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.util.Map;

public class Prototype implements GLEventListener {
  private int direction = 1;
  
  private BufferedImage backingImage;
  private int backingImgWidth;
  private int backingImgHeight;
  private Texture CurrentTexture;
  private Animator a;
  public int numCalls;
  public long startTime;
  public static final int TILE_LENGTH = 1500;
  public static final int VIEWPORT_LENGTH = 400;
  public static final int OVERLAP_LENGTH = 450;
  public static final int SPEED = 1; // no.of pixels moved in each call to display, leads to movement speed of roughly 500 pixels/sec
  
  private Map<String, BufferedImage> subImages;
  private int tileX = 0;
  private int tileY = 0;
  private int xPos = 0;
  private int yPos = 0;
  
  private Transition t;
  private GridCreateTest tiler;
  @Override
  public void display(GLAutoDrawable drawable) {
	GL2 gl = drawable.getGL().getGL2();

    int tileIncrement = t.nextTile(xPos + (int)((float)VIEWPORT_LENGTH/2), yPos + (int)((float)VIEWPORT_LENGTH/2), direction, 0, tileX, tileY);
    
    if (tileIncrement != 0) { // New tile
      System.out.println("Switching tiles! xPos: " + xPos + "; Tile increment: " + tileIncrement);
      //int[] nextCoords = getNextImage(tileX, tileY, tileIncrement, drawable);
      //xPos = nextCoords[0];
      //yPos = nextCoords[1]; Ignore y position for now since moving only in 1-D
      if (!getNextImage(tileX, tileY, tileIncrement, drawable, gl)) {
        System.out.println("Nerd Alert! Reversing!");
        ///direction *= -1;
      }
    }
    
    if ((xPos >= backingImgWidth && direction == 1) || (xPos <= 0 && direction == -1)) {
      System.out.println("Hit end of image, reversing!");
      direction *= -1;
    }
          
    xPos += SPEED * direction;
    
    float x1 = (float)(xPos - tileX)/TILE_LENGTH;
    float x2 = x1 + (float)VIEWPORT_LENGTH/TILE_LENGTH;
    float y1 = 0.05f;
    float y2 = 0.95f;
    render(drawable, x1, x2, y1, y2, gl);
//    numCalls++;
//    if (System.currentTimeMillis() - startTime > 5000) {
      //System.out.println("FPS: " + (double)numCalls/((double)(System.currentTimeMillis() - startTime)/1000));
//    }

  }

  private boolean getNextImage(int tileX, int tileY, int tileIncrement, GLAutoDrawable drawable, GL2 gl) {
    tileIncrement += 4;
    int yDelta = (tileIncrement / 3) - 1;
    int xDelta = (tileIncrement % 3) - 1;
    int nextTileX = tileX + xDelta * TILE_LENGTH - xDelta * OVERLAP_LENGTH;
    int nextTileY = tileY + yDelta * TILE_LENGTH - yDelta * OVERLAP_LENGTH;
    
    System.out.println("Current Tile X: " + this.tileX + "; " + "NextTile X : " + nextTileX);
    int curXTileWidth;
    //compute curXTileWidth rather than using TILE_LENGTH due to last row and column of tiles having different dimensions.
    if (nextTileX + TILE_LENGTH > backingImgWidth){
    	curXTileWidth = backingImgWidth - nextTileX;
    }
    else if (nextTileX < 0){
    	 direction *= -1;
    	 curXTileWidth = TILE_LENGTH;
    	 nextTileX = 0;
    }
    else{
    	curXTileWidth = TILE_LENGTH;
    }
    BufferedImage nextImage = subImages.get(GridCreateTest.coordinateConverter(new int[]{nextTileX, nextTileY, curXTileWidth, TILE_LENGTH}));
    System.out.println(nextImage);

    CurrentTexture = AWTTextureIO.newTexture(gl.getGLProfile(), nextImage, true);
    System.out.println(nextImage);
    System.out.println(CurrentTexture.getTarget());
    System.out.println("---");
    
    this.tileX = nextTileX;
    this.tileY = nextTileY;
    return true;
  }
  
  
  private void render(GLAutoDrawable drawable, float x1, float x2, float y1, float y2, GL2 gl) {
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    gl.glEnable(GL.GL_TEXTURE_2D);
//    earthTexture.enable(gl);
    CurrentTexture.bind(gl);
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(x2, y1); // bot right
    gl.glVertex3f(1.0f, 1.0f, 0);
    gl.glTexCoord2f(x1, y1); // bot left
    gl.glVertex3f(-1.0f, 1.0f, 0);
    gl.glTexCoord2f(x1, y2); // top left
    gl.glVertex3f(-1.0f, -1.0f, 0);
    gl.glTexCoord2f(x2, y2); //top right
    gl.glVertex3f(1.0f, -1.0f, 0);
    gl.glEnd();
  }
  
  private void getNewQuadrant(int quadrant, GLAutoDrawable drawable) {
    BufferedImage subImage = backingImage.getSubimage((quadrant % 2) * backingImage.getWidth()/2, (quadrant/2) * backingImage.getHeight()/2, backingImage.getWidth()/2, backingImage.getHeight()/2);
    GL2 gl = drawable.getGL().getGL2();
    CurrentTexture = AWTTextureIO.newTexture(gl.getGLProfile(), subImage, true);
  }

  @Override
  public void dispose(GLAutoDrawable arg0) {
    //method body
  }

  @Override
  public void init(GLAutoDrawable arg0) {
	System.out.println("init() called");
	
	
    GL2 gl = arg0.getGL().getGL2();
    try {
      File f = new File("Periodic_table_large.png");
//      File f = new File("1999-fletcher-watermanhills.png");
//      File f = new File("hs-2006-10-a-full_png.png");
//      File f = new File("WORLD.png");

//      boolean exists = f.exists(); 
      BufferedImage bufferedImage = ImageIO.read(f);
      backingImage = bufferedImage;
      backingImgWidth = backingImage.getWidth();
      tiler = new GridCreateTest(backingImage, TILE_LENGTH, VIEWPORT_LENGTH, OVERLAP_LENGTH); // Precomputing of tiles is complete
      subImages = tiler.makeGrid(); // map of tile coordinates to subimages
      t = new Transition(OVERLAP_LENGTH, VIEWPORT_LENGTH, TILE_LENGTH);
      String startTileKey = GridCreateTest.coordinateConverter(new int[]{0,0, TILE_LENGTH, TILE_LENGTH});
      BufferedImage startImage = subImages.get(startTileKey);
      CurrentTexture = AWTTextureIO.newTexture(gl.getGLProfile(), startImage, true);
      CurrentTexture.enable(gl);
      final FPSAnimator animator = new FPSAnimator(arg0, 90);
      animator.start();
//      numCalls = 0;
//      startTime = System.currentTimeMillis();
    }
    catch (IOException exc) {
      exc.printStackTrace();
      System.exit(1);
    }
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
    Prototype t = new Prototype();
    glcanvas.addGLEventListener(l);
    glcanvas.addGLEventListener(t);
    glcanvas.setSize(VIEWPORT_LENGTH, VIEWPORT_LENGTH);
    //creating frame
    final Frame frame = new Frame ("straight Line");
    //adding canvas to frame
    frame.add(glcanvas);
    frame.setSize(glcanvas.getWidth(), glcanvas.getHeight());
    //frame.setSize(400, 600);
    frame.setVisible(true);
    //Animator animator = new Animator(glcanvas);
    
      // by default, an AWT Frame doesn't do anything when you click
      // the close button; this bit of code will terminate the program when
      // the window is asked to close
    

  }//end of main



}//end of classimport javax.media.opengl.GL2;
