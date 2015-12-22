#!/bin/bash
#
# Tests the correctness of S4 application.
#
# usage: source s4_correctness_java.sh
#
# This is a sample script that demonstrates the test execution.
# The details and parameters of the actual test might differ.
#

#time to wait for S4 to complete its execution (in seconds)
#(should be adapted depending on the size of the input file)
time_to_finish=5

#wait for all processes to initialize (2 seconds)
init_time=2

#call script to install dependencies like (required) libraries (keep it empty if no dependencies required)
if test -f "dependency.sh"; then sh dependency.sh;fi

#call Makefile to build executables
make

#configure lossy network simulation
#delay packets with value from uniform [150ms-250ms] distribution
#drop packets randomly with probability of 0.1 and correlation of 0.25

sudo tc qdisc change dev lo root netem delay 200ms 50ms loss 10% 25% reorder 25% 50%

#limits the memory allocation for verifying the garbage collection for input of size of atmax 20mb
export JAVA_OPTS="-Xmx100m -Xms50m"

#start 3 processes with their input traces
for i in `seq 0 2`
do
    java -cp target/da15-s4-1.0-SNAPSHOT.jar ch.epfl.lpd.App 127.0.0.1 8900 127.0.0.1 8901 127.0.0.1 8902 $i $i.input &
    da_proc_id[$i]=$!
done

#leave some time for process initialization
sleep $init_time

#start broadcasting
for i in `seq 0 2`
do
    if [ -n "${da_proc_id[$i]}" ]; then
    kill -INT "${da_proc_id[$i]}"
    fi
done

#execute process crashes and delays
#example:
kill -STOP "${da_proc_id[0]}" #pause process 0
sleep 1
kill -STOP "${da_proc_id[2]}" #pause process 2
sleep 1
kill -KILL "${da_proc_id[1]}" #crash process 1
da_proc_id[1]=""
kill -CONT "${da_proc_id[2]}" #resume process 2
kill -CONT "${da_proc_id[0]}" #resume process 0

#leave some time for the correct processes to complete
sleep $time_to_finish

#stop all processes
for i in `seq 0 2`
do
    if [ -n "${da_proc_id[$i]}" ]; then
	  kill -KILL "${da_proc_id[$i]}"
    fi
done

#check output file ($i.output) for correctness

echo "Correctness test done."

# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
#
make clean

#call script to uninstall dependencies like (required) libraries (keep it empty if no dependencies required)
if test -f "removal.sh"; then sh removal.sh;fi
