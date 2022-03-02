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
import pa1.TaskStatus;
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
                getElapsedTime(startTime),
                jobRequestManager.getErrorMsg()
            );
        } else if (taskRequests.isEmpty()) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.SUCCESS,
                getElapsedTime(startTime),
                "Job held a directory with an empty input_dir"
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
        int numTasks = taskRequests.size();
    
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

        ArrayList<String> errorMessages = new ArrayList<String>();

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
                    errorMessages.add(
                        job.getJob() + 
                        " failed: an exception was thrown in runnable.\n"
                    );
                } else if (receipt.status == TaskStatus.FAILURE) {
                    errorMessages.add(
                        job.getJob() + 
                        "A TaskRequest failed to complete\n\t" + 
                        receipt.msg + "\n"
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
                    getElapsedTime(startTime),
                    localErrorMsg
                );
            }
        }       

        if (errorMessages.isEmpty(  )) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.SUCCESS,
                getElapsedTime(startTime),
                "All tasks completed successfully."
            );
        } else {
            String errorMsg = errorMessages.size() + "/" + numTasks + " tasks failed\n";

            while (!errorMessages.isEmpty()) {
                errorMsg += errorMessages.remove(errorMessages.size() - 1);
            }

            return new JobReceipt(
                job.getJob(),
                JobStatus.FAILURE,
                getElapsedTime(startTime),
                errorMsg
            );
        }
    }

    private Long getElapsedTime(Long startTime) {
        return System.currentTimeMillis() - startTime;
    }
}