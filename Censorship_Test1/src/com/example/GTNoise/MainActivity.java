package com.example.GTNoise;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn_sendRequest = (Button)findViewById(R.id.button);
		btn_sendRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openOptionsMenu();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//		return super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
		case R.id.action_invalid_method:
			String str = sendInvalidHeader();
			Toast.makeText(getApplicationContext(), str, 
					Toast.LENGTH_LONG).show();
			break;
		case R.id.action_invalid_field_count:
			String str1 = sendInvalidFieldCount();
			Toast.makeText(getApplicationContext(), str1, 
					Toast.LENGTH_LONG).show();
			break;
		case R.id.action_big_request_method:
			String str2 = sendBigRequest();
			Toast.makeText(getApplicationContext(), str2, 
					Toast.LENGTH_LONG).show();
			break;
		case R.id.action_invalid_version_number:
			String str3 = sendInvalidVersion();
			Toast.makeText(getApplicationContext(), str3, 
					Toast.LENGTH_LONG).show();
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

	/*This method tries to create a invalid http request header
	 */
	private static String sendInvalidHeader(){
		String decoded = null;
		String requestHeader;
		Socket clientSocket;
		try {
			clientSocket = new Socket("10.0.2.2", 8001);
			InputStream from_server = clientSocket.getInputStream();
			PrintWriter to_server =  
					new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			//for generating random http headers.
			requestHeader = generateRandomWords(4);

			//Creating invalid request header
			//Creating a request that looks like this
			//XxXxX / HTTP/1.1nr
			to_server.println(requestHeader + " / HTTP/1.1\n\r");
			to_server.flush();  // Send it right now!

			// Now read the server's response, and write it to the file
			byte[] resultBuff = new byte[0];
			byte[] buff = new byte[1024];
			int k = -1;
			while((k = from_server.read(buff, 0, buff.length)) > -1) {
				byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
				System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
				System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
				resultBuff = tbuff; // call the temp buffer as your result buff
			}
			System.out.println(resultBuff.length + " bytes read.");
			decoded = new String(resultBuff, "UTF-8"); 
			System.out.println(decoded + " bytes read.");
			// When the server closes the connection, we close our stuff
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decoded.toString();
	}

	private static String sendInvalidFieldCount(){
		String decoded = null;
		Socket clientSocket;
		try {
			clientSocket = new Socket("10.0.2.2", 8001);
			InputStream from_server = clientSocket.getInputStream();
			PrintWriter to_server =  
					new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			//Test random invalid field count
			// generating a request that looks like this.
			//XxXxX XxXxX XxXxX XxXxX
			to_server.println(generateRandomWords(4) + " " + generateRandomWords(4) + " " + generateRandomWords(4) + " " + generateRandomWords(4));

			to_server.flush();  // Send it right now!

			// Now read the server's response, and write it to the file
			byte[] resultBuff = new byte[0];
			byte[] buff = new byte[1024];
			int k = -1;
			while((k = from_server.read(buff, 0, buff.length)) > -1) {
				byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
				System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
				System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
				resultBuff = tbuff; // call the temp buffer as your result buff
			}
			System.out.println(resultBuff.length + " bytes read.");
			decoded = new String(resultBuff, "UTF-8"); 
			System.out.println(decoded + " bytes read.");
			// When the server closes the connection, we close our stuff
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decoded.toString();

	}

	private static String sendBigRequest(){
		String decoded = null;
		Socket clientSocket;
		try {
			clientSocket = new Socket("10.0.2.2", 8001);
			InputStream from_server = clientSocket.getInputStream();
			PrintWriter to_server =  
					new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			//Testing random big request method.
			//generating a request that looks like this.
			//Xx*512 / HTTP/1.1
			to_server.println(generateRandomWords(2)+"*512" + " / HTTP/1.1\n\r");

			to_server.flush();  // Send it right now!

			// Now read the server's response, and write it to the file
			byte[] resultBuff = new byte[0];
			byte[] buff = new byte[1024];
			int k = -1;
			while((k = from_server.read(buff, 0, buff.length)) > -1) {
				byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
				System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
				System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
				resultBuff = tbuff; // call the temp buffer as your result buff
			}
			System.out.println(resultBuff.length + " bytes read.");
			decoded = new String(resultBuff, "UTF-8"); 
			System.out.println(decoded + " bytes read.");
			// When the server closes the connection, we close our stuff
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decoded.toString();

	}

	private static String sendInvalidVersion(){
		String decoded = null;
		float invalidVersion;
		Socket clientSocket;
		try {
			clientSocket = new Socket("10.0.2.2", 8001);
			InputStream from_server = clientSocket.getInputStream();
			PrintWriter to_server =  
					new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			//for generating random http headers.
			invalidVersion = generateRandomVersion(0,9);

			//Testing the random invalid version number
			//generating a request that looks like this.
			//GET / HTTP/XxX
			to_server.println("GET" + " / HTTP/" + invalidVersion + "\n\r");

			to_server.flush();  // Send it right now!

			// Now read the server's response, and write it to the file
			byte[] resultBuff = new byte[0];
			byte[] buff = new byte[1024];
			int k = -1;
			while((k = from_server.read(buff, 0, buff.length)) > -1) {
				byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
				System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
				System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
				resultBuff = tbuff; // call the temp buffer as your result buff
			}
			System.out.println(resultBuff.length + " bytes read.");
			decoded = new String(resultBuff, "UTF-8"); 
			System.out.println(decoded + " bytes read.");
			// When the server closes the connection, we close our stuff
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decoded.toString();

	}

	private static String sendHttpRequest(){
		String decoded = null;
		String requestHeader;
		Socket clientSocket;
		try {
			clientSocket = new Socket("10.0.2.2", 8001);
			InputStream from_server = clientSocket.getInputStream();
			PrintWriter to_server =  
					new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			//for generating random http headers.
			requestHeader = generateRandomWords(4);

			//Creating invalid request header
			//Creating a request that looks like this
			//XxXxX / HTTP/1.1nr
			to_server.println(requestHeader + " / HTTP/1.1\n\r");

			//Test random invalid field count
			// generating a request that looks like this.
			//XxXxX XxXxX XxXxX XxXxX
			to_server.println(generateRandomWords(4) + " " + generateRandomWords(4) + " " + generateRandomWords(4) + " " + generateRandomWords(4));

			//Testing random big request method.
			//generating a request that looks like this.
			//Xx*512 / HTTP/1.1
			to_server.println(generateRandomWords(2)+"*512" + " / HTTP/1.1\n\r");

			//Testing the random invalid version number
			//generating a request that looks like this.
			//GET / HTTP/XxX
			to_server.println(requestHeader + " / HTTP/1.1\n\r");

			to_server.flush();  // Send it right now!

			// Now read the server's response, and write it to the file
			byte[] resultBuff = new byte[0];
			byte[] buff = new byte[1024];
			int k = -1;
			while((k = from_server.read(buff, 0, buff.length)) > -1) {
				byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
				System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
				System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
				resultBuff = tbuff; // call the temp buffer as your result buff
			}
			System.out.println(resultBuff.length + " bytes read.");
			decoded = new String(resultBuff, "UTF-8"); 
			System.out.println(decoded + " bytes read.");
			// When the server closes the connection, we close our stuff
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decoded.toString();

	}


	/*
	 * Generating random x letter word where x is passed as 
	 * a parameter
	 */
	public static String generateRandomWords(int numberOfLetters)
	{
		String randomString = null;
		Random random = new Random();
		char[] word = new char[numberOfLetters]; // words of length 3 through 10. (1 and 2 letter words are boring.)
		for(int j = 0; j < word.length; j++)
		{
			word[j] = (char)('a' + random.nextInt(26));
		}
		randomString = new String(word);

		return randomString;
	}

	/*
	 * Generating random version
	 */
	public static float generateRandomVersion(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();
		float version; 

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;
		float randomNum2 = rand.nextInt((max - min) + 1) + min;

		version = randomNum + randomNum2/10; 
		return version;
	}
}
