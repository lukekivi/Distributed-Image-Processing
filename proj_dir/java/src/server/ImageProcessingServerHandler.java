/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droeg (droeg022)
 */
package server;

import java.util.ArrayList;
import java.io.File;
import pa1.JobReceipt;
import pa1.JobRequest;
import pa1.TaskRequest;
import server.ServerManager;
import pa1.InvalidLocation;
import pa1.ImageProcessingServer;
import pa1.JobStatus;
import org.apache.thrift.TException;

public class ImageProcessingServerHandler implements ImageProcessingServer.Iface {
    static private final ServerManager serverManager = new ServerManager();

    public JobReceipt sendJob(JobRequest job) throws InvalidLocation {

        System.out.println("Job received..");

        final Long startTime =  System.currentTimeMillis();

        serverManager.setJob(job);

        ArrayList<TaskRequest> taskRequests = serverManager.getTaskRequests();

        if (taskRequests == null) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.FAILURE,
                System.currentTimeMillis() - startTime,
                serverManager.getErrorMsg()
            );
        }

        for (TaskRequest taskRequest : taskRequests) {
            System.out.println(taskRequest.getTask());
        }

        return new JobReceipt(
            job.getJob(),
            JobStatus.SUCCESS,
            System.currentTimeMillis() - startTime,
            "All is well."
        );
    }
}