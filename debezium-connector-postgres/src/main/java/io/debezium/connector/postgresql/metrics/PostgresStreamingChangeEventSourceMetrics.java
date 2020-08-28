/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.postgresql.metrics;

import io.debezium.connector.base.ChangeEventQueueMetrics;
import io.debezium.connector.common.CdcSourceTaskContext;
import io.debezium.pipeline.metrics.StreamingChangeEventSourceMetrics;
import io.debezium.pipeline.source.spi.EventMetadataProvider;

public class PostgresStreamingChangeEventSourceMetrics
        extends StreamingChangeEventSourceMetrics implements PostgresStreamingChangeEventSourceMetricsMBean {
    private final ChangeEventQueueMetrics receiveQueueMetrics;

    public <T extends CdcSourceTaskContext> PostgresStreamingChangeEventSourceMetrics(T taskContext,
                                                                                      ChangeEventQueueMetrics changeEventQueueMetrics,
                                                                                      ChangeEventQueueMetrics receiveQueueMetrics,
                                                                                      EventMetadataProvider metadataProvider) {
        super(taskContext, changeEventQueueMetrics, metadataProvider);
        this.receiveQueueMetrics = receiveQueueMetrics;
    }

    @Override
    public long getReceiveQueueCapacity() {
        return receiveQueueMetrics.totalCapacity();
    }

    @Override
    public long getReceiveQueueRemainingCapacity() {
        return receiveQueueMetrics.remainingCapacity();
    }
}