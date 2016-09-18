
public class Transition {
	
	private int overlap;
	private int viewportLength; // Assumes viewport is a square, i.e. height and width the same
	private int tileLength;
	
	public Transition(int overlap, int viewportLength, int tileLength) {
		this.overlap = overlap;
		this.viewportLength = viewportLength;
		this.tileLength = tileLength;
	}
	
	// Returns 0 if same tile, i.e. no swap needed
	// -4 for top-left, -3 for top, -2 for top-right, -1 for left, 1 for right, 2 for bottom-left, 3 for bottom, 4 for bottom-right
	public int nextTile(int xPos, int yPos, float xDir, float yDir, int tileX, int tileY) {
		int nextX = 0, nextY = 0;
		int xTileOffset = xPos - tileX, yTileOffset = yPos - tileY;
		//if (xDir > 0 && xTileOffset >= tileLength - overlap + (float)(viewportLength)/2) {
		if (xDir > 0 && xTileOffset > tileLength - overlap + (float)(viewportLength)/2) {
			nextX = 1; 
		}
		//else if (xDir < 0 && xTileOffset <= overlap - (float)(viewportLength)/2) {
		else if (xDir < 0 && xTileOffset < overlap - (float)(viewportLength)/2) {
			nextX = -1;
		}
		//if (yDir > 0 && yTileOffset >= tileLength - overlap + (float)(viewportLength)/2) {
		if (yDir > 0 && yTileOffset > tileLength - overlap + (float)(viewportLength)/2) {
			nextY = 1;
		}
		//else if (yDir < 0 && yTileOffset <= overlap - (float)(viewportLength)/2) {
		else if (yDir < 0 && yTileOffset < overlap - (float)(viewportLength)/2) {
			nextY = -1;
		}
		if (nextX != 0 & nextY != 0){
			System.out.println("NextX: " + nextX + "; NextY: " + nextY);
		}
		return nextY * 3 + nextX;
	}
		
	
	/*
	public static void main(String[] args) {
		
		
		
	}
	*/

}
