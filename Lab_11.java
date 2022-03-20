
import java.lang.*;
import java.util.*;
import java.io.*;

public class Lab_11{

  public static final String BORDER_FORMAT = "\033[1;37m";//+"\u001B[46m";
  public static final String ANSI_RESET = "\u001B[0m";
  private Scanner input;
  private Scanner boardInput;
  private int mirrorsFound = 0;
  private int lazersShot = 0;
  public Lab_11(){
    
    //set up black box
    char[][] displayBox = setUpBoard();
    char[][] blackBox = setUpBoard();
    
    //generate mirrors
    blackBox = genMirrors(blackBox);
    saveKey(blackBox);
    
    //set up scanner
    input = new Scanner(System.in);

    //menu
    while (true){
      display(displayBox);
      System.out.print("Choose:\n  (1) Shoot a Laser\n  (2) Guess at a mirror location\n  (0) Quit the game\n\nEnter choice:");
      int choice = input.nextInt();
      if (choice == 1){
        System.out.print("What number should the laser shoot from?");
        System.out.println("This shot comes out at: "+simulateLaser(blackBox,input.nextInt())+"\n");
      }else if (choice == 2){
        System.out.print("What column would you like to guess?");
        int guessCol=input.nextInt();
        System.out.print("What row would you like to guess?");
        int guessRow=input.nextInt();
        displayBox=guessLocation(blackBox, displayBox, guessCol, guessRow);
      }else if (choice == 3){
        display(blackBox);
      }else if (choice == 4){
        System.out.print("What file would you like to replace the board from? ");
        blackBox=replaceBoard(new File(input.next()));
        saveKey(blackBox);
      }else{
        System.exit(0);
      }
      System.out.println("You have found "+mirrorsFound+" of 10 mirrors\nYou have shot "+lazersShot+" Lazers");
    }
  }

  public void display(char[][] array){
    System.out.print(BORDER_FORMAT+"\n     20   21   22   23   24   25   26   27   28   29      "+ANSI_RESET+"\n");
    for (int i = 0; i < 10; i++){
      System.out.print(BORDER_FORMAT+" "+(19-i)+ " "+ANSI_RESET);
      for (int k = 0; k < 10;k++){
        System.out.print("  "+array[i][k]+"  ");
        //System.out.printf("%3s",item);
      }
      System.out.println(BORDER_FORMAT+" "+(30+i)+" "+ANSI_RESET);
    }
    System.out.println(BORDER_FORMAT+"      0    1    2    3    4    5    6    7    8    9      "+ANSI_RESET+"\n");
  }

  public int simulateLaser(char[][] array, int inputLocation){
    lazersShot++;
    if (inputLocation>=10&&inputLocation<=19)
      return simulateLaser(array, 19-inputLocation, 0, 'R');
    else if (inputLocation>=0&&inputLocation<=9)
      return simulateLaser(array, 9, inputLocation,'U');
    else if (inputLocation>=20&&inputLocation<=29)
      return simulateLaser(array, 0, inputLocation-20,'D');
    else if (inputLocation>=30&&inputLocation<=39)
      return simulateLaser(array, inputLocation-30, 0, 'L');
    return -1;
  }

  public int simulateLaser(char[][] array, int inputRow,int inputCol, char direction){
    char outputDirection = direction;
    int outputRow=inputRow;
    int outputCol=inputCol;

    //change direction
    if (array[inputRow][inputCol]=='/'&&direction=='U')
      outputDirection='R';
    else if(array[inputRow][inputCol]=='/'&&direction=='D')
      outputDirection ='L';
    else if(array[inputRow][inputCol]=='/'&&direction=='L')
      outputDirection = 'D';
    else if(array[inputRow][inputCol]=='/'&&direction=='R')
      outputDirection = 'U';
    else if (array[inputRow][inputCol]=='\\'&&direction=='U')
      outputDirection='L';
    else if(array[inputRow][inputCol]=='\\'&&direction=='D')
      outputDirection ='R';
    else if(array[inputRow][inputCol]=='\\'&&direction=='L')
      outputDirection = 'U';
    else if(array[inputRow][inputCol]=='\\'&&direction=='R')
      outputDirection = 'D';
    
    //move forward
    if (outputDirection == 'R')
      outputCol++;
    else if (outputDirection == 'L')
      outputCol--;
    else if (outputDirection == 'U')
      outputRow--;
    else if (outputDirection == 'D')
      outputRow++;
    
    //check if hit wall of box, return if so
    if (outputRow<0)
      return (outputCol+20);
    else if(outputRow>9)
      return (outputCol);
    else if(outputCol<0)
      return (19-outputRow);
    else if(outputCol>9)
      return (30+outputRow);
    else
      return simulateLaser(array, outputRow, outputCol, outputDirection);
  }

