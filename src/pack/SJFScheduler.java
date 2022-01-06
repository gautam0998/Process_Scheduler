package pack;
import java.util.Arrays;
import java.util.LinkedList;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.*;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;

//Shortest Job First Process Scheduler

public class SJFScheduler {

    public static process[] process_list = new process[initial_values.processes];               // The list of all Processes
    public static LinkedList<event> event_List = new LinkedList<event>();                       // The linked list of all the events
    public static LinkedList<process> ready_queue = new LinkedList<process>();                  // The list representing the Ready Queue for CPU
    public static LinkedList<process> IO_queue = new LinkedList<process>();                     // The list representing the IO Queue to IO Channel
    public static int[] completion_list = new int[initial_values.processes];                    // The array representing the processes which have completed

    //completion_list 0 = incomplete
    //completion_list 1 = complete

    public static File log = new File("log_SFJ.txt");                                          // Creation of File element for logging

    public static cpu cpu = new cpu(1, 0);                                                      // CPU element with free state

    public static io io = new io(1, 0);                                                         // IO Element with free state

    public static long systemClock = 0;                                                         // System Clock initialised to 0

    public static File ss = new File("Snapshot_SJF.txt");                                      //Creation of File Element for Snapshot
    

    public static void log_creator(int a) {                                       //Creating log file
        try {
            if (log.exists()==false) {
                log.createNewFile();
            }

            else {
                try {
                    /*PrintWriter logger = new PrintWriter(new FileWriter(log, false));           //To overwrite any existing file with same name
                    logger.write(" ");
                    logger.close();*/
                    log = new File("log_SJF_"+(a+1)+".txt");                                        //To Change and Try new file name
                    log_creator(a+1);
                }
        
                /*catch (IOException e) {
                    System.out.println("System Exception: " + e);
                }*/

                finally {}
            }

        } catch (Exception e) {
            System.out.println("Exception: "+e);
        }
    }

    public static void logging(CharSequence s) {                             //Log Writer
        try {
            PrintWriter logger = new PrintWriter(new FileWriter(log, true));
            logger.append(s);
            logger.close();
        } 
        catch (IOException e) {
            System.out.println("System Exception: " + e);
        }
    }
    
    public static void ss_creator(int a) {                                       //Creating Snapshot file
        try {
            if (ss.exists()==false) {
                ss.createNewFile();
            }

            else {
                try {
                    /*PrintWriter logger = new PrintWriter(new FileWriter(log, false));           //To overwrite any existing file with same name
                    logger.write(" ");
                    logger.close();*/
                    ss = new File("Snapshot_SJF_"+(a+1)+".txt");                                        //To Change and Try new file name
                    ss_creator(a+1);
                }
        
                /*catch (IOException e) {
                    System.out.println("System Exception: " + e);
                }*/

                finally {}
            }

        } catch (Exception e) {
            System.out.println("Exception: "+e);
        }
    }

    public static void snaping(CharSequence s) {                             //Snapshot Writer
        try {
            PrintWriter snapper = new PrintWriter(new FileWriter(ss, true));
            snapper.append(s);
            snapper.close();
        } 
        catch (IOException e) {
            System.out.println("System Exception: " + e);
        }
    }

    public static CharSequence processState(int a) {                            //To convert Process State to String
        CharSequence t = "";
        if (a == 0) {
            t = "Initial State";
        }

        else if (a == 1) {
            t = "In Ready Queue";
        }

        else if (a == 2) {
            t = "In I/O Queue";
        }

        else if (a == 3) {
            t = "Using CPU";
        }

        else if (a == 4) {
            t = "Using I/O Channel";
        }

        else if (a == 5) {
            t = "Process Completed";
        }

        return t;
    }

    public static void snapshot() {                                             //To take Snapshot of Processes
        snaping("Snapshot at Time: "+systemClock);
        for (int i = 0; i < process_list.length; i++) {
            snaping("\nFor Process: "+process_list[i].id);
            snaping("\nBurst Time: "+process_list[i].burstTime);
            snaping("\tRemaining Time: "+process_list[i].remainingTime);
            snaping("\tProces State: "+processState(process_list[i].state));
            //snaping("\n");
        }
        snaping("\n\n");
    }

