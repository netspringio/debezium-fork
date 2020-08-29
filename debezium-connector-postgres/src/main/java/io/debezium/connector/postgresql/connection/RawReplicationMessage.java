/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.postgresql.connection;

import java.nio.ByteBuffer;

import org.postgresql.replication.LogSequenceNumber;

public class RawReplicationMessage {
    private final ByteBuffer buffer;
    private final LogSequenceNumber lsn;
    private final long seq;
    private static ThreadLocal<RawReplicationMessage> currentMessage = new ThreadLocal();

    RawReplicationMessage(ByteBuffer buffer, LogSequenceNumber lsn, long seq) {
        this.buffer = buffer;
        this.lsn = lsn;
        this.seq = seq;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public LogSequenceNumber getLsn() {
        return this.lsn;
    }

    public long getSeq() {
        return this.seq;
    }

    public static RawReplicationMessage getThreadLocal() {
        return currentMessage.get();
    }

    public void beginProcessing() {
        currentMessage.set(this);
    }

    public void endProcessing() {
        currentMessage.remove();
    }
}
