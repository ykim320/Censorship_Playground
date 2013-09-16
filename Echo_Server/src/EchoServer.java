import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	final static protected int PORT = 8001;
	
	public static void main(String[] args) throws Exception {
		try {
			new EchoServer();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public EchoServer() throws IOException {
		ServerSocket server = new ServerSocket(PORT);
		Socket socket = server.accept();

		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		DataOutputStream out = new DataOutputStream(os);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));

//		out.writeBytes("HTTP/1.1 404 OK\n");
//		out.writeBytes("Content-Type: text/plain\n\n");

		String line;

		//Reading what is sent by the client and 
		//sends it back to server
		line = in.readLine();
		out.writeBytes(line);

//		while ((line = in.readLine()) != null && (line.length() != 0)) {
//			out.writeBytes(line + "\n");
//		}
		out.close();
		socket.close();
	}
}