import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Prototype implements GLEventListener {
	private int xDirection = 1;
	private int yDirection = 0;


	private Texture CurrentTexture;
	public int numCalls;
	public long startTime;
	public static final String TILE_DIR = "lib/tiles/";
	public static final int TILE_LENGTH = 1000;
	public static final int VIEWPORT_LENGTH = 400;
	public static final int OVERLAP_LENGTH = 450;
	public static final int SPEED = 1; // no.of pixels moved in each call to display, leads to movement speed of roughly 500 pixels/sec
	public static final int IMG_WIDTH = 15852;
	public static final int IMG_HEIGHT = 12392;

	private Map<String, Texture> subTextures;

	private int tileX = 0;
	private int tileY = 0;
	private int xPos = 0;
	private int yPos = 0;

	private int corners = 0;

	private Transition t;
	private Tiler tiler;
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		if (t == null) t = new Transition(OVERLAP_LENGTH, VIEWPORT_LENGTH, TILE_LENGTH);

		int tileIncrement = t.nextTile(xPos + (int)((float)VIEWPORT_LENGTH/2), yPos + (int)((float)VIEWPORT_LENGTH/2), xDirection, yDirection, tileX, tileY);

		if (tileIncrement != 0) { // New tile
			System.out.println("Switching tiles! xPos: " + xPos + "; Tile increment: " + tileIncrement);
			if (!getNextImage(tileX, tileY, tileIncrement, drawable, gl)) {
				System.out.println("Nerd Alert! Reversing!");
			}
		}

		if (xPos + TILE_LENGTH >= IMG_WIDTH - SPEED && xDirection == 1) {
			System.out.println("Hit right end of image, reversing!");
			if (++corners % 4 == 0) {
				xDirection *= -1;
			}
			else {
				xDirection = 0;
			}
			yDirection = 1;
		}

		if (xPos - TILE_LENGTH <= SPEED && xDirection == -1) {
			if (++corners % 4 == 0) {
				xDirection *= -1;
			}
			else {
				xDirection = 0;
			}
			yDirection = -1;
		}

		if (yPos + TILE_LENGTH >= IMG_HEIGHT - SPEED && yDirection == 1) {
			if (++ corners % 4 == 0) {
				yDirection *= -1;
			}
			else {
				yDirection = 0;
			}
			xDirection = -1;
		}

		if (yPos - TILE_LENGTH <= SPEED && yDirection == -1) {
			if (++ corners % 4 == 0) {
				yDirection *= -1;
			}
			else {
				yDirection = 0;
			}
			xDirection = 1;
		}

		xPos += SPEED * xDirection;
		yPos += SPEED * yDirection;

		float x1 = (float)(xPos - tileX)/TILE_LENGTH;
		float x2 = x1 + (float)VIEWPORT_LENGTH/TILE_LENGTH;
		float y1 = (float)(yPos - tileY)/TILE_LENGTH;
		float y2 = y1 + (float)VIEWPORT_LENGTH/TILE_LENGTH;

		render(drawable, x1, x2, y1, y2, gl);
	}

	private boolean getNextImage(int tileX, int tileY, int tileIncrement, GLAutoDrawable drawable, GL2 gl) {
		tileIncrement += 4;
		int yDelta = (tileIncrement / 3) - 1;
		int xDelta = (tileIncrement % 3) - 1;
		int nextTileX = tileX + xDelta * TILE_LENGTH - xDelta * OVERLAP_LENGTH;
		int nextTileY = tileY + yDelta * TILE_LENGTH - yDelta * OVERLAP_LENGTH;

		System.out.println("Current Tile X: " + this.tileX + "; " + "NextTile X : " + nextTileX);


		if (nextTileX > tiler.getMaxTileX() || nextTileY > tiler.getMaxTileY()) {
			System.out.println("Next tile exceeds last tile, not switching");
			return false;
		}

		int curXTileWidth;
		//compute curXTileWidth rather than using TILE_LENGTH due to last row and column of tiles having different dimensions.
		if (nextTileX + TILE_LENGTH > IMG_WIDTH){
			curXTileWidth = IMG_WIDTH - nextTileX;
		}
		else if (nextTileX < 0){
			return false;
		}
		else{
			curXTileWidth = TILE_LENGTH;
		}
		int curYTileHeight;

		if (nextTileY + TILE_LENGTH > IMG_HEIGHT) {
			System.out.println("Next TileY: " + nextTileY + "; Setting next tile height to " + (IMG_HEIGHT - nextTileY));
			curYTileHeight = IMG_HEIGHT - nextTileY;
		}
		else if (nextTileY < 0) {
			return false;
		}
		else {
			curYTileHeight = TILE_LENGTH;
		}
		String imgKey = tiler.coordinateConverter(new int[]{nextTileX, nextTileY, curXTileWidth, curYTileHeight});
		System.out.println("Key for next image: " + imgKey);

		CurrentTexture = subTextures.get(imgKey);
		System.out.println("---");

		this.tileX = nextTileX;
		this.tileY = nextTileY;
		return true;
	}


	private void render(GLAutoDrawable drawable, float x1, float x2, float y1, float y2, GL2 gl) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL.GL_TEXTURE_2D);
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

	@Override
	public void dispose(GLAutoDrawable arg0) {
		//method body
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		System.out.println("init() called");
		GL2 gl = arg0.getGL().getGL2();
		tiler = new Tiler(TILE_LENGTH, OVERLAP_LENGTH, IMG_WIDTH, IMG_HEIGHT);
		System.out.println("Constructing new textures");
		subTextures = tiler.makeGridFiles(arg0);
		t = new Transition(OVERLAP_LENGTH, VIEWPORT_LENGTH, TILE_LENGTH);
		String startTileKey = tiler.coordinateConverter(new int[]{0,0, TILE_LENGTH, TILE_LENGTH});
		CurrentTexture = subTextures.get(startTileKey);
		CurrentTexture.enable(gl);
		System.out.println("Initialized and enabled current texture");
		//final FPSAnimator animator = new FPSAnimator(arg0, (int)((float)(180)/SPEED));
		final FPSAnimator animator = new FPSAnimator(arg0, 180);
		animator.start();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// method body
	}

	public class Tiler {
		private int tileLength;
		private int overlapLength;
		private int imageHeight;
		private int imageWidth;
		private int maxTileX = 0;
		private int maxTileY = 0;

		public Tiler(int tileLength, int overlapLength, int imageWidth, int imageHeight) {
			this.tileLength = tileLength;
			this.overlapLength = overlapLength;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
		}

		public List<int[]> CalculateGrids(){
			// Given tileLength, overlapLength and an image, calculate the coordinates of the tiles required to
			// cover the image. GetSubimage accepts coordinates of the form 
			// (xPixelsFromTopLeft, yPixelsFromTopLeft, Width, Height)
			int currentimageWidth = tileLength;
			int currentimageHeight = tileLength;
			int finalColTileWidth = tileLength;
			int finalRowTileLength = tileLength;

			List<Integer> subImgYCoords = new ArrayList<Integer>();
			List<Integer> subImgXCoords = new ArrayList<Integer>();  
			// include 0 for both x and y coordinates
			subImgXCoords.add(0);
			subImgYCoords.add(0);
			List<int[]> coordinates = new ArrayList<int[]>();

			// tiles usually do not exactly cover image. In last row and column they will be shorter. 
			while (currentimageWidth < imageWidth){
				int nextXCoordinate = currentimageWidth - overlapLength;
				subImgXCoords.add(nextXCoordinate);
				currentimageWidth = nextXCoordinate + tileLength;
				if (currentimageWidth > imageWidth){
					finalColTileWidth = tileLength - (currentimageWidth - imageWidth);
					maxTileX = nextXCoordinate;
				}   
			}

			while (currentimageHeight < imageHeight){
				int nextYCoordinate = currentimageHeight - overlapLength;
				subImgYCoords.add(nextYCoordinate);
				currentimageHeight = nextYCoordinate + tileLength;
				if (currentimageHeight > imageHeight){
					finalRowTileLength = tileLength - (currentimageHeight - imageHeight);
					maxTileY = nextYCoordinate;
					//					System.out.println("Max Tile Y : " + maxTileY + "; RowTileLength: " + finalRowTileLength);
				}
			}

			int xTileLength = tileLength;
			int yTileLength = tileLength;
			for (int i = 0; i < subImgXCoords.size(); i++){
				for (int j = 0; j < subImgYCoords.size(); j++){
					if (i == subImgXCoords.size()-1){
						xTileLength = finalColTileWidth;  
					}
					else {
						xTileLength = tileLength;
					}
					if (j == subImgYCoords.size()-1){
						yTileLength = finalRowTileLength;
					}
					else {
						yTileLength = tileLength;
					}
					int[] coords = {subImgXCoords.get(i), subImgYCoords.get(j), xTileLength, yTileLength};
					//System.out.printf("%d %d %d %d\n", coords[0], coords[1], coords[2], coords[3]);
					coordinates.add(coords);
				}
			}
			System.out.printf("Finished calculating coords.");

			return coordinates;
		}

		public String coordinateConverter(int[] coords){
			// Converts int[] of xcoord, ycoord, width, height to string suitable use as hashmap key
			StringBuilder bob = new StringBuilder("");
			for (int i = 0; i<coords.length - 1; i++){
				bob.append(Integer.toString(coords[i]));
				bob.append(" ");
			}
			bob.append(Integer.toString(coords[coords.length - 1]));
			return bob.toString();
		}

		// Approach 2 - Partitioning the original image file into multiple files
		public Map<String, Texture> makeGridFiles(GLAutoDrawable drawable) {
			System.out.printf("Loading files in %s \n", TILE_DIR);
			// Using coordinates from CalculateGrids, Generate hashmap of subimages.
			List<int[]> allCoords = CalculateGrids();
			String key = null;
			BufferedImage subImage = null;
			File subImageFile = null;
			Texture subTexture = null;
			Map<String, Texture> mySubTextures = new HashMap<String, Texture>();
			GL2 gl = drawable.getGL().getGL2();
			for (int[] coords : allCoords) {
				System.out.println(key);
				key = coordinateConverter(coords);
				try {
					subImageFile = new File(TILE_DIR + key + ".jpg");
					if (!subImageFile.exists()){
						throw new IOException("no file found");
					}

					subImage = ImageIO.read(subImageFile);
					subTexture = AWTTextureIO.newTexture(gl.getGLProfile(), subImage, true);
					mySubTextures.put(key, subTexture);
				}
				catch (IOException e) {
					System.out.println("GG I fucked up");
					System.exit(1);
				}
			}
			return mySubTextures;
		}


		public int getMaxTileX() {
			return maxTileX;
		}

		public int getMaxTileY() {
			return maxTileY;
		}
	}


	public static void main(String[] args) {
		//getting the capabilities object of GL2 profile        
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The canvas
		final GLCanvas glcanvas = new GLCanvas(capabilities);
		Prototype t = new Prototype();
		glcanvas.addGLEventListener(t);
		glcanvas.setSize(VIEWPORT_LENGTH, VIEWPORT_LENGTH);
		//creating frame
		final Frame frame = new Frame ("straight Line");
		//adding canvas to frame
		frame.add(glcanvas);
		frame.setSize(glcanvas.getWidth(), glcanvas.getHeight());
		//frame.setSize(400, 600);
		frame.setVisible(true);

	}



}
