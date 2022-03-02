# Distributed-Image-Processing
**Created by**:\
Lucas Kivi - kivix019 \
Charles Droege - droeg022 
---
An application for processing image files in a distributed, synchronous manner.

# Table of Contents
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

# Running
This description is for running this app on machines with **shared memory space**. If you are not using shared memory space this setting up all entites in the system will be complicated and we will not detail that process.

## Environment
In order to run this application you must fulfill the mandatory OpenCV and Thrift dependency.

In order for the application to resolve your dependencies set the environment these environment variables:

> THRIFT_LIB_PATH /\<path\>/thrift-0.15.0/lib/java/build/libs \
> OPENCV_LIB_PATH /\<path\>/opencv/build/lib \
> PROJ_PATH /\<path\>/project/kivix019/Distributed-Image-Processing/proj_dir 

## Machines
Being that the application is a distributed system we have a config document `machine.txt`. Inside of this document you may assign host addresses, port numbers and other details to application entities. The syntax details will be explained in the entity sections below. It is important to keep the syntax the same as displayed currently. The program will not work if the format is changed.

## Commands
`command.txt` contains commands for starting app entities.

If you change a machine address in `machine.txt` be sure to adjust the associated command in `command.txt` to reflect the change in address.

## Nodes
Nodes depend on a scheduling policy that is outlined in the **Scheduling Policy** section below. There are two ways to run nodes.

1. On the correct host machine (as declared in machine.txt) navigate to: `Distributed-Image-Processing/proj_dir/java` and run 1 of 4 commands:
 - ```ant node_zero```
 - ```ant node_one```
 - ```ant node_two```
 - ```ant node_three```
---
2. On any machine run the respective node command in `command.txt`. This will start ssh into a machine and run the node. Be sure the `machine.txt` file reflects which machine is being activated in `commands.txt`.

 The `machine.txt` entry syntax for nodes looks like this:
 ```
 node_<num> <machine address> <load probability> <port number>
 ```
 Example:
 ```
 node_0 csel-kh4250-08.cselabs.umn.edu 0.8 9094
 ```
 ** remember load probability simulates the probability of injecting a delay no matter the scheduling policy . It also simulates the probability of rejection when using the `Random` scheduling policy. More details in `DesignSpecifications.md`. Also take  note that the machine.txt file reads the nodes as 'node_2' while the command to run it will be ant 'node_two'.

## Server
There are two ways to run the server.
 
1. On the correct host machine (as declared in machine.txt) navigate to: `Distributed-Image-Processing/proj_dir/java` and run:
 - ```ant server```
---
2. On any machine run the respective node command in `command.txt`. This will start ssh into a machine and run the node. Be sure the `machine.txt` file reflects which machine is being activated in `commands.txt`.

 The `machine.txt` entry syntax for nodes looks like this:
 ```
 server <machine address> <port number>
 ```
 Example:
 ```
 server csel-kh1260-12.cselabs.umn.edu 9094

 ```
 
## Client
There are two ways to run the client.
 
1. On the correct host machine (as declared in machine.txt) navigate to: `Distributed-Image-Processing/proj_dir/java` and run:
 - ```ant client```
---
2. On any machine run the respective node command in `command.txt`. This will start ssh into a machine and run the node. Be sure the `machine.txt` file reflects which machine is being activated in `commands.txt`.

 The `machine.txt` entry syntax for nodes looks like this:
 ```
 client <machine address>
 ```
 Example:
 ```
 client csel-kh4250-01.cselabs.umn.edu

 ```
 
## Scheduling Policy
Currently two scheduling policies are imlemented:
1. `random` - nodes must accept randomly assigned tasks from the server
2. `balancing` - nodes potentially reject randomly assigned tasks from the server (details are given in `DesignSpecifications.md`).

Scheduling policy is modified via a field at line 7 in `machine.txt`.
```
policy <policy type>
```
example
```
policy random
```

