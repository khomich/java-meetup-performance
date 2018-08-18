package org.spdukraine.javameetup.performance.utils;

import java.util.Arrays;
import java.util.Random;

public class Generators {

    /**
     * Creates buffer of specific size and fill it with random numbers within range 0..size
     */
    public static int[] generateRandomBuffer(int size, int maxVal, long seed) {
        int[] buffer = new int[size];

        Random random = new Random(seed);

        for (int i = 0; i < buffer.length; i += 1) {
            buffer[i] = random.nextInt() % maxVal;
        }

        return buffer;
    }

    /**
     * Creates buffer of specific size and fill it with random numbers
     */
    public static int[] generateRandomBuffer(int size) {
        return generateRandomBuffer(size, size, System.nanoTime());
    }

    /**
     * Creates buffer of specific size and fill it with random numbers
     */
    public static int[] generateUniqueSequentialBuffer(int size) {
        int[] buffer = new int[size];

        for (int n = 0; n < size; ++n) {
            buffer[n] = n + 1;
        }

        return buffer;
    }
}
