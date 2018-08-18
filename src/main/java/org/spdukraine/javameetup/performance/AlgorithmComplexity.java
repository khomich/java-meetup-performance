package org.spdukraine.javameetup.performance;

import it.unimi.dsi.fastutil.ints.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.spdukraine.javameetup.performance.utils.Generators;

import java.util.concurrent.TimeUnit;

public class AlgorithmComplexity {
    private static int SET_SIZE = 10_000;

    private static int[] SET_A = Generators.generateRandomBuffer(SET_SIZE, SET_SIZE, 1);
    private static int[] SET_B = Generators.generateRandomBuffer(SET_SIZE, SET_SIZE, 2);

    private static IntSet primitiveSetA = new IntOpenHashSet(SET_A);
    private static IntSet primitiveSetB = new IntOpenHashSet(SET_B);
    private static IntList primitiveListA = new IntArrayList(primitiveSetA);
    private static IntList primitiveListB = new IntArrayList(primitiveSetB);


    /**
     * Method find a set of values which appears in collectionA or collectionB only
     */
    private IntCollection findUniqueIds(IntCollection collectionA, IntCollection collectionB) {
        IntSet result = new IntOpenHashSet();

        collectionA.forEach((int val) -> {
            if (!collectionB.contains(val)) {
                result.add(val);
            }
        });

        collectionB.forEach((int val) -> {
            if (!collectionA.contains(val)) {
                result.add(val);
            }
        });

        return result;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object measureFindUniqueOnSets() {
        return findUniqueIds(primitiveSetA, primitiveSetB);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object measureFindUniqueOnLists() {
        return findUniqueIds(primitiveListA, primitiveListB);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(AlgorithmComplexity.class.getSimpleName())
                .warmupIterations(2)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
