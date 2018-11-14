package server;

import java.util.Random;

/**
 * This class models a battleship grid for a player. It randomly places ships in a grid.
 * It also keeps track of hits and misses on the grid.
 * @author Jeriah Caplinger
 * @author Kevin Filanowski
 * @version November 2018
 */
public class Grid {
    /** Our 2D grid for the player's battleship. */
    private char[][] grid;
    /** Our 2D grid for displaying the player's grid to other players. */
    private char[][] publicGrid;
    /** A random number generator for randomly placing ships. */
    private Random random;
    /** The size of our grid. */
    private int sizeOfGrid;
    /** Keeps track of how many ships have been hit on this grid.*/
    private int numOfHits;

    /**
     * Constructor for a game grid.
     * Creates a new Grid and places ships randomly into the grid.
     * @param sizeOfGrid How big the grid is.
     */
    public Grid(int sizeOfGrid){
        this.grid = new char[sizeOfGrid][sizeOfGrid];
        this.publicGrid = new char[sizeOfGrid][sizeOfGrid];
        this.random = new Random();
        this.sizeOfGrid = sizeOfGrid;
        this.initiatePrivateGrid();
        this.initiatePublicGrid();
        this.placeShips();
        this.numOfHits = 0;
    }

    /**
     * Initiates the player's personal private grid to empty Chars.
     */
    private void initiatePrivateGrid(){
        for(int i = 0; i < sizeOfGrid; i++){
            for(int j = 0; j < sizeOfGrid; j++){
                grid[i][j] = GridEnum.Blank.getName();
            }
        }
    }

    /**
     * Initiates the player's public grid to empty Chars.
     */
    private void initiatePublicGrid(){
        for(int i = 0; i < sizeOfGrid; i++){
            for(int j = 0; j < sizeOfGrid; j++){
                publicGrid[i][j] = GridEnum.Blank.getName();
            }
        }
    }

    /**
     * Places ships randomly into the grid.
     */
    private void placeShips(){
        // Calls a helper method that places each ship into the grid.
        placeAship(Ship.Carrier.getName(),    Ship.Carrier.size());
        placeAship(Ship.BattleShip.getName(), Ship.BattleShip.size());
        placeAship(Ship.Cruiser.getName(), 	  Ship.Cruiser.size());
        placeAship(Ship.Submarine.getName(),  Ship.Submarine.size());
        placeAship(Ship.Destroyer.getName(),  Ship.Destroyer.size());
    }

    /**
     * Places a single ship into the grid.
     * @param ship - The character that represents the ship.
     * @param sizeOfship - How big the ship is.
     */
    private void placeAship(char ship, int sizeOfship){
        boolean go = true;
        // Tells us how far left and right (or up and down) we can place the ship.
        int[] place = new int[2];
        int x; int y; int sideOrVert;
        while(go) {
            // Randomly select a point in the grid
            x = random.nextInt(sizeOfGrid);
            y = random.nextInt(sizeOfGrid);
            // 2 because we have to decide if we are placing a ship in a
            // vertical or horizontal direction.
            sideOrVert = random.nextInt(2);

            // i.e. If we are doing horizontal
            if(sideOrVert == 0){
                // calls a helper method to determine if the spot is valid
                place = isValidHorozSpot(x, y, sizeOfship);
                // isValidHorozSpot returns -1 in the first index spot if it is not valid
                if(place[0] != -1){
                    go = false;
                    // helper method that places the ship into the grid
                    helpPlaceShipHorz(place, x, y, ship);
                }
            }else{
                // calls a helper method to determine if the spot is valid
                place = isValidVertSpot(x,y, sizeOfship);
                // isValidVertSpot returns -1 in the first index spot if it is not valid
                if(place[0] != -1){
                    go = false;
                    // helper method that places the ship into the grid
                    helpPlaceShipVert(place, x, y, ship);
                }
            }
        }
    }

