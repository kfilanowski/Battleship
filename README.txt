Author - Kevin Filanowski
Version - 09/21/18 - 1.0.0

TABLE-OF-CONTENTS:
------------------
Description
Contents
Compiling
Usage
Input File
------------------

DESCRIPTION:
------------------
This program is a general database program for Employee records. It takes two
files as input, admin.txt and faculty.txt, where both text files contain
information about each Employee. It will create two tables, where each row
is an employee. There are a number of operations that can be used, such as:
0) Quit - Exits the program
1) Intersect - Creates a new table from two tables comprised of records
that have the same value for a specific attribute.
2) Difference - Creates a new table comprised of records in one table
but not another.
3) Union - Creates a new table comprised of records that occur in both tables.
4) Select - Creates a new table comprised of nodes having a value for a
specific attribute
5) Remove - Removes a table record by a matching ID.
6) Print both tables.

The program will be fairly picky with the input file, described in the
section 'INPUT FILE'

CONTENTS:
------------------
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
------------------
To compile the program, ensure that the files described in 'CONTENT',
specifically the src folder, are all in the same directory.
Then run the following command to compile all java files in your current
directory:

javac *.java

There should be no errors or warnings. Many class files should appear.

USAGE:
------------------
java Database
or
./java Database

A menu will appear from there, and the user can navigate that.

Input File:
------------------
The database is pretty rigid at detecting errors in the input files.
Ideally, to keep things nice and clean, we should have 7 pieces of employee
data on each line, so that each line represents one row in the table.
The information must be input in the following order, with any amount of
spacing in between each piece:

First_Name  Last_Name  Martial_Status  ID  Phone_Number  Division  Years_Worked

The program will check for any strange characters in names, as well as check
if phone number, division, years worked, and ID are integers only. Incorrect
or missing information will be mentioned, and the program will gracefully exit.
