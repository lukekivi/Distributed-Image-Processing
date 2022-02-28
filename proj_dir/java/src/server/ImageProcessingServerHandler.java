/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droeg (droeg022)
 */
package server;

import java.util.ArrayList;
import pa1.ImageProcessingServer;
import pa1.InvalidLocation;
import pa1.JobReceipt;
import pa1.JobRequest;
import pa1.JobStatus;
import pa1.TaskReceipt;
import pa1.TaskRequest;
import server.utils.JobRequestManager;
import server.utils.ServerNodeManager;
import server.utils.ThreadAndRunnableContainer;


public class ImageProcessingServerHandler implements ImageProcessingServer.Iface {

    static private final JobRequestManager jobRequestManager = new JobRequestManager();
    private ServerNodeManager serverNodeManager;

    @Override
    public JobReceipt sendJob(JobRequest job) throws InvalidLocation {
        final Long startTime =  System.currentTimeMillis();
        System.out.println("Job received..");

        /**
         * Build TaskRequests to be sent out to nodes.
         */
        jobRequestManager.setJob(job);

        ArrayList<TaskRequest> taskRequests = jobRequestManager.getTaskRequests();

        if (taskRequests == null) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.FAILURE,
                System.currentTimeMillis() - startTime,
                jobRequestManager.getErrorMsg()
            );
        }

        /**
         * Setup nodes
         */
        serverNodeManager = new ServerNodeManager();

        /**
         * Send TaskRequests
         */
        ArrayList<TaskReceipt> completedTasks = new ArrayList<TaskReceipt>();
        ArrayList<ThreadAndRunnableContainer> containers = new ArrayList<ThreadAndRunnableContainer>();
    
        for (int i=0; i < taskRequests.size(); i++) {
            // pop TaskRequest
            TaskRequest task = taskRequests.remove(i);

            // build a runnable
            TaskRequestRunnable runnable = new TaskRequestRunnable(
                serverNodeManager,
                task
            );

            // build a thread
            Thread thread = new Thread(runnable);
            thread.start();

            // build a container for the runnable/thread pair
            ThreadAndRunnableContainer container = new ThreadAndRunnableContainer(
                thread,
                runnable
            );

            containers.add(container);
        }

        /**
         * Check to see that threads completed successfully.
         */
        for (int i = 0; i < containers.size(); i++) {
            try {
                ThreadAndRunnableContainer container = containers.remove(i);
                Thread thread = container.getThread();
                TaskRequestRunnable runnable = container.getRunnable();

                thread.join();

                TaskReceipt receipt = runnable.getTaskReceipt();

                completedTasks.add(receipt);

                System.out.println("TaskRecipt " + i + ":\n" + "\tPath: " + receipt.taskPath +
                            "\n\tMsg: " + receipt.msg);
            } catch (InterruptedException exception) {
                System.out.println("TaskRequest " + i + " thread interrupted.");
                System.exit(1);
            }
        }       
    
        return new JobReceipt(
            job.getJob(),
            JobStatus.SUCCESS,
            System.currentTimeMillis() - startTime,
            "All is well."
        );
    }
}