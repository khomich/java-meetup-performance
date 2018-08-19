package org.spdukraine.javameetup.performance;

import it.unimi.dsi.fastutil.ints.*;
import org.spdukraine.javameetup.performance.utils.Generators;
import org.spdukraine.javameetup.performance.utils.MemoryBenchmark;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PrimitivesVsBoxedMemory {
    private static int SET_SIZE = 1_024 * 64;

    private static int[] DATA = Generators.generateUniqueSequentialBuffer(SET_SIZE);

    private static IntSet primitiveSet = new IntOpenHashSet(DATA);
    private static Set<Integer> boxedSet = new HashSet<>(primitiveSet);

    private static char[] TEST_1_CHAR = "1".toCharArray();
    private static char[] TEST_10_CHARS = "SPDUkraine".toCharArray();
    private static char[] TEST_MORE_CHARS = "The quick brown fox jumps over the lazy dog".toCharArray();


    public static void main(String[] args) {
        MemoryBenchmark benchmark = new MemoryBenchmark();

        System.out.println("Testing primitives...");
        benchmark.reportMeasure(() -> new Byte((byte)123), "Byte");
        benchmark.reportMeasure(() -> new Short((short)12345), "Short");
        benchmark.reportMeasure(() -> new Integer(1234567890), "Integer");
        benchmark.reportMeasure(() -> new Long(1234567890123456789L), "Long");
        benchmark.reportMeasure(() -> new Float(1 / 3f), "Float");
        benchmark.reportMeasure(() -> new Double(1 / 3), "Double");
        System.out.println();

        System.out.println("Testing dates...");
        benchmark.reportMeasure(Date::new, "Date");
        benchmark.reportMeasure(Instant::now, "Instant");
        benchmark.reportMeasure(() -> LocalDate.of(2018, 8, 18), "LocalDate");
        benchmark.reportMeasure(() -> LocalDateTime.of(2018, 8, 18, 13, 30, 0), "LocalDateTime");
        System.out.println();

        System.out.println("Testing strings...");
        benchmark.reportMeasure(() -> new String(""), "String()");
        benchmark.reportMeasure(() -> new String(TEST_1_CHAR), "String(1)");
        benchmark.reportMeasure(() -> new String(TEST_10_CHARS), "String()", TEST_10_CHARS.length);
        benchmark.reportMeasure(() -> new String(TEST_MORE_CHARS), "String()", TEST_MORE_CHARS.length);
        System.out.println();

        System.out.println("Testing arrays...");
        benchmark.reportMeasure(() -> new int[0], "int[]");
        benchmark.reportMeasure(() -> new int[1], "int[]", 1);
        benchmark.reportMeasure(() -> new int[256], "int[]", 256);
        benchmark.reportMeasure(() -> new int[256 * 256], "int[]", 256 * 256);
        System.out.println();

        MemoryBenchmark collectionsBenchmark = new MemoryBenchmark(512, 200);
        System.out.println("Testing lists...");
        collectionsBenchmark.reportMeasure(() -> new IntArrayList(DATA), "IntArrayList()", DATA.length);
        collectionsBenchmark.reportMeasure(() -> new ArrayList<>(new IntArrayList(DATA)), "ArrayList<Integer>()", DATA.length);
        collectionsBenchmark.reportMeasure(() -> new LinkedList<>(new IntArrayList(DATA)), "LinkedList<Integer>()", DATA.length);
        System.out.println();


        System.out.println("Testing sets...");
        IntArrayList setPrototype = new IntArrayList(DATA);
        collectionsBenchmark.reportMeasure(() -> new IntOpenHashSet(setPrototype), "IntOpenHashSet()", setPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new IntAVLTreeSet(setPrototype), "IntAVLTreeSet()", setPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new HashSet<>(setPrototype), "HashSet<Integer>()", setPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new TreeSet<>(setPrototype), "TreeSet<Integer>()", setPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new LinkedHashSet<>(setPrototype), "LinkedHashSet<Integer>()", setPrototype.size());
        System.out.println();

        System.out.println("Testing maps...");
        Int2IntMap mapPrototype = new Int2IntOpenHashMap(DATA, DATA);
        collectionsBenchmark.reportMeasure(() -> new Int2IntOpenHashMap(mapPrototype), "Int2IntOpenHashMap()", mapPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new Int2IntLinkedOpenHashMap(mapPrototype), "Int2IntLinkedOpenHashMap()", mapPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new HashMap<>(mapPrototype), "HashMap<Integer>()", mapPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new TreeMap<>(mapPrototype), "TreeMap<Integer>()",mapPrototype.size());
        collectionsBenchmark.reportMeasure(() -> new LinkedHashMap<>(mapPrototype), "LinkedHashMap<Integer>()", mapPrototype.size());
        System.out.println();
    }
}
