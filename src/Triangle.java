import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.Rectangle;

public class Triangle implements GLEventListener {
  private Texture earthTexture;
  private float state = 0;
  private int direction = 1;
  
  private int quadrant = 0;
  private BufferedImage backingImage;
  
  private Animator a;
  public int numCalls;
  public long startTime;

  
  @Override
  public void display(GLAutoDrawable drawable) {
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    if (state != 10000 & state != 0) {
      if (direction == 1){
        state += 1;
      }
      else{
        state -= 1;
      }
    }
    else if (state == 10000){
      
      if (quadrant == 1 || quadrant == 3) {
        state = 9999;
        direction = -1;
      }
      else {
        System.out.println("Reached right extreme of left subImage! Calling getNewQuadrant!");
        getNewQuadrant(++quadrant, drawable);
        state = 1;
      }
    }
    else if (state == 0) {
      if (quadrant == 0 || quadrant == 2) {
            state = 1;
            direction = 1;
      }
      else {
        System.out.println("Reached left extreme of right subImage! Calling getNewQuadrant!");
        getNewQuadrant(--quadrant, drawable);
        state = 9999;
      }
    }
    x1 = 0.55f + (state/50000);
    x2 = 0.60f + (state/50000);
    y1 = 0.45f;
    y2 = 0.5f;

    render(drawable, x1, x2, y1, y2);
//    render(drawable, 1.0f, 0.0f, 1.0f, 0.0f);
    numCalls++;
    if (System.currentTimeMillis() - startTime > 5000) {
      //System.out.println("FPS: " + (double)numCalls/((double)(System.currentTimeMillis() - startTime)/1000));
    }

  }

  private void render(GLAutoDrawable drawable, float x1, float x2, float y1, float y2) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glEnable(GL.GL_TEXTURE_2D);
    earthTexture.enable(gl);
    earthTexture.bind(gl);
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(x2, y2); // top right
    gl.glVertex3f(1.0f, 1.0f, 0);
    gl.glTexCoord2f(x1, y2); // top left
    gl.glVertex3f(-1.0f, 1.0f, 0);
    gl.glTexCoord2f(x1, y1); // bot left
    gl.glVertex3f(-1.0f, -1.0f, 0);
    gl.glTexCoord2f(x2, y1); //bot right
    gl.glVertex3f(1.0f, -1.0f, 0);

    gl.glEnd();


  }
  
  private void getNewQuadrant(int quadrant, GLAutoDrawable drawable) {
    BufferedImage subImage = backingImage.getSubimage((quadrant % 2) * backingImage.getWidth()/2, (quadrant/2) * backingImage.getHeight()/2, backingImage.getWidth()/2, backingImage.getHeight()/2);
    GL2 gl = drawable.getGL().getGL2();
    earthTexture = AWTTextureIO.newTexture(gl.getGLProfile(), subImage, true);

    
  }

  @Override
  public void dispose(GLAutoDrawable arg0) {
    //method body
  }

  @Override
  public void init(GLAutoDrawable arg0) {
    GL2 gl = arg0.getGL().getGL2();
    try {
      //File f = new File("lib/10MB.jpg");
    //  File f = new File("lib/testimage1.JPG");
      File f = new File("lib/LargeImage.jpg");
      boolean exists = f.exists(); 
      BufferedImage bufferedImage = ImageIO.read(f);
      backingImage = bufferedImage;
      System.out.println(exists);
      //      InputStream stream = new BufferedInputStream(new FileInputStream("testimage1.JPG"));
      
      //BufferedImage subImage = bufferedImage.getData(new Rectangle(bufferedImage.getWidth()/2, bufferedImage.getHeight()/2));
      BufferedImage subImage = bufferedImage.getSubimage(0, 0, bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);
      
      //earthTexture = AWTTextureIO.newTexture(gl.getGLProfile(), bufferedImage, true);
      earthTexture = AWTTextureIO.newTexture(gl.getGLProfile(), subImage, true);
      
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
    Triangle t = new Triangle();
    
    glcanvas.addGLEventListener(l);
    glcanvas.addGLEventListener(t);
    glcanvas.setSize(400, 400);

    //creating frame
    final Frame frame = new Frame ("straight Line");

    
    
    
    
    //adding canvas to frame
    frame.add(glcanvas);

    frame.setSize(400, 600);
    frame.setVisible(true);
    //Animator animator = new Animator(glcanvas);
    
      // by default, an AWT Frame doesn't do anything when you click
      // the close button; this bit of code will terminate the program when
      // the window is asked to close
    

  }//end of main



}//end of classimport javax.media.opengl.GL2;
