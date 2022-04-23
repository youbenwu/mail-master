package com.ys.mail;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-22 13:11
 */

public class TicketPark {

    //加锁标记
    private final AtomicBoolean isLock = new AtomicBoolean(false);
    //票库存
    private int ticketCount = 10;
    //等待线程队列
    private final Queue<Thread> WAIT_THREAD_QUEUE = new LinkedBlockingQueue<>();

    //抢票
    public void bye(){
        while (!lock()) {
            //获取不到锁的线程,添加到队列，并休眠
            lockWait();
        }
        String name = Thread.currentThread().getName();
        //加锁成功，执行业务逻辑
        System.out.println(name + ":加锁成功...");
        System.out.println(name + ":开始抢票...");
        ticketCount--;
        System.out.println(name + ":抢到了，库存:" + ticketCount);
        System.out.println(name + ":释放锁.");
        unlock();
    }

    //加锁的过程必须是原子操作，否则会导致多个线程同时加锁成功。
    public boolean lock(){
        return isLock.compareAndSet(false, true);
    }

    //释放锁
    public void unlock() {
        isLock.set(false);
        //唤醒队列中的第一个线程
        LockSupport.unpark(WAIT_THREAD_QUEUE.poll());
    }

    public void lockWait(){
        //将获取不到锁的线程添加到队列
        WAIT_THREAD_QUEUE.add(Thread.currentThread());
        //并休眠
        LockSupport.park();
    }

    public static void main(String[] args) {
        TicketPark ticketPark = new TicketPark();
        ticketPark.bye();
    }
}
