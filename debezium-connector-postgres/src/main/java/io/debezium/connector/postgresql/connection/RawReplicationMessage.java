/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.postgresql.connection;

import java.nio.ByteBuffer;

import org.postgresql.replication.LogSequenceNumber;

/**
 * Raw replication message read from a replication stream.
 */
public class RawReplicationMessage {
    /** Payload of the message. */
    private final ByteBuffer buffer;
    /** LSN of this message. */
    private final LogSequenceNumber lsn;
    /**
     * Sequence number associated with this message as assigned by the stream reader.
     * This is used for serializing messages downstream.
     */
    private final long seq;

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
}
