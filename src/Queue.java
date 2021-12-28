import java.util.LinkedList;

public class Queue extends LinkedList {                 // extended as linked list to use its properties 
	
	 public void enqueue(Object _o) {
	        add(_o);                                    // make the method to implement action when called
	    }
	    
	    public Object dequeue() {
	        return removeFirst();                        // make the getter
	    }
}
