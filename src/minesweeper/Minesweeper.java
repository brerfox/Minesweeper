/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import minesweeper.Board;
/**
 *
 * @author David
 */
@SuppressWarnings("serial")
public class Minesweeper extends JFrame {

    public static final int CELL_SIZE = 10;
    public static final int ROWS = 8;
    public static final int COLS = 8;
    
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CELL_PADDING = CELL_SIZE/6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING;
    public static final int SYMBOL_STROKE_WIDTH = 7;
    public static final Color BG_COLOUR_NOT_REVEALED = Color.GREEN;
    public static final Color FG_COLOUR_NOT_REVEALED = Color.RED;
    public static final Color BG_COLOUR_REVEALED = Color.DARK_GRAY;
    public static final Color FG_COLOUR_REVEALED = Color.LIGHT_GRAY;
       public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    JButton btncells[][]  = new JButton[ROWS][COLS];
    
    //enum for game states
    public enum GameState{
        PLAYING, WON, LOST
    }
    //enum for flag value
    public enum HasFlag{
        NOFLAG, FLAG
    }
   //public enum Board{
      //  MINE, BLANK, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT
    //}
    private Board[][] cell = new Board[ROWS][COLS];
 
    GameState game;
    
    public Minesweeper(){
        Container cp = this.getContentPane();
        
        cp.setLayout(new GridLayout(ROWS,COLS,2,2));
        
        for(int row=0; row<ROWS; row++){
            for(int col=0; col<COLS; col++){
                btncells[row][col] = new JButton(" ");
                cp.add(btncells[row][col]);
            }
        }
        for(int row=0; row<ROWS; row++){
        	for(int col=0; col<COLS; col++)
        		cell[i][j] = new Board();
        }
       cp.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
       pack();
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setTitle("Minesweeper");
       setVisible(true);
       
       initGame();
       CellMouseListener listener = new CellMouseListener();
       
       for(int row=0; row<ROWS; row++){
           for(int col=0; col<COLS; col++){
               btncells[row][col].addMouseListener(listener);
               
           }
       }
       if(hasWon()){
           game = GameState.WON;
       }
       
       repaint();
    }
   
    public void initGame(){
        Random randGen = new Random();
        for(int row=0; row<ROWS; row++){
            for(int col=0; col<COLS; col++){
                btncells[row][col].setEnabled(true);  // enable button
                btncells[row][col].setForeground(FG_COLOUR_NOT_REVEALED);
                btncells[row][col].setBackground(BG_COLOUR_NOT_REVEALED);
                btncells[row][col].setFont(FONT_NUMBERS);
                btncells[row][col].setText("");
                cell[row][col].hasFlag = false;
                cell[row][col].hasMine = false;
                cell[row][col].isOpen = false;
                cell[row][col].number = 0;
            }
        }
        
        int mines = 10;
        while(mines>0){
            int row = randGen.nextInt(ROWS);
            int col = randGen.nextInt(COLS);
            if(cell[row][col].hasMine == true){
                continue;
            }
            else{
                cell[row][col].hasMine = true;
                mines--;
            }
        }
        for(int row=0; row<ROWS; row++){
            for(int col=0; col<COLS; col++){
                int count = 0;
                if(cell[row][col].hasMine == true)
                    continue;
                
                for(int i=-1; i<=1; i++){
                    if(row+i<0 || row+i>ROWS-1)
                        continue;
                    for(int j=-1; j<=1; j++){
                        if(col+j<0 || col+j>COLS-1)
                            continue;
                        else{
                            if(cell[row+i][col+j].hasMine == true)
                            count++;
                            
                        }
                    }
                }
                cell[row][col].number = count;
            }
        }
        game = GameState.PLAYING;
    }
    
    public boolean hasWon(){
        int count = 0;
        
        for(int row=0; row<ROWS; row++){
            for(int col=0; col<COLS; col++){
                if((cell[row][col].isOpen==false && cell[row][col].hasFlag==true && 
                    cell[row][col].hasMine==false) ||
                    (cell[row][col].isOpen==true && cell[row][col].hasMine==true))
                    count++;
                
            }
        }
        if(count>0)
            return false;
        else
            return true;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable(){
        @Override
        public void run(){
            new Minesweeper();
        }
        });
        
    }
    
    private class CellMouseListener extends MouseAdapter{
        @Override
        public void mouseClicked( MouseEvent e){
            
            int rowselected = -1;
            int colselected = -1;
            
            JButton source = (JButton)e.getSource();
            boolean found = false;
            
            //finding the cell clicked
            for(int row=0; row<ROWS&&!found; row++){
                for(int col=0; col<COLS&&!found; col++){
                    if(source == btncells[row][col]){
                        rowselected = row;
                        colselected = col;
                        found = true;
                    }
                }
            }
            if(e.getButton() == MouseEvent.BUTTON1){
                if(cell[rowselected][colselected].hasMine == true){
                    game = GameState.LOST;
                }
                else{
                    btncells[rowselected][colselected].setBackground(BG_COLOUR_REVEALED);
                    btncells[rowselected][colselected].setForeground(FG_COLOUR_REVEALED);
                    btncells[rowselected][colselected].setFont(FONT_NUMBERS);
                    switch (cell[rowselected][colselected].number){
                        case 1:btncells[rowselected][colselected].setText("1");
                            break;
                        case 2:btncells[rowselected][colselected].setText("2");
                            break;
                        case 3:btncells[rowselected][colselected].setText("3");
                            break;
                        case 4:btncells[rowselected][colselected].setText("4");
                            break;
                        case 5:btncells[rowselected][colselected].setText("5");
                            break;
                        case 6:btncells[rowselected][colselected].setText("6");
                            break;
                        case 7:btncells[rowselected][colselected].setText("7");
                            break;
                        case 8:btncells[rowselected][colselected].setText("8");
                            break;
                        default:btncells[rowselected][colselected].setText("  ");
                        
                    }
                    btncells[rowselected][colselected].setEnabled(false);
                }
                
            }
            
            if(e.getButton() == MouseEvent.BUTTON3){
                if(cell[rowselected][colselected].hasFlag == true){
                    cell[rowselected][colselected].hasFlag = false;
                    btncells[rowselected][colselected].setText(" ");
                }
                else{
                    cell[rowselected][colselected].hasFlag = true;
                    btncells[rowselected][colselected].setText("F");
                }
            }
            
        }
    }
    
}
