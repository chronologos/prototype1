import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.jogamp.opengl.*;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;


public class Triangle implements GLEventListener {
  private Texture earthTexture;
  private float state = 0;
  private int direction = 1;
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
      state = 9999;
      direction = -1;
    }
    else if (state == 0) {
      state = 1;
      direction = 1;
    }
    x1 = 0.55f + (state/50000);
    x2 = 0.60f + (state/50000);
    y1 = 0.45f;
    y2 = 0.5f;

    render(drawable, x1, x2, y1, y2);
//    render(drawable, 1.0f, 0.0f, 1.0f, 0.0f);

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

  @Override
  public void dispose(GLAutoDrawable arg0) {
    //method body
  }

  @Override
  public void init(GLAutoDrawable arg0) {
    GL2 gl = arg0.getGL().getGL2();
    try {
      File f = new File("hs-2006-10-a-full_jpg.jpg"); 
      boolean exists = f.exists(); 
      BufferedImage bufferedImage = ImageIO.read(f);
      System.out.println(exists);
      //			InputStream stream = new BufferedInputStream(new FileInputStream("testimage1.JPG"));
      earthTexture = AWTTextureIO.newTexture(gl.getGLProfile(), bufferedImage, true);
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


}//end of classimport javax.media.opengl.GL2;
