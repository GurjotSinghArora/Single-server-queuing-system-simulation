import java.util.LinkedList;

public class EventList extends LinkedList {                    // extend the eventlist as linked list to use its properties.
	
	 public EventList() {
                                          // made the constructor
	    }
	    
	    public Object getMin() {        
	        return getFirst();                              // make the getter
	    }
	        
	    public void enqueue(Object _o) {
	        add(_o);                                         // make the respective methods to implement actions when called 
	    }
	    
	    public void dequeue() {
	        removeFirst();
	    }

}
