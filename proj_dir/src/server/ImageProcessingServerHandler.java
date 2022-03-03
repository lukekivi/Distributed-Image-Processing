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

/**
 * The server interface. Handle client invokations.
 */
public class ImageProcessingServerHandler implements ImageProcessingServer.Iface {

    static private final JobRequestManager jobRequestManager = new JobRequestManager();
    private ServerNodeManager serverNodeManager;

    /**
     * Receive a job from the client. Break it into tasks and
     * send them out to nodes
     * @param job a path to a directory that contains an input_dir and output_dir
     * @return A JobReciept indicating the status of the job.
     */
    @Override
    public JobReceipt sendJob(JobRequest job) throws InvalidLocation {

        // start stopwatch
        final Long startTime =  System.currentTimeMillis();
        System.out.println("Job received..");

        /**
         * Build TaskRequests to be sent out to nodes.
         */
        jobRequestManager.setJob(job);

        ArrayList<TaskRequest> taskRequests = jobRequestManager.getTaskRequests();

        // null is the error value for getTaskRequests()
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

        // check to see if the nodes were setup successfully
        if (!serverNodeManager.isSuccessful()) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.FAILURE,
                getElapsedTime(startTime),
                "Error with config.txt and machine.txt file matching." +
                    "Make sure they contain the same amount of nodes."
            );
        }

        /**
         * Send TaskRequests
         */
        ArrayList<TaskReceipt> completedTasks = new ArrayList<TaskReceipt>();

        // containes for the thread (to join later) and the runnable (to extract data)
        ArrayList<ThreadAndRunnableContainer> containers = new ArrayList<ThreadAndRunnableContainer>();

        int numTasks = taskRequests.size();
    
        // start all runnables on threads
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
                
                // crack open the containers
                ThreadAndRunnableContainer container = containers.remove(containers.size() - 1);
                Thread thread = container.getThread();
                TaskRequestRunnable runnable = container.getRunnable();

                thread.join();

                // extract receipt from runnable
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

        // if there are no error messages things went well!
        if (errorMessages.isEmpty()) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.SUCCESS,
                getElapsedTime(startTime),
                "All tasks completed successfully."
            );

        // if there are error messages report them
        } else {

            /** 
             * build a useful error message
             */ 
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