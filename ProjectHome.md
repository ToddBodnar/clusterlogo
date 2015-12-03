# What is it? #

Cluster Logo is an extension of NetLogo that allows the user to take advantage of multiple computers to speed up simulations.

# How it works #

Cluster Logo is a simple client-server set of programs. The Client is executed on each of the computers and pointed to the IP address of the server. On the server, the user selects the .nLogo file for the simulation, and chooses the paramaters to vary in a Behavior Space like way. The server then sends the .nLogo file and the parameters to the clients where they run the simulations. Finally the client reports the results back to the server before taking the next assignment in the queue.

# To Do #
  * End user documentation and tutorials
  * Redo this page