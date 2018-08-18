package org.spdukraine.javameetup.performance;

import java.util.HashMap;
import java.util.Map;

/**
 * Use following JVM arguments:
 *      -Xmx4G -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime
 * Check number against: Total time for which application threads were stopped: #.####### seconds
 * It shows how long application was not working at all
 *
 * The main idea of the example is to create conditions when lots of mid-aged objects are created and then refreshed.
 * Discarded version are not in YoungGen and moves to OldGen as it survived few cycles. And now requires full GC to be collected.
 * Because we have lots of them - system needs a significant time to perform a collections.
 * With growing memory size we can reach a point when "stop the world" will take several seconds and even more....
 */
public class GCKiller {
    public static void main(String[] args) {
        Map<Integer, int[]> holder = new HashMap<>();

        for (int i = 1; i <= 10; ++i) {
            System.out.println("******************************************");
            System.out.println("* Round " + i);
            System.out.println("******************************************");

            //iterating over 1M of cases
            for (int n = 0; n < 1_000_000; n += 1) {
                //create a massive object for each case
                int[] a = new int[700];
                //just store object for a this loop and then rewrite on next iteration
                holder.put(n, a);
            }
        }
    }
}
