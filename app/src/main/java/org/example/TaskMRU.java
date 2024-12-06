package org.example;
import java.util.LinkedList;

public class TaskMRU implements Runnable {
    private int[] sequence;
    private int maxMemoryFrames;
    private int maxPageReference;
    private int[] pageFaults;
    private int pageFaultCount;

    public TaskMRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
        this.pageFaultCount = 0;
    }

    @Override
    public void run() {
        LinkedList<Integer> frames = new LinkedList<Integer>();
        for (int page : sequence) {
            if (frames.contains(page)) {
                frames.remove(frames.indexOf(page));
                frames.push(page);
                continue;
            }
            if (frames.size() >= maxMemoryFrames) {
                frames.pop();
            }
            frames.push(page);
            pageFaultCount++;
        }

        pageFaults[maxMemoryFrames] = pageFaultCount;
    }
}
