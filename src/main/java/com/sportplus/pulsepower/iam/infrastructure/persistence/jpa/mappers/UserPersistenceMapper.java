package com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.mappers;

import com.sportplus.pulsepower.iam.domain.model.aggregates.User;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.DisplayName;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.EmailAddress;
import com.sportplus.pulsepower.iam.domain.model.valueobjects.PersonalGoal;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities.PersonalGoalEntity;
import com.sportplus.pulsepower.iam.infrastructure.persistence.jpa.entities.UserEntity;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static UserEntity toEntity(User user) {
        var entity = new UserEntity(user.getEmail().address(), user.getPasswordHash(),
                user.getDisplayName() == null ? null : user.getDisplayName().value(), user.getFocus());
        entity.setId(user.getId());
        entity.setProfile(user.getDisplayName() == null ? null : user.getDisplayName().value(), user.getFocus(),
                user.getAge(), user.getWeightKg(), user.getHeightCm(), user.getMainSport(),
                user.getTrainingRoutine(), user.getPhysicalHistory());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setStatus(user.getStatus());
        entity.setGoals(user.getGoals().stream()
                .map(goal -> new PersonalGoalEntity(goal.id(), goal.description(), goal.completed()))
                .toList());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        var goals = entity.getGoals().stream()
                .map(goal -> new PersonalGoal(goal.getId(), goal.getDescription(), goal.isCompleted()))
                .toList();
        return new User(entity.getId(), new EmailAddress(entity.getEmail()), entity.getPasswordHash(),
                new DisplayName(entity.getDisplayName()), entity.getFocus(), entity.getAge(), entity.getWeightKg(),
                entity.getHeightCm(), entity.getMainSport(), entity.getTrainingRoutine(),
                entity.getPhysicalHistory(), entity.getStatus(), goals);
    }
}
