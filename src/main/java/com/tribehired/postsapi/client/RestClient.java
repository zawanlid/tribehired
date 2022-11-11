package com.tribehired.postsapi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.context.annotation.Configuration;

/**
 * Rest client.
 * 
 * @author dil.nawaz
 *
 */
@Configuration
public class RestClient {


	public String get(String endpoint) {
		URL url = null;
		HttpURLConnection conn = null;
		String json = "";
		try {

			url = new URL(endpoint);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String line;
			
			while ((line = br.readLine()) != null) {
				json +=line;
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			conn.disconnect();
		}

		return json;
	}

	public static void main(String[] args) {

		// This method is to test git merging strategy modify lines added in 1.0 and 2.0
		String str = "";
		System.out.println(str);
	}

	public void dosome(){
		// some
		// change in connect-globalmaster. This method is to test git merging strategy
		System.out.print("");

	}
}
