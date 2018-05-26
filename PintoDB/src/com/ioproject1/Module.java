package com.ioproject1;

import java.util.LinkedList;
import java.util.Queue;

public abstract class  Module {

    /*
    * Members declaration block
    */
    protected int servers;
    protected int maxServers;
    protected RandomNumberGenerator randNoGen;
    protected Queue<Query> queryQueue;
    protected LinkedList<Integer> queueSizeRegister;
    protected  SimPintoDB simPintoDBPointer;
    protected  Module nextModule;

    public Module(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        this.servers = servers;
        this.maxServers = maxServers;
        this.simPintoDBPointer = simPintoDBPointer;
        this.nextModule = nextModule;
    }

    public abstract void processTimeOut();
    public abstract void processArrive();
    public abstract void processExit();
    public abstract void genereteAction();

}
