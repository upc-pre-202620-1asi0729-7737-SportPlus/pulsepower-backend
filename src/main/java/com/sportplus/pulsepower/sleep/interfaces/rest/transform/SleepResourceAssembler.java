package com.sportplus.pulsepower.sleep.interfaces.rest.transform;

import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.model.valueobjects.SleepSource;
import com.sportplus.pulsepower.sleep.interfaces.rest.resources.SleepRecordResource;
import com.sportplus.pulsepower.sleep.interfaces.rest.resources.SleepRoutineResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;
import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class SleepResourceAssembler {

    private SleepResourceAssembler() {
    }

    public static SleepRecordResource toResource(SleepRecord record) {
        return new SleepRecordResource(record.getId(), record.getStartedAt().toString(),
                record.getEndedAt().toString(), toValue(record.getSource()), record.getSleepScore(),
                record.getNotes(), record.durationHours());
    }

    public static SleepRoutineResource toResource(SleepRoutine routine) {
        return new SleepRoutineResource(routine.getBedtime().toString(), routine.getWakeTime().toString(),
                routine.getTargetHours(), routine.isReminderEnabled());
    }

    public static SleepSource toSource(String value) {
        return toEnum(SleepSource.class, value);
    }
}
