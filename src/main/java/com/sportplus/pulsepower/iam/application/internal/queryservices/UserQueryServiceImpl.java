package com.sportplus.pulsepower.iam.application.internal.queryservices;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.queries.GetUserByEmailQuery;
import com.sportplus.pulsepower.iam.domain.model.queries.GetUserByIdQuery;
import com.sportplus.pulsepower.iam.domain.repositories.UserRepository;
import com.sportplus.pulsepower.iam.domain.services.UserQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }
}
