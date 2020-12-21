import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * TODO: You will have to implement memoization somewhere in this class.
 */
public class WikiScraper {
		private static Map <String, Set<String>> memo = new HashMap<String, Set<String>>();
	/*
	 * This function takes in a String link and if the link and its associated
	 * links are not contained in the memo map calls a series of functions to find
	 * the link URL and the links on that page.
	 */
	public static Set<String> findWikiLinks(String link) {
		if (memo.containsKey(link)) {
			return memo.get(link);
		}
		else {
			String html = fetchHTML(link);
			Set<String> links = scrapeHTML(html);
			memo.put(link, links);
			return links;
		}
	}
	
	/*
	 * This method connects to the internet and gets the HTML from
	 * the URL page returned from the next method. The HTML is returned and 
	 * then searched in the scrapeHTML method.
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
}
	
	/*
	 * This function returns the URL form of the link passed in.
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/*
	 * This method recursively goes through the HTML passed in and searches for valid
	 * links. When one is found and passes the check to make sure it is valid, the page
	 * title is then stored in a set. After all the HTML is searched the set is returned.
	 */
	private static Set<String> scrapeHTML(String html) {
		Set<String> links = new HashSet<String>();
		String [] holder = html.split("<");
		
		for (String line : holder) {
			helper(line, links);
		}
		return links;
	}
	
	private static Set<String> helper(String line, Set<String> hold) {
		if (line.contains("a href=\"/wiki/")) {
			int start = line.indexOf("a href=\"/wiki/");
			String temp = line.substring(start);
			int endOfTitle = temp.indexOf("\"", start + 14);
			if (!temp.substring(start, endOfTitle).contains("#") && !temp.substring(start, endOfTitle).contains(":")) {
				hold.add(temp.substring(start + 14, endOfTitle));
				helper(temp.substring(endOfTitle), hold);
			}
			
		}
		return hold;
	}
	
}
