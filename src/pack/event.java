package pack;

    // Event Class

public class event {
    public long time = 0;                   //The time the event will occur
    public int type = 0;                    //The type of Process
    public int process_id = 0;              //ID of the process related to the event

    //type 1 = IO Interrupt
    //type 2 = IO Completion
    //type 3 == RR Interrupt

    public event(long time, int type, int process_id) {
        this.time = time;
        this.type = type;
        this.process_id = process_id;
    }
}
