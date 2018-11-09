package server;

import java.util.Random;

/**
 * This class models a battleship grid for a player. It randomly places ships in a grid.
 * It also keeps track of hits and misses on the grid.
 * @author Jeriah Caplinger & Kevin Filanowski
 * @version 11/9/2018
 */
public class Grid {
    /** Our 2D grid for the player's battleship*/
    private char[][] grid;
    /** Our 2D grid for displaying the player's grid to other players*/
    private char[][] publicGrid;
    /** A random number generator for randomly placing ships*/
    private Random random;
    /** the size of our grid*/
    private int sizeOfGrid;
    /** Keeps track of how many ships have been hit on this grid*/
    private int numOfHits;

    /**
     * Creates a new Grid and places the ships into the grid
     * @param sizeOfGrid how big the grid is
     */
    public Grid(int sizeOfGrid){
        this.grid = new char[sizeOfGrid][sizeOfGrid];
        this.publicGrid = new char[sizeOfGrid][sizeOfGrid];
        this.random = new Random();
        this.sizeOfGrid = sizeOfGrid;
        this.initiateGrid();
        this.initiatePublicGrid();
        this.placeShips();
        this.numOfHits = 0;
    }

    /**
     * Initiates the grid to empty Chars
     */
    private void initiateGrid(){
        for(int i = 0; i < this.sizeOfGrid; i++){
            for(int j = 0; j < this.sizeOfGrid; j++){
                this.grid[i][j] = ' ';
            }
        }
    }

    /**
     * Initiates the grid to empty Chars
     */
    private void initiatePublicGrid(){
        for(int i = 0; i < this.sizeOfGrid; i++){
            for(int j = 0; j < this.sizeOfGrid; j++){
                this.publicGrid[i][j] = ' ';
            }
        }
    }


    /**
     * Places ships into the grid
     */
    private void placeShips(){
        // Sets up all the ships
        // *****NOTE***** all ships sizes are one less, this is due to logic in the loops
        char carrier = 'C';
        int carrierSize = 4;
        char battleShip = 'B';
        int battleShipSize = 3;
        char cruiser = 'R';
        int cruiserSize = 2;
        char sub = 'S';
        int subSize = 2;
        char destroyer = 'D';
        int destSize = 1;

        // Calls a helper method that places each ship into the grid
        this.placeAship(carrier, carrierSize);
        this.placeAship(battleShip, battleShipSize);
        this.placeAship(cruiser, cruiserSize);
        this.placeAship(sub, subSize);
        this.placeAship(destroyer, destSize);
    }


    /**
     * Places a ship into the grid
     * @param ship the character that represents the ship
     * @param sizeOfship how big the ship is
     */
    private void placeAship(char ship, int sizeOfship){
        boolean go = true;
        // tells us how far left and right (or up and down) we can place the ship
        int[] place = new int[2];
        int x; int y; int sideOrVert;
        while(go) {
            // randomly select a point in the grid
            x = random.nextInt(this.sizeOfGrid);
            y = random.nextInt(this.sizeOfGrid);
            // 2 because we have to decide if we are placing a ship in a
            // vertical or horizontal direction
            sideOrVert = random.nextInt(2);

            // i.e. if we are doing horizontal
            if(sideOrVert == 0){
                // calls a helper method to determine if the spot is valid
                place = isValidHorozSpot(x, y, sizeOfship);
                // isValidHorozSpot returns -1 in the first index spot if it is not valid
                if(place[0] != -1){
                    go = false;
                    // helper method that places the ship into the grid
                    this.helpPlaceShipHorz(place, x, y, ship);
                }
            }else{
                // calls a helper method to determine if the spot is valid
                place = isValidVertSpot(x,y, sizeOfship);
                // isValidVertSpot returns -1 in the first index spot if it is not valid
                if(place[0] != -1){
                    go = false;
                    // helper method that places the ship into the grid
                    this.helpPlaceShipVert(place, x, y, ship);
                }
            }
        }
    }

    /**
     * helper method that places the ship into the grid in a horizontal fashion
     * @param move what moves we can take left and right. with left being move[0] and right move[1]
     * @param x our x coordinate in the grid
     * @param y our y coordinate in the grid
     * @param ship our character representing the ship
     */
    private void helpPlaceShipHorz(int[] move, int x, int y, char ship){
        // we just put the chars in the grid according to the moves we are allowing
        // int the horizontal directions
        grid[x][y] = ship;
        for(int i = 1; i < move[0]; i++){
            grid[x - i][y] = ship;
        }

        for(int j = 1; j < move[1]; j++){
            grid[x + j][y] = ship;
        }
    }

    /**
     * helper method that places the ship into the grid in a vertical fashion
     * @param move what moves we can take left and right. with left being move[0] and right move[1]
     * @param x our x coordinate in the grid
     * @param y our y coordinate in the grid
     * @param ship our character representing the ship
     */
    private void helpPlaceShipVert(int[] move, int x, int y, char ship){
        // we just put the chars in the grid according to the moves we are allowing
        // int the vertical directions
        grid[x][y] = ship;
        for(int i = 1; i < move[0]; i++){
            grid[x][y - i] = ship;
        }

        for(int j = 1; j < move[1]; j++){
            grid[x][y + j] = ship;
        }
    }

