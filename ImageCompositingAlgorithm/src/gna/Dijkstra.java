package gna;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import libpract.Position;

public class Dijkstra {
	
	private PriorityQueue<Position> positionPQ;
	private int width;
	private int height;
	private double[][] distTo;
	private Position[][] previousVertex;
	
	/**

	 * Performs the Dijkstra algorithm to calculate the shortest path from the upper left corner to the bottom right corner.

	 * @param image1 The values of image 1.

	 * @param image2 The values of image 2.

	 */

	void performDijkstra(int[][] image1, int[][] image2) {

		// all nodes except the start node are initialised to zero.

		for (int h = 0; h < this.getHeight(); h++) {

			for (int w = 0; w < this.getWidth(); w++) {

				this.setDistoValue(h, w, Double.POSITIVE_INFINITY);

			}

		}

		this.setDistoValue(0, 0, 0);



		//Dijkstra calculation and stops when the shortest path to the destination is reached

		Position currentPosition = new Position(0, 0);

		this.getPositionPQ().add(currentPosition);

		while (!currentPosition.equals(this.getTargetPosition())) {

			for (Position neighbor : this.getPossibleNeighbors(currentPosition)) {

				double calcDistance = this.getDistTo()[currentPosition.getY()][currentPosition.getX()]

						+ ImageCompositor.pixelSqDistance(image1[neighbor.getY()][neighbor.getX()],

								image2[neighbor.getY()][neighbor.getX()]);

				if (calcDistance < this.getDistTo()[neighbor.getY()][neighbor.getX()]) {

					this.setDistoValue(neighbor.getY(), neighbor.getX(), calcDistance);

					this.setPreviousVertexValue(neighbor.getY(), neighbor.getX(), currentPosition);

					if (this.getPositionPQ().contains(neighbor)) {

						this.getPositionPQ().remove(neighbor);

					}

					this.getPositionPQ().add(neighbor);

				}

			}

			currentPosition = this.getPositionPQ().poll();

		}

	}
	
	
	/**

	 * Returns the height of the image (2D array).

	 * @return The height of the image (2D array).

	 */

	public int getHeight() {

		return height;

	}
	
	

	/**

	 * Returns the width of the image (2D array).

	 * @return The width of the image (2D array).

	 */

	public int getWidth() {

		return width;

	}
	
	/**

	 * Calculates the shortest path out of the previousVertex 2D array.

	 * @return The shortest path from top left to bottom right.

	 */

	List<Position> getShortestPathSolution(){

		if(!hasPathTo(this.getTargetPosition())) return null;

		//calculate shortest path starting from the target

		Stack<Position> reversePath = new Stack<Position>();

		Position current = this.getTargetPosition();

		reversePath.add(current);

		while(this.getPreviousVertex()[current.getY()][current.getX()] != null) {

			current = this.getPreviousVertex()[current.getY()][current.getX()];

			reversePath.add(current);

		}

		//reverse the list again

		List<Position> shortestPath = new ArrayList<>();

		while(!reversePath.isEmpty()) {

			shortestPath.add(reversePath.pop());

		}

		return shortestPath;	

	}
	
	/**

	 * Returns the targetPosition. The targetPosition is the final position of the Dijkstra algorithm.

	 * @return The targetPosition (used for Dijkstra's algorithm).

	 */

	Position getTargetPosition() {

		return new Position(this.getHeight()-1, this.getWidth()-1);

	}
	
	/**

	 * Returns the previous vertex 2D array of positions.

	 * @return the previous vertex 2D array of positions.

	 */

	Position[][] getPreviousVertex() {

		return previousVertex;

	}
	
	/**

	 * Returns all the possible neighbors of a vertex. (Used in Dijkstra's algorithm).

	 * @param p The position of which the neighbors should be calculated.

	 * @return a List of neighbors of the given position. A neighbor itself is also a position.

	 */

	List<Position> getPossibleNeighbors(Position p){

		List<Position> neighbors = new ArrayList<>();

		neighbors.addAll(this.getBasicNeighbors(p,this.getWidth(),this.getHeight()));

		

		//left + top

		if( ( (p.getX()-1) >= 0 ) &&  ((p.getY()-1) >= 0 ) ) {

			neighbors.add(new Position(p.getY()-1, p.getX()-1));

		}

		//left + bottom

		if( ( (p.getX()-1) >= 0 ) && ((p.getY()+1) < this.getHeight()) ) {

			neighbors.add(new Position(p.getY()+1, p.getX()-1));

		}

		//right + top

		if( ((p.getX()+1) < this.getWidth()) && ((p.getY()-1) >= 0 )  ) {

			neighbors.add(new Position(p.getY()-1, p.getX()+1));

		}

		//right + bottom

		if( ((p.getX()+1) < this.getWidth())  && ((p.getY()+1) < this.getHeight())) {

			neighbors.add(new Position(p.getY()+1, p.getX()+1));

		}

		return neighbors;

	}
	
