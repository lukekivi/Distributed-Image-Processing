# Distributed Image Processing
Created by Lucas Kivi and Charles Droeg
Last edited 2/16/2022

&nbsp; 
## Architecture Overview
---
There is a **ImageProcessingClient** who sends jobs to an **ImageProcessingServer**. The server has access to *n* **ImageProcessingNode**s. The server then logically divides the job and attempts to divide the work between the population of available nodes. The nodes who receive jobs then carry out a filtering task on some portion of the overall data. The entire process is synchronous.

Jobs are passed as **JobRequest**. Data is stored in some shared memory space and is not transferred via the socket.

&nbsp; 
## ImageProcessingClient
---
The **ImageProcessingClient** runs with a config file that contains information about the shared memory space and the machine hosting the **ImageProcessingServer**. It also has takes a directory of images as an argument. The client then makes a request to the **ImageProcessingServer** and eventually gets a **JobReceipt** confirming the work has been completed as expected.

The key jobs are:
- Send **JobRequest**s to **ImageProcessingServer**
- Receive **JobReceipt** from **ImageProcessingServer**

&nbsp; 
## ImageProcessingServer
---
The **ImageProcessingServer** receives a **JobRequest** from the **ImageProcessingClient**. It then logically divides the job into new **JobDesciption**s and sends those out to **ImageProcessingNode**s and then awaits a **JobReceipt**. The server starts a thread to communicate with each node. When a response is received the server must review it. If the job was accepted and completed it updates the main **JobReceipt** and waits for other nodes to respond. If the job was rejected then the server must make another request with a newly chosen node. Once the main **JobReceipt** is completed it is sent back to the **ImageProcessingClient**.

The key jobs are:
- Receive **JobRequest**s from **ImageProcessingClient**
- Divide labor amongst **ImageProcessingNode**s
- Manage **JobReceipt**'s returned by **ImageProcessingNode**s
- Return a main **JobReceipt** to the **ImageProcessingClient**

&nbsp; 
## ImageProcessingNodes
---
The **ImageProcessingNode**s receives a **JobRequest** from the **ImageProcessingServer**. The node should then look at its **SchedulingPolicy**. If the policy is **Mandatory** the node must accept the job. If the policy is **Optional** the node then rejects the job with a probability equal to its current load percentage. If the job is rejected it returns a **JobReceipt** indicating the rejection. If the job is accepted it then starts a thread to process the images specified in that description. The node accesses shared memory in order to get the image data it needs. The node may be processing multiple **JobRequest**s simultaniously—one per thread. Upon completing a **JobRequest** the node returns a **JobReceipt** to the **ImageProcessingServer** indicating its successful completion.

The key jobs are:
- Receive **JobRequest**s from **ImageProcessingServer**
- Process images
- Send **JobReceipt** to **ImageProcessingServer**

#### Note
For the sake of school we will be implementing an explicit *load percentage* parameter instead of a true *load balance*. This will allow the ImageProcessingNodes to behave as if they are truly experiencing variable load thus allowing us to test our load balancing technique of rejection by probability. Also, nodes will have a probability of adding a delay proportional to the *load percentage* parameter. So for each accepted job there will be a probability of a constant three second delay prior to that job being processed.

&nbsp; 
## JobRequest
---
A **JobRequest** identifies what work needs to be done. It has an array of directory paths and the array size value. This is passed from the **ImageProcessingClient** to the **ImageProcessingServer** to identify which data should be processed. It is also passed from the server to the **ImageProcessingNodes**  to identify which data it should process.

&nbsp; 
## JobReceipt
---
A **JobReceipt** is a boolean value representing whether or not the job was completed. **JobReceipt**s are passed returned to the **ImageProcessingServer** by the **ImageProcessingNode** to specify how the job was handled. They are also returned by the **ImageProcessingServer** to the **ImageProcessingClient** for the same purpose. (This is its own class right now because we might need to package other data with the status)

&nbsp; 
## Scheduling Policies
There are two scheduling policies:
1. **Mandatory**: the server chooses nodes at random for jobs and they are mandatorilly accepted.
2. **Optional**: the server chooses nodes at random for jobs and they are optionally accepted. If they are rejected the server continues to search for a node to accept the job.
---