  public char[][] guessLocation(char[][] hiddenArray, char[][] knownArray, int guessCol, int guessRow){
    int col = 0, row = 0;
    
    //translate guess location into something more logical
    if (guessRow>=10&&guessRow<=19)
      row=19-guessRow;
    else if (guessRow>=30&&guessRow<=39)
      row=guessRow-30;
    
    if (guessCol>=0&&guessCol<=9)
      col=guessCol;
    else if (guessCol>=20&&guessCol<=29)
      col=guessCol-20;
    

    //if you hit a mirror, change things & print stuff
    if (hiddenArray[row][col]=='\\'){
      if (knownArray[row][col]=='.')
        mirrorsFound++;
      knownArray[row][col]=hiddenArray[row][col];
      System.out.println("Found mirror \\!");
    }else if (hiddenArray[row][col]=='/'){
      if (knownArray[row][col]=='.')
        mirrorsFound++;
      knownArray[row][col]=hiddenArray[row][col];
      System.out.println("Found mirror /!");
    }else{
      System.out.println("No mirror found.");
    }
    return knownArray;
  }

  public char[][] setUpBoard(){
    char[][] board = new char[10][10];
    for (var row = 0; row < 10; row++){
      for (var col = 0; col < 10; col++){
        board[row][col]='.';
      }
    }
    return board;
  }

  public char[][] genMirrors(char[][] board){
    char[][] board2 = board;
    for (var i = 0; i<5; i++){
      boolean mirrorPlaced=false;
      while (mirrorPlaced==false){
        int row = (int)(Math.random()*10);
        int col = (int)(Math.random()*10);
        if (board[row][col]=='.') {
          board2[row][col]='/';
          mirrorPlaced=true;
        }
      }
      mirrorPlaced=false;
      while (mirrorPlaced==false){
        int row = (int)(Math.random()*10);
        int col = (int)(Math.random()*10);
        if (board[row][col]=='.') {
          board2[row][col]='\\';
          mirrorPlaced=true;
        }
      }
    }

    return board2;
  }

  public char[][] replaceBoard(File file){
    try{
      boardInput = new Scanner(file);
    }catch(FileNotFoundException ex){
      System.out.println("Cannot access seed file");
		  System.exit(1);
    }

    char[][] board = new char[10][10];
    for (int r = 0; r<10; r++){
      for (int c = 0; c<10; c++){
        board[r][c]=boardInput.next().charAt(0);
      }
    }
    boardInput.close();
    return board;
  }

  public void saveKey(char[][] array){
    File file = new File("key.txt");
    PrintWriter output = null;
	  try
	  {
		  output = new PrintWriter(file);
	  }
	  catch (FileNotFoundException ex)
	  {
		  System.out.println("Cannot overwrite file");
		  System.exit(1);  // quit the program
	  }

    output.print("\n     20   21   22   23   24   25   26   27   28   29      \n");
    for (int i = 0; i < 10; i++){
      output.print(" "+(19-i)+ " ");
      for (int k = 0; k < 10;k++){
        output.print("  "+array[i][k]+"  ");
        //System.out.printf("%3s",item);
      }
      output.println(" "+(30+i)+" ");
    }
    output.println("      0    1    2    3    4    5    6    7    8    9      \n");
    output.close();
  }
}