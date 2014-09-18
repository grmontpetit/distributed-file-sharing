Dropbox clone using JAVA
========================

# Description
This project was made during my Distributed Systems class.
We had to choose a project within a list of project and
we chose that.

I didn't know much about futures back then so I had to
code it witht he knowledge that I possessed.

The code is in the french, there is a client and a server. I included the lib folder (there is only 1 library) since the project isn't a maven project.

# Unimplemented features
I would have liked to sync the files using a byte difference system but could not complete that in time so everytime a file is modified, the client resends the whole file the the server.

# Architecture
The software is using a client-server architecture and had to abide by the Distributed Systems properties, meaning no global clock, transparency, heterogeneity.

If I remember correctly, we had to follow the principles in this brick of a book:
http://irrealis.me/papers/Distributed%20Systems%20-%20Concepts%20and%20Design%20(Coulouris).pdf
