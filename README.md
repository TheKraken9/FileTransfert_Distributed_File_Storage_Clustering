# FileTransfert_Distributed_File_Storage_Clustering
File transfert and distributed file storage using Clustering

The principle is simple, a file sent in the server (main) will send this last one towards other servers (here, 3 servers) and divides the file towards these seveurs.
Once sent, the main server plays the role of a switcher.
If a client wants to download a file existing in the server, the main server will search in the sub-servers (if it exists) and restore them in a single file. 
