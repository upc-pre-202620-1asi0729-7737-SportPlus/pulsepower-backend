package com.sportplus.pulsepower.sleep.application.internal.queryservices;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRoutineByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRecordRepository;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRoutineRepository;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SleepQueryServiceImpl implements SleepQueryService {

    private final SleepRecordRepository sleepRecordRepository;
    private final SleepRoutineRepository sleepRoutineRepository;

    public SleepQueryServiceImpl(SleepRecordRepository sleepRecordRepository,
                                 SleepRoutineRepository sleepRoutineRepository) {
        this.sleepRecordRepository = sleepRecordRepository;
        this.sleepRoutineRepository = sleepRoutineRepository;
    }

    @Override
    public List<SleepRecord> handle(GetSleepRecordsByUserIdQuery query) {
        return sleepRecordRepository.findAllByUserIdOrderByStartedAtDesc(query.userId());
    }

    @Override
    public Optional<SleepRoutine> handle(GetSleepRoutineByUserIdQuery query) {
        return sleepRoutineRepository.findByUserId(query.userId());
    }
}
