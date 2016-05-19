/** Gui2048.java */
/** PSA8 Release */

/* 
 * Name: Chao Sun 
 * Login: cs8bwatc
 * Date: 3.2.2016
 * File:  Gui2048.java
 * This java file creats a completed 2048 game and include the
 * scene and whole interface of the game
 */



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
    private static final Color myColor = Color.rgb(237, 197, 63,0);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
                        // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
                       // For tiles < 8

    private GridPane pane;

    private String text = null;

    // a new text variable for score
    private Text scoreT = new Text();

    // a 2d array for tile text
    private Text [][] textArray = new Text[4][4];

    // a array of rectangles for tiles
    private ArrayList<Rectangle> rec = new ArrayList<Rectangle>();

    // a rectangle for game over overlay 
    private Rectangle gameOverR = new Rectangle(499,560);



    /** Add your own Instance Variables here */




    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        pane = new GridPane();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
        // Set the spacing between the Tiles
        pane.setHgap(15); 
        pane.setVgap(15);
        // create a scene and stage for the pane
       
        // add the 16 tiles to the pane
        for (int i =0; i< 16; i++) {
        rec.add(new Rectangle (TILE_WIDTH, TILE_WIDTH));
       }

        // set the 16 tiles to empty color
        for (int i =0; i< 16; i++) {
        rec.get(i).setFill(COLOR_EMPTY);
       }

        // set the text of title 2048
        Text titleT = new Text();
        titleT.setText("2048");
        titleT.setFont(Font.font("Times New Roman", FontWeight.BOLD,
        FontPosture.ITALIC, 40));
        titleT.setFill(Color.BLACK);
 
        // set the text of the score
        scoreT.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
        titleT.setFill(Color.BLACK);
        
        // add the 16 tiles to the pane
        for (int i=0; i<4;i++) {
           pane.add(rec.get(i),i,1);
     }

        for (int i=0; i<4; i++) {
           pane.add(rec.get(i+4),i,2);
     }
        for (int i=0; i<4;i++) {
           pane.add(rec.get(i+8),i,3);
     }

        for (int i=0; i<4; i++) {
           pane.add(rec.get(i+12),i,4);
     }

        // add the rectangle of the title and text
        Rectangle title = new Rectangle();
        pane.add(titleT,0,0,2,1);
        pane.add(scoreT,2,0,2,1);
        pane.setHalignment(titleT, HPos.CENTER);
        pane.setHalignment(scoreT, HPos.CENTER);

        
        // add the texts in the title
        for (int i=0; i<4; i++) {
          for (int j=0; j<4; j++) {
             textArray[i][j] = new Text();
           }
         }
        updateColor();
        updateText();
        updateScore();

        // copy the numbers in the board to the pane
        for (int i=0; i<4; i++) {
          for (int j=0; j<4; j++) {      
             textArray[i][j].setText(
             Integer.toString(board.getGrid()[i][j]));
             pane.add(textArray[i][j], j, i+1);
             if (board.getGrid()[i][j] == 0) {
                 textArray[i][j].setText("");
          }
         }
      }
        // add game over transpanrent rectangle
        gameOverR.setFill(myColor);
        pane.add(gameOverR,0,0,4,5);
        pane.setHalignment(gameOverR, HPos.CENTER);
        Scene scene = new Scene(pane, 499,560);
        // add the pane to the scene and show the stage
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        scene.setOnKeyPressed(new myKeyHandler());
        primaryStage.show();
        
        
    }

    /** Add your own Instance Methods Here */

