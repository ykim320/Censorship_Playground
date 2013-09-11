package com.example.censorship_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static String text = "Hello, World";
	TextView tv;
	Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		tv = (TextView) findViewById(R.id.textView1);
		button = (Button) findViewById(R.id.button1);

		/*
		 * Button click listener and randomly changes capitalization
		 */
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	String updateText = randomCapitalization(text);
				tv.setText("Original: " + text + 
						"\nModified: " + updateText);
				connect("http://www.cc.gatech.edu");
		    }
		});
	}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
