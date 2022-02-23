/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droeg (droeg022)
 */

import pa1.ImageProcessingServer;
import pa1.InvalidLocation;
import org.apache.thrift.TException;

public class ImageProcessingServerHandler implements ImageProcessingServer.Iface {
    private static final String INPUT_DIRECTORY_NAME = "input_dir";
    private static final String OUTPUT_DIRECTORY_NAME = "output_dir";

    public JobReceipt sendJob(JobRequest job) throws InvalidLocation {
        final long startTime = System.currentTimeMillis();

        File folder = new File(job.getJob());

        if (!folderIsValid(folder)) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.FAILURE,
                System.currentTimeMillis() - startTime,
                "JobPath is invalid."
            );
        }

        if ((folder = getInputFolder(folder)) == null) {
            return new JobReceipt(
                job.getJob(),
                JobStatus.FAILURE,
                System.currentTimeMillis() - startTime,
                "Folder is not laid out correctly."
            );
        }

        ArrayList<TaskRequest> taskRequests = generateTaskRequests(folder);

        for (TaskRequest taskRequest : taskRequests) {
            System.out.println(taskRequest.getTask());
        }

        return new JobReceipt(
            job.getJob(),
            JobStatus.SUCCESS,
            System.currentTimeMillis() - startTime, 
            "All is well"
            );            
    }

    /**
     * @brief 
     * @param folder
     * @return
     */
    private ArrayList<TaskRequest> generateTaskRequests(File folder) {
        ArrayList<TaskRequest> taskRequests = new ArrayList<TaskRequest>();
        String[] fileNames = folder.list();

        for (String fileName : fileNames) {
            if (fileName.contains(".jpg") || fileName.contains(".png")) {
                taskRequests.add(new TaskRequest(folder.getPath() + fileName));
            } else {
                System.out.println(folder.getPath() + fileName + " is not a jpg or png.");
            }
        }

        return taskRequests;
    }

    private boolean folderIsValid(File folder) {
        if (!folder.isDirectory()) {
            return false;
        } else {
            File[] dirs = folder.listFiles();

            if (dirs.length != 2 || !dirs[0].isDirectory() || !dirs[1].isDirectory()) {
                return false;
            } else {
                boolean caseOne = dirs[0].getName().equals(INPUT_DIRECTORY_NAME) || 
                                    dirs[1].getName().equals(OUTPUT_DIRECTORY_NAME);

                boolean caseTwo = dirs[1].getName().equals(INPUT_DIRECTORY_NAME) || 
                                    dirs[0].getName().equals(OUTPUT_DIRECTORY_NAME);

                return caseOne || caseTwo;
            }
        }
    }

    private File getInputFolder(File folder) {
        File[] dirs = folder.listFiles();
        
        if (dirs[0].getName().equals(INPUT_DIRECTORY_NAME)) {
            return dirs[0];
        } else if (dirs[1].getName().equals(INPUT_DIRECTORY_NAME)) {
            return dirs[1];
        } else { 
            return null;
        }
    }
}