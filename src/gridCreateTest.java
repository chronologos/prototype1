
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import java.util.Map;
import java.util.HashMap;

public class gridCreateTest {
  /*
  public int TILELENGTH = 1000; // all sizes in px
  public int VIEWPORT_LENGTH = 400;
  public int OVERLAP_LENGTH = 450; // slightly larger than VIEWPORT_LENGTH
  */
  private int tileLength;
  private int viewportLength;
  private int overlapLength;

  private int imageHeight;
  private int imageWidth;
  private BufferedImage mainImage;
  
  private Map<String, BufferedImage> subImages;


  private int maxTileX = 0;
  private int maxTileY = 0;
  
  //public gridCreateTest(String imagePath){
  public gridCreateTest(BufferedImage mainImage, int tileLength, int viewportLength, int overlapLength) {
  /*
	  try {
      mainImage = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      // TODO Auto-generated catch block
    	e.printStackTrace();
    }
    */
	this.mainImage = mainImage;
	this.tileLength = tileLength;
	this.viewportLength = viewportLength;
	this.overlapLength = overlapLength;
    imageWidth = mainImage.getWidth();
    imageHeight = mainImage.getHeight();
    subImages = new HashMap<String, BufferedImage>();
    /**
    makeGrid();
    System.out.println("x length is " + Integer.toString(imageWidth) + " px.");
    System.out.println("y length is " + Integer.toString(imageHeight) + " px.");
    **/
    
 
  }
  
  public List<int[]> CalculateGrids(){
	  // Calculate List Containing
	  /*
	  int currentimageWidth = TILELENGTH;
	  int currentimageHeight = TILELENGTH;
	  int finalColTileWidth = TILELENGTH; 
	  int finalRowTileLength = TILELENGTH;
	  */
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
		  //int nextXCoordinate = currentimageWidth - OVERLAP_LENGTH;
		  int nextXCoordinate = currentimageWidth - overlapLength;
		  subImgXCoords.add(nextXCoordinate);
		  //currentimageWidth = nextXCoordinate + TILELENGTH;
		  currentimageWidth = nextXCoordinate + tileLength;
		  if (currentimageWidth > imageWidth){
			  //finalColTileWidth = TILELENGTH - (currentimageWidth - imageWidth);
			  finalColTileWidth = tileLength - (currentimageWidth - imageWidth);
			  maxTileX = nextXCoordinate;
		  }	  
	  }
	  while (currentimageHeight < imageHeight){
		  //int nextYCoordinate = currentimageHeight - OVERLAP_LENGTH;
		  int nextYCoordinate = currentimageHeight - overlapLength;
		  subImgYCoords.add(nextYCoordinate);
		  //currentimageHeight = nextYCoordinate + TILELENGTH;
		  currentimageHeight = nextYCoordinate + tileLength;
		  if (currentimageHeight > imageHeight){
			  //finalRowTileLength = TILELENGTH - (currentimageHeight - imageHeight);
			  finalRowTileLength = tileLength - (currentimageHeight - imageHeight);
			  maxTileY = nextYCoordinate;
		  }
	  }
	  //int xTileLength = TILELENGTH;
	  int xTileLength = tileLength;
	  //int yTileLength = TILELENGTH;
	  int yTileLength = tileLength;
	  for (int i = 0; i < subImgXCoords.size(); i++){
		  for (int j = 0; j < subImgYCoords.size(); j++){
			  if (i == subImgXCoords.size()-1){
				  xTileLength = finalColTileWidth;  
			  }
			  else {
				  //xTileLength = TILELENGTH;
				  xTileLength = tileLength;
			  }
			  if (j == subImgYCoords.size()-1){
				  // last row
				  yTileLength = finalRowTileLength;
				  
			  }
			  else {
				  //yTileLength = TILELENGTH;
				  yTileLength = tileLength;
			  }
			  int[] coords = {subImgXCoords.get(i), subImgYCoords.get(j), xTileLength, yTileLength};
			  //System.out.printf("%d %d %d %d\n", coords[0], coords[1], coords[2], coords[3]);
			  coordinates.add(coords);
		  }
	  }
	  return coordinates;
  }
  
  public static String coordinateConverter(int[] coords){
	  // Converts int[] of xcoord, ycoord, width, height to string suitable for storing in
	  // hashmap
	  StringBuilder bob = new StringBuilder("");
	  for (int i = 0; i<coords.length - 1; i++){
		  bob.append(Integer.toString(coords[i]));
		  bob.append(" ");
	  }
	  bob.append(Integer.toString(coords[coords.length - 1]));
	  return bob.toString();
  }
  
  public Map<String, BufferedImage> makeGrid(){
	  List<int[]> allCoords = CalculateGrids();
	  String key = null;
	  BufferedImage subImage = null;
	  for (int[] coords : allCoords) {
		  key = coordinateConverter(coords);
		  subImage = mainImage.getSubimage(coords[0], coords[1], coords[2], coords[3]);
		  subImages.put(key, subImage);
	  }
	  return subImages;
  }
  
  public int getMaxTileX() {
	  return maxTileX;
  }
    
  public int getMaxTileY() {
	  return maxTileY;
  }
  

  /*
  public static void main(String[] args){
    gridCreateTest a = new gridCreateTest("hs-2006-10-a-full_jpg.jpg");    
    Transition t = new Transition(a.OVERLAP_LENGTH, a.VIEWPORT_LENGTH, a.TILELENGTH);
    
  }
  */
}