	/**

	 * Returns the basic neighbors of a position in a field with given width and height.

	 * A basic neighbor is left, right, top, bottom

	 * @param p The position of which the neighbors need to be calculated.

	 * @param width The width of the field.

	 * @param height The height of the field.

	 * @return The list of neighbors of the given position.

	 */

	List<Position> getBasicNeighbors(Position p, int width, int height) {

		List<Position> neighbors = new ArrayList<>();

		// left

		if ((p.getX() - 1) >= 0) {

			neighbors.add(new Position(p.getY(), p.getX() - 1));

		}

		// right

		if ((p.getX() + 1) < width) {

			neighbors.add(new Position(p.getY(), p.getX() + 1));

		}

		// top

		if ((p.getY() - 1) >= 0) {

			neighbors.add(new Position(p.getY() - 1, p.getX()));

		}

		// bottom

		if ((p.getY() + 1) < height) {

			neighbors.add(new Position(p.getY() + 1, p.getX()));

		}

		return neighbors;

	}
	
	

	/**

	 * Sets the priorityQue of positions.

	 * @param positionPQ The position to which it will be set.

	 * @throws IllegalArgumentException when the positionPQ parameter equals null

	 *                                  | positionPQ == null

	 * @post The positionPQ is set to the given parameter

	 *  

	 */

	void setPositionPQ(PriorityQueue<Position> positionPQ) {

		if(positionPQ == null) {

			throw new IllegalArgumentException("The positionPQ cannot be null.");

		}

		this.positionPQ = positionPQ;

	}

	

	/**

	 * Returns the priorityQueue of positions.

	 * @return The priorityyQueue of positions.

	 */

	PriorityQueue<Position> getPositionPQ() {

		return positionPQ;

	}







	void setWidth(int width) {

		if(width < 0) {

			throw new IllegalArgumentException("Width of an image cannot be negative.");

		}

		this.width = width;

	}







	

	void setHeight(int height) {

		if(height < 0) {

			throw new IllegalArgumentException("The height of an image cannot be negative.");

		}

		this.height = height;

	}
	
	
	/**

	 * Returns the Distance to (a certain vertex on the location) 2D matrix.

	 * @return the Distance to (a certain vertex on the location) 2D matrix.

	 */

	public double[][] getDistTo() {

		return distTo;

	}





	void setDistTo(double[][] distTo) {

		if(distTo == null) {

			throw new IllegalArgumentException("Disto cannot be set to null.");

		}

		this.distTo = distTo;

	}








	void setPreviousVertex(Position[][] previousVertex) {

		if(previousVertex == null) {

			throw new IllegalArgumentException("EdgeTo cannot be set to null.");

		}

		this.previousVertex = previousVertex;

	}

	



	void setDistoValue(int y, int x, double value) {

		if(x <0 || y <0) {

			throw new IllegalArgumentException("The positions of disto array cannot be null.");

		}

		distTo[y][x] = value;

	}

	



	void setPreviousVertexValue(int y, int x, Position previousPosition) {

		if((x <0) || (y<0) || (previousPosition== null)) {

			throw new IllegalArgumentException("Cannot set the previousVertex value because it cannot be null and cannot be outside the grid.");

		}

		previousVertex[y][x] = previousPosition;

	}

		



	

	/**

	 * returns whether or not there exists a path to the vertex on the given position. 

	 * @param p The position the vertex is located.

	 * @return True when there exists a path to the vertex; otherwise false.

	 */

	boolean hasPathTo(Position p) {

		if(p == null) {

			throw new IllegalArgumentException("Path cannot be null to determine if a path exists.");

		}

		return this.getDistTo()[p.getY()][p.getX()] < Double.POSITIVE_INFINITY;

	}
	
	/**

	 * Returns the comparator used to compare Position objects.

	 * @return The comparator used to compare Position objects.

	 */

	Comparator<Position> getPositionComparator() {

		return new Comparator<Position>() {

			@Override

			public int compare(Position position1, Position position2) {

				if(position1 == null || position2 == null) {

					return 0;

				}

				if(getDistTo()[position1.getY()][position1.getX()] < getDistTo()[position2.getY()][position2.getX()]) {

					return -1;

				} else if(getDistTo()[position1.getY()][position1.getX()] > getDistTo()[position2.getY()][position2.getX()]) {

					return 1;

				}else {

					return 0;

				}

			}

		};

	}

}
