/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Assign6 {
    static final int REF_SEQ_SIZE = 1000;
    static final int NUMBER_OF_SIMS = 1000;
    static final int MAX_MEM_FRAMES = 100;
    static final int MAX_PAGE_REF = 250;

    private void runSim() {
        Random randomGenerator = new Random();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < NUMBER_OF_SIMS; i++) {
            int[] seq = generateSequence(randomGenerator, 250);

            for (int memFrames = 1; memFrames <= MAX_MEM_FRAMES; memFrames++) {
                int[] pageFaults = new int[MAX_PAGE_REF];
                Runnable[] tasks = {
                    new TaskFIFO(seq, memFrames, MAX_MEM_FRAMES, pageFaults),
                    new TaskLRU(seq, memFrames, MAX_MEM_FRAMES, pageFaults),
                    new TaskMRU(seq, memFrames, MAX_MEM_FRAMES, pageFaults)
                };

                for (Runnable task : tasks) {
                    pool.execute(task);
                }
            }
        }
        pool.shutdown();

        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch(InterruptedException e) {
            System.out.println(e);
        }
    }

    private int[] generateSequence(Random rand, int upper) {
        int[] seq = new int[REF_SEQ_SIZE];
        for (int i = 0; i < REF_SEQ_SIZE; i ++) {
            seq[i] = rand.nextInt(upper);
        }
        return seq;
    }

    public static void main(String[] args) {
        Assign6 app = new Assign6();
        app.runSim();
    }
}
