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
	@Override
	public void display(GLAutoDrawable drawable) {
        render(drawable);
		
	}
	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL.GL_TEXTURE_2D);
        earthTexture.enable(gl);
        earthTexture.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLES);                           // Begin drawing triangle sides

      

//	    gl.glColor3f( 1.0f, 0.0f, 0.0f);                     // Set colour to red
	    gl.glTexCoord2f(0.0f, 0.0f);
	    gl.glVertex3f( 0.0f, 1.0f, 1.0f);                       // Top vertex
	    gl.glTexCoord2f(1.0f, 1.0f);
	    gl.glVertex3f(-1.0f,-1.0f, 0.0f);                       // Bottom left vertex
	    gl.glTexCoord2f(0.0f, 1.0f);
	    gl.glVertex3f( 1.0f,-1.0f, 0.0f);                       // Bottom right vertex

        gl.glEnd();
//		gl.glBegin (GL2.GL_LINES);
//
//		//drawing the base
//		gl.glBegin (GL2.GL_LINES);
//		gl.glVertex3f(-0.50f, -0.50f, 0);
//		gl.glVertex3f(0.50f, -0.50f, 0);
//		gl.glEnd();
//
//		//drawing the right edge
//		gl.glBegin (GL2.GL_LINES);
//		gl.glVertex3f(0f, 0.50f, 0);
//		gl.glVertex3f(-0.50f, -0.50f, 0);
//		gl.glEnd();
//
//		//drawing the lft edge
//		gl.glBegin (GL2.GL_LINES);
//		gl.glVertex3f(0f, 0.50f, 0);
//		gl.glVertex3f(0.50f, -0.50f, 0);
//		gl.glEnd();
//		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		//method body
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		try {
			File f = new File("testimage1.JPG"); 
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