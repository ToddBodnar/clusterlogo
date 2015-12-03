### Setting up the client ###
Before running the client you need to make a lib folder in the same directory as the program and include the three jar files (which should be posted at some point under the downloads tab) in it.

### Running the client ###
Note: Before running the client you must start up the server.

Run the client by either double clicking on the jar file or using the command

`java -jar "ClusterLogo client.jar`

Then you need to enter the ip of the host to connect to, the number of threads to run (Its recommended to run one thread per processor), and choose a temporary working folder.

At this point the client will start getting experiments from the server.


### Closing the client ###
The safest way to close the client is to check the "Quit after current assignment" box. This will allow the client to finish its current assignments but not download any more. After its current assignments are completed, it will be safe to close the client.

Alternatively, it is normally safe to simply close the client's window. However the temp folder will not be emptied, and any progress will be lost.