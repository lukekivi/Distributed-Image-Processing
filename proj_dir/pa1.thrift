# Charles Droege
# droeg022
# Lucas Kivi
# kivix019

namespace java pa1


exception InvalidLocation {
    1: string msg,
    2: string type,
}

enum Status {
    ACCEPTED = 0,
    REJECTED = 1,
}

enum SchedulingPolicy {
    RANDOM = 0,
    BALANCING = 1,
}

struct JobReceipt {
    1: i64 time,
    2: status status,
    3: string job,
}

struct NodeReceipt {
    1: string directory,
    2: string file,
}

struct JobRequest {
    1: string job,
    2: SchedulingPolicy policy,
}

struct TaskRequest {
    1: string task,
    2: SchedulingPolicy policy,
}

service ImageProcessServer {
    JobReceipt sendJob(1:JobRequest job) throws (1:InvalidLocation error),
}

service ImageProcessNode {
    NodeReceipt sendTask(1:TaskRequest task) throws (1:InvalidLocation error),
}
