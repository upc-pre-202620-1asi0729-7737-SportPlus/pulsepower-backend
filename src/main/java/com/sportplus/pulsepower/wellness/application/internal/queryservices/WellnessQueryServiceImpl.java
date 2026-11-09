package com.sportplus.pulsepower.wellness.application.internal.queryservices;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetCheckInsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetHabitLogsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetHabitsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.repositories.HabitLogRepository;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessCheckInRepository;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessHabitRepository;
import com.sportplus.pulsepower.wellness.domain.services.WellnessQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WellnessQueryServiceImpl implements WellnessQueryService {

    private final WellnessCheckInRepository checkInRepository;
    private final WellnessHabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;

    public WellnessQueryServiceImpl(WellnessCheckInRepository checkInRepository, WellnessHabitRepository habitRepository,
                                    HabitLogRepository habitLogRepository) {
        this.checkInRepository = checkInRepository;
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
    }

    @Override
    public List<WellnessCheckIn> handle(GetCheckInsByUserIdQuery query) {
        return checkInRepository.findAllByUserIdOrderByDateDesc(query.userId());
    }

    @Override
    public List<WellnessHabit> handle(GetHabitsByUserIdQuery query) {
        return habitRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<HabitLog> handle(GetHabitLogsByUserIdQuery query) {
        return habitLogRepository.findAllByUserId(query.userId());
    }
}
