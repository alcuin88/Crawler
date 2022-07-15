import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Crawler {
	
	public static JSONObject result = new JSONObject();			
	public static JSONArray values = new JSONArray();
	
	public static void main(String[] args) {
		String jsonUrl = "{\r\n"
				+ "\"urls\": [\"https://google.com\"]\r\n"
				+ "}";
		
		
		JSONObject obj = new JSONObject(jsonUrl);
		JSONArray url = obj.getJSONArray("urls");
		
		
		for(int i = 0; i < url.length(); i++) {
			crawl(1, url.get(i).toString(), new ArrayList<String>());
		}
		result.put("result", values);						
		System.out.println(result);
	}

	private static void crawl(int level, String url, ArrayList<String> visited) {
		Document doc;
		if(level <= 5) {
			doc = request(url, visited);
			if(doc != null) {
				for(Element link : doc.select("a[href]")) {
					String next_link = link.absUrl("href");
					if(visited.contains(next_link) == false) {
						crawl(++level, next_link, visited);
					}
				}
			}
		}
	}
	
	private static Document request(String url, ArrayList<String> visited) {
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();
			
			if(con.response().statusCode() == 200) {
				JSONObject data = new JSONObject();
				data.put("url", url);
				data.put("data", doc.data());
				values.put(data);
				visited.add(url);
				return doc;
			}
			return null;
		}
		catch(IOException e) {
			return null;
		}
	}
}
