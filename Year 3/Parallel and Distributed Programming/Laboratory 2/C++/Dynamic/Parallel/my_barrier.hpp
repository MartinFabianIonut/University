#ifndef MY_BARRIER_HPP
#define MY_BARRIER_HPP

#include <mutex>
#include <condition_variable>

using namespace std;

class my_barrier
{
public:
    my_barrier(int count);
    void wait();

private:
    mutex m;
    condition_variable cv;
    int counter;
    int waiting;
    int thread_count;
};

#endif