    public static void initialize() {                                                       //Initialize needed Processes
        int base_id = 10001;                                                                //Process ID
        for (int i = 0; i < initial_values.processes; i++) {
            int k = initial_values.base_interIO + initial_values.increment_interIO*i;           //Mean I/O Time
            int t = getBurstTime();                                                             //Burst Time
            process_list[i] = new process(base_id+i, t, 0, t, (double)1/k);                     //adding to Process List

            //ready_queue.add(process_list[i]);
            addproc(i);                                                                         //Adding to Ready Queue
            process_list[i].state = 1;                                                      //Changing the Process State

            logging("\n"+systemClock+": Processes Initialized with ID: "+process_list[i].id);
        }

        for (int i = 0; i < completion_list.length; i++) {
            completion_list[i] = 0;
        }

    }

    public static double getIOtime(double lambda) {                             //get IO Time
        Random rand = new Random();
        double temp = -1*(Math.log(1 - rand.nextDouble())/(lambda));            //Exponentially Random IO Time
        //System.out.println("lambda value = "+lambda);
        //System.out.println("temp value = " +temp);
        return temp;
    }

    public static boolean checkCompletion() {                                       //To check if all processes ae completed
        boolean t = true;
        for (int i = 0; i < completion_list.length; i++) {
            if(completion_list[i]<=0) {
                t = false;
            }
        }
        if (t) {
            System.out.println("All Processes Completed");
        }
        return t;
    }

    public static void IOgenerator (process p1) {                                   //IO Interrupt generator
        long t = (long)getIOtime(p1.lambda);
        while (t<1) {                                                               //To make sure the Interrupt time is not less that 1 ms
            t = (long)getIOtime(p1.lambda);
        }
        event e1 = new event(systemClock+t, 1, p1.id);                                                  //Add Interrupt to event list
        addEvent(e1);
        logging("\n"+systemClock+": IO Interrupt event generated for time: "+(systemClock+t));
        //System.out.println("Event Time: "+e1.time);
    }
    
    public static int getBurstTime() {                              //Generate Burst Time
        Random rand = new Random();                                     //Uniformly distributed Random Number
        int temp = 0;
        while(true) {
            if(temp<120000) {                                           //To make sure it is greater than 2 minutes
                temp = rand.nextInt(240001);                            //To make sure it is less than 4 minutes
            }
            else{
                break;
            }
        }
        return temp;
    }

    public static void increaseWaiting(int p1) {                                                //Increase the waiting time of the process except p1
        for (int i = 0; i < process_list.length; i++) {
            if (i!=p1 && (process_list[i].state == 1 || process_list[i].state == 2)) {                      //Processes which are not p1 and are either in ready queue or I/O queue
                process_list[i].waitingTime += 1;
            }
        }
    }
    
    public static int searchPID(int pid) {                                                          //To search the index of process in process list with the give ID
        int t = -1;
        for (int i = 0; i < process_list.length; i++) {
            if(process_list[i].id == pid) {
                t = i;
                break;
            }
        }
        return t;
    }
   
    public static void ioInterrupt(event e1) {                                      //To handle IO Interrupt
        int p1 = searchPID(e1.process_id);
        IO_queue.add(process_list[p1]);                                                     //Add to IO Queue
        process_list[p1].state = 2;                                                         //Change Process State
        cpu.val = 1;                                                                        //Free Up CPU
        cpu.pid = 0;
        //System.out.println("IO Interrupt");
   }

    public static void eventIOCompletion(process p1) {                                                 //To generate IO Completion Event                    
       event e1 = new event(systemClock+60, 2, p1.id);                                                  //Generate Event
       addEvent(e1);                                                                                    //add event to event list
       logging("\n"+systemClock+": IO Completion Event Generated at: "+(systemClock+60));               //log it
       //System.out.println("Event added for IO Completion for time: "+(systemClock+60));
   }

    public static void ioCompletion(event e1) {                         //To handle IO Completion Event
        int f = searchPID(e1.process_id);
        //ready_queue.add(process_list[f]);
        addproc(f);                                                                 //Add the process back to ready queue
        process_list[f].state = 1;                                                  //Change state of process
        //System.out.println("IO Completed");
        io.val = 1;                                                                 //Free Up I/O Channel
        io.pid = 0;
   }

