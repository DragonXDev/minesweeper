public class Space {
    private boolean hasMine;
    private boolean isCovered;
    private int numMines;
    private boolean hasFlag;


    public Space(){
        hasMine = false;
        isCovered = true;
        numMines = 0;
        hasFlag = false;
    }
    public void setHasMine(boolean mine){
        hasMine = mine;
    }

    public boolean getHasMine(){
        return hasMine;
    }

    public void setIsCovered(boolean covered){
        isCovered = covered;
    }

    public boolean getIsCovered(){
        return isCovered;
    }

    public void setNumMines(int mines){
        numMines = mines;
    }

    public int getNumMines(){
        return numMines;
    }

    public void setHasFlag(boolean flag){
        hasFlag = flag;
    }

    public boolean getHasFlag(){
        return hasFlag;
    }

    public String toString(){
        if(hasFlag && isCovered){
            return Color.CYAN + ">" + Color.RESET;
        }
        if(isCovered){
            return ".";
        }
        if(hasMine){
            return Color.BOLD + Color.RED + "*" + Color.RESET;
        }
        if(numMines==0){
            return " ";
        }
        return Color.NUM[2] + numMines + Color.RESET;


    }

    public String showAll(){
        if(hasFlag && isCovered){
            return Color.CYAN + ">" + Color.RESET;
        }
        if(hasMine){
            return Color.BOLD + Color.RED + "*" + Color.RESET;
        }
        if(numMines==0){
            return " ";
        }
        return Color.NUM[2] + numMines + Color.RESET;
    }
}