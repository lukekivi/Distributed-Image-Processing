# Distributed-Image-Processing
**Created by**:\
Lucas Kivi - kivix019 \
Charles Droege - droeg022 
---
An application for processing image files in a distributed, synchronous manner.

## Table of Contents
* **Running**
    1. Environment
    2. Machines
    3. Commands
    4. Nodes
    5. Server
    6. Client
    7. Scheduling Policy
    8. Data
* **Tests**

## Project Structure

Pre build:
```
Distributed-Image-Processing
|-- proj_dir
|   |-- input_dir
|   |-- src
|   |   |-- client
|   |   |-- node
|   |   |-- server
|   |   |-- utils
|   |-- build.xml
|   |-- commands.txt
|   |-- config.txt
|   |-- env.txt
|   |-- machine.txt
|   |-- pa1.thrift
|-- tests
|-- DesignSpecifications.md
|-- README.md
|-- ssh_cleanup.txt
|-- ssh_commands.txt
```
Upon running any `ant` targets there will be new directories within `proj_dir`:
```
proj_dir
|-- build	<-- 
|-- gen-java	<-- 
|-- input_dir
|-- log	        <--
|-- output_dir	<--
|-- src
|-- build.xml
|-- commands.txt
|-- config.txt
|-- env.txt
|-- machine.txt
|-- pa1.thrift
```

# Running
This description is for running this app on machines with **shared memory space**. If you are not using shared memory space this setting up all entites in the system will be complicated and we will not detail that process.

## Environment
In order to run this application you must fulfill the mandatory OpenCV and Thrift dependencies.

In order for the application to resolve your dependencies set the these environment variables:

> THRIFT_LIB_PATH /\<path-to-thrift-libs/> \
> OPENCV_LIB_PATH /\<path-to-opencv-jar/> \
> PROJ_PATH /\<path\>/proj_dir 

## Machines
Being that the application is a distributed system we have a `machine.txt` document. This file contains the addresses for each node, server, and client. This file is used by the application to locate the address at which the nodes, server, and client are operating.

Feel free to change machines however do not disturb the identifiers at the beginning of each line as they are required by the application.

```
node_0 csel-kh4250-20.cselabs.umn.edu
node_1 csel-kh4250-21.cselabs.umn.edu
node_2 csel-kh4250-22.cselabs.umn.edu
node_3 csel-kh4250-23.cselabs.umn.edu
server csel-kh4250-24.cselabs.umn.edu
client csel-kh4250-25.cselabs.umn.edu
```

## Running the Submission via AutoGrader
* Follow **Environment** step above^
* Place the `grader.sh` file within the `proj_dir`
* I had to explicitly set the `USERID` variable within the autograder in order for it to work.
* Verify that the input_dir has the correct images in it. Duplicates can be found in `tests/test01`
* Run the autograder.
* Output can be found in the logs found in the `log` directory
* if you need to change ports use the `config.txt`. Details about that are found in the **Configuration Details** section below
* If you run our tests via the autograder just know that ouput is directed to the test diretory and will not be picked up by the autograder.

## Running the Submission our way
* Open up 6 terminals and run the commands found in ssh_commands.txt in order. Be sure the machines correspond to the machines in `machine.txt` and that you wait for the server and nodes to start prior to running the client.
* View output in `log` directory
* Run `source ssh_cleanup.sh` to close all processes on the machines. Once again, be sure the machines correspond to the same machines in `machine.txt`/`ssh_command.txt`.
* To change run configuration simply alter the `config.txt` file or replace the existing one with a `config.txt` file from one of our tests. Run the `ssh_cleanup.sh` file and then restart all machines in your six terminals as outlined in the first bullet.

## Configuration Details
The `config.txt` file contains configuration details for each entity in the progam. This is where nodes get their load percentage and port number and the server gets its port number. This is also where the load injection policy is set from. Finally, the data path is set here too. Read more about the data attribute in the **Data** section below. If you change the config file you must restart all entities for the effect to take place.

```
node_0 0.8 8125
node_1 0.6 8126
node_2 0.5 8127
node_3 0.2 8128
server 8129
policy random
data
```

## Commands
`command.txt` contains commands for starting app entities.

If you change a machine address in `machine.txt` be sure to adjust the associated command in `command.txt` to reflect the change in address.

## Nodes
Nodes depend on a scheduling policy that is outlined in the **Scheduling Policy** section below.

