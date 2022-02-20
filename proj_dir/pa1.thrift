# Charles Droege
# droeg022
# Lucas Kivi
# kivix019

namespace java pa1

/**
 * Thrown if a job or task attempts is told to access a
 * file that doesn't exist.
 */
exception InvalidLocation {
    1: string msg,
    2: string type
}

/**
 * Work scheduling policy.
 */
enum SchedulingPolicy {
    RANDOM = 0,
    BALANCING = 1
}

/**
 * A request for processing of a batch of images. This is 
 * sent to the ImageProcessingServer.
 * -    job: path to folder containing images/folders of images.
 * - policy: work scheduling policy
 */
struct JobRequest {
    1: string job,
    2: SchedulingPolicy policy,
}

/**
 * Completion status of a JobRequest
 */
enum JobStatus {
    SUCCESS = 0,
    FAILURE = 1
}

/**
 * Receipt for a JobRequest.
 * -    time: duration of work required to complete the job
 * -  status: completion status of the job
 * - jobPath: path to folder that was worked on
 */
struct JobReceipt {
    1: i64 time,
    2: JobStatus status,
    3: string jobPath
}

/**
 * A request for the processing of a single image.
 * -   task: the path to the specific image to be processed
 * - policy: the scheduling policy for the task
 */
struct TaskRequest {
    1: string task,
    2: SchedulingPolicy policy
}

/**
 * Completion status of the task.
 */
enum TaskStatus {
    SUCCESS = 0,
    FAILURE = 1,
    REJECTED = 2
}

/**
 * Receipt for a TaskRequest.
 * - taskPath: the path of the assigned image
 * -   status: the status of the task
 */
struct TaskReceipt {
    1: string taskPath,
    2: TaskStatus status
}

/**
 * Server which receives paths to folders of images to be processed. Serves as a client to
 * a population of ImageProcessingNodes whom it sends tasks to.
 */
service ImageProcessingServer {
    JobReceipt sendJob(1:JobRequest job) throws (1:InvalidLocation error)
}

/**
 * Node, or really a server, who receives TaskRequests from the ImageProcessingServer.
 */
service ImageProcessingNode {
    TaskReceipt sendTask(1:TaskRequest task) throws (1:InvalidLocation error)
}