   public static void addEvent(event e1) {                                              //Add Event to Event List
       long t = e1.time;

       if (event_List.size()==0) {                                                      //If event list is empty then add the event
           event_List.add(e1);
           return;
       }

       for (int i = 0; i < event_List.size(); i++) {
           if (t < event_List.get(i).time && i==0) {                                             //If the event is to be added at starting
               event_List.add(0, e1);
               break;
           }

           if (t < event_List.get(i).time) {                                                        //Adding the event in the middle
               event_List.add(i, e1);
               break;
           }

           if (i == event_List.size()-1) {                                                            //If the event is to be added at last
               event_List.addLast(e1);
               break;
           }

       }
   }
   
   public static void printStates() {                                                       //To print the Process States
       int[] states = new int[process_list.length];
       for (int i = 0; i < process_list.length; i++) {
           states[i] = process_list[i].state;
       }

       //System.out.println("The process states are: "+Arrays.toString(states));
   }

   public static void printEventTimes() {                                           //To print the Event Times of events in event list
    long[] times = new long[event_List.size()];
    for (int i = 0; i < event_List.size(); i++) {
        times[i] = event_List.get(i).time;
    }

    System.out.println("The Event Times are: "+Arrays.toString(times));
}
   
    public static void printReadyQueue() {                                                  //To print the Ready Queue
        int[] temp = new int[ready_queue.size()];
        for (int i = 0; i < ready_queue.size(); i++) {
            temp[i] = ready_queue.get(i).id;
        }
        logging("\nThe Ready Queue is: "+Arrays.toString(temp));
        logging("\n");
    }

    public static void procComplete(int p1) {                                               //To handle a process completion    
        int p = process_list[p1].id;
        int t = event_List.size();
        int i = 0;

        process_list[p1].completionTime = systemClock;                                                  //Set Process Completion Time
        while (i<t) {                                                                                       //To remove all events related to the process from event list
            if (event_List.get(i).process_id == p) {
                event_List.remove(i);                                                                           //remove from event list
                t=event_List.size();
            }
            else{
                i += 1;
            }
        }
    }
   
