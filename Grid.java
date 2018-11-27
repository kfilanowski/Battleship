package server;

import java.util.Random;

/**
 * This class models a battleship grid for a player. 
 * It randomly places ships in a grid.
 * It also keeps track of hits and misses on the grid.
 * @author Jeriah Caplinger
 * @author Kevin Filanowski
 * @version November 2018
 */
public class Grid {
    /** Our private 2D grid for the player's battleship. */
    private char[][] privateGrid;
    /** Our 2D grid for displaying the player's grid to other players. */
    private char[][] publicGrid;
    /** A random number generator for randomly placing ships. */
    private Random random;
    /** The size of our grid. */
    private int sizeOfGrid;
    /** Keeps track of how many ships have been hit on this grid. */
    private int numOfHits;

    /**
     * Constructor for a game grid.
     * Creates a new Grid and places ships randomly into the grid.
     * @param sizeOfGrid How big the grid is.
     */
    public Grid(int sizeOfGrid){
        this.privateGrid = new char[sizeOfGrid][sizeOfGrid];
        this.publicGrid = new char[sizeOfGrid][sizeOfGrid];
        this.random = new Random();
        this.sizeOfGrid = sizeOfGrid;
        this.initiatePrivateGrid();
        this.initiatePublicGrid();
        this.placeShips();
        this.setUpNumHitsNeed();
    }

    /**
     * Sets up the total number of hits left on this grid before all
     * ships have been sunk.
     */
    private void setUpNumHitsNeed() {
        // Add up all of the ship sizes for the total hits needed.
        this.numOfHits = Ship.Carrier.size() + Ship.BattleShip.size()
         + Ship.Cruiser.size() + Ship.Submarine.size() + Ship.Destroyer.size();
    }

    /**
     * Initiates the player's personal private grid to empty Chars.
     */
    private void initiatePrivateGrid(){
        for(int i = 0; i < sizeOfGrid; i++){
            for(int j = 0; j < sizeOfGrid; j++){
                privateGrid[i][j] = GridEnum.Blank.getSymbol();
            }
        }
    }

    /**
     * Initiates the player's public grid to empty Chars.
     */
    private void initiatePublicGrid(){
        for(int i = 0; i < sizeOfGrid; i++){
            for(int j = 0; j < sizeOfGrid; j++){
                publicGrid[i][j] = GridEnum.Blank.getSymbol();
            }
        }
    }

    /**
     * Places ships randomly into the grid.
     */
    private void placeShips(){
        // Calls a helper method that places each ship into the grid.
        placeAship(Ship.Carrier.getSymbol(),    Ship.Carrier.size());
        placeAship(Ship.BattleShip.getSymbol(), Ship.BattleShip.size());
        placeAship(Ship.Cruiser.getSymbol(), 	  Ship.Cruiser.size());
        placeAship(Ship.Submarine.getSymbol(),  Ship.Submarine.size());
        placeAship(Ship.Destroyer.getSymbol(),  Ship.Destroyer.size());
    }

    /**
     * Places a single ship into the grid.
     * @param ship - The character that represents the ship.
     * @param sizeOfship - How big the ship is.
     */
    private void placeAship(char ship, int sizeOfship) {
        boolean go = true;
        // Tells us how far left, right, or up, down, we can place the ship.
        int[] place = new int[2];
        int x; int y; int sideOrVert;
        while(go) {
            // Randomly select a point in the grid.
            x = random.nextInt(sizeOfGrid);
            y = random.nextInt(sizeOfGrid);
            // 0 = Horizontal, 1 = Vertical.
            sideOrVert = random.nextInt(2);

            if(sideOrVert == 0) {
                // Calls a helper method to determine if the spot is valid.
                place = isValidHorozSpot(x, y, sizeOfship);
                // isValidHorozSpot returns -1 in the first index spot if 
                // it is not valid.
                if(place[0] != -1) {
                    go = false;
                    // Helper method that places the ship into the grid.
                    helpPlaceShipHorz(place, x, y, ship);
                }
            }else{
                // Calls a helper method to determine if the spot is valid.
                place = isValidVertSpot(x,y, sizeOfship);
                // isValidVertSpot returns -1 in the first index spot if 
                // it is not valid.
                if(place[0] != -1) {
                    go = false;
                    // Helper method that places the ship into the grid.
                    helpPlaceShipVert(place, x, y, ship);
                }
            }
        }
    }

