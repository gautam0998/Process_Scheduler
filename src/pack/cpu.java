package pack;

//CPU class

public class cpu {
    public int val = 1;                     // To indicate if the CPU is occupied or not
    public int pid = 0;                     // The ID of the process occupying the CPU, otherwise 0
    public int utilTime = 0;                // The Time the CPU has been active

    //CPU val 1 = Free
    //CPU val 0 = Busy

    public cpu(int val, int pid) {
        this.pid = pid;
        this.val = val;
        this.utilTime = 0;
    }
}
