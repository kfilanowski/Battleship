Author - Kevin Filanowski
Author - Jeriah Caplinger
Version - December 09, 2018 - 1.0.0

TABLE-OF-CONTENTS:
==================
Description
Contents
Compiling
Usage - Client
Usage - Server
Game Logic
==================

DESCRIPTION:
==================
A command-line based BattleShip game including a client and a server.
There may be multiple players playing against each other, but a minimum of
two players is required. 
See usage for the client and the server for specific details about each.

CONTENTS:
==================
README - This file.
doc - A HTML document showing off the java docs.
src - A folder containing the source files of the program.

Inside src:
Database.java : The main driver of the program. This file should be called
when the program is to be run. This file reads the input, populates the tables,
and contains the flow of the user interaction and menu.
Table.java : The code for the generic table. Built using a linked list. This
file contains the logical part of the operations, such as "union", "select",
etc.
Employee.java : This class models an employee, and contains all of the
data an employee would have. Some of the data include: ID, Divison, Department,
and so on.
Person.java : A simple class modeling a person with personal information
such as their first and last name, and their martial status.
Status.java : A simple class modeling a martial status.
AttributeInterface.java : The API for a table record.
admin.txt : A sample text file showing an example of what the input file
should look like.
faculty.txt : A sample text file showing an example of what the input file
should look like.
other class files : The program compiled from my home computer, but it is
recommended you re-compile.

COMPILING:
==================
To compile the program, 
First ensure that we are in the directory BattleShip/src, and that the
desired files are within the src directory.
Then in the terminal, run the following command to compile all files:
javac */*.java

There should be no errors or warnings. Many class files should appear
in their respective folders.

USAGE - CLIENT:
==================
A client can join a server lobby by typing:
/java client/BattleDriver <host address> <port number> <username>
or 
./java client/BattleDriver <host address> <port number> <username>

When a player joins the server lobby, they
have two useable command options:
/quit : Closes the connection with the server.
/play : Starts the game with all of the players currently in the lobby,
        so as long as there are at least two players waiting.

Once the game is started, each player is assigned a game board with ships
randomly placed onto it. Players also have three useable command options:
/attack <user> <x coordinate> <y coordinate> : This command attacks a specified
                                               player's coordinates. 
/show <user> : Shows the public game board of another user, or the private
               game board of the user calling on the command. The public
               game board shows only the hits and misses, while the private
               game board shows both the placement of the ships and the hits
               and misses.
/quit : Closes the connection with the server, and kicks them out of the game.

USAGE - SERVER:
==================
A server can be created to serve multiple clients and host the BattleShip game.

/java server/BattleShipDriver [port]
or 
./java server/BattleShipDriver [port]

When a server is created, it will begin listening for client connections.
The server handles the logic of the game and handles the requests sent by
the clients.

GAME LOGIC:
==================
