/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.postgresql.connection;

import java.nio.ByteBuffer;

import org.postgresql.replication.LogSequenceNumber;

public class RawReplicationMessage {
    private ByteBuffer buffer;
    private LogSequenceNumber lsn;

    RawReplicationMessage(ByteBuffer buffer, LogSequenceNumber lsn) {
        this.buffer = buffer;
        this.lsn = lsn;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public LogSequenceNumber getLsn() {
        return this.lsn;
    }
}
