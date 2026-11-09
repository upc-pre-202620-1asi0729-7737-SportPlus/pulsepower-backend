package com.sportplus.pulsepower.sleep.domain.services;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.model.commands.AddSleepRecordCommand;
import com.sportplus.pulsepower.sleep.domain.model.commands.DeleteSleepRecordCommand;
import com.sportplus.pulsepower.sleep.domain.model.commands.SetSleepRoutineCommand;

public interface SleepCommandService {

    SleepRecord handle(AddSleepRecordCommand command);

    void handle(DeleteSleepRecordCommand command);

    SleepRoutine handle(SetSleepRoutineCommand command);
}
