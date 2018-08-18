package org.spdukraine.javameetup.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.spdukraine.javameetup.performance.utils.Generators;

import java.util.concurrent.TimeUnit;

public class CPUCacheEffect {
    private static int[] DATA_BUFFER = Generators.generateRandomBuffer(1024 * 1024 * 1024 /Integer.BYTES);

    private static int L1_CACHE_ARRAY_SIZE = 32 * 1024 / Integer.BYTES;
    private static int L2_CACHE_ARRAY_SIZE = 1024 * 1024 / Integer.BYTES;
    private static int MID_RAM_CACHE_ARRAY_SIZE = DATA_BUFFER.length / 8;
    private static int FULL_RAM_CACHE_ARRAY_SIZE = DATA_BUFFER.length;



    private static int ITERATION_COUNT = 1_000_000;


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureL1Cache() {
        int sum = 0;

        for (int n = 0; n < ITERATION_COUNT; n += 1) {
            sum += DATA_BUFFER[Math.abs(sum) % L1_CACHE_ARRAY_SIZE];
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureL2Cache() {
        int sum = 0;

        for (int n = 0; n < ITERATION_COUNT; n += 1) {
            sum += DATA_BUFFER[Math.abs(sum) % L2_CACHE_ARRAY_SIZE];
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureRAM128M() {
        int sum = 0;

        for (int n = 0; n < ITERATION_COUNT; n += 1) {
            sum += DATA_BUFFER[Math.abs(sum) % MID_RAM_CACHE_ARRAY_SIZE];
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureRAM1G() {
        int sum = 0;

        for (int n = 0; n < ITERATION_COUNT; n += 1) {
            sum += DATA_BUFFER[Math.abs(sum) % FULL_RAM_CACHE_ARRAY_SIZE];
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(CPUCacheEffect.class.getSimpleName())
                .warmupIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    
}
