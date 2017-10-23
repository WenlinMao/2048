/** 
 * Name: Wenlin Mao
 * Date: Jan. 28 2017
 * File: Board.java
 *
 * Class name: Board
 * This class is used to create a 2048 board. It can initialize tiles with 2 
 * or 4 randomly in random location and save the board with the passed in 
 * name. It can also flip the board in different position according to 
 * the number the user set. 
 * */

import java.util.*;
import java.io.*;

/*  
 *  Name: Board
 *  Purpose: This class is used to create a 2048 board. And actually 
 *  do the operation on to board according to the user's operation. 
 *  It can move the tiles in the board in all four possible direciton 
 *  and add the tile randomly. And this class also can undo the 
 *  operation and expand the 2048 board.         
 */
public class Board {
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public int GRID_SIZE;
    private int undoGRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int[][] undoGrid;
    private int score;
    private int undoScore;


     /* Constructor
      * Purpose: Constructs a new board with passed in size and initialize random 
      *          tiles.
      * Parameter: random - the random object that can be used to generate random 
      *                     number.
      *            boardSize - the size of the new board
      */     
    public Board(Random random, int boardSize) {
        this.random = random;   
        this.GRID_SIZE = boardSize;
        this.score = 0;
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        // initial NUM_START_TILES tiles on the empty board
        for (int i = 0; i < NUM_START_TILES; i++)
        {
            this.addRandomTile();
        }
        this.undoScore = this.score;
        this.undoGrid = this.grid;
        this.undoGRID_SIZE = this.GRID_SIZE;
    }

   /* Constructor
    * Purpose: Constructs a board based on the information of the input file
    * Parameter: random - the random object that can be used to generate 
    *                     random number.
    *            inputBoard - the input file where store the new board
    */    
    public Board(Random random, String inputBoard) throws IOException {
        File file = new File(inputBoard);
        Scanner input = new Scanner(file);
        this.random = random;
        GRID_SIZE = input.nextInt(); // read first line which is board size
        score = input.nextInt(); // read second line which is score
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        // read the rest information to initial the board 2D array.
        for(int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                grid[i][j] = input.nextInt();
            }
        }

