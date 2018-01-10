package slm;

import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.net.*;
import java.io.*;

public class BarCodeReader{
	public static String read(){
		System.out.print("Enter bar code entry : ");
		Scanner scan = new Scanner(System.in);
		String barcode = scan.nextLine();
		return barcode;
	}

	public static Book ISBN_to_book(String isbn){
		JSONParser parser = new JSONParser();
		String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn;
		String data = read_from_url(url);
		Book myBook = new Book(0,"","","",0);

		try{
			Object obj = parser.parse(data);
			JSONObject jobj = (JSONObject)obj;
			JSONObject vol_info = (JSONObject)(((JSONObject)((JSONArray)jobj.get("items")).get(0)).get("volumeInfo"));

			String title = (String)vol_info.get("title");
			String publication = (String)vol_info.get("publisher");
			String author = (String)((JSONArray)vol_info.get("authors")).get(0);
			// System.out.println(title+"\n"+author+"\n"+publication);

			myBook.update_data(0,title,author,publication,0);
		}catch(ParseException pe){
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		}
		return myBook;
	}

	public static String read_from_url(String u){
		String out = "";
		try{
			Scanner s = new Scanner(new URL(u).openStream(), "UTF-8");
			out = s.useDelimiter("\\A").next();
			s.close();
		}
		catch(MalformedURLException e){
		// Scanner#close();
		}
		catch(IOException e){

		}
		return out;
	}
}