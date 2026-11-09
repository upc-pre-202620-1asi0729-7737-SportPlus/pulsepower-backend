package com.sportplus.pulsepower.physiology.domain;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.GuidanceAlert;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalRecord;
import com.sportplus.pulsepower.physiology.domain.model.aggregates.WearableConnection;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.ConnectionStatus;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.Metric;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhysiologyAggregatesTest {

    private static final Instant NOW = Instant.parse("2026-11-07T12:00:00Z");

    @Test
    void newConnectionStartsConnectedWithoutSync() {
        var connection = new WearableConnection(1L, "  AIoTI ");
        assertEquals("AIoTI", connection.getProvider());
        assertEquals(ConnectionStatus.CONNECTED, connection.getStatus());
        assertNull(connection.getLastSyncedAt());
    }

    @Test
    void connectionRequiresOwnerProviderAndStatus() {
        assertThrows(IllegalArgumentException.class, () -> new WearableConnection(null, "AIoTI"));
        assertThrows(IllegalArgumentException.class, () -> new WearableConnection(1L, " "));
        assertThrows(IllegalArgumentException.class,
                () -> new WearableConnection(1L, 1L, "AIoTI", null, null));
    }

    @Test
    void disconnectAndSyncChangeStatus() {
        var connection = new WearableConnection(1L, "AIoTI");
        connection.disconnect();
        assertEquals(ConnectionStatus.DISCONNECTED, connection.getStatus());
        connection.recordSync(NOW);
        assertEquals(ConnectionStatus.CONNECTED, connection.getStatus());
        assertEquals(NOW, connection.getLastSyncedAt());
    }

    @Test
    void recordNormalizesUnitAndRequiresMandatoryFields() {
        var record = new PhysiologicalRecord(1L, Metric.HRV, 65.5, " ms ", NOW);
        assertEquals("ms", record.getUnit());
        assertEquals(65.5, record.getValue());
        assertEquals("", new PhysiologicalRecord(1L, Metric.HRV, 1, null, NOW).getUnit());
        assertThrows(IllegalArgumentException.class, () -> new PhysiologicalRecord(null, Metric.HRV, 1, "ms", NOW));
        assertThrows(IllegalArgumentException.class, () -> new PhysiologicalRecord(1L, null, 1, "ms", NOW));
        assertThrows(IllegalArgumentException.class, () -> new PhysiologicalRecord(1L, Metric.HRV, 1, "ms", null));
    }

    @Test
    void alertIsReviewedOnDemand() {
        var alert = new GuidanceAlert(1L, "  High strain ", "  Rest tomorrow ", NOW);
        assertEquals("High strain", alert.getTitle());
        assertEquals("Rest tomorrow", alert.getMessage());
        assertNull(alert.getReviewedAt());
        alert.markReviewed(NOW.plusSeconds(60));
        assertEquals(NOW.plusSeconds(60), alert.getReviewedAt());
    }

    @Test
    void alertRequiresOwnerTitleAndIssueTime() {
        assertThrows(IllegalArgumentException.class, () -> new GuidanceAlert(null, "T", "m", NOW));
        assertThrows(IllegalArgumentException.class, () -> new GuidanceAlert(1L, " ", "m", NOW));
        assertThrows(IllegalArgumentException.class, () -> new GuidanceAlert(1L, "T", "m", null));
        assertEquals("", new GuidanceAlert(1L, "T", null, NOW).getMessage());
    }
}