### Data
Users may use data directories that adhere to the below hierarchy.
```
proj_dir
|-- <directory_name>
    |-- input_dir
    |-- output dir
```
The <directory_name> can be anything and is assigned in line 8 in `machine.txt`
```
data <path>
```
example with data directory named `data` within proj_dir
```
data data
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
 * Within each test directory you will find the same structure. The difference is in the `input_dir`, contents of the `machine.txt` file.
    ```
    |-- test02
        |-- data
        |-- machine.txt
    ```
* Copy (don't cut) the `machine.txt` file and replace the `machine.txt` located here:
    ```
    Distributed-ImageProcessing
    |-- proj_dir
    |   |-- data
    |   |-- java
    |   |-- ...
    |   |-- machine.txt  <-- replace me
    |   |-- ...
    ```
* Run the commands found in `commands.txt` in order.
* See transformed images in the test's `output_dir` and see logs in `Distributed-Image-Processing/proj_dir/java/log`
    ```
    Distributed-Image-Processing
    |-- proj_dir
    |   |-- data
    |   |-- java
    |   |   |-- log   <-- logs
    |   |   |-- ...
    |   |-- ... 
    |-- tests
        |-- test01
        |-- test02
        |   |-- data
        |   |   |-- input_dir
        |   |   |-- output_dir  <-- output images
        |   |-- machine.txt
        |-- ...   
    ```
    
### Test 1 - Random

Test a spread of probabilities with **random** scheduling policy.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
client:
     [echo] tutorial client simple:
     [java] Job Receipt:
     [java]     Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test02/data
     [java]     Time: 3145
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
     [java]
```

### Test 2 - Balancing
Test a spread of probabilities with **balancing** scheduling policy. 

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
client:
     [echo] tutorial client simple:
     [java] Job Receipt:
     [java]     Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test02/data
     [java]     Time: 3145
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
     [java]
```

### Test 3 - Input Testing
Test that if `input_dir` is empty we get a success status and a report that the directory was empty.
```
client:
     [echo] tutorial client simple:
     [java] Job Receipt:
     [java]     Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test03/data
     [java]     Time: 41
     [java]     Status: SUCCESS
     [java]     Msg: Job held a directory with an empty input_dir
     [java]
```

### Test 4 - Input Testing
If the `data` directory is incorrectly laid out the app should return a failure and a useful message about why it failed.
```
client:
     [echo] tutorial client simple:
     [java] Job Receipt:
     [java]     Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test04/data
     [java]     Time: 27
     [java]     Status: FAILURE
     [java]     Msg: Target directory does not contain exactly two directories.
     [java]
```

### Test 5 - Random
Two nodes are 100% full and two are completely open at the time of each access. This guarantees a delay for half of the tasks.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 0.0          | 0.0            |

```
client:
     [echo] tutorial client simple:
     [java] Job Receipt:
     [java]     Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test05/data
     [java]     Time: 3247
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
     [java]
```

### Test 6 - Balancing
Two nodes are 100% full and two are completely open at the time of each access. This guarantees a rejection for half of the tasks. There will be no explictly imposed delays like with the **random** policy. The only delay is caused by the server retrying a different node.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 0.0          | 0.0            |

```
     [echo] tutorial client simple:
     [java] Job Receipt:
     [java]     Job: /project/kivix019/Distributed-Image-Processing/proj_dir/../tests/test06/data
     [java]     Time: 328
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
     [java]
