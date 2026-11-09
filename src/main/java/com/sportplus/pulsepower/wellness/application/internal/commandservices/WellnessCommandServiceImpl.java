package com.sportplus.pulsepower.wellness.application.internal.commandservices;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.HabitLog;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessCheckIn;
import com.sportplus.pulsepower.wellness.domain.model.aggregates.WellnessHabit;
import com.sportplus.pulsepower.wellness.domain.model.commands.CreateHabitCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.DeleteHabitCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.SaveCheckInCommand;
import com.sportplus.pulsepower.wellness.domain.model.commands.ToggleHabitLogCommand;
import com.sportplus.pulsepower.wellness.domain.repositories.HabitLogRepository;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessCheckInRepository;
import com.sportplus.pulsepower.wellness.domain.repositories.WellnessHabitRepository;
import com.sportplus.pulsepower.wellness.domain.services.WellnessCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WellnessCommandServiceImpl implements WellnessCommandService {

    private final WellnessCheckInRepository checkInRepository;
    private final WellnessHabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final CurrentUserProvider currentUserProvider;

    public WellnessCommandServiceImpl(WellnessCheckInRepository checkInRepository, WellnessHabitRepository habitRepository,
                                      HabitLogRepository habitLogRepository, CurrentUserProvider currentUserProvider) {
        this.checkInRepository = checkInRepository;
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public WellnessCheckIn handle(SaveCheckInCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Wellness check-in");
        var existingId = checkInRepository.findByUserIdAndDate(command.userId(), command.date())
                .map(WellnessCheckIn::getId).orElse(null);
        var checkIn = new WellnessCheckIn(existingId, command.userId(), command.date(), command.mood(),
                command.stress(), command.discomfort(), command.notes());
        return checkInRepository.save(checkIn);
    }

    @Override
    public WellnessHabit handle(CreateHabitCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Wellness habit");
        return habitRepository.save(new WellnessHabit(command.userId(), command.name(), command.weeklyTarget()));
    }

    @Override
    public void handle(DeleteHabitCommand command) {
        var habit = habitRepository.findById(command.habitId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit", command.habitId()));
        currentUserProvider.ensureOwnership(habit.getUserId(), "Wellness habit");
        habitLogRepository.deleteAllByHabitId(command.habitId());
        habitRepository.deleteById(command.habitId());
    }

    @Override
    public boolean handle(ToggleHabitLogCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Habit log");
        var habit = habitRepository.findById(command.habitId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit", command.habitId()));
        currentUserProvider.ensureOwnership(habit.getUserId(), "Wellness habit");
        var existing = habitLogRepository.findByHabitIdAndDate(command.habitId(), command.date());
        if (existing.isPresent()) {
            habitLogRepository.delete(existing.get());
            return false;
        }
        habitLogRepository.save(new HabitLog(command.userId(), command.habitId(), command.date()));
        return true;
    }
}
