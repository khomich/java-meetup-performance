package org.spdukraine.javameetup.performance.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class MemoryBenchmark {
    private long maxMemoryLimit;
    private int maxSamplingCount;
    private Object[] store;


    private static long getCurrentMemorySize() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }


    public MemoryBenchmark(long memoryLimitMBytes, int maxSamplingCount) {
        maxMemoryLimit = memoryLimitMBytes * 1024 * 1024;
        this.maxSamplingCount = maxSamplingCount;
    }

    public MemoryBenchmark() {
        this(512, 1024 * 1024);
    }


    private synchronized double doMeasure(Supplier<Object> factory) {
        store = new Object[maxSamplingCount];

        //try to compact everything
        System.gc();

        long startMemory = getCurrentMemorySize();
        long memoryConsumed = 0;
        int cursor = 0;

        while (memoryConsumed < maxMemoryLimit && cursor < store.length) {
            for (int i = 0; i < 1024 && i < maxSamplingCount; i += 1 ) {
                store[cursor] = factory.get();
                cursor += 1;
            }
            memoryConsumed = getCurrentMemorySize() - startMemory;
        }

        System.gc();

        memoryConsumed = getCurrentMemorySize() - startMemory;

        return (double)memoryConsumed / cursor;
    }

    public double measure(Supplier<Object> factory) {
        doMeasure(factory);
        return doMeasure(factory);
    }

    public void reportMeasure(Supplier<Object> factory, String measurable) {
        reportMeasure(factory, measurable, 0);
    }

    public void reportMeasure(Supplier<Object> factory, String measurable, int elementCount) {
        double result = measure(factory);
        double roundedResult = Math.round(result);
        boolean precisedMeasured;

        //prepare write metric for entire test
        if (Math.abs(result - roundedResult) > 0.09) {
            roundedResult = Math.round(result * 100d) / 100d;
            precisedMeasured = false;
        } else {
            precisedMeasured = true;
        }

        //prepare right message for single element size
        double oneElementSize = 0;
        double roundedOneElementSize = 0;
        if (elementCount > 0 ) {
            oneElementSize = result / elementCount;
            roundedOneElementSize = Math.round(oneElementSize);
            if (Math.abs(oneElementSize - roundedOneElementSize) > 0.09) {
                roundedOneElementSize = Math.round(result / elementCount * 100d) / 100d;
            }
        }

        measurable = measurable.replaceAll(Pattern.quote("[]"), "[" + elementCount + "]").replaceAll(Pattern.quote("()"), "(" + elementCount + ")");

        DecimalFormat format = new DecimalFormat( "#,###,###,##0.#" );

        System.out.println(
            measurable + " is " + (precisedMeasured ? "" : "~") + format.format(roundedResult) + " bytes"
                + (oneElementSize > 0 ? " | one item is ~" + format.format(roundedOneElementSize) + " byte" + (roundedOneElementSize != 1d ? "s" : "") : "")
        );
    }
}
