package com.sportplus.pulsepower.wellness.interfaces.rest.transform;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.HabitLogResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.WellnessCheckInResource;
import com.sportplus.pulsepower.wellness.interfaces.rest.resources.WellnessHabitResource;

public final class WellnessResourceAssembler {

    private WellnessResourceAssembler() {
    }

    public static WellnessCheckInResource toResource(WellnessCheckIn checkIn) {
        return new WellnessCheckInResource(checkIn.getId(), checkIn.getDate().toString(), checkIn.getMood(),
                checkIn.getStress(), checkIn.isDiscomfort(), checkIn.getNotes());
    }

    public static WellnessHabitResource toResource(WellnessHabit habit) {
        return new WellnessHabitResource(habit.getId(), habit.getName(), habit.getWeeklyTarget());
    }

    public static HabitLogResource toResource(HabitLog log) {
        return new HabitLogResource(log.getHabitId(), log.getDate().toString());
    }
}
