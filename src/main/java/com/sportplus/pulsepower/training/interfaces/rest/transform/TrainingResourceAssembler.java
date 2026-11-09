package com.sportplus.pulsepower.training.interfaces.rest.transform;

import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingPlan;
import com.sportplus.pulsepower.training.domain.model.aggregates.TrainingSession;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import com.sportplus.pulsepower.training.interfaces.rest.resources.AddPlannedActivityResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.PlannedActivityResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.RecordTrainingSessionResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.TrainingPlanResource;
import com.sportplus.pulsepower.training.interfaces.rest.resources.TrainingSessionResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;
import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class TrainingResourceAssembler {

    private TrainingResourceAssembler() {
    }

    public static TrainingPlanResource toResource(TrainingPlan plan) {
        var activities = plan.getActivities().stream()
                .map(activity -> new PlannedActivityResource(activity.id(), activity.date().toString(),
                        activity.startTime() == null ? null : activity.startTime().toString(), activity.activity(),
                        activity.durationMinutes(), toValue(activity.intensity()), toValue(activity.kind()),
                        toValue(activity.status())))
                .toList();
        var period = plan.getPeriod();
        return new TrainingPlanResource(plan.getId(), plan.getName(), period == null ? null : period.from().toString(),
                period == null ? null : period.to().toString(), activities);
    }

    public static TrainingSessionResource toResource(TrainingSession session) {
        return new TrainingSessionResource(session.getId(), session.getDate().toString(), session.getActivity(),
                session.getDurationMinutes(), session.getPerceivedEffort(), session.getNotes(),
                session.getPlannedActivityId());
    }

    public static Intensity toIntensity(String value) {
        return toEnum(Intensity.class, value);
    }

    public static ActivityKind toKind(String value) {
        return toEnum(ActivityKind.class, value);
    }
}
