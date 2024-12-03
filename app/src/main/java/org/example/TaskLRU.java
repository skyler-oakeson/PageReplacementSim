package org.example;

public class TaskLRU implements Runnable {
    private int[] sequence;
    private int maxMemeoryFrames;
    private int maxPageReference;
    private int[] pageFaults;

    public TaskLRU(int[] sequence, int maxMemeoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemeoryFrames = maxMemeoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {

    }
}
