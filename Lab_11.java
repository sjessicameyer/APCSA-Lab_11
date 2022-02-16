import java.util.Scanner;
import java.lang.Math;

public class Lab_11{

  public static final String BORDER_FORMAT = "\033[1;37m";//+"\u001B[46m";
  public static final String ANSI_RESET = "\u001B[0m";
  private Scanner input;
  private int mirrorsFound = 0;
  private int lazersShot = 0;
  public Lab_11(){
    
    //set up black box
    char[][] displayBox = setUpBoard();
    char[][] blackBox = setUpBoard();
    
    //generate mirrors
    blackBox = genMirrors(blackBox);
    
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

    //move forward
    if (array[outputRow][outputCol]=='/'){
        if (outputDirection=='U'){
          outputDirection='R';
          outputCol++;
        }else if (outputDirection=='D'){
          outputDirection='L';
          outputCol--;
        }else if (outputDirection=='L'){
          outputDirection='D';
          outputRow++;
        }else if (outputDirection=='R'){
          outputDirection='U';
          outputRow--;
        }
      }else if(array[outputRow][outputCol]=='\\'){
        if (outputDirection=='R'){
          outputDirection='D';
          outputRow--;
        }else if (outputDirection=='D'){
          outputDirection='R';
          outputCol--;
        }else if (outputDirection=='L'){
          outputDirection='U';
          outputRow++;
        }else if (outputDirection=='U'){
          outputDirection='L';
          outputCol++;
        }
      }else {
        if (direction == 'R')
          outputCol++;
        else if (direction == 'L')
          outputCol--;

        if (direction == 'U')
          outputRow--;
        else if (direction == 'D')
          outputRow++;
      }
    
    //check if hit wall of box, return if so
    if (outputRow<0){
      return (outputCol+20);
    }else if(outputRow>9){
      return (outputCol);
    }else if(outputCol<0){
      return (19-outputRow);
    }else if(outputCol>9){
      return (30+outputRow);
    }

    //recurse
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
    }

    for (var i = 0; i<5; i++){
      boolean mirrorPlaced=false;
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
}