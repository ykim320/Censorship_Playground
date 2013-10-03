import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer extends Thread
{ 
 protected Socket clientSocket;

 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 

    try { 
         serverSocket = new ServerSocket(8001); 
         System.out.println ("Connection Socket Created");
         try { 
              while (true)
                 {
                  System.out.println ("Waiting for Connection");
                  new EchoServer (serverSocket.accept()); 
                 }
             } 
         catch (IOException e) 
             { 
              System.err.println("Accept failed."); 
              System.exit(1); 
             } 
        } 
    catch (IOException e) 
        { 
         System.err.println("Could not listen on port: 8001."); 
         System.exit(1); 
        } 
    finally
        {
         try {
              serverSocket.close(); 
             }
         catch (IOException e)
             { 
              System.err.println("Could not close port: 8001."); 
              System.exit(1); 
             } 
        }
   }

 private EchoServer (Socket clientSoc)
   {
    clientSocket = clientSoc;
    start();
   }

 public void run()
   {
    System.out.println ("New Communication Thread Started");

    try { 
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                                      true); 
         BufferedReader in = new BufferedReader( 
                 new InputStreamReader( clientSocket.getInputStream())); 

         String inputLine; 

         while ((inputLine = in.readLine()) != null) 
             { 
              System.out.println ("Server: " + inputLine); 
              out.println(inputLine); 

              if (inputLine.equals("Bye.")) 
                  break; 
             } 

         out.close(); 
         in.close(); 
         clientSocket.close(); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
        } 
    }
} 


//public class EchoServer {
//
//final static protected int PORT = 8001;
//
//public static void main(String[] args) throws Exception {
//	try {
//		new EchoServer();
//	} catch(IOException e) {
//		e.printStackTrace();
//	}
//}
//
//public EchoServer() throws IOException {
//	ServerSocket server = new ServerSocket(PORT);
//	Socket socket = server.accept();
//
//	InputStream is = socket.getInputStream();
//	OutputStream os = socket.getOutputStream(); 
//	DataOutputStream out = new DataOutputStream(os);
//	BufferedReader in = new BufferedReader(new InputStreamReader(is));
//
//	String line;
//	line = in.readLine();
//      
//	System.out.println(line);
//	out.writeBytes(line);
////	out.writeBytes("HTTP/1.1 404 OK\n");
////	out.writeBytes("Content-Type: text/plain\n\n");
//	
//	out.close();
//	socket.close();
//}
//}