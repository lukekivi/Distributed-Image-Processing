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
    |   |-- config
    |   |   |-- machine.txt  <-- replace me
    ```
* Run the commands found in `commands.txt` in order.
* See transformed images in the test's `output_dir` and see logs in `Distributed-Image-Processing/proj_dir/java/log`
    ```
    Distributed-Image-Processing
    |-- proj_dir
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
    
### Test 1

Test a spread of probabilities. We would expect.

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

### Test 2


### Test 3


### Test 4


### Test 5


### Test 6


### Test 7


### Test 8


### Test 9


### Test 10


### Test 11


### Test 12


### Test 13


### Test 14


### Test 15


### Test 16

