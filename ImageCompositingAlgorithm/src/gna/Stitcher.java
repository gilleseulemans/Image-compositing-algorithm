package gna;



import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

import java.util.PriorityQueue;

import java.util.Stack;



import libpract.*;


public class Stitcher

{


	

	/**

	 * Return the sequence of positions on the seam. The first position in the

	 * sequence is (0, 0) and the last is (width - 1, height - 1). Each position

	 * on the seam must be adjacent to its predecessor and successor (if any).

	 * Positions that are diagonally adjacent are considered adjacent.

	 * 

	 * image1 and image2 are both non-null and have equal dimensions.

	 *

	 * Remark: Here we use the default computer graphics coordinate system,

	 *   illustrated in the following image:

	 * 

	 *        +-------------> X

	 *        |  +---+---+

	 *        |  | A | B |

	 *        |  +---+---+

	 *        |  | C | D |

	 *        |  +---+---+

	 *      Y v 

	 * 

	 *   The historical reasons behind using this layout is explained on the following

	 *   website: http://programarcadegames.com/index.php?chapter=introduction_to_graphics

	 * 

	 *   Position (y, x) corresponds to the pixels image1[y][x] and image2[y][x]. This

	 *   convention also means that, when an automated test mentioned that it used the array

	 *   {{A,B},{C,D}} as a test image, this corresponds to the image layout as shown in

	 *   the illustration above.

	 *      

	 */
	
	Dijkstra dijk = new Dijkstra();

	public List<Position> seam(int[][] image1, int[][] image2) {

		this.dijk.setHeight(image1.length);

		this.dijk.setWidth(image1[0].length);

		

		this.dijk.setPositionPQ(new PriorityQueue<Position>(this.dijk.getPositionComparator()));

		this.dijk.setDistTo(new double[this.dijk.getHeight()][this.dijk.getWidth()]);

		this.dijk.setPreviousVertex(new Position[this.dijk.getHeight()][this.dijk.getWidth()]);

		

		this.dijk.performDijkstra(image1, image2);

		

		return this.dijk.getShortestPathSolution();

	}

	



	





	/**

	 * Apply the floodfill algorithm described in the assignment to mask. You can assume the mask

	 * contains a seam from the upper left corner to the bottom right corner. The seam is represented

	 * using Stitch.SEAM and all other positions contain the default value Stitch.EMPTY. So your

	 * algorithm must replace all Stitch.EMPTY values with either Stitch.IMAGE1 or Stitch.IMAGE2.

	 *

	 * Positions left to the seam should contain Stitch.IMAGE1, and those right to the seam

	 * should contain Stitch.IMAGE2. You can run `ant test` for a basic (but not complete) test

	 * to check whether your implementation does this properly.

	 */

	public void floodfill(Stitch[][] mask) {

		//start by filling Image1, the bottom left corner is the starting position.

		//if this one is not colored, the others aren't either.

		this.floodFill(mask, mask.length-1, 0, Stitch.IMAGE1);

		//start by filling Image2, the top right corner is the starting position.

		//if this one is not colored, the others aren't either.

		this.floodFill(mask, 0, mask[0].length-1, Stitch.IMAGE2);

	}



	/**

	 * Fills the mask with the given type on the given coordinates. It does not cross the seam.

	 * @param mask The mask with a given seam.

	 * @param y    The starting y-position.

	 * @param x    The starting x-position.

	 * @param type The Stich type to which it will be set.

	 */

	private void floodFill(Stitch[][] mask, int y, int x, Stitch type) {

		//use of stack because we need to go depth first

		Stack<Position> nextColoring = new Stack<>();

		nextColoring.add(new Position(y, x));

		while(!nextColoring.isEmpty()) {

			Position currentPosition = nextColoring.pop();

			Stitch currentStitch = mask[currentPosition.getY()][currentPosition.getX()];

			if(currentStitch == Stitch.EMPTY) {

				mask[currentPosition.getY()][currentPosition.getX()] = type;

				nextColoring.addAll(this.dijk.getBasicNeighbors(currentPosition,mask[0].length,mask.length));

			}

		}

	}

	

	

	/**

	 * Return the mask to stitch two images together. The seam runs from the upper

	 * left to the lower right corner, where in general the rightmost part comes from

	 * the second image (but remember that the seam can be complex, see the spiral example

	 * in the assignment). A pixel in the mask is Stitch.IMAGE1 on the places where

	 * image1 should be used, and Stitch.IMAGE2 where image2 should be used. On the seam

	 * record a value of Stitch.SEAM.

	 * 

	 * ImageCompositor will only call this method (not seam and floodfill) to

	 * stitch two images.

	 * 

	 * image1 and image2 are both non-null and have equal dimensions.

	 */

	public Stitch[][] stitch(int[][] image1, int[][] image2) {

		Stitch[][] mask = new Stitch[image1.length][image1[0].length];

		for (int i = 0; i < mask.length; i++) {

			for (int j = 0; j < mask[0].length; j++) {

				mask[i][j] = Stitch.EMPTY;

			}

		}

		for (Position position : this.seam(image1, image2)) {

			mask[position.getY()][position.getX()] = Stitch.SEAM;

		}

		this.floodfill(mask);		

		return mask;

	}








	

	

	



	



		

}