/* 
 * Name: updateColor()
 * Purpose: update the color of the tile depending on the number
 *          in tne tile
 */


   private void updateColor() {
        // check number in each tile and change color depently
        for (int i=0; i<4; i++) {
          for (int j=0; j<4; j++) {
             if (board.getGrid()[i][j] == 0) {
                rec.get(4*i + j).setFill(COLOR_EMPTY);
          }
          
             else if (board.getGrid()[i][j]==2) {
                 rec.get(4*i + j).setFill(COLOR_2);
          }
             else if (board.getGrid()[i][j]==4) {
                 rec.get(4*i + j).setFill(COLOR_4);
          }
             else if (board.getGrid()[i][j]==8) {
                 rec.get(4*i + j).setFill(COLOR_8);
          }
             else if (board.getGrid()[i][j]==16) {
                 rec.get(4*i + j).setFill(COLOR_16);
          }
             else if (board.getGrid()[i][j]==32) {
                 rec.get(4*i + j).setFill(COLOR_32);
          }
             else if (board.getGrid()[i][j]==64) {
                 rec.get(4*i + j).setFill(COLOR_64);
          }
             else if (board.getGrid()[i][j]==128) {
                 rec.get(4*i + j).setFill(COLOR_128);
          }
             else if (board.getGrid()[i][j]==256) {
                 rec.get(4*i + j).setFill(COLOR_256);
          }
             else if (board.getGrid()[i][j]==512) {
                 rec.get(4*i + j).setFill(COLOR_512);
          }
             else if (board.getGrid()[i][j]==1024) {
                 rec.get(4*i + j).setFill(COLOR_1024);
          }
             else if (board.getGrid()[i][j]==2048) {
                 rec.get(4*i + j).setFill(COLOR_2048);
          }
             else {
                 rec.get(4*i + j).setFill(COLOR_OTHER);
          }


         }
       }
     }
   

