/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collections;

public class Assign6 {
    static final int REF_SEQ_SIZE = 1000;
    static final int NUMBER_OF_SIMS = 1000;
    static final int MAX_MEM_FRAMES = 100;
    static final int MAX_PAGE_REF = 250;


    public class Thruple<T, S, K> {
        public T x;
        public S y;
        public K z;

        public Thruple(T x, S y, K z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private void runSim() {
        Random randomGenerator = new Random();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int[][] fifoPageFaults = new int[NUMBER_OF_SIMS][MAX_MEM_FRAMES+1];
        int[][] lruPageFaults = new int[NUMBER_OF_SIMS][MAX_PAGE_REF];
        int[][] mruPageFaults = new int[NUMBER_OF_SIMS][MAX_PAGE_REF];
        for (int sim = 0; sim < NUMBER_OF_SIMS; sim++) {
            int[] seq = generateSequence(randomGenerator, 250);

            for (int memFrames = 1; memFrames <= MAX_MEM_FRAMES; memFrames++) {
                pool.submit(new TaskFIFO(seq, memFrames, MAX_MEM_FRAMES, fifoPageFaults[sim]));
                pool.submit(new TaskLRU(seq, memFrames, MAX_MEM_FRAMES, lruPageFaults[sim]));
                pool.submit(new TaskMRU(seq, memFrames, MAX_MEM_FRAMES, mruPageFaults[sim]));
            }
        }

        try {
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch(InterruptedException e) {
            System.out.println(e);
            pool.shutdownNow();
        }

        System.out.println("Belady's Report for FIFO");
        printSummary(fifoPageFaults);
        System.out.println("Belady's Report for LRU");
        printSummary(lruPageFaults);
        System.out.println("Belady's Report for MRU");
        printSummary(mruPageFaults);
    }

    private ArrayList<Thruple<Integer, Integer, Integer>> countBeladys(int[][] pageFaults) {
        ArrayList<Thruple<Integer, Integer, Integer>> anomalys = new ArrayList<Thruple<Integer, Integer, Integer>>();
        for (int sim = 0; sim < pageFaults.length; sim++) {
            int prev = Integer.MAX_VALUE;
            for (int frames = 1; frames < MAX_MEM_FRAMES+1; frames++) {
                int curr = pageFaults[sim][frames];
                if (curr > prev) {
                    Thruple<Integer, Integer, Integer> anom = new Thruple<Integer, Integer, Integer>(sim, frames-1, frames);
                    anomalys.add(anom);
                }
                prev = curr;
            }
        }
        return anomalys;
    }

    private void printSummary(int[][] pageFaults) {
        ArrayList<Thruple<Integer, Integer, Integer>> anomalys = countBeladys(pageFaults);
        ArrayList<Integer> deltas = new ArrayList<Integer>();
        for (Thruple<Integer, Integer, Integer> anom : anomalys) {
            int pf1 = pageFaults[anom.x][anom.y];
            int pf2 = pageFaults[anom.x][anom.z];
            int delta = pf2 - pf1;
            deltas.add(delta);
            System.out.printf("    Anomaly detected in simulation #%d - %d PF's @ %d frames vs. %d PF's @ %d frames (Δ%d)\n", anom.x, pf1, anom.y, pf2, anom.z, delta);
        }
        System.out.printf("Anomalys detected %d in %d with a max delta of %d \n", anomalys.size(), NUMBER_OF_SIMS, deltas.isEmpty() ? 0 : Collections.max(deltas));
    }

    private int[] generateSequence(Random rand, int upper) {
        int[] seq = new int[REF_SEQ_SIZE];
        for (int i = 0; i < REF_SEQ_SIZE; i ++) {
            seq[i] = rand.nextInt(upper+1);
        }
        return seq;
    }

    public static void main(String[] args) {
        Assign6 app = new Assign6();
        app.runSim();
    }
}

