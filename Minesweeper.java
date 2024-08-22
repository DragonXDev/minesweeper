import java.util.Scanner;
import java.util.Random;

public class Minesweeper {
    private static int flagsUsed = 0;
    private static Space[][] board;
    private static int numberOfMines = 0;
    private static boolean win = false;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        
        String difficulty = "";
        boolean pass = true;

        while (pass){
            System.out.println("Select Difficulty: Easy (E), Medium (M), or Hard (H):");

            difficulty = scan.next();

            if(!difficulty.equals("E")&&!difficulty.equals("M")&&!difficulty.equals("H")){
                System.out.println(Color.RED + Color.BOLD + "INVALID INPUT" + Color.RESET);
            }
            else{
                pass = false;
            }
        }

        if (difficulty.equals("E")){
            board = new Space[9][9];
            numberOfMines = 10;
        }
        else if(difficulty.equals("M")){
            board = new Space[16][16];
            numberOfMines = 40;
        }
        else{
            board = new Space[16][30];
            numberOfMines = 99;
        }

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                board[i][j] = new Space();
            }
        }


        String[] wholeMove;
        String move;
        boolean firstMove = true;

        scan.nextLine();

        do {
            printBoard();

            System.out.println("<row> <column> <uncover(uc)|flag(f)|unflag(uf)>");
            System.out.print("-> ");

            move = scan.nextLine();
            wholeMove = move.split(" ");

            if (wholeMove.length != 3) {
                System.out.println();
                System.out.println(Color.RED + Color.BOLD + "INVALID INPUT: Please enter row, column, and action (uc, f, or uf)" + Color.RESET + "\n");
                continue;
            }

            int c1, c2;
            try {
                c1 = Integer.parseInt(wholeMove[0]);
                c2 = Integer.parseInt(wholeMove[1]);
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(Color.RED + Color.BOLD + "INVALID INPUT: Row and column must be integers" + Color.RESET + "\n");
                continue;
            }

            if (c1 < 0 || c1 >= board.length || c2 < 0 || c2 >= board[0].length) {
                System.out.println();
                System.out.println(Color.RED + Color.BOLD + "INVALID INPUT: Row and column must be within the board's bounds" + Color.RESET + "\n");
                continue;
            }

            move = wholeMove[2];
            if (move.equals("uc")) {
                if (firstMove) {
                    initMines(c1, c2);
                    firstMove = false;
                }
                if (board[c1][c2].getHasMine()) {
                    if(board[c1][c2].getHasFlag()){
                        board[c1][c2].setHasFlag(false);
                        flagsUsed--;
                    }
                    board[c1][c2].setIsCovered(false);
                }
                uncover(c1, c2);
            } else if (move.equals("f")) {
                if(board[c1][c2].getHasFlag()){
                    System.out.println();
                    System.out.println(Color.RED + Color.BOLD + "Coordinates already have a flag" + Color.RESET);
                    System.out.println();
                    continue;
                }
                else{
                    board[c1][c2].setHasFlag(true);
                    flagsUsed++;
                }
            } else if (move.equals("uf")) {
                if (!board[c1][c2].getHasFlag()) {
                    System.out.println();
                    System.out.println(Color.RED + Color.BOLD + "Coordinates do not have a flag to uncover" + Color.RESET);
                    System.out.println();
                    continue;
                } else {
                    board[c1][c2].setHasFlag(false);
                    flagsUsed--;
                }
            } else {
                System.out.println();
                System.out.println(Color.RED + Color.BOLD + "INVALID INPUT: Action must be 'uc', 'f', or 'uf'" + Color.RESET + "\n");
            }
    } while (!gameOver());

        if(!win){
            printBoard();
            System.out.println("You Lost.");
        }
        else{
            showAll();
            System.out.println("You won Minesweeper!");
        }

    }

    public static void uncover(int c1, int c2) {
        if (board[c1][c2].getHasMine()) {
            return;
        }

        if (!board[c1][c2].getIsCovered()) {
            return;
        }
        board[c1][c2].setIsCovered(false);

        if (board[c1][c2].getNumMines() > 0) {
            return;
        }

        int[] dRow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dCol = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = c1 + dRow[i];
            int newCol = c2 + dCol[i];

            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                if (!board[newRow][newCol].getHasMine() && board[newRow][newCol].getIsCovered()) {
                    uncover(newRow, newCol);
                }
            }
        }
}



    public static boolean gameOver(){
        boolean gameOver = false;
        for(int i = 0; i < board.length && !gameOver; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j].getHasMine() && !board[i][j].getIsCovered()){
                    gameOver = true;
                }
            }
        }
        if(gameOver){
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[0].length; j++){
                    if(board[i][j].getHasMine()){
                        board[i][j].setIsCovered(false);
                    }
                }
            }
            return true;
        }
        else{
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[0].length; j++){
                    if(!board[i][j].getHasMine() && board[i][j].getIsCovered()){
                        return false;
                    }
                }
            }
        }
        win = true;
        return true;
    }
    public static void initMines(int c1, int c2){
        Random rand = new Random();

        int mines = 0;
        while(mines < numberOfMines){
            int y = rand.nextInt(board.length);
            int x = rand.nextInt(board[y].length);

            Space existing = board[y][x];
            if (existing.getHasMine() || (y==c1 && x==c2)) {
                continue;
            }

            board[y][x].setHasMine(true);
            mines++;
        }

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(i==0 && j==0){
                    if(board[0][1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[1][1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[1][0].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                }
                else if(i==board.length-1 && j==board[0].length-1){
                    if(board[board.length-2][board[0].length-1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[board.length-2][board[0].length-2].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[board.length-1][board[0].length-2].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                }
                else if(i==board.length-1 && j==0){
                    if(board[board.length-2][0].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[board.length-2][1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[board.length-1][1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                }
                else if(i==0 && j==board[0].length){
                    if(board[0][board[0].length-2].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[1][board[0].length-2].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    if(board[1][board[0].length-1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                }
                else{
                    if(i!=0){
                        if(j!=0){
                            if(board[i-1][j-1].getHasMine()){
                                board[i][j].setNumMines(board[i][j].getNumMines()+1);
                            }
                        }
                    if(board[i-1][j].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                        if(j!=board[0].length-1){
                            if(board[i-1][j+1].getHasMine()){
                                board[i][j].setNumMines(board[i][j].getNumMines()+1);
                            }
                        }
                    }
                    if(j!=0){
                    if(board[i][j-1].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                    }
                    if(j!=board[0].length-1){
                        if(board[i][j+1].getHasMine()){
                            board[i][j].setNumMines(board[i][j].getNumMines()+1);
                        }
                    }
                    if(i!=board.length-1){
                        if(j!=0){
                            if(board[i+1][j-1].getHasMine()){
                                board[i][j].setNumMines(board[i][j].getNumMines()+1);
                            }
                        }
                    if(board[i+1][j].getHasMine()){
                        board[i][j].setNumMines(board[i][j].getNumMines()+1);
                    }
                        if(j!=board[0].length-1){
                            if(board[i+1][j+1].getHasMine()){
                                board[i][j].setNumMines(board[i][j].getNumMines()+1);
                            }
                        }
                    }
                }
            }
        }
        
    }

    public static void printBoard(){
        System.out.print("   ");
        for(int i = 0; i < board[0].length; i++){
            if((i) < 10){
            System.out.print((i) + "  ");
            }
            else{
                System.out.print((i) + " ");
            }
        }
        System.out.println();
        for(int i = 0; i < board.length; i++){
            if((i)<10){
            System.out.print(i + "  ");
            }
            else{
                System.out.print(i + " ");
            }
            for(int j = 0;  j < board[0].length; j++){
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("# of Flags Used: " + flagsUsed);
    }

    public static void showAll(){
        System.out.print("   ");
        for(int i = 0; i < board[0].length; i++){
            if((i) < 10){
            System.out.print((i) + "  ");
            }
            else{
                System.out.print((i) + " ");
            }
        }
        System.out.println();
        for(int i = 0; i < board.length; i++){
            if((i)<10){
            System.out.print(i + "  ");
            }
            else{
                System.out.print(i + " ");
            }
            for(int j = 0;  j < board[0].length; j++){
                System.out.print(board[i][j].showAll() + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