On the correct host machine (as declared in machine.txt) navigate to: `Distributed-Image-Processing/proj_dir` and run 1 of 4 commands:
 - ```ant node_zero```
 - ```ant node_one```
 - ```ant node_two```
 - ```ant node_three```
---

You can also run the node commands that reside in ssh_commands.txt. This will automatically ssh into a kh4250 lab machine and start up the specific node you select.

---

Be sure the `machine.txt` file reflects the proper address of the machine you are running the specific node from.

 The `machine.txt` entry syntax for nodes looks like this:
 ```
 node_<num> <machine address>
 ```
 Example:
 ```
 node_0 csel-kh4250-08.cselabs.umn.edu
 ```

Remember load probability simulates the probability of injecting a delay no matter the scheduling policy . It also simulates the probability of rejection when using the `Random` scheduling policy. More details in `DesignSpecifications.md`. Also take  note that the machine.txt file reads the nodes as 'node_2' while the command to run it will be ant 'node_two'.

## Server
 
On the correct host machine (as declared in machine.txt) navigate to: `Distributed-Image-Processing/proj_dir` and run:
 - ```ant server```
---

You can also run the server command that resides in ssh_commands.txt. This will automatically ssh into a kh4250 lab machine and start up the server.

---
Be sure the `machine.txt` file reflects which machine you are going to run the server on.

 The `machine.txt` entry syntax for nodes looks like this:
 ```
 server <machine address>
 ```
 Example:
 ```
 server csel-kh1260-12.cselabs.umn.edu

 ```
 


## Client
 
On the correct host machine (as declared in machine.txt) navigate to: `Distributed-Image-Processing/proj_dir` and run:
 - ```ant client```
---

You can also run the client command that resides in ssh_commands.txt. This will automatically ssh into a kh4250 lab machine and start up the client.

---
Be sure the `machine.txt` file reflects which machine the client is going to run on.

 The `machine.txt` entry syntax for nodes looks like this:
 ```
 client <machine address>
 ```
 Example:
 ```
 client csel-kh4250-01.cselabs.umn.edu

 ```
 
## Server, Client, and Node Output
 
We have inserted print statements throughout the code to help bring life to the process. Instead of printing to the terminal, each server, client, and node will get their own sepcific log.txt file to record their output. These will be titled `server_log.txt`, `client_log.txt`, and `node_<num>_log.txt` where num is the number of the node it represents. We can look at the statements printed to these files and see more details on what each machine is doing. Examples of this include printing out messages if a task is rejected or a delay is implemented (three second sleep). The final JobReceipt that the client receives will also be recorded in its respective client_log.txt file.
 
--- 
 
## Scheduling Policy
Currently two scheduling policies are implemented:
1. `random` - nodes must accept randomly assigned tasks from the server
2. `balancing` - nodes potentially reject randomly assigned tasks from the server (details are given in `DesignSpecifications.md`).

Scheduling policy can be set in the `config.txt` file on any line starting with 'policy'.
```
policy <policy type>
```
example
```
policy random
```

### Data
Users may set the directory from which images are used and then outputted to in the `config.txt` file. This should be done for testing cases. If the data you want to modify is in `proj_dir/input_dir` then the field should be followed by nothing.

Remember, if you explicitly set a data directory (not proj_dir/input_dir) then the directory must have an `input_dir` and an `output_dir` within it.

Testing:
```
node_0 0.8 8115
node_1 0.6 8116
node_2 0.5 8117
node_3 0.2 8118
server 8119
policy random
data ../tests/test03/data
```

Normal:
```
node_0 0.8 8115
node_1 0.6 8116
node_2 0.5 8117
node_3 0.2 8118
server 8119
policy random
data
```

# Tests
## Running
In order to run the tests users must complete the following steps:
* navigate to the respective test directory, for instance `test02`:
    ```
    Distributed-Image-Processing
    |-- proj_dir
    |-- tests
        |-- test01
        |-- test02   <-- expand
        |-- ...
    ```
 * Within each test directory you will find the same structure (unless it is an error case). The difference is in the `input_dir` and the contents of the `config.txt` file.
    ```
    |-- test02
        |-- data
        |-- machine.txt
    ```
