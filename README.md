# Login-Manager
A basic Login Management system that stores inputted Usernames, Passwords, and Notes in a table.
## Versions
**V1.0** <br />
Using basic Jframe as the GUI of the program, allowing users to interact with the program and displays all stored information in a table. There are 4 buttons allowing the user to: Add information, Remove selected information, Export stored information, Import information. <br />
 - Add button prompts the user for 3 inputs for the Username, Password, and Notes respectivly. <br />
 - Remove button removes the selected entry in the table. <br />
 - Export button creates a "data.txt" document which contains the information stored in the table. <br />
 - Import button locates a "data.txt" document and reads the information into the table. <br />
 - Each entry information can be edited by double clicking on the box in the table. <br />

Notes for this version: 

 - Information stored in "data.txt" text document are not secured. Allowing for simple reading of the document to gain access to all information.
 - Exporting and Importing of information has to be done manually.

-----

**V1.1** <br />
Added export and import functions on start and closing of the program. <br />
 - Import function added to the creation of the JFrame, allowing the program to automatically import from text document on boot of the code.
 - Export function added to event listener for the closing of the window. Allows the program to automatically export to a text document on closing of the code.
 - Adjusted and simplified code

Notes for this version: 

 - Information stored in "data.txt" text document are still not secured. Allowing for simple reading of the document to gain access to all information.

-----
