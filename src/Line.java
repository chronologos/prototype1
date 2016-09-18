import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import java.awt.Frame;

import javax.swing.JFrame;

public class Line implements GLEventListener {

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();

		gl.glBegin (GL2.GL_LINES);//static field
		gl.glVertex3f(0.50f,-0.50f,0);
		gl.glVertex3f(-0.50f,0.50f,0);
		gl.glEnd();

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// method body
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		//method body
	}

	@Override
	public void init(GLAutoDrawable arg0) {
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
		glcanvas.setSize(400, 400);

		//creating frame
		final Frame frame = new Frame ("straight Line");

		//adding canvas to frame
		frame.add(glcanvas);

		frame.setSize(400, 600);
		frame.setVisible(true);
		Animator animator = new Animator(glcanvas);
		animator.start();
        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close

	}//end of main

}//end of classimport javax.media.opengl.GL2;