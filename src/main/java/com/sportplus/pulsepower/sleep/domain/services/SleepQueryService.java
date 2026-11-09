package com.sportplus.pulsepower.sleep.domain.services;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRoutineByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface SleepQueryService {

    List<SleepRecord> handle(GetSleepRecordsByUserIdQuery query);

    Optional<SleepRoutine> handle(GetSleepRoutineByUserIdQuery query);
}
