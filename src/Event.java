
public class Event {
	
	private double time;   // Initialized the variables 
    private int type;

    public Event(int _type, double _time) {               // created a constructor to pass the values to type and time
        type = _type;                                     // make the setter
        time = _time;
    }

    public int getType() {
        return type;                                      
    }
                                                          // make the getter
    public double getTime() {
        return time;
    }

};
