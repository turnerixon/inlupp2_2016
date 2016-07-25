package inlupp2_2016;


public class Position {

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

	public Position getPosition(){
		Position position = new Position(x,y);
		return position;
	}

	@Override
	public String toString() {
		return "x = " + x + " y= " + y ; 
	}

	public boolean equals (Object other){
	    if(other instanceof Position){
            Position p = (Position)other;
            if(x==p.x && y==p.y)
            return true;
        else
            return  false;
        }
        else return false;
    }//End equals()

	@Override
    public int hashCode (){
         return x * 10000 +y;
    }// End hashCode()


} //End class Position

