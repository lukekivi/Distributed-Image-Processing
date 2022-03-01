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
    
        while (!taskRequests.isEmpty()) {
            // pop TaskRequest
            TaskRequest task = taskRequests.remove(taskRequests.size() - 1);

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
        while (!containers.isEmpty()) {
            try {
                ThreadAndRunnableContainer container = containers.remove(containers.size() - 1);
                Thread thread = container.getThread();
                TaskRequestRunnable runnable = container.getRunnable();

                thread.join();

                TaskReceipt receipt = runnable.getTaskReceipt();

                if (receipt == null) {
                     return new JobReceipt(
                        job.getJob(),
                        JobStatus.FAILURE,
                        System.currentTimeMillis() - startTime,
                        "Error occurred in an TaskRequest"
                    );
                }

                completedTasks.add(receipt);

                System.out.println("TaskReceipt:\n" + "\tPath: " + receipt.taskPath + "\n\tMsg: " + receipt.msg);

            } catch (Exception exception) {
                String localErrorMsg = "TaskRequest thread interrupted.\n" + exception;
                System.out.println(localErrorMsg);
                
                return new JobReceipt(
                    job.getJob(),
                    JobStatus.FAILURE,
                    System.currentTimeMillis() - startTime,
                    localErrorMsg
                );
            }
        }       
        
        return new JobReceipt(
            job.getJob(),
            JobStatus.SUCCESS,
            System.currentTimeMillis() - startTime,
            "All tasks completed successfully."
        );
    }
}