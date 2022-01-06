Process Scheduler (CS520) - Final Project
Author - @Gautam Agrawal

The Following is the directory Structure
- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
Meanwhile, the compiled output files will be generated in the `bin` folder by default.

The Output Files which include log files and Snapshot Files will be Created in the main directory.

The Output Files contain Millisecond by Millisecond logging of what the Process Scheduler is doing.
The Snapshot Files contain snapshots of the processes at specific times predetermined in the initial_values.java file. The Snapshot specifies the time of Snapshot along with Process Burst Time and its remaining time and what state the process was in during the snapshot time.

The FCFSScheduler.java implements the FCFS(Firsct Come First Serve) Process Scheduler.

The SJFScheduler.java implements the SJF(Shortest Job First) Process Scheduler.

The RRScheduler implements the RR(Round Robin) Process Scheduler.

All values in the initial_values.java can be changed safely as long as they make logical sense.

To run any scheduler, compile all files and then run the scheduler you want to. 

The VSCode Workspace Settings has been provided in the main directory.


The Log files have not been attached due to their large size but can be requested at the following email id
email ID: mete.zodiac0c@icloud.com
