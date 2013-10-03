package com.example.GTNoise;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static String text = "Hello, World";
	TextView tv;
	Button btn_sendRequest;
	Button btn_headerManip;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		    StrictMode.setThreadPolicy(policy);
//		}
		
		btn_sendRequest = (Button)findViewById(R.id.button);
		btn_sendRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openOptionsMenu();
			}
		});

		btn_headerManip = (Button)findViewById(R.id.button1);
		tv = (TextView)findViewById(R.id.textView1);
		btn_headerManip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String updateText = randomCapitalization(text);
				tv.setText("Original: " + text + 
						"\nModified: " + updateText);
				connect("http://www.cc.gatech.edu");
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

	/*This method tries to create an invalid http request header
	 */
	private static String sendInvalidHeader(){
		String requestHeader = null;
		Socket clientSocket;
		String modifiedSentence = null;
		try {
			clientSocket = new Socket("ruggles.gtnoise.net", 8001);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			//			  sentence = inFromUser.readLine();


			//for generating random http headers.
			requestHeader = generateRandomWords(4);
			//Creating invalid request header
			//Creating a request that looks like this
			//XxXxX / HTTP/1.1nr
			outToServer.writeBytes(requestHeader + " / HTTP/1.1\n\r");

			modifiedSentence = "FROM SERVER: " + inFromServer.readLine();
			clientSocket.close();

		} catch (UnknownHostException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modifiedSentence;
	}

	/*This method tries to create an invalid number of 
	 * fields in the request
	 */
	private static String sendInvalidFieldCount(){
		String request = null;
		Socket clientSocket;
		String modifiedSentence = null;
		try {
			clientSocket = new Socket("ruggles.gtnoise.net", 8001);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//Test random invalid field count
			// generating a request that looks like this.
			//XxXxX XxXxX XxXxX XxXxX
			request = generateRandomWords(4) + " " + generateRandomWords(4) + " " + generateRandomWords(4) + " " + generateRandomWords(4);
			outToServer.writeBytes(request + "\n\r");

			modifiedSentence = "FROM SERVER: " + inFromServer.readLine();
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modifiedSentence;

	}

	/*
	 * Testing with the big request method
	 */
	private static String sendBigRequest(){

		String request = null;
		Socket clientSocket;
		String modifiedSentence = null;
		try {
			clientSocket = new Socket("ruggles.gtnoise.net", 8001);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//Testing random big request method.
			//generating a request that looks like this.
			//Xx*512 / HTTP/1.1
			request = generateRandomWords(1024);
			outToServer.writeBytes(request + " / HTTP/1.1\n\r");
			modifiedSentence = "FROM SERVER: " + inFromServer.readLine();
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modifiedSentence;
	}

	/*
	 * Adding an invalid version number in the HTTP request.
	 */
	private static String sendInvalidVersion(){

		float invalidVersion;
		Socket clientSocket;
		String modifiedSentence = null;
		try {
			clientSocket = new Socket("ruggles.gtnoise.net", 8001);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//for generating random http headers.
			invalidVersion = generateRandomVersion(0,9);

			//Testing the random invalid version number
			//generating a request that looks like this.
			//GET / HTTP/XxX
			outToServer.writeBytes("GET" + " / HTTP/" + invalidVersion + "\n\r");
			modifiedSentence = "FROM SERVER: " + inFromServer.readLine();
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modifiedSentence;
	}

	/*
	 * Generating random x letter word where x is passed as 
	 * a parameter
	 */
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

	
	
	//----------------Young's Test------------------
	
	public static void connect(String url) 
	{
		Map<String,String> default_headers = new TreeMap<String,String>();
		HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(url); 
		//httpget.addHeader("host", "http://www.google.com");
		httpget.addHeader("Accept-Language", "en-US,en;q=0.8");
		httpget.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpget.addHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.3");
		httpget.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)");
		Header[] headers = httpget.getAllHeaders();
		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			Log.i("Praeda",response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result= convertStreamToString(instream);
				// now you have the string representation of the HTML request
				instream.close();
			}
		} catch (Exception e) {
			Log.i("Censorship_Test-connect",e.toString());
		}
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/*
	 * Randomly capitalizes characters in a given String
	 */
    public String randomCapitalization(String input) {
    	String output = "";
    	String original_input = input;
    	for (int i = 0; i < input.length(); i++) {
    		if (getRandomBoolean()) {
    			output += swapCase(input.charAt(i));
    		} 
    		else {
    			output += input.charAt(i);
    		}
    	}
		if (original_input.equals(output)) {
			return randomCapitalization(output);			
		}
		else {
			return output;   						
		}
    }

    /*
     * Swaps a given character to upper or lower case
     */
    public static char swapCase(char ch) {
    	if (Character.isUpperCase(ch)) {
    		ch = Character.toLowerCase(ch);
    	} else if (Character.isTitleCase(ch)) {
    		ch = Character.toLowerCase(ch);
    	} else if (Character.isLowerCase(ch)) {
    		ch = Character.toUpperCase(ch);
    	}
    	return ch;
    }

    /*
     * Get a random boolean
     */
    public static boolean getRandomBoolean() {
    	Random random = new Random();
    	return random.nextBoolean();
    }
}
