/** 
 * Name: Wenlin Mao
 * Date: Jan. 28 2017
 * File: Direction.java
 *
 * Class name: Direction
 * Enumeration Type used to represent a Movement Direction. Each 
 * Direction object includes the vector of Direction
 * */

public enum Direction {
    // The Definitions for UP, DOWN, LEFT, and RIGHT
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    private final int y;
    private final int x;

    /* Constructor
     * Purpose: Constructs a new direction by difintions
     * Parameter: x - X component of the direction
     *            y - Y component of the direction
     */   
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Name : getX
     * Purpose: Retrieve the X component of direction
     * Parameter: none
     * Return: void 
     */ 
    public int getX() {
        return x;
    }

    /* Name : getY
     * Purpose: Retrieve the Y component of direction
     * Parameter: none
     * Return: void 
     */ 
    public int getY() {
        return y;
    }

    /* Name : toString
     * Purpose: overide toString method to print out direction
     * Parameter: none
     * Return: void 
     */ 
    @Override
    public String toString() {
        return name() + "(" + x + ", " + y + ")";
    }

}
