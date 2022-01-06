package pack;

//Process Class
//Also can be considered as a PCB

public class process {
    public int id = 0;                          //Process ID     
    public int burstTime = 0;                   //The Generated Burst Time of the Process
    public int waitingTime = 0;                 //The Time the process spent waiting in the queue
    public int remainingTime = 0;               //The Time remaining for completion
    public double lambda = 0;                   //The lambda value for generation of I/O Interrupts
    public int state = 0;                       //The state of the process
    public long completionTime = 0;             //The time when the process completed

    //state 0 = Initial
    //state 1 = Ready Queue
    //state 2 = I/O Queue
    //state 3 = Running (CPU)
    //state 4 = Running (I/O)
    //state 5 = Completed

    public process(int id, int burstTime, int waitingTime, int remainingTime, double lambda) {
        this.id = id;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
        this.remainingTime = remainingTime;
        this.lambda = lambda;
        this.completionTime = 0;
    }
}

