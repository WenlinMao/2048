/** 
 * Name: Wenlin Mao
 * Date: Mar. 7 2017
 * File: Gui2048.java
 *
 * File name: Gui2048.java
 * In this file, I uses the javafx to creat a visulized windows for 2048 game. 
 * There are many features as the players play this game. The number's size 
 * and the rectangle's color will change as the player combines more tiles.
 * */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/*  
 *  Class Name: Gui2048
 *  Purpose: This class is used to initialize a 2048 game visually. Players  
 *  do the operation on to board and the feature of each tile will change. 
 *  This class will decide how the game's basic structure looks like 
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TILE_WIDTH = 106;

    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
                                                 //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
                                                  //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
                        // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
                       // For tiles < 8

    private GridPane pane;

    /** Add your own Instance Variables here */
    // decide whether the game should stop taking command
    private boolean isStopTyping = false; 

    // a private stackpane that is used for overlap the gameover rectangle
    private StackPane overPane;



   /* Method Name : start
    * Purpose: decide basic layout structure of the 2048 game and regigister 
    *          the keyevent handler in this method. This method is used to 
    *          set up the stage so that we can see it visually
    * Parameter: Stage primaryStage -- the stage object that is the base of 
    *                                  every other JavaFX object. We plan all
    *                                  designs on the stage.
    * Return: void 
    */ 
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        pane = new GridPane();

        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
        // Set the spacing between the Tiles
        pane.setHgap(15); 
        pane.setVgap(15);

        /** Add your Code for the GUI Here */
        // create new stackpane object
        overPane = new StackPane();
        overPane.getChildren().add(pane);

        // show the title of the game on the upperleft connor at the beginning
        guiTitle();

        // show the score that shows on the upperright connor at the beginning
        guiScore(board.getScore());

        // show the initial board at the beginning
        guiBoard(board.getGrid());

        // create a scene with initially 600 * 600 size
        Scene scene = new Scene(overPane, 600, 600);

        // regisiter the key event handler
        scene.setOnKeyPressed(new MoveHandler());
        // set the title for the windows
        primaryStage.setTitle("Gui2048");
        // add scene to the stage
        primaryStage.setScene(scene);
        // show the stage
        primaryStage.show();

        // request focus for key press event on the stack pane
        overPane.requestFocus();
    }

    /*  
     *  Class Name: MoveHandler
     *  Purpose: This inner class is used to decide the behavior of the 2048
     *  game based on the player's operation. As the players presses on the 
     *  directional arrow key, the board will be refreshed and change the 
     *  status.
     */
    class MoveHandler implements EventHandler<KeyEvent> 
    {
       /* Method Name : handle
        * Purpose: decide the 2048 game's behavior based on the player's 
        *          command when the player hit the key from keyboard.
        * Parameter: KeyEvent e -- the KeyEvent object that will used to 
        *                          get the players command whenever the 
        *                          user hit the keyboard.
        * Return: void 
        */ 
        @Override
        public void handle(KeyEvent e)
        {
            // if the game is over, stop taking command
            if (isStopTyping)
            {
                return;
            }

            // if the user hit up arrow key, then move upward and 
            // add a new random tile
            if (e.getCode() == KeyCode.UP)
            {   
                Direction direction = Direction.UP;
                // move the board's grid upward
                if (board.move(direction))
                {
                    // add new tile after move successfully
                    board.addRandomTile();
                    System.out.println("Moving <UP>");                    
                }

            }

            // if the user hit down arrow key, then move downward and 
            // add a new random tile
            else if (e.getCode() == KeyCode.DOWN)
            {
                Direction direction = Direction.DOWN;
                // move the board downward
                if (board.move(direction))
                {
                    // add new tile after move successfully
                    board.addRandomTile();
                    System.out.println("Moving <DOWN>");
                }
            }

            // if the user hit left arrow key, then move the board to the left 
            // and add a new random tile
            else if (e.getCode() == KeyCode.LEFT)
            {
                Direction direction = Direction.LEFT;
                // move the board left
                if (board.move(direction))
                {
                    // add new tile after move successfully
                    board.addRandomTile();
                    System.out.println("Moving <Left>");
                }
            }

            // if the user hit right arrow key, then move the board to the 
            // right and add a new random tile
            else if (e.getCode() == KeyCode.RIGHT)
            {
                Direction direction = Direction.RIGHT;
                // move the board right
                if (board.move(direction))
                {
                    // add new tile after move successfully
                    board.addRandomTile();
                    System.out.println("Moving <Right>");
                }
            }
            // if the user type s, then save the board.
            else if (e.getCode() == KeyCode.S)
            {
                try
                {
                    System.out.println("Saving Board to <" + outputBoard + ">");
                    // save the board to outputBoard
                    board.saveBoard(outputBoard);
                }
                catch (IOException ex)
                {
                    System.out.println("saveBoard threw an Exception");
                } 
            }
            // if the user type r, then rotate the board clockwise
            else if (e.getCode() == KeyCode.R)
            {
                board.flip(3);
            }
            // if the user type e, then expand the board
            else if (e.getCode() == KeyCode.E)
            {
                board.expand();
            }
            // if the user type u, then undo the last step
            else if (e.getCode() == KeyCode.U)
            {
                board.undo();
            }
            // if other key is hitten, do nothing.
            else
            {
                return;
            }
            // if the isGamveover method returns true, then show the game over
            // picture
            if (board.isGameOver())
            {
                // add a new rectangle that overlap the window
                guiGameOver();
                // stop taking command
                isStopTyping = true; 
            }
            // clear all cells on the board
            pane.getChildren().clear();
            // set the title again
            guiTitle();
            // update the board after the board is changed
            guiBoard(board.getGrid());
            // update score after the score is changed
            guiScore(board.getScore());
        }
    }

   /* Method Name : guiBoard
    * Purpose: This helper method is used to set up all background and value 
    *          of each tile onto the gridpane
    * Parameter: grid -- the board that used to represent the value of tiles
    * Return: void 
    */ 
    private void guiBoard(int[][] grid)
    {
        // the loop is count from one since the first row in grid pane is 
        // occupied
        for (int i = 1; i < grid.length + 1; i++)
        {
            for (int j = 0; j < grid[i - 1].length; j++)
            {
                if (grid[i - 1][j] == 0)
                {
                    // set the backgroup to color empty
                    setTile(i, j, COLOR_EMPTY);
                    // set value of the tile to nothing
                    setValue(i, j, 0);
                }
                else if (grid[i - 1][j] == 2)
                {
                    // set the background color for tile with value 2
                    setTile(i, j, COLOR_2);
                    // set value of the tile to 2
                    setValue(i, j, 2);
                }
                else if (grid[i - 1][j] == 4)
                {
                    // set the background color for tile with value 4
                    setTile(i, j, COLOR_4);
                    // set value of the tile to 4
                    setValue(i, j, 4);
                }
                else if (grid[i - 1][j] == 8)
                {
                    // set the background color for tile with value 8
                    setTile(i, j, COLOR_8);
                    // set value of the tile to 8
                    setValue(i, j, 8);
                }
                else if (grid[i - 1][j] == 16)
                {
                    // set the background color for tile with value 16
                    setTile(i, j, COLOR_16);
                    // set value of the tile to 16
                    setValue(i, j, 16);
                }
                else if (grid[i - 1][j] == 32)
                {
                    // set the background color for tile with value 32
                    setTile(i, j, COLOR_32);
                    // set value of the tile to 32
                    setValue(i, j, 32);
                }
                else if (grid[i - 1][j] == 64)
                {
                    // set the background color for tile with value 64
                    setTile(i, j, COLOR_64);
                    // set value of the tile to 64
                    setValue(i, j, 64);
                }
                else if (grid[i - 1][j] == 128)
                {
                    // set the background color for tile with value 128
                    setTile(i, j, COLOR_128);
                    // set value of the tile to 128
                    setValue(i, j, 128);
                }
                else if (grid[i - 1][j] == 256)
                {
                    // set the background color for tile with value 256
                    setTile(i, j, COLOR_256);
                    // set value of the tile to 256
                    setValue(i, j, 256);
                }
                else if (grid[i - 1][j] == 512)
                {
                    // set the background color for tile with value 512
                    setTile(i, j, COLOR_512);
                    // set value of the tile to 512
                    setValue(i, j, 512);
                }
                else if (grid[i - 1][j] == 1024)
                {
                    // set the background color for tile with value 1024
                    setTile(i, j, COLOR_1024);
                    // set value of the tile to 1024
                    setValue(i, j, 1024);
                }
                else if (grid[i - 1][j] == 2048)
                {
                    // set the background color for tile with value 2048
                    setTile(i, j, COLOR_2048);
                    // set value of the tile to 2048
                    setValue(i, j, 2048);
                }
                else
                {
                    // set the background color for tile with other value
                    setTile(i, j, COLOR_OTHER);
                    // set value of the tile to other value.
                    setValue(i, j, grid[i - 1][j]);
                }
                
            }
        }
    }

   /* Method Name : guiTitle
    * Purpose: This helper method is used to set up the title for this game. 
    *          Since all element in the grid pane will be clear and added again
    *          The title is also be cleared. We use this method to readded the
    *          title
    * Parameter: none
    * Return: void 
    */ 
    private void guiTitle()
    {
        // create a text object with 2048 as content
        Text name = new Text("2048");
        // set the font of the title
        name.setFont(Font.font("SansSerif", FontWeight.BOLD
                                , FontPosture.ITALIC, 40));
        // set the color of the title
        name.setFill(Color.BLACK);
        // add the designed title onto the grid pane depending on the 2048's 
        // board's size. And it will be in the middle of the upper left conner
        pane.add(name, 0, 0, board.GRID_SIZE / 2 + board.GRID_SIZE % 2, 1);
        // set the tilte in the middle horizontally
        GridPane.setHalignment(name, HPos.CENTER);
        // set the title in the middle vertically
        GridPane.setValignment(name, VPos.CENTER);

    }

   /* Method Name : guiScore
    * Purpose: This helper method is used to update the score for each time
    *          the score changed. And the gui setting is also changed
    *          everytime.
    * Parameter: score -- the score that will be updated
    * Return: void 
    */ 
    private void guiScore(int score)
    {
        // text object that is created express the score
        Text textScore = new Text("Score: " + score);
        // set the font of the score
        textScore.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        // set the color of the score
        textScore.setFill(Color.BLACK);
        // add score to pane and express it to the upper right connor
        pane.add(textScore, board.GRID_SIZE / 2 + board.GRID_SIZE % 2, 0, 
                board.GRID_SIZE / 2, 1);
        // set the score at the middle horizontally
        GridPane.setHalignment(textScore, HPos.CENTER);
        // set the score at the middle vertically
        GridPane.setValignment(textScore, VPos.CENTER);
    }

   /* Method Name : guiGameOver
    * Purpose: This helper method is used to change the gui setting after the 
    *          game is determined to be over
    * Parameter: none
    * Return: void 
    */ 
    private void guiGameOver()
    {
        // create the rectangle object 
        Rectangle whiteScreen = new Rectangle();
        // set its color to a transparent white
        whiteScreen.setFill(COLOR_GAME_OVER);
        // bind the gameover screen's width with the grid pane's width
        whiteScreen.widthProperty().bind(pane.widthProperty());
        // bind the gameover screen's height with the grid pane's height
        whiteScreen.heightProperty().bind(pane.heightProperty());
        // add the gameover screen onto the stack pane
        overPane.getChildren().add(whiteScreen);

        // create the gameover text
        Text gameOver = new Text("Game Over");
        gameOver.setFont(Font.font("SansSerif", FontWeight.BOLD, 100));
        gameOver.setFill(Color.BLACK);
        // add the text onto the stack pane
        overPane.getChildren().add(gameOver);
    }

   /* Method Name : setTile
    * Purpose: This helper method is used to set each tile onto the grid board
    * Parameter: row -- the row index on grid pane
    *            column -- the column index on the grid pane
    *            color -- the tile's color
    * Return: void 
    */ 
    private void setTile(int row, int column, Color color)
    {
        // create a rectangle object for each tile
        Rectangle tile = new Rectangle();
        // bind the tile's width with approxmatly (4 / 3) * board size of the
        // grid pane's width 
        tile.widthProperty().bind(pane.widthProperty().divide(
                                  board.GRID_SIZE + board.GRID_SIZE * 0.3));
        // bind the tile's height with approxmatly (3 / 2) * board size of the 
        // grid pane's Height
        tile.heightProperty().bind(pane.heightProperty().divide(
                                   board.GRID_SIZE + board.GRID_SIZE * 0.5));
        // set the color of the tile
        tile.setFill(color);
        // add the tile into the grid pane
        pane.add(tile, column, row);
    }

   /* Method Name : setValue
    * Purpose: This helper method is used to set color and font of each tile's 
    *          value based on the tiles value.
    * Parameter: row -- the row index on grid pane
    *            column -- the column index on the grid pane
    *            value -- the tile's value
    * Return: void 
    */ 
    private void setValue(int row, int column, int value)
    {
        // nothing will be added onto the grid pane if the value is 0
        if (value == 0)
        {
            return;
        }

        // create a text object
        Text tileNumber = new Text();
        // set the text object with the passed in tile value
        tileNumber.setText("" + value);  

        // set the color dark when the tile's value lower than 8
        if (value < 8)
        {
            tileNumber.setFill(COLOR_VALUE_DARK);
        }
        // set the color light when the tile's value higher or equal to 8
        else
        {
            tileNumber.setFill(COLOR_VALUE_LIGHT);
        }

        // set the font bigger when the tile value is less than 128
        if (value < 128)
        {
            tileNumber.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, TEXT_SIZE_LOW));
        }
        // set the font in middle size when the tile value is bigger or 
        // equal to 128 but smaller than 1024
        else if (value >= 128 && value < 1024 )
        {
            tileNumber.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, TEXT_SIZE_MID));
        }
        // set the font in smaller size when the tile's value is bigger than 
        // 1024
        else
        {
            tileNumber.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, TEXT_SIZE_HIGH));
        }
        // add the tile's value onto tile
        pane.add(tileNumber, column, row);
        // set the tile's middle in the middle of the tile
        GridPane.setHalignment(tileNumber, HPos.CENTER);
    }

    /* Method Name : processArgs
    * Purpose: The method used to process the command line arguments
    * Parameter: args -- command line argumetns that will be processed
    * Return: void 
    */ 
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(new Random(), inputBoard);
            else
                board = new Board(new Random(), boardSize);
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + 
                               " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    /* Method Name : printUsage
    * Purpose: Print the Usage Message 
    * Parameter: none
    * Return: void 
    */ 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the "+ 
                           "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
                           "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " + 
                           "used to save the 2048 board");
        System.out.println("                If none specified then the " + 
                           "default \"2048.board\" file will be used");  
        System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
                           "board if an input file hasn't been"); 
        System.out.println("                specified.  If both -s and -i" + 
                           "are used, then the size of the board"); 
        System.out.println("                will be determined by the input" +
                           " file. The default size is 4.");
    }
}
