package com.sportplus.pulsepower.wellness.domain.services;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetCheckInsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetHabitLogsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetHabitsByUserIdQuery;

import java.util.List;

public interface WellnessQueryService {

    List<WellnessCheckIn> handle(GetCheckInsByUserIdQuery query);

    List<WellnessHabit> handle(GetHabitsByUserIdQuery query);

    List<HabitLog> handle(GetHabitLogsByUserIdQuery query);
}
