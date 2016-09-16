import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


public class gridCreateTest {
  public int TILELENGTH = 1000; // all sizes in px
  public int VIEWPORT_LENGTH = 400;
  public int OVERLAP_LENGTH = 450; // slightly larger than VIEWPORT_LENGTH

  private int imageHeight;
  private int imageWidth;
  private BufferedImage mainImage;


  public gridCreateTest(String imagePath){
//    try {
//      mainImage = ImageIO.read(new File(imagePath));
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    imageWidth = mainImage.getWidth();
//    imageHeight = mainImage.getHeight();
	imageWidth = 15852;
	imageHeight = 12392; // HARDCODED TEMPORARILY FOR SPEED
    this.makeGrid();
    System.out.println("x length is " + Integer.toString(imageWidth) + " px.");
    System.out.println("y length is " + Integer.toString(imageHeight) + " px.");
  }
  
  private List<int[]> CalculateGrids(){
	  // Calculate List Containing
	  int currentimageWidth = TILELENGTH;
	  int currentimageHeight = TILELENGTH;
	  int finalColTileLength = TILELENGTH; 
	  int finalRowTileLength = TILELENGTH;
	  List<Integer> subImgYCoords = new ArrayList<Integer>();
	  List<Integer> subImgXCoords = new ArrayList<Integer>();  
	  // include 0 for both x and y coordinates
	  subImgXCoords.add(0);
	  subImgYCoords.add(0);
	  List<int[]> coordinates = new ArrayList<int[]>();
	 
	  // tiles usually do not exactly cover image. In last row and column they will be shorter. 
	  while (currentimageWidth < imageWidth){
		  int nextXCoordinate = currentimageWidth - OVERLAP_LENGTH;
		  subImgXCoords.add(nextXCoordinate);
		  currentimageWidth = nextXCoordinate + TILELENGTH;
		  if (currentimageWidth > imageWidth){
			  finalRowTileLength = TILELENGTH - (currentimageWidth - imageWidth);
		  }	  
	  }
	  while (currentimageHeight < imageHeight){
		  int nextYCoordinate = currentimageHeight - OVERLAP_LENGTH;
		  subImgYCoords.add(nextYCoordinate);
		  currentimageHeight = nextYCoordinate + TILELENGTH;
		  if (currentimageHeight > imageHeight){
			  finalColTileLength = TILELENGTH - (currentimageHeight - imageHeight);
		  }
	  }
	  int xTileLength = TILELENGTH;
	  int yTileLength = TILELENGTH;
	  for (int i = 0; i < subImgXCoords.size(); i++){
		  for (int j = 0; j < subImgYCoords.size(); j++){
			  if (i == subImgXCoords.size()-1){
				  xTileLength = finalColTileLength;  
			  }
			  else {
				  xTileLength = TILELENGTH;
			  }
			  if (j == subImgYCoords.size()-1){
				  // last row
				  yTileLength = finalRowTileLength;
				  
			  }
			  else {
				  yTileLength = TILELENGTH;
			  }
			  int[] coords = {subImgXCoords.get(i), subImgYCoords.get(j), xTileLength, yTileLength};
			  System.out.printf("%d %d %d %d\n", coords[0], coords[1], coords[2], coords[3]);
			  coordinates.add(coords);
		  }
	  }
	  return coordinates;
  }
  
  private String coordinateConverter(int[] coords){
	  // Converts int[] of xcoord, ycoord, width, height to string suitable for storing in
	  // hashmap
	  StringBuilder ret = new StringBuilder("");
	  for (int i = 0; i<coords.length; i++){
		  ret.append(Integer.toString(coords[i]));
		  ret.append(" ");
	  }
	  return ret.toString();
  }
  
  public BufferedImage makeGrid(){
	  this.CalculateGrids();
	  return null;
//    if (TILELENGTH > imageWidth | TILELENGTH > imageHeight){
//      throw new Error("Tiling an image smaller than the tilesize.");
//    }
//    
//    BufferedImage ret = mainImage.getSubimage(7000, 0, 1000, 1000);
//
//    return ret;
  }

  public static void main(String[] args){
    gridCreateTest a = new gridCreateTest("hs-2006-10-a-full_jpg.jpg");    
  }
}
