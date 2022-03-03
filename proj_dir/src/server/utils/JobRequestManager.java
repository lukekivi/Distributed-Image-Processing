/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */
 
package server.utils;

import java.io.File;
import java.util.ArrayList;
import pa1.JobRequest;
import pa1.TaskRequest;


/**
 * Manager of JobRequests. The server can hand me a JobRequest and
 * I will disect it and tell the server what it needs to know.
 */
public class JobRequestManager {

    private static final String INPUT_DIRECTORY_NAME = "input_dir";
    private static final String OUTPUT_DIRECTORY_NAME = "output_dir";
    private static final String TASK_REQUEST_EXT = "/" + INPUT_DIRECTORY_NAME + "/";

    private JobRequest jobRequest;
    private File inputFolder;
    private String errorMsg;
    
    /**
     * Entry point for the JobRequest
     */
    public void setJob(JobRequest job) {
        System.out.println("Processing job..");

        // Refresh job information.
        jobRequest = job;
        inputFolder = null;
        errorMsg = null;

        File folder = new File(job.getJob()); 

        setInputFolder(folder); // Sets the input folder
    }

    /**
     * Grabs the error message, only set if error encountered
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Get the input_dir from a folder and set it. Does no sanity checking on
     * input_dir itself.
     * @param folder
     * @return the input_dir or null
     */
    private void setInputFolder(File folder) {

        // Checks if the file is a folder or not
        if (!folder.isDirectory()) { 
            errorMsg = "Path does not point to a directory.";
        } else {
            boolean foundInput = false;
            boolean foundOutput = false;

            // Get the contents of the folder
            File[] dirs = folder.listFiles(); 
            int i = 0;

            // iterate until finding both the output and input dirs
            while (i < dirs.length && (!foundInput || !foundOutput)) { 
                if (dirs[i].getName().equals(INPUT_DIRECTORY_NAME)) {

                    // Set input_dir, contains images
                    inputFolder = dirs[i]; 
                    foundInput = true;
                } else if (dirs[i].getName().equals(OUTPUT_DIRECTORY_NAME)) {
                    foundOutput = true;
                }
                i++;
            }

            // Assures both input and output dirs are present
            if (!foundInput || !foundOutput) { 
                inputFolder = null;
                errorMsg = "Data directory must contain an input_dir and an output_dir";
            }
        }
    }

    /**
     * Generate a list of TaskRequests from the input folder. Looks for files with
     * ".jpg" or ".png" file extensions.
     * @return a list of TaskRequests or null as an error value
     */
    public ArrayList<TaskRequest> getTaskRequests() {
        if (inputFolder == null) {
            return null;
        }

        ArrayList<TaskRequest> taskRequests = new ArrayList<TaskRequest>();
        String[] fileNames = inputFolder.list();

        for (String fileName : fileNames) {
            if (fileName.contains(".jpg") || fileName.contains(".png")) {
                taskRequests.add(new TaskRequest(jobRequest.getJob() + TASK_REQUEST_EXT + fileName));
            } else {
                System.out.println(jobRequest.getJob() + fileName + " is not a jpg or png.");
            }
        }

        return taskRequests;
    }
}
