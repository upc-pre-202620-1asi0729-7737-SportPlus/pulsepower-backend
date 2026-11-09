package com.sportplus.pulsepower.sleep.application.internal.commandservices;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.BusinessRuleViolationException;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRecord;
import com.sportplus.pulsepower.sleep.domain.model.aggregates.SleepRoutine;
import com.sportplus.pulsepower.sleep.domain.model.commands.AddSleepRecordCommand;
import com.sportplus.pulsepower.sleep.domain.model.commands.DeleteSleepRecordCommand;
import com.sportplus.pulsepower.sleep.domain.model.commands.SetSleepRoutineCommand;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRecordRepository;
import com.sportplus.pulsepower.sleep.domain.repositories.SleepRoutineRepository;
import com.sportplus.pulsepower.sleep.domain.services.SleepCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SleepCommandServiceImpl implements SleepCommandService {

    private final SleepRecordRepository sleepRecordRepository;
    private final SleepRoutineRepository sleepRoutineRepository;
    private final CurrentUserProvider currentUserProvider;

    public SleepCommandServiceImpl(SleepRecordRepository sleepRecordRepository,
                                   SleepRoutineRepository sleepRoutineRepository,
                                   CurrentUserProvider currentUserProvider) {
        this.sleepRecordRepository = sleepRecordRepository;
        this.sleepRoutineRepository = sleepRoutineRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public SleepRecord handle(AddSleepRecordCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Sleep record");
        var record = new SleepRecord(command.userId(), command.startedAt(), command.endedAt(), command.source(),
                command.sleepScore(), command.notes());
        var overlaps = sleepRecordRepository.findAllByUserIdOrderByStartedAtDesc(command.userId()).stream()
                .anyMatch(record::overlaps);
        if (overlaps) {
            throw new BusinessRuleViolationException("This period overlaps an existing sleep record");
        }
        return sleepRecordRepository.save(record);
    }

    @Override
    public void handle(DeleteSleepRecordCommand command) {
        var record = sleepRecordRepository.findById(command.recordId())
                .orElseThrow(() -> new ResourceNotFoundException("Sleep record", command.recordId()));
        currentUserProvider.ensureOwnership(record.getUserId(), "Sleep record");
        sleepRecordRepository.deleteById(command.recordId());
    }

    @Override
    public SleepRoutine handle(SetSleepRoutineCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Sleep routine");
        var routine = sleepRoutineRepository.findByUserId(command.userId())
                .orElseGet(() -> new SleepRoutine(command.userId(), command.bedtime(), command.wakeTime(),
                        command.targetHours(), command.reminderEnabled()));
        routine.update(command.bedtime(), command.wakeTime(), command.targetHours(), command.reminderEnabled());
        return sleepRoutineRepository.save(routine);
    }
}
