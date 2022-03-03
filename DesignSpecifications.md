# Distributed Image Processing
Created by Lucas Kivi and Charles Droege
Last edited 3/02/2022

&nbsp; 
## Architecture Overview
---
There is a **ImageProcessingClient** who sends jobs to an **ImageProcessingServer**. The server has access to *n* **ImageProcessingNode**s. The server then logically divides the job and attempts to divide the work into tasks which are assigned to the population of available nodes. The nodes who receive jobs then carry out a image transformation task on some portion of the overall data. The nodes respond to the server with a success status. Once each task is completed the server returns a success status to the client. The entire process is synchronous.

Data is stored in some shared memory space and is not transferred via the socket. Instead, file locations are passed for jobs and tasks.

&nbsp; 
## ImageProcessingClient
---
The **ImageProcessingClient** runs without needing any arguments. It uses `Machine.txt` and `config.txt` to grab values that are necessary for the program, these are stored in the central `proj_dir` directory. `Machine.txt` contains the addresses of each node, the client, and the server. `Config.txt` contains the load-injecting probability and port for each node, the port for the server, type of scheduling policy, and the path used for the images (blank unless conducting a test which would then make it be ../tests/test10/data except the number can be changed). The client creates an instance of the ReadIn class and calls its functions to grab those important values from the `machine.txt` and `config.txt` files. The client then makes a request to the **ImageProcessingServer**. This request contains a path to a directory with two sub-directories, input_dir and output_dir, should be blank unless testing is being done. Eventually a **JobReceipt** confirming the work has been completed as expected is returned to the client.

The key jobs are:
- Use **ReadIn class** to grab addresses and important info
- Build a **JobRequest**
- Send **JobRequest**s to **ImageProcessingServer**
- Receive **JobReceipt** from **ImageProcessingServer**

The `Machine.txt` file has the following format:
- node_0 [Address]
- node_1 [Address]
- node_2 [Address]
- node_3 [Address]
- server [Address]
- client [Address]

The `Config.txt` file has the following format:
- node_0 [Probability] [Port]
- node_1 [Probability] [Port]
- node_2 [Probability] [Port]
- node_3 [Probability] [Port]
- server [Port]
- policy [Scheduling Policy]
- data [Path]

Address: address of the machine hosting the process (string)
Probability: probability used for load injection and rejection (double)
Port: port on which communication takes place (int)
Scheduling Policy: the scheduling policy to use, either random or balancing (string)
Path: path to the directory containing input_dir and output_dir (string)

&nbsp; 
## ReadIn
---
This class defines functions for each piece of information being provided in the machine.txt and config.txt file. These functions utilize the scanner class to read in those values and conduct checks on the first word of the line, making sure they align with the information being provided. This is used by the client and server to grab addresses and values specified by the user.

&nbsp; 
## ImageProcessingServer
The **ImageProcessingServer** receives a **JobRequest** from the **ImageProcessingClient**. It then logically divides the job into **TaskRequest**s and sends those out to **ImageProcessingNode**s and then awaits a **TaskReceipt**. The server starts a thread for each **TaskRequest**. When a response is received the server must review it. If the task was accepted and completed it updates the main **TaskReceipt** and waits for other nodes to respond. If the task was rejected then the server must make another request with a newly chosen node. Once the main **TaskReceipt** is completed it is sent back to the **ImageProcessingClient**.

The key jobs are:
- Receive **JobRequest**s from **ImageProcessingClient**
- Divide labor amongst **ImageProcessingNode**s
- Manage **TaskReceipt**'s returned by **ImageProcessingNode**s
- Return a main **JobReceipt** to the **ImageProcessingClient**

&nbsp; 
## ImageProcessingNodes
---
The **ImageProcessingNode**s receives a **TaskRequest** from the **ImageProcessingServer** and they set their current load percentages and **SchedulingPolicy**s via config.txt. The node should then look at its **SchedulingPolicy** and if the policy is **MANDATORY** the node must accept the job. If the policy is **OPTIONAL** the node then rejects the task with a probability equal to its current `load-inject probability`. If the task is accepted it then starts a thread to process the images specified in that description. The node accesses shared memory in order to get the image it needs. Any node may be processing multiple **TaskRequest**s simultaniously—one per thread. Upon completing a **TaskRequest** the node returns a **TaskReceipt** to the **ImageProcessingServer** indicating its success, failure, or rejection.

The key jobs are:
- Receive **TaskRequest**s from **ImageProcessingServer**
- Process images
- Send **TaskReceipt** to **ImageProcessingServer**

#### Note
For the sake of school we will be implementing an explicit *`load-inject probability`* parameter instead of a true *load balance*. This will allow the ImageProcessingNodes to behave as if they are truly experiencing variable load thus allowing us to test our load balancing technique of rejection by probability. Also, nodes will have a probability of adding a delay which is equal to the `load-inject probability` parameter. So for each accepted job there will be a probability of a constant three second delay prior to that job being processed. The probability for each node is passed in via the config file. This design decison was made because it doesn't make sense for the client or server to tell the node how much load it is experiencing.

&nbsp; 
## JobRequest
---
A **JobRequest** identifies what work needs to be done via a directory path. This is passed from the **ImageProcessingClient** to the **ImageProcessingServer** to identify which data should be processed. 

&nbsp; 
## JobReceipt
---
A **JobReceipt** contains a **JobStatus** value representing whether or not the job was completed, an Integer containing the time it took the job to complete, the directory path of the image, and a message to go with it. **JobReceipt**s are returned by the **ImageProcessingServer** to the **ImageProcessingClient**. A job can fail or succeed.

&nbsp;
## TaskRequest
---
A **TaskRequest** is an order for the **ImageProcessingNode** to process an image. The particular image is identified by a path in the request.

&nbsp;
## TaskReceipt
---
A **TaskReceipt** contains a **TaskStatus** representing the completion status of the task designated to a particular node. The task can succeed, fail, or be rejected.

&nbsp;
## NodeData
---
A **NodeData** object contains all the information needed for a node. The class also has getter functions for all of its fields. The object has 
- `String address`: address of the node
- `double prob`: load-inject probability, used to simualate load injecting or rejection of tasks
- `int port`: The port at which the node can be reached by the server
&nbsp; 
## Scheduling Policies
---
There are two scheduling policies:
1. **Mandatory**: Load-Balancing, the server chooses nodes at random for jobs and they are mandatorily accepted. Then `load-inject probability` is used to simulate a load and inject a delay, 3 seconds in our case.
2. **Optional**: Random, the server chooses nodes at random for jobs and they are optionally accepted using the `load-probability value`. If they are rejected the server continues to search for a node to accept the job. If accepted, the probability value is once again used to choose whether or not to inject a delay, 3 seconds in this case as well.

