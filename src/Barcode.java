package slm;

import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.net.*;
import java.io.*;


class BookNotFoundException extends Exception{
			
}
class ConnectionException extends Exception{
			
}
public class Barcode{

	public static Book ISBN_to_book(String isbn) throws BookNotFoundException,ConnectionException{
		JSONParser parser = new JSONParser();
		String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn;

		System.out.println("Trying to connect to "+url);
		String title = "";
		String publication = "";
		String author = "";

		try{
			String data = read_from_url(url);

			try{
				Object obj = parser.parse(data);
				JSONObject jobj = (JSONObject)obj;
				
				System.out.println("Total items found is "+(Long)jobj.get("totalItems"));

				if((Long)jobj.get("totalItems")==0){
					throw new BookNotFoundException();
				}
				else
				{
					JSONObject vol_info = (JSONObject)(((JSONObject)((JSONArray)jobj.get("items")).get(0)).get("volumeInfo"));

					JSONArray authors = (JSONArray)vol_info.get("authors");

					author = String.join(", ", authors);
					title = (String)vol_info.get("title");
					publication = (String)vol_info.get("publisher");
					// System.out.println(title+"\n"+author+"\n"+publication);
				}
			}catch(ParseException pe){
				System.out.println("position: " + pe.getPosition());
				System.out.println(pe);
			}
			catch(NullPointerException e){
				System.out.println("Error while parsing data from the internet");	
			}
		}
		catch(IOException e){
			throw new ConnectionException();
		}
		Book myBook = new Book(0,isbn,title,author,publication,0);
		return myBook;
	}

	public static String read_from_url(String u) throws IOException{
		String out = "";
		try{
			Scanner s = new Scanner(new URL(u).openStream(), "UTF-8");
			out = s.useDelimiter("\\A").next();
			s.close();
		}
		catch(MalformedURLException e){
			System.out.println(e.toString());	
		}
		catch(IOException e){
			System.out.println(e.toString());
			throw e;
		}
		return out;
	}
}