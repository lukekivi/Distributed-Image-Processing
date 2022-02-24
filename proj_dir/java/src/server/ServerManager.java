package server;

import java.io.File;
import java.util.ArrayList;

import pa1.JobReceipt;
import pa1.JobRequest;
import pa1.TaskRequest;
import pa1.JobStatus;
import pa1.JobStatus;

public class ServerManager {

    private static final String INPUT_DIRECTORY_NAME = "input_dir";
    private static final String OUTPUT_DIRECTORY_NAME = "output_dir";

    private JobRequest jobRequest;
    private File inputFolder;
    private String errorMsg;
    
    public void setJob(JobRequest job) {
        System.out.println("Processing job..");

        // Refresh job information.
        jobRequest = job;
        inputFolder = null;
        errorMsg = null;

        File folder = new File(job.getJob()); 

        setInputFolder(folder);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @brief Get the input_dir from a folder and set it. Does no sanity checking on folder.
     * @param folder
     * @return the input_dir or null
     */
    private void setInputFolder(File folder) {

        if (!folder.isDirectory()) {
            errorMsg = "Path does not point to a directory.";
        } else {

            File[] dirs = folder.listFiles();

            if (dirs.length != 2 || !dirs[0].isDirectory() || !dirs[1].isDirectory()) {
                errorMsg = "Target directory does not contain exactly two directories.";
            } else {
        
                if (dirs[0].getName().equals(INPUT_DIRECTORY_NAME) || 
                    dirs[1].getName().equals(OUTPUT_DIRECTORY_NAME)) {        
                    
                    inputFolder = dirs[0];

                } else if (dirs[1].getName().equals(INPUT_DIRECTORY_NAME) || 
                        dirs[0].getName().equals(OUTPUT_DIRECTORY_NAME)) {

                    inputFolder = dirs[1];

                } else {
                    errorMsg = "Target directory does not abide by naming standards. (input_dir, output_dir)";
                }
            }
        }
    }


    /**
     * @brief Generate a list of TaskRequests from a folder. Looks for files with
     * ".jpg" or ".png" file extensions. Does no sanity checking of the folder.
     * @return a list of TaskRequests
     */
    public ArrayList<TaskRequest> getTaskRequests() {
        if (inputFolder == null) {
            return null;
        }

        ArrayList<TaskRequest> taskRequests = new ArrayList<TaskRequest>();
        String[] fileNames = inputFolder.list();

        for (String fileName : fileNames) {
            if (fileName.contains(".jpg") || fileName.contains(".png")) {
                taskRequests.add(new TaskRequest(jobRequest.getJob() + fileName));
            } else {
                System.out.println(jobRequest.getJob() + fileName + " is not a jpg or png.");
            }
        }

        return taskRequests;
    }
}
