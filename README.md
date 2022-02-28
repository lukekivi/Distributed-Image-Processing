# Distributed-Image-Processing
**Created by**:\
Lucas Kivi - kivix019 \
Charles Droeg - droeg022 
---
An application for processing image files in a distributed, synchronous manner.

# Running
This description is for running this app on machines with **shared memory space**. If you are not using shared memory space this setting up all entites in the system will be complicated and we will not detail that process.

## Environment
In order to run this application you must fulfill the mandatory OpenCV and Thrift dependency.

In order for the application to resolve your dependencies set the environment these environment variables:

> THRIFT_LIB_PATH /\<path\>/thrift-0.15.0/lib/java/build/libs \
> OPENCV_LIB_PATH /\<path\>/opencv/build/lib \
> PROJ_PATH /\<path\>/project/kivix019/Distributed-Image-Processing/proj_dir 

## Machines
Being that the application is a distributed system we have a config document `machine.txt`. Inside of this document you may assign host addresses, port numbers and other details to application entities. The syntax details will be explained in the entity sections below.

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
 node_0 kh4250-08.cselabs.umn.edu 0.8 9094
 ```
 ** remember load probability simulates load percentage. More details in `DesignSpecifications.md`.

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
 client kh4250-01.cselabs.umn.edu

 ```
 
## Scheduling Policy
Currently two scheduling policies are imlemented:
1. `random` - nodes must accept randomly assigned tasks from the server
2. `balancing` - nodes potentiall reject randomly assigned tasks from the server (details are given in `DesignSpecifications.md`).

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
-+ proj_dir
-+-+ <directory_name>
---+-+- input_dir
-----+- output dir
```
The <directory_name> can be anything and is assigned in line 8 in `machine.txt`
```
data <path>
```
example with data directory named `data` within proj_dir
```
data data
```