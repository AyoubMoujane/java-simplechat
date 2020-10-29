// This file contains material supporting section 3.7 of the textbook:// "Object Oriented Software Engineering" and is issued under the open-source// license found at www.lloseng.com import java.io.*;import common.ChatIF;import ocsf.server.*;/** * This class overrides some of the methods in the abstract  * superclass in order to give more functionality to the server. * * @author Dr Timothy C. Lethbridge * @author Dr Robert Lagani&egrave;re * @author Fran&ccedil;ois B&eacute;langer * @author Paul Holden * @version July 2000 */public class EchoServer extends AbstractServer {		//Instance variables **********************************************	  	  /**	   * The interface type variable.  It allows the implementation of 	   * the display method in the client.	   */	  ChatIF serverUI; 	    //Class variables *************************************************    /**   * The default port to listen on.   */  final public static int DEFAULT_PORT = 5555;    //Constructors ****************************************************    /**   * Constructs an instance of the echo server.   *   * @param port The port number to connect on.   */  public EchoServer(int port,ChatIF serverUI)   {    super(port);    this.serverUI = serverUI;  }    /**   * Hook method called each time a new client connection is   * accepted. The default implementation does nothing.   * @param client the connection connected to the client.   */  protected void clientConnected(ConnectionToClient client) {	  serverUI.display("Client connected");  }  /**   * Hook method called each time a client disconnects.   * The default implementation does nothing. The method   * may be overridden by subclasses but should remains synchronized.   *   * @param client the connection with the client.   */  synchronized protected void clientDisconnected(    ConnectionToClient client) {	  serverUI.display("Client disconnected");  }  /**   * Hook method called each time an exception is thrown in a   * ConnectionToClient thread.   * The method may be overridden by subclasses but should remains   * synchronized.   *   * @param client the client that raised the exception.   * @param Throwable the exception thrown.   */  synchronized protected void clientException(		    ConnectionToClient client, Throwable exception) {	  serverUI.display("Client disconnected brutally");  }    //Instance methods ************************************************    /**   * This method handles any messages received from the client.   *   * @param msg The message received from the client.   * @param client The connection from which the message originated.   */  public void handleMessageFromClient    (Object msg, ConnectionToClient client)  {	String[] msgSplit = ((String) msg).split(" ");	if(((String) msg).startsWith("#")){		if(((String) msg).startsWith("#quit") || ((String) msg).startsWith("#logoff")){			try {				client.sendToClient(msg);				client.close();			}			catch(IOException e){				e.printStackTrace();			}		}	} else {		serverUI.display("Message received: " + msg + " from " + client);	    this.sendToAllClients(msg);	}      }    /**   * This method handles all data coming from the UI               *   * @param message The message from the UI.       */  public void handleMessageFromServerUI(String message)  {	  if(message.startsWith("#")) {		  String[] msgSplit = message.split(" ");		  if(message.startsWith("#quit")) {			  quit();		  }		  else if(message.startsWith("#stop")) {			  stopListening();		  }		  else if(message.startsWith("#close")) {			  try {				  close();			  }			  catch(IOException e){				  e.printStackTrace();			  }		  } 		  else if(message.startsWith("#start")) {			  if(isListening()) {				  serverUI.display("Server must be closed to use this command ");			  } else {				  try {					  listen();				  }				  catch(IOException e) {					  e.printStackTrace();				  }			  }		  }		  else {			  serverUI.display("Unknown command");		  }	  } else {		  serverUI.display("Server MSG: " + message);	  }  }      /**   * This method overrides the one in the superclass.  Called   * when the server starts listening for connections.   */  protected void serverStarted()  {    serverUI.display("Server listening for connections on port " + getPort());        }    /**   * This method overrides the one in the superclass.  Called   * when the server stops listening for connections.   */  protected void serverStopped()  {	  serverUI.display      ("Server has stopped listening for connections.");  }    /**   * Hook method called when the server is clased.   * The default implementation does nothing. This method may be   * overriden by subclasses. When the server is closed while still   * listening, serverStopped() will also be called.   */  protected void serverClosed() {	  serverUI.display      ("Server closed.");  }    public void quit() {	  try {		  close();	  }	  catch(IOException e){		  e.printStackTrace();	  }	  System.exit(0);  }    //Class methods ***************************************************    /**   * This method is responsible for the creation of    * the server instance (there is no UI in this phase).   *   * @param args[0] The port number to listen on.  Defaults to 5555    *          if no argument is entered.   */  /*  public static void main(String[] args)   {    int port = 0; //Port to listen on    try    {      port = Integer.parseInt(args[0]); //Get port from command line    }    catch(Throwable t)    {      port = DEFAULT_PORT; //Set port to 5555    }	    EchoServer sv = new EchoServer(port);        try     {      sv.listen(); //Start listening for connections    }     catch (Exception ex)     {      System.out.println("ERROR - Could not listen for clients!");    }  }  */}//End of EchoServer class