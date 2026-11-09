package com.sportplus.pulsepower.wellness.domain.services;

import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.domain.model.commands.CreateHabitCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.DeleteHabitCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.SaveCheckInCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.ToggleHabitLogCommand;

public interface WellnessCommandService {

    WellnessCheckIn handle(SaveCheckInCommand command);

    WellnessHabit handle(CreateHabitCommand command);

    void handle(DeleteHabitCommand command);

    boolean handle(ToggleHabitLogCommand command);
}
