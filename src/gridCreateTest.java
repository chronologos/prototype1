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

  private int yLength;
  private int xLength;
  private BufferedImage mainImage;


  public gridCreateTest(String imagePath){
//    try {
//      mainImage = ImageIO.read(new File(imagePath));
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    xLength = mainImage.getWidth();
//    yLength = mainImage.getHeight();
	xLength = 15852;
	yLength = 12392; // HARDCODED TEMPORARILY FOR SPEED
    this.makeGrid();
    System.out.println("x length is " + Integer.toString(xLength) + " px.");
    System.out.println("y length is " + Integer.toString(yLength) + " px.");
  }
  
  private List<List<Integer>> CalculateGrids(){
	  int currentXLength = TILELENGTH;
	  int currentYLength = TILELENGTH;
	  List<Integer> ycoordinates = new ArrayList<Integer>();
	  List<Integer> xcoordinates = new ArrayList<Integer>();
	  
	  // include 0 for both x and y coordinates
	  xcoordinates.add(0);
	  ycoordinates.add(0);
	  List<int[]> coordinates = new ArrayList<int[]>();
	  
	  while (currentXLength < xLength){
		  int nextXCoordinate = currentXLength - OVERLAP_LENGTH;
		  xcoordinates.add(nextXCoordinate);
		  currentXLength = nextXCoordinate + TILELENGTH;
		  
	  }
	  while (currentYLength < yLength){
		  int nextYCoordinate = currentYLength - OVERLAP_LENGTH;
		  ycoordinates.add(nextYCoordinate);
		  currentYLength = nextYCoordinate + TILELENGTH;
	  }
	  for (int i = 0; i < xcoordinates.size(); i++){
		  for (int j = 0; j < ycoordinates.size(); j++){
			  int[] coords = {xcoordinates.get(i), ycoordinates.get(j), TILELENGTH, TILELENGTH};
			  System.out.printf("%d %d %d %d\n", coords[0], coords[1], coords[2], coords[3]);
			  coordinates.add(coords);
		  }
	  }
	  for (int i = 0; i < coordinates.size(); i++){
		  System.out.println(coordinates.get(i).toString());
	  }
	 
	  
	  return null;
  }
  public BufferedImage makeGrid(){
	  this.CalculateGrids();
	  return null;
//    if (TILELENGTH > xLength | TILELENGTH > yLength){
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