```

### Test 7 - Random
Three nodes have a 100% load injecting probability and the last has a 0% probability. As long as one of those three nodes are randomly chose, there will be a three seconds delay injected. Therefore we expect the time to be greater than 3000 ms. This will take more time than the Balancing policy version of this test since the balancing will just reject the tasks for the three nodes and the last node will always accept and never inject a delay since its probability is 0%.


|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 0.0            |

```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test07/data
     [java]     Time: 3318
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully. 
```


### Test 8 - Balancing
Three nodes have a 100% load injecting probability and the last has a 0% probability. Therefore those three nodes will reject every task assigned to them and then the last node will acccept every task assigned. Since the only node accepting tasks has a 0% probability for load injecting and task rejection, there won't be a three second delay implemented so this should take much less time than its Random policy counterpart where it has to accept everything.


|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 0.0            |

```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test08/data
     [java]     Time: 382
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully. 
```

### Test 9 - Random

One node has a 100% load injection probability, other three have a 0% probability. We expect the time to be greater than 3000 ms since there will be a 3 second delay if node Three is ever chosen for a task.


|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.0      | 0.0          | 0.0          | 1.0            |

```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test09/data
     [java]     Time: 3690
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```
  

### Test 10 - Balancing

One node has a 100% load injection probability, other three have a 0% probability. Node Three will always reject a task and the other nodes have a 0% injection probability so a load injection delay should never occur in this task.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.0      | 0.0          | 0.0          | 1.0            |

  ```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test10/data
     [java]     Time: 346
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
  ```
  

### Test 11 - Random
All Nodes have an 80% load injection probability so most of the tasks will have a three second delay injected. However, the tasks are run as threads so they don't have to wait for each other to finish before the next starts so most of these will probably overlap with each other when executing. No tasks should be rejected as well since we are using the random policy.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.8          | 0.8          | 0.8            |

```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test11/data
     [java]     Time: 3306
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```  
  

### Test 12 - Balancing
All Nodes have an 80% load injection probability so most of the tasks will actually be rejected since we are using the balancy policy. If the task gets accepted, there is a strong chance there is a delay injected as well.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.8          | 0.8          | 0.8            |
```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test12/data
     [java]     Time: 3359
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
 ```
  
  

### Test 13 - Random
All Nodes have an 20% load injection probability so most of the tasks will not have a load injected. Random policy is used as well so no tasks can be rejected. 

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.2      | 0.2          | 0.2          | 0.2            |
```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test13/data
     [java]     Time: 3210
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```
  
  

### Test 14 - Balancing
All Nodes have an 20% load injection probability so most of the tasks will not have the load injected. Since it's the balancing policy, tasks can be rejected but there is still a decently low chance of that happening. When compared to test 13, we can expect this one to take a bit longer since tasks can be rejeted in this test.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.2      | 0.2          | 0.2          | 0.2            |
```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test14/data
     [java]     Time: 3284
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```
  
  

### Test 15 - Random
All Nodes have a 100% load injection rate so every single task will have a three second delay injected. However, these tasks are run as separate threads so the delay of one task shouldn't delay another task from starting.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 1.0            |
```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test15/data
     [java]     Time: 3339
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```
  
  

### Test 16 - Balancing
All Nodes have a 100% load injection rate but since it is the balancing policy, the nodes also have a 100% rejection rate. Therefore the tasks will never finish. The server will see that every single task is being rejected and detect a node clog and send back a FAILURE status to the client.

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      1.0      | 1.0          | 1.0          | 1.0            |

```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test16/data
     [java]     Time: 937
     [java]     Status: FAILURE
     [java]     Msg: 6/6 tasks failed     [java] /project/droeg022/Distributed-Image-Processing/proj_dir/../tests
```

### Test 17 - Random
18 images

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
[java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test17/data   
     [java]     Time: 3308
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```

### Test 18 - Balancing
18 images

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test18/data   
     [java]     Time: 3267
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```

### Test 19 - Random
1 image

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
Run 1
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test19/data   
     [java]     Time: 292
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```
```
Run 2
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test19/data   
     [java]     Time: 3307
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```

### Test 20 - Balancing
1 image

|                 | **Node Zero** | **Node One** | **Node Two** | **Node Three** |
|:---------------:|:-------------:|--------------|--------------|----------------|
| **Probability** |      0.8      | 0.6          | 0.5          | 0.2            |

```
Run 1
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test20/data   
     [java]     Time: 3403
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```

```
Run 2
     [java] Job Receipt:
     [java]     Job: /project/droeg022/Distributed-Image-Processing/proj_dir/../tests/test20/data   
     [java]     Time: 460
     [java]     Status: SUCCESS
     [java]     Msg: All tasks completed successfully.
```

