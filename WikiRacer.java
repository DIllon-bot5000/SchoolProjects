import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WikiRacer extends WikiScraper {

	/*
	 * Do not edit this main function
	 */
	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/*
	 * Do not edit the method signature/header of this function
	 * TODO: Fill this function in.
	 */
	private static List<String> findWikiLadder(String start, String end) {
		// This method does what the pseudocode describes in the spec, creates a 
		// Max heap priority queue, takes the start page and creates a List<String>
		// with it and enqueues it. While the queue is
		// not empty it then dequeues the List and finds all the links
		// on the page and creates new lists as potential paths to the end page. These
		// lists are then added to the priority queue.
		// If the end page is found the ladder containing the path is returned otherwise
		// an empty list is returned.
		
		MaxPQ queue = new MaxPQ();
		
		List<String> startPage = new ArrayList<String>();
		Set <String> endLinks = findWikiLinks(end);
		Set <String> searchedLinks = new HashSet<String>();
		
		startPage.add(start);
		queue.enqueue(startPage, 0);
		
		while (queue.isEmpty() != true) {
			List<String> current = queue.dequeue();
			Set <String> links = findWikiLinks(current.get(current.size() - 1)); 
			if (links.contains(end)) {
				current.add(end);
				return current;
			}
			
			links.parallelStream().forEach(link -> {
			WikiScraper.findWikiLinks(link);
			});
			
			for (String link : links) {
				if (searchedLinks.contains(link) == false) {
					searchedLinks.add(link);
					List<String> temp = new ArrayList<String>();
					temp.addAll(current);
					temp.add(link);
					queue.enqueue(temp, findPriority(link, endLinks));
				}
				}
			}
		
		return new ArrayList<String>();
	}
	
	private static int findPriority(String temp, Set <String> end) {
		// This method takes the String name of a page and all the links associated
		// with the end page. It finds the links on the page and then finds
		// the number of links in common with the end page and returns
		// that value.
		Set <String> neighborLinks = findWikiLinks(temp);
		Set <String> common = new HashSet<String>(end);
		common.retainAll(neighborLinks);
		return common.size();
	}

}
