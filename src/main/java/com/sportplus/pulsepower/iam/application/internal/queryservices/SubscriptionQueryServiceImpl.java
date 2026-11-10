package com.sportplus.pulsepower.iam.application.internal.queryservices;

import com.sportplus.pulsepower.iam.domain.model.aggregates.Subscription;
import com.sportplus.pulsepower.iam.domain.model.aggregates.SubscriptionPlan;
import com.sportplus.pulsepower.iam.domain.model.queries.GetAllSubscriptionPlansQuery;
import com.sportplus.pulsepower.iam.domain.model.queries.GetSubscriptionByUserIdQuery;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionPlanRepository;
import com.sportplus.pulsepower.iam.domain.repositories.SubscriptionRepository;
import com.sportplus.pulsepower.iam.domain.services.SubscriptionQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository,
                                        SubscriptionRepository subscriptionRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query) {
        return subscriptionPlanRepository.findAll();
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByUserIdQuery query) {
        return subscriptionRepository.findByUserId(query.userId());
    }
}