    /**
     * Helper method that places the ship into the grid in a horizontal fashion
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
            grid[x][y - i] = ship;
        }

        for(int j = 1; j < move[1]; j++){
            grid[x][y + j] = ship;
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
            grid[x - i][y] = ship;
        }

        for(int j = 1; j < move[1]; j++){
            grid[x + j][y] = ship;
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
        int i = 0;
        // we determine how far we can go down the grid
        while(shipSize > 0 && go){
            // check array bounds
            if(x - i >= 0) {
                // if it is a valid spot to place
                if (grid[x - i][y] == GridEnum.Blank.getName()) {
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
        int j = 0;
        if (i > 0) {
        	j = 1;
        }
        // we determine how far we can go up the grid
        while(shipSize > 0 && go){
            // check array bounds
            if(x + j < sizeOfGrid) {
                // if it is a valid spot to place
                if (grid[x + j][y] == GridEnum.Blank.getName()) {
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
    private int[] isValidHorozSpot(int x, int y, int shipSize) {
        int[] result = new int[2];
        boolean go = true;
        int i = 0;
        // we determine how far we can go left on the grid
        while(shipSize > 0 && go) {
            // bound check
            if (y - i >= 0) {
                // if it is a valid spot to place
                if (grid[x][y - i] == GridEnum.Blank.getName()) {
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
        int j = 0;
        if (i > 0) {
        	j = 1;
        }
        // we determine how far right we can go on the grid
        while(shipSize > 0 && go) {
        	// bound check
        	if(y + j < sizeOfGrid) {
        		// if it is a valid spot to place
        		if (grid[x][y + j] == GridEnum.Blank.getName()) {
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
        if(grid[x][y] != GridEnum.Miss.getName() && 
        		grid[x][y] != GridEnum.Hit.getName()){
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
        if(grid[x][y] != GridEnum.Blank.getName()){
            hit = true;
            publicGrid[x][y] = GridEnum.Hit.getName();
            grid[x][y] = GridEnum.Hit.getName();
        }else{
            publicGrid[x][y] = GridEnum.Miss.getName();
            grid[x][y] = GridEnum.Miss.getName();
        }
        return hit;
    }

    /**
     * Converts the player's private grid to a readable string
     * @return a readable string of the player's private grid
     */
    public String getPrivateGrid() {        
        StringBuilder result = new StringBuilder();
        result.append(" ");
        for (int k = 0; k < sizeOfGrid; k++) {
        	result.append(String.format("%4d", k));
        }
        result.append("\n");
        for(int i = 0; i < sizeOfGrid; i++){
        	result.append("  ");
        	for (int k = 0; k < sizeOfGrid; k++) {
            	result.append("+---");
            }
            result.append("+\n");
            result.append(i + " ");
            for(int j = 0; j < sizeOfGrid; j++){
                result.append("| ");
                result.append(grid[i][j]);
                result.append(" ");
            }
            result.append("|\n");
        }
        result.append("  ");
        for (int k = 0; k < sizeOfGrid; k++) {
        	result.append("+---");
        }
        result.append("+\n");
        return result.toString();
    }

    /**
     * Converts the player's public grid to a readable string
     * @return a readable string of the player's public grid
     */
    public String getPublicGrid(){
        StringBuilder result = new StringBuilder();
        result.append(" ");
        for (int k = 0; k < sizeOfGrid; k++) {
        	result.append(String.format("%4d", k));
        }
        result.append("\n");
        for(int i = 0; i < sizeOfGrid; i++) {
        	result.append("  ");
        	for (int k = 0; k < sizeOfGrid; k++) {
            	result.append("+---");
            }
            result.append("+\n");
            result.append(i + " ");
            for(int j = 0; j < sizeOfGrid; j++) {
                result.append("| ");
                result.append(publicGrid[i][j]);
                result.append(" ");
            }
            result.append("|\n");
        }
        result.append("  ");
        for (int k = 0; k < sizeOfGrid; k++) {
        	result.append("+---");
        }
        result.append("+\n");
        return result.toString();
    }

    // main method that just tests the grid object
    public static void main(String[] args){
        Grid grid1 = new Grid(10);
        // Sizes less than 5 lead to infinite loops. (We can't fit a size 5 ship).
        // System.out.println(new Grid(4).getPrivateGrid());
        System.out.println(new Grid(5).getPrivateGrid());
        System.out.println(new Grid(6).getPrivateGrid());
        System.out.println(new Grid(7).getPrivateGrid());
        System.out.println(new Grid(8).getPrivateGrid());
        System.out.println(new Grid(9).getPrivateGrid());
        
        System.out.println("Printing the public grid...");
        System.out.println(grid1.getPublicGrid());
        System.out.println("Printing the private grid...");
        System.out.println(grid1.getPrivateGrid());
        
        int xcoord = grid1.random.nextInt(grid1.sizeOfGrid);
        int ycoord = grid1.random.nextInt(grid1.sizeOfGrid);
        System.out.println("Shooting random coordinate x: " + xcoord + ", y: " + ycoord);
        System.out.println("hit a ship?: " + grid1.shoot(xcoord, ycoord));
        System.out.println("Printing the public grid...");
        System.out.println(grid1.getPublicGrid());
        System.out.println("Printing the private grid...");
        System.out.println(grid1.getPrivateGrid());
        
        int hits = grid1.sizeOfGrid*3;
        System.out.println("shooting " + hits + " times randomly.");
        for (int i = 0; i < hits; i++) {
        	xcoord = grid1.random.nextInt(grid1.sizeOfGrid);
            ycoord = grid1.random.nextInt(grid1.sizeOfGrid);
            System.out.println("Shooting random coordinate x: " + xcoord + ", y: " + ycoord);
            System.out.println("hit a ship?: " + grid1.shoot(xcoord, ycoord));
        }
        System.out.println("Printing the public grid...");
        System.out.println(grid1.getPublicGrid());
        System.out.println("Printing the private grid...");
        System.out.println(grid1.getPrivateGrid());
        
        System.out.println("New grid:");
        Grid grid2 = new Grid(8);
        
        System.out.println("Printing the public grid...");
        System.out.println(grid2.getPublicGrid());
        System.out.println("Printing the private grid...");
        System.out.println(grid2.getPrivateGrid());
        
        System.out.println("Hitting entire grid.");
        for (int i = 0; i < grid2.sizeOfGrid; i++) {
        	for (int j = 0; j < grid2.sizeOfGrid; j++) {
        		grid2.shoot(i, j);
        	}
        }
        System.out.println("Printing the public grid...");
        System.out.println(grid2.getPublicGrid());
        System.out.println("Printing the private grid...");
        System.out.println(grid2.getPrivateGrid());
    }
}
