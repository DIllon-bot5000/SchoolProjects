
import java.util.List;

/*
 * TODO: This file should contain your priority queue backed by a binary
 * max-heap.
 */
public class MaxPQ {
	private Ladder[] queue;
	private int size;
	private static final int CAPACITY = 100000;
	private int capacity;
	
	public MaxPQ() {
		// This constructor creates a new priority queue of Ladder objects 
		// with a default capacity and sets the size to 0.
		queue = new Ladder[CAPACITY];
		size = 0;
		capacity = CAPACITY;
	}
	
	public void growQueue() {
        // This method doubles the capacity of the queue and copies the elements
        // over in the event that original queue reaches max capacity.
        Ladder[] newQueue = new Ladder[capacity * 2];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }
        queue = newQueue;
        capacity *= 2;
    }
	
	public void enqueue(List<String> list, int priority) {
		// This method adds new elements to the priority queue. If 
		// the queue is empty the element is added at the front.
		// If the queue is full, the queue is expanded and then
		// the element inserted and bubbled. Otherwise the item is
		// added to end of the queue and then bubbled.
		if (size == 0) {
			Ladder arrival = new Ladder(list, priority);
			queue[1] = arrival;
			size += 1;
		}
		Ladder arrival = new Ladder(list, priority);
		if (size >= capacity) {
            this.growQueue();
            size += 1;
            queue[size] = arrival;
            bubbleUp(size);
        }
		else {
			size += 1;
			int index = size;
			if (size >= capacity) {
				this.growQueue();
			}
			queue[size] = arrival;
			bubbleUp(index);
		}
	}
	
	public List<String> dequeue() {
		// This method removes the element at the front
		// and returns the list. Afterwards, the remaining
		// items in the queue and bubbled to the correct
		// order.
		List<String> links = queue[1].input;
        queue[1] = queue[size];
        size -= 1;
        bubbleDown(1);
        return links;
    }
	
	private void bubbleUp(int position) {
		// This method checks if the element at the parent position has
		// a lower priority then the child. If so the items are swapped.
		int parentPosition = position / 2;
        int current = position;
        while (current > 1
                && queue[parentPosition].priority <= queue[current].priority) {
            swap(parentPosition, current);
            current = parentPosition;
            parentPosition = parentPosition / 2;
        }
	}
	
	 private void bubbleDown(int a) {
		 // This method checks if the child indexes have a higher priority
		 // than the parent and if so the elements are swapped until proper
		 // ordering is achieved.
		 int largest = a;
	     int leftChildIdx = 2 * a;
	     int rightChildIdx = 2 * a + 1;
	     if (leftChildIdx < size
                && queue[largest].priority <= queue[leftChildIdx].priority) {
	    	 	largest = leftChildIdx;
	        }
	     if (rightChildIdx < size
	            && queue[largest].priority <= queue[rightChildIdx].priority) {
	            largest = rightChildIdx;
	        }
	     if (largest != a) {
	         swap(a, largest);
	         bubbleDown(largest);
	        }
	        
	 }
	
	private void swap(int a, int b) {
        // This method takes two indexes from the queue and
        // swaps the objects contained at those two indexes.
        Ladder temp = queue[a];
        queue[a] = queue[b];
        queue[b] = temp;
    }
	
	public boolean isEmpty() {
		// Returns if the queue is empty or not.
		return size == 0;
	}
		
	
	public String toString() {
		// This method returns a string representation of the queue, displaying
		// all the lists and priority at each index.
		String out = "";
		for (Ladder item : queue) {
			out += "{" + item.input + item.priority + "}";
		}
		return out;
	}
	
	public class Ladder {
		// This class creates objects to hold the lists and
		// and priorities to be stored at each element.
		public List<String> input;
		public int priority;
		
		public Ladder(List<String> input, int priority) {
			// This constructor creates a new object with the passed in
			// list and priority.
			this.input = input;
			this.priority = priority;
			}
	}
}
