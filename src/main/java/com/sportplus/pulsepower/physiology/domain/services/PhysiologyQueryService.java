package com.sportplus.pulsepower.physiology.domain.services;

import com.sportplus.pulsepower.physiology.domain.model.aggregates.PhysiologicalOverview;
import com.sportplus.pulsepower.physiology.domain.model.queries.GetPhysiologicalOverviewByUserIdQuery;

public interface PhysiologyQueryService {

    PhysiologicalOverview handle(GetPhysiologicalOverviewByUserIdQuery query);
}
