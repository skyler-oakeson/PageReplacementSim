package org.example;
import java.util.LinkedList;
import java.util.Queue;

public class TaskLRU implements Runnable {
    private int[] sequence;
    private int maxMemoryFrames;
    private int maxPageReference;
    private int[] pageFaults;
    private int pageFaultCount;

    public TaskLRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
        this.pageFaultCount = 0;
    }

    @Override
    public void run() {
        Queue<Integer> frames = new LinkedList<Integer>();
        for (int page : sequence) {
            if (frames.contains(page)) {
                frames.remove(page);
                frames.add(page);
                continue;
            }
            if (frames.size() >= maxMemoryFrames) {
                frames.poll();
            }
            frames.add(page);
            pageFaultCount++;
        }

        pageFaults[maxMemoryFrames] = pageFaultCount;
    }
}
