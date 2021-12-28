import java.util.ArrayList;
import java.util.Random;

public class VideoAss {
	
	
    public static double clock; 
    public static double meanInterArrivalTime; 
    public static double meanServiceTime; 
    public static double sigma_interarrival;
    public static double sigma_service;
    

    public static int numberOfCustomers; 
    public static int queueLength; 
    public static int numberInService; // either 0 or 1
    public static int totalCustomers; 
    public static int numberOfDepartures; 
    

    public final static int arrival = 1;  //Constant value of 1 given for arrival
    public final static int departure = 2; // Constant value of 2 given for departure

    public static EventList futureEventList;  //object of EventList class is generated to store the event notice in the FEL.

    public static Queue customers;    // object of Queue class is created to add the customers in the Queue list. 

    public static Random stream;     // object of Random class is created which is a built in class in java to generate Random stream
    
    static ArrayList<Double> arrivalList = new ArrayList<>(); //Initialized 4 ArrayLists of type double to store the time stamp of each event for different customers.
    
    static ArrayList<Double> waitList = new ArrayList<>();
    
    static ArrayList<Double> serviceList = new ArrayList<>();
    
    static ArrayList<Double> systemTimeList = new ArrayList<>();
    
    
    public static void main(String argv[]) {
        meanInterArrivalTime = 50;     // Normal Distributed InterArrival time with mean given in seconds.
        meanServiceTime = 40;          // Normal Distributed Service time with mean given in seconds.
        sigma_interarrival = 10;      // standard deviation given in seconds for InterArrival time that is normally distributed.
        sigma_service = 8;            // standard deviation in seconds for Service time that is normally distributed.
        totalCustomers = 10;
        long seed = Long.parseLong("0",10);   // Seed is generated to generate a consistent random value

        stream = new Random(seed); // initialize random stream by passing the seed
        futureEventList = new EventList(); //initialized futureEventList
        customers = new Queue(); //initialized customers
        
        initialization();     // initialization method is called to set the variables and clock to 0 and schedule the first arrival. 

        // Loop until first "TotalCustomers" have departed
        while (numberOfDepartures < totalCustomers) {
            Event evt = (Event) futureEventList.getMin();  // get imminent event by getting the event from FEL with shortest time stamp
            futureEventList.dequeue();                     // remove that event from the FEL
            clock = evt.getTime();                         // update the clock time to the time of the Event
            if (evt.getType() == arrival)                  // check the Event type and call the respective method for that event
                processArrival(evt);
            else
                processDeparture(evt);
        }
        reportGeneration();          // After all the 10 customers have departed report generation method is called to generate the output        
    }
    
    
    
    public static void initialization() {
        clock = 0.0;
        queueLength = 0;
        numberInService = 0;   // no customer currently in service and server is free.
        numberOfDepartures = 0;
        
        // create first arrival event
        double interarrivalTime;         // initialized the variable for InterArrival time
        while ((interarrivalTime = normal(stream, meanInterArrivalTime, sigma_interarrival)) < 0 );  // loop until interarrival time is positive.
        Event evt = new Event(arrival,interarrivalTime);  // Generate the event with event type and event time
        futureEventList.enqueue(evt);    // add it to the FEL
    }
    
    
    public static void processArrival(Event evt) {
        customers.enqueue(evt);    // add the customer to the queue and get time of this event                 
        queueLength++;             // increase the length of the queue 
        
        arrivalList.add(evt.getTime());      // add the time of this arrival event in the arrival list
        
        // if the server is idle, fetch the event, do statistics
        // and put into service
        if (numberInService == 0)        	
        	scheduleDeparture(evt);
        
        // schedule the next arrival
        double interarrivalTime;        
        while ((interarrivalTime = normal(stream, meanInterArrivalTime, sigma_interarrival)) < 0 );
        Event nextArrival = new Event(arrival, (clock + interarrivalTime));
        futureEventList.enqueue(nextArrival);
    }
    

    public static void scheduleDeparture(Event e) {
        double ServiceTime;     // variable as double for Service Time   
        while (( ServiceTime = normal(stream, meanServiceTime, sigma_service)) < 0 );
        Event depart = new Event(departure, (clock + ServiceTime));
        futureEventList.enqueue(depart);
        numberInService = 1;                 // number of customers in server is 1 
        queueLength--;      // update the customer queue length
    }

    
    public static void processDeparture(Event e) {
        // get the customer description
        customers.dequeue();
        
        // measure the response time and add to the sum
        if(queueLength > 0) scheduleDeparture((Event)futureEventList.peek());      //schedule departure of the next customer in the FEL   
        else numberInService = 0;    // if no one in the queue set the number in service as 0
        serviceList.add(e.getTime());                // add this time in the service list
        numberOfDepartures++;    
    }   
    
    
    
    public static void reportGeneration() {
    	 waitList.add(0.0);
    	 for(int i=0; i<serviceList.size()-1; i++) {
             if(serviceList.get(i) > arrivalList.get(i+1))                          // logic for calculating the wait time.
         		waitList.add(serviceList.get(i) - arrivalList.get(i+1));
             
             else if(serviceList.get(i) < arrivalList.get(i+1))
         		waitList.add(0.0);
    	 }
    	 
    	 
    	 for(int j=0; j<serviceList.size(); j++) {	 
    		 double systemTime = serviceList.get(j) + waitList.get(j) - arrivalList.get(j);   // logic for the time spent in the system for each customer
    		 systemTimeList.add(systemTime);    // add it in the System time list 
    	 } 
    	 
        System.out.println("\t\tSINGLE-SERVER QUEUEING SYSTEM FOR THE FIRST 10 CUSTOMERS - VIDEO ASSIGNMENT SYS5110 \n");		
        
        System.out.println("\tWAITING TIME -\n                                  " );
        for(int t=0; t<waitList.size(); t++) {
        	System.out.println("\tCustomer " + (t+1) + "                                    " + waitList.get(t) + " sec");
        }
        
        System.out.println("\n");
        System.out.println("\tTOTAL SYSTEM TIME FOR EACH CUSTOMER -\n                        " );
        for(int l=0; l<systemTimeList.size(); l++) {
        	System.out.println("\tCustomer " + (l+1) + "                                    " + systemTimeList.get(l) + " sec");
        }
    }
    
    
     
    public static double SaveNormal;                       // method created for normal distribution
	public static int  NumNormals = 0;
	public static final double  PI = 3.1415927 ;

	public static double normal(Random rng, double mean, double sigma) {
	        double ReturnNormal;
	        // should we generate two normals?
	        if(NumNormals == 0 ) {
	          double r1 = rng.nextDouble();    // nextDouble is a pre-defined method to create Random values from (0-1)
	          double r2 = rng.nextDouble();
	          ReturnNormal = Math.sqrt(-2*Math.log(r1))*Math.cos(2*PI*r2);
	          SaveNormal   = Math.sqrt(-2*Math.log(r1))*Math.sin(2*PI*r2);
	          NumNormals = 1;
	        } else {
	          NumNormals = 0;
	          ReturnNormal = SaveNormal;
	        }
	        return ReturnNormal*sigma + mean ;
	 }
	}

