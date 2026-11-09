package com.sportplus.pulsepower.sleep.domain.model.aggregates;

import java.time.LocalTime;

public class SleepRoutine {

    private final Long userId;
    private LocalTime bedtime;
    private LocalTime wakeTime;
    private double targetHours;
    private boolean reminderEnabled;

    public SleepRoutine(Long userId, LocalTime bedtime, LocalTime wakeTime, double targetHours,
                        boolean reminderEnabled) {
        if (userId == null) throw new IllegalArgumentException("A sleep routine must belong to a user");
        update(bedtime, wakeTime, targetHours, reminderEnabled);
        this.userId = userId;
    }

    public void update(LocalTime bedtime, LocalTime wakeTime, double targetHours, boolean reminderEnabled) {
        if (bedtime == null || wakeTime == null) throw new IllegalArgumentException("Choose valid bedtime and wake-up times");
        if (targetHours < 1 || targetHours > 24) throw new IllegalArgumentException("Sleep target must be between 1 and 24 hours");
        this.bedtime = bedtime;
        this.wakeTime = wakeTime;
        this.targetHours = targetHours;
        this.reminderEnabled = reminderEnabled;
    }

    public Long getUserId() { return userId; }

    public LocalTime getBedtime() { return bedtime; }

    public LocalTime getWakeTime() { return wakeTime; }

    public double getTargetHours() { return targetHours; }

    public boolean isReminderEnabled() { return reminderEnabled; }
}