    /**
     * Helper method that places the ship horizontally into the grid.
     * @param move - What moves we can take left and right. 
     * Left being move[0] and right move[1].
     * @param x - Our x coordinate in the grid
     * @param y - Our y coordinate in the grid
     * @param ship - Our character representing the ship
     */
    private void helpPlaceShipHorz(int[] move, int x, int y, char ship) {
        // We just put the chars in the grid according to the moves 
        // we are allowing in the horizontal directions.
        privateGrid[x][y] = ship;
        for(int i = 1; i < move[0]; i++) {
            privateGrid[x][y - i] = ship;
        }

        for(int j = 1; j < move[1]; j++) {
            privateGrid[x][y + j] = ship;
        }
    }

    /**
     * Helper method that places the ship vertically into the grid.
     * @param move - What moves we can take left and right.
     * Left being move[0] and right being move[1].
     * @param x - Our x coordinate in the grid.
     * @param y - Our y coordinate in the grid.
     * @param ship - Our character representing the ship.
     */
    private void helpPlaceShipVert(int[] move, int x, int y, char ship) {
        // We just put the chars in the grid according to the moves
        // we are allowing in the vertical directions.
        privateGrid[x][y] = ship;
        for(int i = 1; i < move[0]; i++){
            privateGrid[x - i][y] = ship;
        }

        for(int j = 1; j < move[1]; j++){
            privateGrid[x + j][y] = ship;
        }
    }

    /**
     * Determines if the randomly selected spot 
     * we chose will be valid for the ship.
     * @param x - Our x coordinate that we randomly chose.
     * @param y - Our y coordinate that we randomly chose.
     * @param shipSize - How big our ship is.
     * @return - An int array of size 2 representing how far we 
     * can go down(int[0]) and up(int[1]).
     * NOTE: int[0] = -1 if the spot is not valid.
     */
    private int[] isValidVertSpot(int x, int y, int shipSize) {
        int[] result = new int[2];
        boolean go = true;
        int i = 0, j = 0;
        // We determine how far we can go down the grid.
        while(shipSize > 0 && go){
            // Check array bounds.
            if(x - i >= 0) {
                // If it is a valid spot to place.
                if (privateGrid[x - i][y] == GridEnum.Blank.getSymbol()) {
                    // If we can place a ship, decrement the ship size.
                    i++; shipSize--;
                } else {
                    go = false;
                }
            } else {
                go = false;
            }
        }

        go = true;
        if (i > 0)
            j = 1;
        // We determine how far we can go up the grid.
        while(shipSize > 0 && go){
            // Check array bounds.
            if(x + j < sizeOfGrid) {
                // If it is a valid spot to place.
                if (privateGrid[x + j][y] == GridEnum.Blank.getSymbol()) {
                    // If we can place a ship, decrement the ship size.
                    j++; shipSize--;
                } else {
                    go = false;
                }
            } else {
                go = false;
            }
        }
        // ShipSize equals 0 if we can place the ship in this random spot.
        if(shipSize == 0) {
            result[0] = i;
            result[1] = j;
        } else {
            result[0] = -1;
        }
        return result;
    }

    /**
     * Determines if the randomly selected spot 
     * we chose will be valid for the ship.
     * @param x - Our x coordinate that we randomly chose.
     * @param y - Our y coordinate that we randomly chose.
     * @param shipSize - How big our ship is.
     * @return - An int array of size 2 representing how far we can go 
     * left(int[0]) and right(int[1]).
     * NOTE: int[0] = -1 if the spot is not valid.
     */
    private int[] isValidHorozSpot(int x, int y, int shipSize) {
        int[] result = new int[2];
        boolean go = true;
        int i = 0, j = 0;
        // We determine how far we can go left on the grid.
        while(shipSize > 0 && go) {
            // Bound check.
            if (y - i >= 0) {
                // If it is a valid spot to place.
                if (privateGrid[x][y - i] == GridEnum.Blank.getSymbol()) {
                    // If we can place a ship, decrement the ship size.
                    i++; shipSize--;
                } else {
                    go = false;
                }
            } else {
                go = false;
            }
        }
        go = true;
        if (i > 0)
        	j = 1;
        // We determine how far right we can go on the grid.
        while(shipSize > 0 && go) {
        	// Bound check.
        	if(y + j < sizeOfGrid) {
        		// If it is a valid spot to place.
        		if (privateGrid[x][y + j] == GridEnum.Blank.getSymbol()) {
                    // If we can place a ship, decrement the ship size.
        			j++; shipSize--;
        		} else {
        			go = false;
        		}
        	}else{
                go = false;
            }
        }
        // shipSize equals 0 if we can place the ship in this random spot.
        if(shipSize == 0){
            result[0] = i;
            result[1] = j;
        }else{
            result[0] = -1;
        }
        return result;
    }