        input.close();
        this.undoScore = this.score;
        this.undoGrid = this.grid;
        this.undoGRID_SIZE = this.GRID_SIZE;
    }

   /* Name : saveBoard
    * Purpose: Saves the the board to a file with a certain format
    * Parameter: outputBoard - the name of the output file that the board will 
    *                          save to
    * Return: void 
    */ 
    public void saveBoard(String outputBoard) throws IOException {
        File file = new File(outputBoard);
        try(PrintWriter output = new PrintWriter(file))
        {
            output.println(GRID_SIZE);
            output.println(score);
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    output.print(grid[i][j]);
                    output.print(" ");
                }
                output.println();
            }
        }

    }

   /* Name : addRandomTile
    * Purpose: add a random tile in value of 2 or 4 to the random loction on 
    *          the board
    * Parameter: none
    * Return: void 
    */ 
    public void addRandomTile() {
        int count = 0;
        // count all empty space on the board
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (grid[i][j] == 0)
                {
                    count++;
                }
            }
        }
        // if there is not any empty space then quit.
        if (count == 0)
        {
            return; 
        }

        // generate random location to put the tile.
        int location = this.random.nextInt(count);

        // generate a random number to decide the value of the tile should be 
        // 2 or 4
        int value = this.random.nextInt(100);

        // count the empty space again. The value start from 0 because the 
        // random location is generated between 0 to count - 1 which is start 
        // with 0.
        int countEmpty = 0;
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // if there is a empty space on the board
                if (grid[i][j] == 0)
                {
                    // if the empty count hit the random location, add a new 
                    // tile at this location
                    if (countEmpty == location)
                    {
                        // If the random value smallier than the probablity 
                        // then add a tile with value 2. Otherwise, add a tile 
                        // with value 4 on current location.
                        if (value < TWO_PROBABILITY)
                        {
                            grid[i][j] = 2;
                        }
                        else
                        {
                            grid[i][j] = 4;
                        }
                    }
                    // add the tile until reach the random location
                    countEmpty++;
                }
            }
        }

    }

   /* Name : flip
    * Purpose: Flip the board horizontally or vertically or
               rotate the board by 90 degrees clockwise or 90 degrees 
               counter-clockwise depending on the user's demand. 
    * Parameter: change - the value that can decide if the board should 
    *                     horizontally or vertically or
    *                     rotate the board by 90 degrees clockwise or 
    *                     90 degrees counter-clockwise
    * Return: void 
    */ 
    public void flip(int change) {
        // make a new empty board with the same size as the grid
        int[][] result = new int[GRID_SIZE][GRID_SIZE];

        // if the value of change equal to 1, then flip the board horizontally
        if (change == 1)
        {
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    // copy each elements of board in horizontally opposite 
                    // direction
                    result[i][result[i].length - j - 1] = grid[i][j];
                }
            }

        }

        // if the value of change equal to 2, then flip the board vertically
        else if (change == 2)
        {
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    // copy each elements of board in vertically opposite 
                    // direction
                    result[result.length - i - 1][j] = grid[i][j];
                }
            }
        }

        // if the value of change equal to 3, then rotate the board clockwise
        else if (change == 3)
        {
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {   // copy each column of the original board from left to 
                    // right to each row of the result board from top to bottom
                    result[j][grid.length - i - 1] = grid[i][j];
                } 
            }

        }

        // if the value of change equal to 4, then rotate the board 
        // counterclockwise.
        else if (change == 4)
        {
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    // copy each column of the original board from left to 
                    // right to each row of the result board from bottom to top
                    result[grid[i].length - j - 1][i] = grid[i][j];
                } 
            }

        }
        // if the value of change equal to 5, then flip through the diagonal 
        // to the upper left.
        else if (change == 5)
        {
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    result[grid.length - j - 1][grid[i].length - i - 1] 
                    = grid[i][j];
                }
            }
        }
        // let the grid to be the new with board one more random tile is added
        this.grid = result;

    }

    /* Name : isInputFileCorrectFormat
    * Purpose: Check the input board file is in the correct format. If the 
    *          Input board file is not in the correct format this method 
    *          will return false and the board object will not be created.
    * Parameter: inputFile - the file name that we want to check if it is in 
    *                         in the correct format.
    * Return: boolean - if the input file has correct format then return true 
    *                   otherwise return false.
    */ 
    public static boolean isInputFileCorrectFormat(String inputFile) {
        try {
            //write your code to check for all conditions and 
            //return true if it satisfies
            //all conditions else return false
            //when the path is null, return false
            if (inputFile == null)
               return false;

            // when the file doesn't exist, return false
            File file = new File(inputFile);
            if (!file.exists())
                return false;

            Scanner input = new Scanner(file);

            // read the first line in the file, transfer the string into 
            // integer, this would work only if there is one integer number in 
            // the first line
            int size = Integer.parseInt(input.nextLine());
            // if size smaller than 2 return false
            if (size < 2) 
                return false;
            // read the second and test the same way.
            int score = Integer.parseInt(input.nextLine());
            // if the score smaller than 0, return fales
            if (score < 0 || score % 2 != 0)
                return false;

            int tempInt;
            String currentLine;
            int countRow = 0;

            while (input.hasNextLine())
            {
                // count line numbers
                countRow++;
                currentLine = input.nextLine();
                // for each line, trade as string
                Scanner readLine = new Scanner(currentLine);
                int countColumn = 0;
                while (readLine.hasNextInt())
                {
                    // count column number
                    countColumn++;
                    // read each number in this line
                    tempInt = readLine.nextInt();
                    // if it is not an even number or it is negative, return 
                    // false
                    if(tempInt < 0 || !isPowerOfTwo(tempInt) || tempInt == 1)
                    {
                        return false;
                    }
                }
                // if the count is not exactly equal to size, it has wrong 
                // format
                if (countColumn != size)
                {
                    return false;
                }
            }
            // if the count for line is not exactly equal to size, it has wrong
            // format
            if (countRow != size)
            {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

   /* Name : isPowerOfTwo
    * Purpose: Check if the input is power of two
    * Parameter: tempInt - the number that will be checked
    * Return: boolean - if the input number is power of two then return true 
    *                   otherwise return false.
    */ 
    public static boolean isPowerOfTwo(int number)
    {
        while (number > 1)
        {
            if (number % 2 != 0)
            {
                return false;
            }
            number /= 2;
        }
        return true;
    }

   /* Name : canMove
    * Purpose: Check the tiles can move in certain direction
    * Parameter: direction - the direction that need to be checked
    * Return: boolean - if the tiles can be moved to that direction then 
    *                   return true, otherwise, return false.
    */ 
    public boolean canMove(Direction direction)
    {   
        // check if the tiles can move to the left
        if (direction.equals(Direction.LEFT))
        {
            return canMoveLeft();
        }
        // check if the tiles can move to the right
        else if (direction.equals(Direction.RIGHT))
        {
            return canMoveRight();
        }
        // check if the tiles can move up
        else if (direction.equals(Direction.UP))
        {
            return canMoveUp();
        }
        // check if the tiles can move down
        else if (direction.equals(Direction.DOWN))
        {
            return canMoveDown();
        }
        // if there is any other direction is passed in for any reason, 
        // the program will return false.
        else
        {
            return false;
        }
    }

   /* Name : canMoveLeft
    * Purpose: Check the tiles can move to the left
    * Parameter: none
    * Return: boolean - if the tiles can be moved to the left then 
    *                   return true, otherwise, return false.
    */ 
    private boolean canMoveLeft()
    {
        // loop through all tiles in the grid
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 1; j < GRID_SIZE; j++)
            {
                // when the value of current tile is not zero
                if (grid[i][j] != 0)
                {
                    // check whether there is an empty tile or whether 
                    // there is a tile with same value on the current tile's 
                    // left
                    if (grid[i][j] == grid[i][j - 1] || grid[i][j - 1] == 0)
                    {
                        return true;
                    }
                }
            }
        }
        // if no such tile can be moved, then return false
        return false;
    }

   /* Name : canMoveRight
    * Purpose: Check the tiles can move to the right
    * Parameter: none
    * Return: boolean - if the tiles can be moved to the right then 
    *                   return true, otherwise, return false.
    */ 
    private boolean canMoveRight()
    {
        // loop through all tiles in the grid
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = GRID_SIZE - 2; j >= 0; j--)
            {
                // when the value of current tile is not zero
                if (grid[i][j] != 0)
                {
                    // check whether there is an empty tile or whether 
                    // there is a tile with same value on the current tile's 
                    // right
                    if(grid[i][j] == grid[i][j + 1] || grid[i][j + 1] == 0)
                    {
                        return true;
                    }
                }
            }
        }
        // if no such tile can be moved, then return false
        return false;
    }

   /* Name : canMoveUp
    * Purpose: Check the tiles can move upward
    * Parameter: none
    * Return: boolean - if the tiles can be moved upward then 
    *                   return true, otherwise, return false.
    */ 
    private boolean canMoveUp()
    {
        // loop through all tiles in the grid
        for (int i = 1; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of current tile is not zero
                if (grid[i][j] != 0)
                {
                    // check whether there is an empty tile or whether 
                    // there is a tile with same value on the current tile's 
                    // top
                    if(grid[i][j] == grid[i - 1][j] || grid[i - 1][j] == 0)
                    {
                        return true;
                    }
                }
            }
        }
        // if no such tile can be moved, then return false
        return false;
    }

   /* Name : canMoveDown
    * Purpose: Check the tiles can move downward
    * Parameter: none
    * Return: boolean - if the tiles can be moved downwards then 
    *                   return true, otherwise, return false.
    */ 
    private boolean canMoveDown()
    {
        // loop through all tiles in the grid
        for (int i = GRID_SIZE - 2; i >= 0; i--)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of current tile is not zero
                if (grid[i][j] != 0)
                {
                    // check whether there is an empty tile or whether 
                    // there is a tile with same value on the current tile's 
                    // bottom
                    if(grid[i][j] == grid[i + 1][j] || grid[i + 1][j] == 0)
                    {
                        return true;
                    }
                }
            }
        }
        // if no such tile can be moved, then return false
        return false;
    }

   /* Name : move
    * Purpose: This method is used to actually move the tiles in certain 
    *          direction
    * Parameter: direction - the direction that the tiles will be moved
    * Return: boolean - if the tiles are moved successfully then return true. 
    *                   Otherwise, return false.
    */ 
    public boolean move(Direction direction) {
        // check if the tiles can be moved into the passed-in direciton
        if (canMove(direction))
        {
            // memorize previous score and board size for Undo
            this.undoScore = this.score;
            this.undoGRID_SIZE = this.GRID_SIZE;
            // deep copy the previous grid to undoGrid
            this.undoGrid = new int[GRID_SIZE][GRID_SIZE];
            for (int i = 0; i < GRID_SIZE; i++)
            {
                for (int j = 0; j < GRID_SIZE; j++)
                {
                    undoGrid[i][j] = grid[i][j];
                }
            }

            // move to the left
            if (direction.equals(Direction.LEFT))
            {
                moveLeft();
                // return true after move
                return true;
            }

            // move to the right
            else if (direction.equals(Direction.RIGHT))
            {
                moveRight();
                // return true after move
                return true;
            }

            // move upward
            else if (direction.equals(Direction.UP))
            {
                moveUp();
                // return true after move
                return true;
            }

            // move downward
            else if (direction.equals(Direction.DOWN))
            {
                moveDown();
                // return true after move
                return true;
            }
        }

        // if the tiles cannot move in that direction, the move method will 
        // return false.
        return false;
    }

   /* Name : moveLeft
    * Purpose: This method is used to actually move the tiles to the left
    * Parameter: none
    * Return: void
    */ 
    private void moveLeft()
    {
        // firstly shift all tiles to the left
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // check all tiles from left to right
            for (int j = 1; j < GRID_SIZE; j++)
            {   
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the left
                    for (int k = j - 1; k >= 0; k--)
                    {
                        // whenever the left tile has a value of zero
                        if (grid[i][k] == 0)
                        {
                            // move the current tile to the left by one tile
                            grid[i][k] = grid[i][k + 1];
                            // and set the original tile to be zero
                            grid[i][k + 1] = 0;
                        }
                    }
                }
            }
        }

        // secondly combine tiles with same value to the left
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 1; j < GRID_SIZE; j++)
            {
                // if the tile has a nonzero value and it has the same value
                // with its left tile, then combine them and update the score
                if (grid[i][j] != 0 && grid[i][j - 1] == grid[i][j])
                {
                    score += grid[i][j] * 2;
                    grid[i][j - 1] = grid[i][j] * 2;
                    grid[i][j] = 0;
                }
            }
        }

        // shift all tiles to the left again after combine all tiles with same 
        // value
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // check all tiles from left to right
            for (int j = 1; j < GRID_SIZE; j++)
            {   
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the left
                    for (int k = j - 1; k >= 0; k--)
                    {
                        // whenever the left tile has a value of zero
                        if (grid[i][k] == 0)
                        {
                            // move the current tile to the left by one tile
                            grid[i][k] = grid[i][k + 1];
                            // and set the original tile to be zero
                            grid[i][k + 1] = 0;
                        }
                    }
                }
            }
        }
    }

   /* Name : moveRight
    * Purpose: This method is used to actually move the tiles to the right
    * Parameter: none
    * Return: void
    */ 
    private void moveRight()
    {
        // firstly shift all tiles to the right
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // check all tiles from right to left
            for (int j = GRID_SIZE - 2; j >= 0; j--)
            {
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the right
                    for (int k = j + 1; k < GRID_SIZE; k++)
                    {
                        // whenever the right tile has a value of zero
                        if (grid[i][k] == 0)
                        {
                            // move the current tile to the right by one tile
                            grid[i][k] = grid[i][k - 1];
                            // and set the original tile to be zero
                            grid[i][k - 1] = 0;
                        }
                    }
                }
            }
        }

        // secondly combine tiles with same value to the right
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = GRID_SIZE - 2; j >= 0; j--)
            {
                // if the tile has a nonzero value and it has the same value
                // with its right tile, then combine them and update the score
                if (grid[i][j] != 0 && grid[i][j + 1] == grid[i][j])
                {
                    score += grid[i][j] * 2;
                    grid[i][j + 1] = grid[i][j] * 2;
                    grid[i][j] = 0;
                }
            }
        }

        // shift all tiles to the right again after combine all tiles with 
        // same value
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // check all tiles from right to left
            for (int j = GRID_SIZE - 2; j >= 0; j--)
            {
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the right
                    for (int k = j + 1; k < GRID_SIZE; k++)
                    {
                        // whenever the right tile has a value of zero
                        if (grid[i][k] == 0)
                        {
                            // move the current tile to the right by one tile
                            grid[i][k] = grid[i][k - 1];
                            // and set the original tile to be zero
                            grid[i][k - 1] = 0;
                        }
                    }
                }
            }
        }

    }

   /* Name : moveUp
    * Purpose: This method is used to actually move the tiles upwards
    * Parameter: none
    * Return: void
    */ 
    private void moveUp()
    {
        // firstly shift all tiles upwards
        // check each tiles from top to bottom
        for (int i = 1; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the top
                    for (int k = i - 1; k >= 0; k--)
                    {
                        // whenever the top tile has a value of zero
                        if (grid[k][j] == 0)
                        {
                            // move the current tile to the top by one tile
                            grid[k][j] = grid[k + 1][j];
                            // set the original tile to be zero
                            grid[k + 1][j] = 0;
                        }
                    }
                }
            }
        }

        // secondly combine tiles with same value upwards
        // check each tiles from top to bottom
        for (int i = 1; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of current tile is not zero and it has the 
                // same value with the tile on its top then combine them and 
                // update the score
                if (grid[i][j] != 0 && grid[i - 1][j] == grid[i][j])
                {
                    score += grid[i][j] * 2;
                    grid[i - 1][j] = grid[i][j] * 2;
                    grid[i][j] = 0;
                }
            }
        }

        // shift upward again after combine all tiles with same value
        // check each tiles from top to bottom
        for (int i = 1; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the top
                    for (int k = i - 1; k >= 0; k--)
                    {
                        // whenever the top tile has a value of zero
                        if (grid[k][j] == 0)
                        {
                            // move the current tile to the top by one tile
                            grid[k][j] = grid[k + 1][j];
                            // set the original tile to be zero
                            grid[k + 1][j] = 0;
                        }
                    }
                }
            }
        }

    }

   /* Name : moveDown
    * Purpose: This method is used to actually move the tiles downwards
    * Parameter: none
    * Return: void
    */ 
    private void moveDown()
    {
        // firstly shift all tiles downwards
        // check each tile from bottom to top
        for (int i = GRID_SIZE - 2; i >= 0; i--)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the bottom
                    for (int k = i + 1; k < GRID_SIZE; k++)
                    {
                        // whenever the bottom tile has a value of zero
                        if (grid[k][j] == 0)
                        {
                            // move the current tile to the bottom by one tile
                            grid[k][j] = grid[k - 1][j];
                            // set the original tile to be zero
                            grid[k - 1][j] = 0;
                        }
                    }
                }
            }
        }

        // secondly combine tiles with same value downwards
        // check each tile from bottom to top
        for (int i = GRID_SIZE - 2; i >= 0; i--)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of the current tile is not zero and its 
                // bottom tile has the same value as it, then combine them and 
                // update the score
                if (grid[i][j] != 0 && grid[i + 1][j] == grid[i][j])
                {
                    score += grid[i][j] * 2;
                    grid[i + 1][j] = grid[i][j] * 2;
                    grid[i][j] = 0;
                }
            }
        }

        // shift downwards again after combine all tiles with same value
        for (int i = GRID_SIZE - 2; i >= 0; i--)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // when the value of tile is not zero
                if (grid[i][j] != 0)
                {
                    // check from the current tile to the bottom
                    for (int k = i + 1; k < GRID_SIZE; k++)
                    {
                        // whenever the bottom tile has a value of zero
                        if (grid[k][j] == 0)
                        {
                            // move the current tile to the bottom by one tile
                            grid[k][j] = grid[k - 1][j];
                            // set the original tile to be zero
                            grid[k - 1][j] = 0;
                        }
                    }
                }
            }
        }      
    }

   /* Name : expand
    * Purpose: This method is used to expand the board
    * Parameter: none
    * Return: void
    */ 
    public void expand()
    {
        // memerize the previous board
        this.undoGrid = this.grid;
        this.undoGRID_SIZE = this.GRID_SIZE;

        // create a new board with bigger board size
        int[][] expandGrid = new int[GRID_SIZE + 1][GRID_SIZE + 1];
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // copy all the tiles into the bigger board
                expandGrid[i][j] = grid[i][j];
            }
        }

        // update the grid size
        this.GRID_SIZE += 1;
        // update the board with new board 
        this.grid = expandGrid;
    }

   /* Name : undo
    * Purpose: This method is used to undo the operation that the user did
    * Parameter: none
    * Return: void
    */ 
    public void undo()
    {
        // set the score to be previous score
        this.score = this.undoScore;

        // set the board size to be the previous size
        this.GRID_SIZE = this.undoGRID_SIZE;

        // set the board to be the previous board
        this.grid = this.undoGrid;
    }

   /* Name : isGameOver
    * Purpose: check to see if the game is over, the tile cannot be moved into 
    *          any direction
    * Parameter: none
    * Return: void
    */ 
    public boolean isGameOver() {
        if (!canMoveLeft() && !canMoveRight() && !canMoveDown() 
            && !canMoveUp())
        {
            return true;
        }
        return false;
    }


    /* Name : getGrid
     * Purpose: Return the reference to the 2048 Grid
     * Parameter: none
     * Return: int[][] - 2048 grid that is returned
     */ 
    public int[][] getGrid() {
        return grid;
    }

    /* Name : getScore
     * Purpose: Return the score
     * Parameter: none
     * Return: int - the score that is returned
     */ 
    public int getScore() {
        return score;
    }

    /* Name : toString
     * Purpose: override the toString method to print out the board
     * Parameter: none
     * Return: String - 2048 board that is printed
     */ 
    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
