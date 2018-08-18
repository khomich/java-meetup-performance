package org.spdukraine.javameetup.performance;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.spdukraine.javameetup.performance.utils.Generators;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PrimitivesVsBoxedCpu {
    private static int SET_SIZE = 1_000_000;

    private static int[] SET_A = Generators.generateRandomBuffer(SET_SIZE, SET_SIZE, 1);
    private static int[] SET_B = Generators.generateRandomBuffer(SET_SIZE, SET_SIZE, 2);

    private static IntSet primitiveSetA = new IntOpenHashSet(SET_A);
    private static IntSet primitiveSetB = new IntOpenHashSet(SET_B);

    private static Set<Integer> boxedSetA = new HashSet<>(primitiveSetA);
    private static Set<Integer> boxedSetB = new HashSet<>(primitiveSetB);

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object measurePrimitiveSetConstruction() {
        IntSet set = new IntOpenHashSet();
        for (int i : SET_A) {
            set.add(i);
        }
        return set;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object measureBoxedSetConstruction() {
        Set<Integer> set = new HashSet<>();
        for (int i : SET_A) {
            set.add(i);
        }
        return set;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object measurePrimitiveSetReconstruction() {
        return new IntOpenHashSet(primitiveSetA);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object measureBoxedSetReconstruction() {
        return new HashSet<>(boxedSetA);
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int measurePrimitiveSetIntersection() {
        IntSet intersectionSet = new IntOpenHashSet();

        primitiveSetA.forEach((int val) -> {
            if (primitiveSetB.contains(val)) {
                intersectionSet.add(val);
            }
        });

        return intersectionSet.size();
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int measureBoxedSetIntersection() {
        Set<Integer> intersectionSet = new HashSet<>();

        for (Integer val : boxedSetA) {
            if (boxedSetB.contains(val)) {
                intersectionSet.add(val);
            }
        }

        return intersectionSet.size();
    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(PrimitivesVsBoxedCpu.class.getSimpleName())
                .warmupIterations(2)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
