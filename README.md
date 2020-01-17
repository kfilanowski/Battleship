# Author
Kevin Filanowski

Jeriah Caplinger

# Version
December 10, 2018 - 1.0.0

# Table of Contents
* [Description](#description)
* [Contents](#contents)
* [Compiling](#compiling)
* [Usage - Client](#usage-for-client)
* [Usage - Server](#usage-for-server)

# Description
A command-line based BattleShip game including a client and a server.
There may be multiple players playing against each other, but a minimum of
two players is required. 
See usage for the client and the server for specific details about each.

# Contents
doc - A HTML document showing off the java docs.

src - A folder containing the source files of the program.

## Inside src:

### Inside Client: The client-sided portion of BattleShip.

BattleDriver
* The driver that initializes the client and passes command line
               arguments to. This class is also responsible for user input.
               
BattleClient
* The client that connects to the server and listens for responses
               from the server, and passes messages along to the server.
               
PrintStreamMessageListener
* This class listens to BattleClient and controls the
                            output of what BattleClient recieves.

### Inside Common: The files that exist in both client and server.
ConnectionAgent
* The intermediary between the client and the server. This
                 class serves both the client and the server, and is
                 responsible for maintaining the connection and sending
                 and receiving messages to and from the client and server.
                 
MessageListener
* This interface represents observers of MessageSources.
                 The client, for example, is a message listener. It 
                 listens for messages from the ConnectionAgent.
                 
MessageSource
* An interface defining the sources of where messages come from.
               The ConnectionAgent is a message source, as it updates its
               listers of messages it receives.

### Inside Server: 
BattleServer
* The server handles the responses sent by the clients,
              and performs certain actions depending on the command.
              
BattleShipDriver
* The driver for the server, initializes the server
                  on a specified port and tells the server to begin
                  listening for client requests.
                  
CoordinateOutOfBoundsException
* A custom exception for when the server 
                                receives an attack command that is not 
                                within the size of the game board.
                                
Game
* The game logic of BattleShip.

GameOverException
* A custom exception that is thrown when the game ends.

Grid
* The grid logic of BattleShip. The results of the game are updated
      in the grid.
      
GridEnum
* An enum class holding some of the common elements in the grid.

IllegalCoordinateException
* A custom exception class for when the server
                            receives attack coordinates that have already
                            been attacked before.
                            
Ship
* An enum class holding all of the ships and their sizes.

# Compiling
To compile the program, 
First ensure that we are in the directory BattleShip/src, and that the
desired files are within the src directory.
Then in the terminal, run the following command to compile all files:
`javac */*.java`

There should be no errors or warnings. Many class files should appear
in their respective folders.

# Usage for Client
A client can join a server lobby by typing:
`/java client/BattleDriver <host address> <port number> <username>`
or 
`./java client/BattleDriver <host address> <port number> <username>`

When a player joins the server lobby, they
have two useable command options:
`/quit` : Closes the connection with the server.

`/play` : Starts the game with all of the players currently in the lobby,
        so as long as there are at least two players waiting.

Once the game is started, each player is assigned a game board with ships
randomly placed onto it. Players also have three useable command options:

`/attack <user> <x coordinate> <y coordinate>` : This command attacks a specified
                                               player's coordinates. 
                                               
`/show <user>` : Shows the public game board of another user, or the private
               game board of the user calling on the command. The public
               game board shows only the hits and misses, while the private
               game board shows both the placement of the ships and the hits
               and misses.
               
`/quit` : Closes the connection with the server, and kicks them out of the game.

# Usage for Server
A server can be created to serve multiple clients and host the BattleShip game.

`/java server/BattleShipDriver [port]`
or 
`./java server/BattleShipDriver [port]`

When a server is created, it will begin listening for client connections.
The server handles the logic of the game and handles the requests sent by
the clients.
