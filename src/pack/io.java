package pack;

// I/O Channel

public class io {
    public int val = 1;             //To indicate if the I/O Channel is busy or not
    public int pid = 0;             //The ID of the process occuping the I/O Channel

    //IO val 1 = Free
    //IO val 0 = Busy

    public io(int val, int pid) {
        this.pid = pid;
        this.val = val;
    }
}
