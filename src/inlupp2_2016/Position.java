package inlupp2_2016;


public class Position implements Comparable<Position>{

	private int x;
	private int y;

	public Position(int x, int y) {
		this.x=x;
		this.y=y;
	} 

	public int getY () {
		return y;
	}

	public int getX() {
		return x;
	}
	@Override
	public String toString() {
		return "x = " + x + " y= " + y ; 
	}


	@Override
	public int compareTo (Position other) {
		if (x <other.x)
			return -1;
		else if (x > other.x)
			return 1;
		else 
			return y - other.y;
	}

} //End class Position