/* 
 * Name: updateText()
 * Purpose: set the font of each number in the pane depending on 
 *          how many digital places it has
 */

   private void updateText() {
        for (int i=0; i<4; i++) {
          for (int j=0; j<4; j++) {
            if (board.getGrid()[i][j] ==2||board.getGrid()[i][j] ==4) {
               textArray[i][j].setFont(Font.font("Helvetica Nueu", 
               FontWeight.BOLD, TEXT_SIZE_LOW));  
               pane.setHalignment(textArray[i][j], HPos.CENTER);
               textArray[i][j].setFill(COLOR_VALUE_DARK);

                
           }
            else if (board.getGrid()[i][j] ==8) {
               textArray[i][j].setFont(Font.font("Helvetica Nueu", 
               FontWeight.BOLD, TEXT_SIZE_LOW));  
               pane.setHalignment(textArray[i][j], HPos.CENTER);
               textArray[i][j].setFill(COLOR_VALUE_LIGHT);

           }

         
            else if (board.getGrid()[i][j] ==16||board.getGrid()[i][j] ==32||
                board.getGrid()[i][j] ==64) {
               textArray[i][j].setFont(Font.font("Helvetica Nueu", 
               FontWeight.BOLD, TEXT_SIZE_MID)); 
               pane.setHalignment(textArray[i][j], HPos.CENTER);
               textArray[i][j].setFill(COLOR_VALUE_LIGHT);
  
           }
         

            else {
               textArray[i][j].setFont(Font.font("Helvetica Nueu", 
               FontWeight.BOLD, TEXT_SIZE_HIGH)); 
               pane.setHalignment(textArray[i][j], HPos.CENTER);
               textArray[i][j].setFill(COLOR_VALUE_LIGHT);
 
             } 
           }
          }
         }

        
    /* 
 * Name: updateScore()
 * Purpose: update score, copying from the score in board
 */
   private void updateScore() {
        scoreT.setText("Score: " + Integer.toString(board.getScore())); 
        scoreT.setFont(Font.font("Times New Roman", FontWeight.BOLD,
        FontPosture.ITALIC, 25));
        scoreT.setFill(Color.BLACK);
     
     }


  

   private class myKeyHandler implements EventHandler<KeyEvent>
    {
        @Override
        public void handle(KeyEvent e)
        {    
     // if the game is over
     if (board.isGameOver() == true) {
           // add text game over and make the rectangle half transparent
           Text gameOver = new Text();

           gameOver.setText("Game Over!");
           gameOver.setFont(Font.font("Helvetica Nueu", 
                            FontWeight.BOLD, 50));
           gameOver.setFill(COLOR_VALUE_DARK); 
           gameOverR.setFill(COLOR_GAME_OVER) ;
           pane.add(gameOver,0,0,4,5);
           pane.setHalignment(gameOver, HPos.CENTER);

        }

     // if the game is not over
     else{
       // if the user press up key
       if (e.getCode() == KeyCode.UP) {
             // if the board can be moved up
             // move the board up and copy from the board to the pane
             // and update color, text and score
             if(board.canMove(Direction.UP)) {
                board.move(Direction.UP);
                System.out.println("Moving <UP>"); 
                board.addRandomTile(); 
                for (int i =0; i<4; i++) {
                  for (int j =0; j<4; j++) {
                     if (board.getGrid()[i][j] != 0) {
                     textArray[i][j].setText(
                     Integer.toString(board.getGrid()[i][j]));
                }
                    else {
                    textArray[i][j].setText("");
                } 
              }
             }
             updateColor();
             updateText();
             updateScore();
             }            
          }
        // if the user press down key
        else if (e.getCode() == KeyCode.DOWN) {
             if(board.canMove(Direction.DOWN)) {
             // if the board can be moved up
             // move the board up and copy from the board to the pane
             // and update color, text and score
                board.move(Direction.DOWN);
                board.addRandomTile(); 
                System.out.println("Moving <DOWN>"); 
                for (int i =0; i<4; i++) {
                  for (int j =0; j<4; j++) {
                     if (board.getGrid()[i][j] != 0) {
                     textArray[i][j].setText(
                     Integer.toString(board.getGrid()[i][j]));
                }
                    else {
                    textArray[i][j].setText("");
                } 

             }
            }
             updateColor();
             updateText();
             updateScore();

             }            
          }

        // if the user press left key
        else if (e.getCode() == KeyCode.LEFT) {
             if(board.canMove(Direction.LEFT)) {
             // if the board can be moved up
             // move the board up and copy from the board to the pane
             // and update color, text and score
                board.move(Direction.LEFT);
                board.addRandomTile(); 
                System.out.println("Moving <LEFT>"); 
                for (int i =0; i<4; i++) {
                  for (int j =0; j<4; j++) {
                     if (board.getGrid()[i][j] != 0) {
                     textArray[i][j].setText(
                     Integer.toString(board.getGrid()[i][j]));
                 }
                    else {
                    textArray[i][j].setText("");
                } 

              }
             }
             updateColor();
             updateText();
             updateScore();

             }            
          }

         // if the user press right key
         else if (e.getCode() == KeyCode.RIGHT) {
             if(board.canMove(Direction.RIGHT)) {
             // if the board can be moved up
             // move the board up and copy from the board to the pane
             // and update color, text and score
                board.move(Direction.RIGHT);
                board.addRandomTile(); 
                System.out.println("Moving <RIGHT>");
                for (int i =0; i<4; i++) {
                  for (int j =0; j<4; j++) {
                     if (board.getGrid()[i][j] != 0) {
                     textArray[i][j].setText(
                     Integer.toString(board.getGrid()[i][j]));
                }
                    else {
                    textArray[i][j].setText("");
                } 

              }
             }
             updateColor();
             updateText();
             updateScore();

             }            
          }

         // if the user press s key
         // save the board
         else if (e.getCode() == KeyCode.S) {
           try {
           board.saveBoard(outputBoard);
           } catch (IOException o) { 
          System.out.println("saveBoard threw an Exception");

           }
          System.out.println("Saving Board to: " + outputBoard);
          
            
          }    
       }
      }
    }


    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
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
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
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

    // Print the Usage Message 
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