* Copy (don't cut) the `config.txt` file and replace the `config.txt` located here:
    ```
    Distributed-ImageProcessing
    |-- proj_dir
    |   |-- input_dir
    |   |-- java
    |   |-- ...
    |   |-- config.txt  <-- replace me
    |   |-- ...
    ```
* Run the commands for the nodes, followed by the server, then finally the client.
* See transformed images in the test's `output_dir` and see logs in `Distributed-Image-Processing/proj_dir/log`
    ```
    Distributed-Image-Processing
    |-- proj_dir
    |   |-- input_dir
    |   |-- java
    |   |-- log   <-- logs
    |   |-- ...
    |-- tests
        |-- test01
        |-- test02
        |   |-- data
        |   |   |-- input_dir
        |   |   |-- output_dir  <-- output images
        |   |-- config.txt
        |-- ...   
    ```
    
### Test 1 - Random

Test a spread of probabilities with **random** scheduling policy.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
client:
     Job Receipt:
         Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test02/data
         Time: 3145
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 2 - Balancing
Test a spread of probabilities with **balancing** scheduling policy. 

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
     Job Receipt:
         Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test02/data
         Time: 3145
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 3 - Input Testing
Test that if `input_dir` is empty we get a success status and a report that the directory was empty.
```
     Job Receipt:
         Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test03/data
         Time: 41
         Status: SUCCESS
         Msg: Job held a directory with an empty input_dir
```

### Test 4 - Input Testing
If the `data` directory is incorrectly laid out the app should return a failure and a useful message about why it failed.
```
Job Receipt:
	Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test05/data
	Time: 23
	Status: FAILURE
	Msg: Data directory must contain an input_dir and an output_dir
```

### Test 5 - Random
Two nodes are 100% full and two are completely open at the time of each access. This guarantees a delay for half of the tasks.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 0.0          | 0.0            |

```
     Job Receipt:
         Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test05/data
         Time: 3247
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 6 - Balancing
Two nodes are 100% full and two are completely open at the time of each access. This guarantees a rejection for half of the tasks. There will be no explictly imposed delays like with the **random** policy. The only delay is caused by the server retrying a different node.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 0.0          | 0.0            |

```
     Job Receipt:
         Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test06/data
         Time: 328
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 7 - Random
Three nodes have a 100% load injecting probability and the last has a 0% probability. As long as one of those three nodes are randomly chose, there will be a three seconds delay injected. Therefore we expect the time to be greater than 3000 ms. This will take more time than the Balancing policy version of this test since the balancing will just reject the tasks for the three nodes and the last node will always accept and never inject a delay since its probability is 0%.


|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 0.0            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test07/data
         Time: 3318
         Status: SUCCESS
         Msg: All tasks completed successfully. 
```


### Test 8 - Balancing
Three nodes have a 100% load injecting probability and the last has a 0% probability. Therefore those three nodes will reject every task assigned to them and then the last node will acccept every task assigned. Since the only node accepting tasks has a 0% probability for load injecting and task rejection, there won't be a three second delay implemented so this should take much less time than its Random policy counterpart where it has to accept everything.


|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 0.0            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test08/data
         Time: 382
         Status: SUCCESS
         Msg: All tasks completed successfully. 
```

### Test 9 - Random

One node has a 100% load injection probability, other three have a 0% probability. We expect the time to be greater than 3000 ms since there will be a 3 second delay if node Three is ever chosen for a task.


|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.0      | 0.0          | 0.0          | 1.0            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test09/data
         Time: 3690
         Status: SUCCESS
         Msg: All tasks completed successfully.
```
  

### Test 10 - Balancing

One node has a 100% load injection probability, other three have a 0% probability. Node Three will always reject a task and the other nodes have a 0% injection probability so a load injection delay should never occur in this task.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.0      | 0.0          | 0.0          | 1.0            |

  ```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test10/data
         Time: 346
         Status: SUCCESS
         Msg: All tasks completed successfully.
  ```
  

### Test 11 - Random
All Nodes have an 80% load injection probability so most of the tasks will have a three second delay injected. However, the tasks are run as threads so they don't have to wait for each other to finish before the next starts so most of these will probably overlap with each other when executing. No tasks should be rejected as well since we are using the random policy. Also used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.8          | 0.8          | 0.8            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test11/data
         Time: 3306
         Status: SUCCESS
         Msg: All tasks completed successfully.
```  
  

### Test 12 - Balancing
All Nodes have an 80% load injection probability so most of the tasks will actually be rejected since we are using the balancy policy. If the task gets accepted, there is a strong chance there is a delay injected as well. Also used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.8          | 0.8          | 0.8            |
```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test12/data
         Time: 3359
         Status: SUCCESS
         Msg: All tasks completed successfully.
 ```
  
  

### Test 13 - Random
All Nodes have an 20% load injection probability so most of the tasks will not have a load injected. Random policy is used as well so no tasks can be rejected. Also used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.2      | 0.2          | 0.2          | 0.2            |
```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test13/data
         Time: 3210
         Status: SUCCESS
         Msg: All tasks completed successfully.
```
  
  

### Test 14 - Balancing
All Nodes have an 20% load injection probability so most of the tasks will not have the load injected. Since it's the balancing policy, tasks can be rejected but there is still a decently low chance of that happening. When compared to test 13, we can expect this one to take a bit longer since tasks can be rejeted in this test. Also used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.2      | 0.2          | 0.2          | 0.2            |
```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test14/data
         Time: 3284
         Status: SUCCESS
         Msg: All tasks completed successfully.
```
  
  

### Test 15 - Random
All Nodes have a 100% load injection rate so every single task will have a three second delay injected. However, these tasks are run as separate threads so the delay of one task shouldn't delay another task from starting. Also used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 1.0            |
```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test15/data
         Time: 3339
         Status: SUCCESS
         Msg: All tasks completed successfully.
```
  
  

### Test 16 - Balancing
All Nodes have a 100% load injection rate but since it is the balancing policy, the nodes also have a 100% rejection rate. Therefore the tasks will never finish. The server will see that every single task is being rejected and detect a node clog and send back a FAILURE status to the client. Also used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 1.0            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test16/data
         Time: 937
         Status: FAILURE
         Msg: 6/6 tasks failed     [java] /project/droeg022/Distributed-Image-Processing/proj_dir/../tests
```

### Test 17 - Random
18 images, used to make sure the number of images doesn't affect the distributed system's integrity.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
    Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test17/data   
         Time: 3308
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 18 - Balancing
18 images, used to make sure the number of images doesn't affect the distributed system's integrity.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test18/data   
         Time: 3267
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 19 - Random
1 image, used to make sure the number of images doesn't affect the distributed system's integrity. Added a 2nd run where the node happened to inject a delay on the process just for comparison.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
Run 1
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test19/data   
         Time: 292
         Status: SUCCESS
         Msg: All tasks completed successfully.
```
```
Run 2
    Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test19/data   
         Time: 3307
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 20 - Balancing
1 image, used to make sure the number of images doesn't affect the distributed system's integrity. Added a 2nd run where the node didn't happen to inject a delay on the process just for comparison.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
Run 1
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test20/data   
         Time: 3403
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

```
Run 2
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test20/data   
         Time: 460
         Status: SUCCESS
         Msg: All tasks completed successfully.
```

### Test 21 - Random
Nodes have a 40% chance at injecting a three second delay. Used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.4      | 0.4          | 0.4          | 0.4            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test21/data
         Time: 3278
         Status: SUCCESS      
         Msg: All tasks completed successfully.
```

### Test 22 - Balancing
Nodes have a 40% chance of rejecting tasks and a 40% chance at injecting a three second delay. Used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.4      | 0.4          | 0.4          | 0.4            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test22/data
         Time: 3319
         Status: SUCCESS      
         Msg: All tasks completed successfully.
```

### Test 23 - Random
Nodes have a 60% chance of injecting a load delay and always accept the task. Used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.6      | 0.6          | 0.6          | 0.6            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test23/data
         Time: 3268
         Status: SUCCESS      
         Msg: All tasks completed successfully.
```

### Test 24 - Balancing
Nodes have 60% chance of rejection and then a 60% chance of injecting a load delay. Used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.6      | 0.6          | 0.6          | 0.6            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test24/data
         Time: 3399
         Status: SUCCESS      
         Msg: All tasks completed successfully.
```

### Test 25 - Random
Nodes won't inject any delay so time shold be low. Used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.0      | 0.0          | 0.0          | 0.0            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test25/data
         Time: 633
         Status: SUCCESS      
         Msg: All tasks completed successfully.
```

### Test 26 - Balancing
Nodes will accept every time and there is no injected delay so time should be low. Used to compare against other consistent node values.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.0      | 0.0          | 0.0          | 0.0            |

```
     Job Receipt:
         Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test26/data
         Time: 271
         Status: SUCCESS      
         Msg: All tasks completed successfully.
```