    /**
     * Determines if the randomly selected spot we chose will be valid for the ship
     * @param x our x coordinate that we randomly chose
     * @param y our y coordinate that we randomly chose
     * @param shipSize how big our ship is
     * @return an int array of size 2 representing how far we can go down(int[0]) and up(int[1])
     *          int[0] = -1 if the spot is not valid
     */
    private int[] isValidVertSpot(int x, int y, int shipSize){
        int[] result = new int[2];
        boolean go = true;
        int i = 1;
        // we determine how far we can go down the grid
        while(shipSize > 0 && go){
            // check array bounds
            if(y - i >= 0) {
                // if it is a valid spot to place
                if (grid[x][y - i] == ' ') {
                    i++;
                    // we decrement ship size meaning we can place a ship there
                    shipSize--;
                } else {
                    go = false;
                }
            }else{
                go = false;
            }
        }

        go = true;
        int j = 1;
        // we determine how far we can go up the grid
        while(shipSize > 0 && go){
            // check array bounds
            if(y + j < this.sizeOfGrid) {
                // if it is a valid spot to place
                if (grid[x][y + j] == ' ') {
                    j++;
                    // we decrement ship size meaning we can place a ship there
                    shipSize--;
                } else {
                    go = false;
                }
            }else{
                go = false;
            }
        }
        // shipSize equals 0 if we can place the ship in this random spot
        if(shipSize == 0){
            result[0] = i;
            result[1] = j;
        }else{
            result[0] = -1;
        }
        return result;
    }

    /**
     * Determines if the randomly selected spot we chose will be valid for the ship
     * @param x our x coordinate that we randomly chose
     * @param y our y coordinate that we randomly chose
     * @param shipSize how big our ship is
     * @return an int array of size 2 representing how far we can go left(int[0]) and right(int[1])
     *          int[0] = -1 if the spot is not valid
     */
    private int[] isValidHorozSpot(int x, int y, int shipSize){
        int[] result = new int[2];
        boolean go = true;
        int i = 1;
        // we determine how far we can go left on the grid
        while(shipSize > 0 && go) {
            // bound check
            if (x - i >= 0) {
                // if it is a valid spot to place
                if (grid[x - i][y] == ' ') {
                    i++;
                    // we decrement ship size meaning we can place a ship there
                    shipSize--;
                } else {
                    go = false;
                }
            }else{
                go = false;
            }
        }
        go = true;
        int j = 1;
        // we determine how far right we can go on the grid
        while(shipSize > 0 && go){
            // bound check
            if(x + j < this.sizeOfGrid) {
                // if it is a valid spot to place
                if (grid[x + j][y] == ' ') {
                    j++;
                    // we decrement ship size meaning we can place a ship there
                    shipSize--;
                } else {
                    go = false;
                }
            }else{
                go = false;
            }
        }
        // shipSize equals 0 if we can place the ship in this random spot
        if(shipSize == 0){
            result[0] = i;
            result[1] = j;
        }else{
            result[0] = -1;
        }
        return result;
    }


    /**
     *  Determines if the player can shoot at the specific point in the grid
     * @param x x coordinate of shot
     * @param y y coordinate of shot
     * @return true if the shot was successfully launched either hit or miss
     *          false if the spot to shoot at has already been shot at
     */
    protected boolean isValidShot(int x, int y){
        boolean result = false;
        if(grid[x][y] != 'X' && grid[x][y] != '@'){
            result = true;
        }
        return result;
    }

    /**
     * Shoots the player's shot at the specific point in the grid
     * @param x x coordinate of the shot
     * @param y y coordinate of the shot
     * @return true if the shot hit a ship, false if it was a miss
     */
    protected boolean shoot(int x, int y){
        boolean hit = false;
        if(grid[x][y] != ' '){
            hit = true;
            this.publicGrid[x][y] = '@';
            this.grid[x][y] = '@';
        }else{
            this.publicGrid[x][y] = 'X';
            this.grid[x][y] = 'X';
        }

        return hit;
    }

    /**
     * Converts the player's private grid to a readable string
     * @return a readable string of the player's private grid
     */
    public String GetPrivateGrid(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < this.sizeOfGrid; i++){
            for(int j = 0; j < this.sizeOfGrid; j++){
                result.append("| ");
                result.append(this.grid[i][j]);
                result.append(" ");
            }
            result.append("|\n");
        }
        return result.toString();
    }


    /**
     * Converts the player's public grid to a readable string
     * @return a readable string of the player's public grid
     */
    public String GetPublicGrid(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < this.sizeOfGrid; i++){
            for(int j = 0; j < this.sizeOfGrid; j++){
                result.append("| ");
                result.append(this.publicGrid[i][j]);
                result.append(" ");
            }
            result.append("|\n");
        }
        return result.toString();
    }



    // main method that just tests the grid object
    public static void main(String[] args){
        System.out.println(new Grid(10));
    }
}
