/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.postgresql.connection;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import io.debezium.connector.base.ChangeEventQueue;
import io.debezium.util.LoggingContext;

/**
 * A {@link ChangeEventQueue} implementation which buffers records until
 * directed to release to the {@link ChangeEventQueue#enqueue}.
 *
 * @param <T> type of record being queued, e.g. DataChangeEvent.
 */
public class BufferingChangeEventQueue<T> extends ChangeEventQueue<T> {
    private static ThreadLocal<BufferingContext> context = new ThreadLocal();

    public BufferingChangeEventQueue(
                                     Duration pollInterval, int maxQueueSize, int maxBatchSize,
                                     Supplier<LoggingContext.PreviousContext> loggingContextSupplier) {
        super(pollInterval, maxQueueSize, maxBatchSize, loggingContextSupplier);
    }

    @Override
    public void enqueue(T record) throws InterruptedException {
        if (record == null) {
            return;
        }

        // The calling thread has been interrupted, let's abort
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        // Check if there's a BufferingContext, if so, buffer.
        BufferingContext c = context.get();
        if (c != null) {
            if (c.queue == null) {
                c.queue = this;
            }
            c.buffer.add(record);
        }
        else {
            super.enqueue(record);
        }
    }

    private void addAll(LinkedList<T> buffer) throws InterruptedException {
        // Since super.enqueue() may throw an exception before actually enqueuing
        // the given element, we do not remove elem until enqueue has succeeded.
        while (!buffer.isEmpty()) {
            T elem = buffer.getFirst();
            super.enqueue((T) elem);
            buffer.removeFirst();
        }
    }

    /**
     * Starts buffering all records submitted via {@link #enqueue} in the
     * calling thread until {@link #endBuffering} is invoked.
     */
    public static void startBuffering() {
        context.set(new BufferingContext());
    }

    /**
     * Flushes all records buffered in this thread.
     * @throws InterruptedException
     */
    public static void flush() throws InterruptedException {
        BufferingContext c = context.get();
        if (c.queue != null) {
            c.queue.addAll(c.buffer);
        }
    }

    /**
     * Stops buffering.
     * Caller must re-invoke {@link #startBuffering()} to resume buffering.
     */
    public static void endBuffering() {
        context.remove();
    }

    private static class BufferingContext<T> {
        BufferingChangeEventQueue queue;
        private LinkedList<T> buffer = new LinkedList();
    }
}
