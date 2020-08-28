/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.debezium.connector.postgresql.metrics;

import io.debezium.connector.base.ChangeEventQueueMetrics;
import io.debezium.connector.common.CdcSourceTaskContext;
import io.debezium.pipeline.metrics.DefaultChangeEventSourceMetricsFactory;
import io.debezium.pipeline.metrics.StreamingChangeEventSourceMetrics;
import io.debezium.pipeline.source.spi.EventMetadataProvider;

public class PostgresChangeEventSourceMetricsFactory
        extends DefaultChangeEventSourceMetricsFactory {
    private final ChangeEventQueueMetrics receiveQueueMetrics;

    public PostgresChangeEventSourceMetricsFactory(ChangeEventQueueMetrics receiveQueueMetrics) {
        this.receiveQueueMetrics = receiveQueueMetrics;
    }

    @Override
    public <T extends CdcSourceTaskContext> StreamingChangeEventSourceMetrics getStreamingMetrics(T taskContext,
                                                                                                  ChangeEventQueueMetrics changeEventQueueMetrics,
                                                                                                  EventMetadataProvider eventMetadataProvider) {
        return new PostgresStreamingChangeEventSourceMetrics(taskContext, changeEventQueueMetrics, receiveQueueMetrics, eventMetadataProvider);
    }
}