    public static void addproc(int p1) {                                                            //Add Process to Ready Queue
        if (ready_queue.size() == 0) {
            ready_queue.add(process_list[p1]);                                                          //Add Process at starting of queue since empty
            logging("\n Process added at starting. \n");
            return;
        }
        for (int i = 0; i < ready_queue.size(); i++) {
            if (ready_queue.get(i).burstTime > process_list[p1].burstTime && i==0) {                        //Add at starting since Shortest Burst Time
                ready_queue.add(0, process_list[p1]);
                logging("\n Process added at starting. \n");
                break;
            }

            if (ready_queue.get(i).burstTime > process_list[p1].burstTime) {                                    //Add Somewhere in between
                ready_queue.add(i, process_list[p1]);
                logging("\n Process added Somewhere in between \n");
                break;
            }

            if (i == ready_queue.size()-1) {                                                                        //Add at last
                ready_queue.addLast(process_list[p1]);
                logging("\n Process added at last \n");
                break;
            }
        }
    }
   public static void main(String[] args) throws Exception{
        boolean flag = true;                                                                    //Flag for the while loop
        long avgTurnaround = 0;                                                                 //Average Turnaround Time initialized
        int avgWaiting = 0;                                                                     //Average Waiting Time initialized

        log_creator(1);                                                                         //Creating Log File

        ss_creator(1);                                                                      //Creating Snapshot File

        initialize();                                                                           //Initialize Processes

        /*for (int i = 0; i < process_list.length; i++) {
            System.out.println("Process id: "+process_list[i].id);
        }*/

        while(flag) {                                                                           //While loop to go through System Clock
            int tempVal = -1;                                                                   //For IncreaseWaiting
            systemClock += 1;                                                                   //Increment System Clock
            //System.out.println("System Time: "+systemClock);
            //printReadyQueue();
            if (event_List.size()!=0 && event_List.getFirst().time == systemClock) {                        //If there is an event with the current System Clock
                while(event_List.size()!=0 && event_List.getFirst().time == systemClock) {                  //To Handle all events with the same Time
                    event e1 = event_List.poll();                                                           //Get the event
                    //System.out.println("Handling IO event");

                    if (e1.type == 1) {                                                                     //IO Interrupt
                        logging("\n"+systemClock+": IO Interrupt for Process: "+e1.process_id);
                        ioInterrupt(e1);
                    }

                    else {                                                                                      //IO Completion
                        logging("\n"+systemClock+": IO Completion for Process: "+e1.process_id);
                        ioCompletion(e1);
                    }
                }
                //tempVal = -1;
                //continue;
            }

            if (cpu.val == 1 && ready_queue.size()>0) {                                                     //If CPU is Free and Ready Queue is not empty
                process p1 = ready_queue.poll();                                                            //Get Process from Ready Queue
                logging("\n"+systemClock+": Process assigned CPU: "+p1.id);
                int pi = searchPID(p1.id);
                cpu.val = 0;                                                                                 //Indicate that CPU is occupied
                cpu.pid = p1.id;
                cpu.utilTime += 1;                                                                              //Increase Util Time of CPU
                process_list[pi].state = 3;                                                                     //Change state of Process
                IOgenerator(p1);                                                                                //Generate IO Event for the process
                tempVal = pi;
                //System.out.println("Addded process");
            }

            else if (cpu.val == 0) {                                                                            //If CPU is Occupied
                logging("\n"+systemClock+": CPU busy with Process: "+cpu.pid);
                int p1 = searchPID(cpu.pid);
                process_list[p1].remainingTime -= 1;                                                            //Decrease the Remaining Time of Process
                cpu.utilTime += 1;                                                                              //Increase CPU Utilization Time
                if (process_list[p1].remainingTime <= 0) {
                    process_list[p1].state = 5;                                                                  //Change state to completed
                    completion_list[p1] = 1;                                                                        //Edit Completion List
                    procComplete(p1);                                                                               //Complete Process
                    System.out.println(systemClock+": Process Completed with ID: "+cpu.pid);
                    logging("\n"+systemClock+": Process Completed with ID: "+cpu.pid);
                    cpu.val = 1;                                                                                    //Indicate CPU Free
                    cpu.pid = 0;
                }
                //increaseWaiting(p1);
                tempVal = p1;
                //System.out.println("CPU occupied: "+systemClock);
            }

            else {                                              //For when CPU is Free and No process in Ready Queue
                tempVal = -1;                                           //Increase waiting time of all
            }

            if (io.val == 1 && IO_queue.size()>0) {                                 // I/O Channel is free and I/O Queue is not empty
                process p1 = IO_queue.poll();
                logging("\n"+systemClock+": Process assigned IO: "+p1.id);
                int pi = searchPID(p1.id);
                io.val = 0;                                                                     // Indicate I/O is busy
                io.pid = p1.id;                                                                  // Add Process ID
                process_list[pi].state = 4;                                                     // Change State
                eventIOCompletion(p1);                                                              // Generate I/O Completion event
            }

            if (systemClock%initial_values.snapTime == 0) {                             //To take Snapshot
                snapshot();
            }

            increaseWaiting(tempVal);                                                               // Increase Waiting
            flag = !checkCompletion();                                                              //Check if all processes are completed
            //printStates();
            //printEventTimes();
            //System.out.println("The Completion List is: "+Arrays.toString(completion_list));
        }

        /*int ti = 0;
        for (int i = 0; i < process_list.length; i++) {
            ti += process_list[i].burstTime;
        }*/

        logging("\n"+systemClock+": All Processes Finished!");
        //System.out.println("Total Burst time of processes: " + ti);


        //System.out.println("The Throughput is: "+((double)systemClock/initial_values.processes));
        logging("\n\n\nThe Throughput is: "+((double)systemClock/initial_values.processes));                    // Calculate Throughput and Log

        //System.out.println("The CPU Utilization is: "+((double)cpu.utilTime/systemClock)*100);
        logging("\nThe CPU Utilization is: "+((double)cpu.utilTime/systemClock)*100);                               // Calculate CPU Utilization and Log

        for (int i = 0; i < initial_values.processes; i++) {
            avgTurnaround += process_list[i].completionTime;
        }

        for (int i = 0; i < initial_values.processes; i++) {
            avgWaiting += process_list[i].waitingTime;
        }

        //System.out.println("The Average Turnaround time is: "+((double)avgTurnaround/initial_values.processes));
        logging("\nThe Average Turnaround time is: "+((double)avgTurnaround/initial_values.processes));                                         // Calculate Average Turnaround and Log

        //System.out.println("The Average Waiting time is: "+((double)avgWaiting/initial_values.processes));    
        logging("\nThe Average Waiting time is: "+((double)avgWaiting/initial_values.processes));                                               // Calculate Average Waiting Time

        snapshot();
    }
}