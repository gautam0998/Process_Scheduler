package pack;

public class initial_values {
    public static int processes = 10;                   //No. of Processes
    public static int base_interIO = 30;                //Base Inter I/O interval 
    public static int increment_interIO = 5;            //Increments
    public static int IO_time = 60;                     //Time to complete I/O
    public static int snapTime = 60000;                 //Time between each snapshot
    public static int quantum = 120;                    //Quantum for RR Scheduler
}

//All Times are in Millisecond