package com.sportplus.pulsepower.community.application.internal.outboundservices.acl;

import com.sportplus.pulsepower.physiology.interfaces.acl.PhysiologyContextFacade;
import com.sportplus.pulsepower.sleep.interfaces.acl.SleepContextFacade;
import com.sportplus.pulsepower.training.interfaces.acl.TrainingContextFacade;
import com.sportplus.pulsepower.wellness.interfaces.acl.WellnessContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * The only place in the community context that talks to the other bounded
 * contexts, through their anti-corruption facades. Backs the streak and
 * achievement-earning rules, which treat a day as "active" if it has any
 * training, sleep, wellness or physiology record - matching the frontend's own
 * WorkspaceActivityHistory/WorkspaceReportSource composition.
 */
@Service
public class CommunityActivityGateway {

    private final TrainingContextFacade trainingContextFacade;
    private final SleepContextFacade sleepContextFacade;
    private final WellnessContextFacade wellnessContextFacade;
    private final PhysiologyContextFacade physiologyContextFacade;

    public CommunityActivityGateway(TrainingContextFacade trainingContextFacade,
                                    SleepContextFacade sleepContextFacade,
                                    WellnessContextFacade wellnessContextFacade,
                                    PhysiologyContextFacade physiologyContextFacade) {
        this.trainingContextFacade = trainingContextFacade;
        this.sleepContextFacade = sleepContextFacade;
        this.wellnessContextFacade = wellnessContextFacade;
        this.physiologyContextFacade = physiologyContextFacade;
    }

    public List<LocalDate> fetchRecordedDates(Long userId) {
        return java.util.stream.Stream.of(
                        trainingContextFacade.fetchRecordedDates(userId),
                        sleepContextFacade.fetchRecordedDates(userId),
                        wellnessContextFacade.fetchRecordedDates(userId),
                        physiologyContextFacade.fetchRecordedDates(userId))
                .flatMap(List::stream)
                .distinct()
                .toList();
    }
}
