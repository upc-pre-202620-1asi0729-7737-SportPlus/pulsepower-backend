package com.sportplus.pulsepower.physiology.application.internal.queryservices;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalOverview;
import com.sportplus.pulsepower.physiology.domain.model.queries.GetPhysiologicalOverviewByUserIdQuery;
import com.sportplus.pulsepower.physiology.domain.repositories.GuidanceAlertRepository;
import com.sportplus.pulsepower.physiology.domain.repositories.PhysiologicalRecordRepository;
import com.sportplus.pulsepower.physiology.domain.repositories.WearableConnectionRepository;
import com.sportplus.pulsepower.physiology.domain.services.PhysiologyQueryService;
import com.sportplus.pulsepower.physiology.domain.services.RecoveryCalculator;
import com.sportplus.pulsepower.physiology.domain.model.valueobjects.ConnectionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PhysiologyQueryServiceImpl implements PhysiologyQueryService {

    private static final int RECENT_RECORDS_LIMIT = 50;

    private final WearableConnectionRepository wearableConnectionRepository;
    private final PhysiologicalRecordRepository physiologicalRecordRepository;
    private final GuidanceAlertRepository guidanceAlertRepository;

    public PhysiologyQueryServiceImpl(WearableConnectionRepository wearableConnectionRepository,
                                      PhysiologicalRecordRepository physiologicalRecordRepository,
                                      GuidanceAlertRepository guidanceAlertRepository) {
        this.wearableConnectionRepository = wearableConnectionRepository;
        this.physiologicalRecordRepository = physiologicalRecordRepository;
        this.guidanceAlertRepository = guidanceAlertRepository;
    }

    @Override
    public PhysiologicalOverview handle(GetPhysiologicalOverviewByUserIdQuery query) {
        var connection = wearableConnectionRepository.findByUserId(query.userId()).orElse(null);
        if (connection == null) {
            return new PhysiologicalOverview(null, List.of(), null, List.of(), List.of(), false);
        }

        var allRecords = physiologicalRecordRepository.findAllByUserIdOrderByRecordedAtDesc(query.userId());
        var records = allRecords.size() > RECENT_RECORDS_LIMIT
                ? allRecords.subList(0, RECENT_RECORDS_LIMIT)
                : allRecords;
        var assessment = RecoveryCalculator.assess(records).orElse(null);
        var recommendations = RecoveryCalculator.recommend(assessment);
        var alerts = guidanceAlertRepository.findAllByUserIdOrderByIssuedAtDesc(query.userId());
        var available = connection.getStatus() == ConnectionStatus.CONNECTED;

        return new PhysiologicalOverview(connection, records, assessment, recommendations, alerts, available);
    }
}
