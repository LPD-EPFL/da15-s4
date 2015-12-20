#!/bin/bash
#
# Tests the performance of S4 application.
#
# usage: sh s4_performance.sh
#

#time to wait for S4 to complete its performance evaluation (in seconds)
evaluation_time=5

#wait for all processes to initialize (2 seconds)
init_time=2

#call script to install dependencies like (required) libraries (empty if no dependencies required)
if test -f "dependency.sh"; then sh dependency.sh;fi

#call Makefile to build executables
make

#limits the memory allocation for verifying the garbage collection for input of size of atmax 20mb
export JAVA_OPTS="-Xmx100m -Xms50m"

#start 3 processes with their input traces
for i in `seq 0 2`
do
    ./s4 127.0.0.1 8900 127.0.0.1 8901 127.0.0.1 8902 $i $i.input &
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


#let the processes do the work for some time
sleep $evaluation_time

#stop all processes
for i in `seq 0 2`
do
    if [ -n "${da_proc_id[$i]}" ]; then
	  kill -KILL "${da_proc_id[$i]}"
    fi
done

#count number of messages ($i.output) in the output files

echo "Performance test done."
# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
#
make clean

#call script to uninstall dependencies like (required) libraries (keep it empty if no dependencies required)
if test -f "removal.sh"; then sh removal.sh;fi