    /**
     * Determines if the player can shoot at the specific point in the grid.
     * @param x - x coordinate of the shot.
     * @param y - y coordinate of the shot.
     * @return - true if the shot can be taken.
     * false if the spot to shoot at has already been shot at.
     */
    protected boolean isValidShot(int x, int y) {
        boolean result = false;
        if(privateGrid[x][y] != GridEnum.Miss.getSymbol() && 
        		privateGrid[x][y] != GridEnum.Hit.getSymbol()) {
            result = true;
        }
        return result;
    }

    /**
     * Shoots the player's shot at the specific point in the grid.
     * @param x - x coordinate of the shot.
     * @param y - y coordinate of the shot.
     * @return true if the shot hit a ship, false if it was a miss.
     */
    protected boolean shoot(int x, int y) throws GameOverException {
        boolean hit = false;
        if(privateGrid[x][y] != GridEnum.Blank.getSymbol()) {
            hit = true;
            publicGrid[x][y] = GridEnum.Hit.getSymbol();
            privateGrid[x][y] = GridEnum.Hit.getSymbol();
            this.shotAShip();
        }else{
            publicGrid[x][y] = GridEnum.Miss.getSymbol();
            privateGrid[x][y] = GridEnum.Miss.getSymbol();
        }
        return hit;
    }

    /**
     * Detects when there are no more ships to hit and 
     * thus causes the game to be over.
     * @throws GameOverException - Thrown when the game is over.
     */
    private void shotAShip() throws GameOverException {
        this.numOfHits--;
        if(this.numOfHits == 0) {
            throw new GameOverException();
        }
    }

    /**
     * Converts the player's private grid to a readable string.
     * @return - A readable string of the player's private grid.
     */
    protected String getPrivateGrid() {        
        StringBuilder result = new StringBuilder();
        // Prints the first row of numbers.
        result.append(GridEnum.Blank);
        for (int k = 0; k < sizeOfGrid; k++) {
            result.append(String.format("%4d", k));
        }
        result.append("\n");
        // Prints the inner grid.
        for (int i = 0; i < sizeOfGrid; i++) {
            result.append(GridEnum.Blank.toString() + GridEnum.Blank);
            for (int k = 0; k < sizeOfGrid; k++) {
                result.append("+---");
            }
            result.append("+\n");
            result.append(String.format("%-2d", i));
            for (int j = 0; j < sizeOfGrid; j++) {
                result.append(GridEnum.Bar);
                result.append(GridEnum.Blank);
                result.append(privateGrid[i][j]);
                result.append(GridEnum.Blank);
            }
            result.append(GridEnum.Bar + "\n");
        }
        // Prints the last grid-line.
        result.append(GridEnum.Blank.toString() + GridEnum.Blank);
        for (int k = 0; k < sizeOfGrid; k++) {
            result.append("+---");
        }
        result.append("+\n");
        return result.toString();
    }

    /**
     * Converts the player's public grid to a readable string.
     * @return - A readable string of the player's public grid.
     */
    protected String getPublicGrid(){
        StringBuilder result = new StringBuilder();
        // Prints the first row of numbers.
        result.append(GridEnum.Blank);
        for (int k = 0; k < sizeOfGrid; k++) {
        	result.append(String.format("%4d", k));
        }
        result.append("\n");
        // Prints the inner grid.
        for (int i = 0; i < sizeOfGrid; i++) {
        	result.append(GridEnum.Blank.toString() + GridEnum.Blank);
        	for (int k = 0; k < sizeOfGrid; k++) {
            	result.append("+---");
            }
            result.append("+\n");
            result.append(String.format("%-2d", i));
            for(int j = 0; j < sizeOfGrid; j++) {
                result.append(GridEnum.Bar);
                result.append(GridEnum.Blank);
                result.append(publicGrid[i][j]);
                result.append(GridEnum.Blank);
            }
            result.append(GridEnum.Bar + "\n");
        }
        // Prints the last grid-line.
        result.append(GridEnum.Blank.toString() + GridEnum.Blank);
        for (int k = 0; k < sizeOfGrid; k++) {
        	result.append("+---");
        }
        result.append("+\n");
        return result.toString();
    }

    // Main method that just tests the grid object
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
        try {
            System.out.println("hit a ship?: " + grid1.shoot(xcoord, ycoord));
        }catch(GameOverException goe){
            System.out.println("game over!");
        }
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
            try {
                System.out.println("hit a ship?: " + grid1.shoot(xcoord, ycoord));
            }catch(GameOverException goe){
                System.out.println("game over!");
            }
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
        	    try {
                    grid2.shoot(i, j);
                }catch(GameOverException goe){
                    System.out.println("game over!");
                }
        	}
        }
        System.out.println("Printing the public grid...");
        System.out.println(grid2.getPublicGrid());
        System.out.println("Printing the private grid...");
        System.out.println(grid2.getPrivateGrid());
    }
}
