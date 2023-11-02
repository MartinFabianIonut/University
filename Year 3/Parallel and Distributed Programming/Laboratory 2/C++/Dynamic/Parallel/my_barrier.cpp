#include "my_barrier.hpp"

my_barrier::my_barrier(int count) : thread_count(count), counter(0), waiting(0)
{
}

void my_barrier::wait()
{
    unique_lock<mutex> lk(m);
    ++counter;
    ++waiting;
    cv.wait(lk, [&]
            { return counter >= thread_count; });
    cv.notify_one();
    --waiting;
    if (waiting == 0)
    {
        counter = 0;
    }
    lk.unlock();
}