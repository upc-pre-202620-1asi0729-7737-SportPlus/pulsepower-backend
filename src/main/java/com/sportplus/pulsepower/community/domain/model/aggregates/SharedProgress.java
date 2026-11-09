package com.sportplus.pulsepower.community.domain.model.aggregates;

import com.sportplus.pulsepower.community.domain.model.valueobjects.Audience;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

/**
 * A private progress note the user may write and optionally mark as shared with
 * a group or a contact audience. Visibility is always private - sharing here only
 * records intent, consistent with the frontend's own demo-mode-only sharing.
 */
public class SharedProgress {

    private final Long id;
    private final Long userId;
    private String title;
    private String content;
    private final Instant createdAt;
    private final Set<Audience> audiences;

    public SharedProgress(Long userId, String title, String content, Instant createdAt) {
        this(null, userId, title, content, createdAt, EnumSet.noneOf(Audience.class));
    }

    public SharedProgress(Long id, Long userId, String title, String content, Instant createdAt,
                          Set<Audience> audiences) {
        if (userId == null) throw new IllegalArgumentException("A progress note must belong to a user");
        if (title == null || title.isBlank() || title.length() > 100)
            throw new IllegalArgumentException("Title must be between 1 and 100 characters");
        if (content == null || content.isBlank() || content.length() > 2000)
            throw new IllegalArgumentException("Progress note must be between 1 and 2000 characters");
        this.id = id;
        this.userId = userId;
        this.title = title.trim();
        this.content = content.trim();
        this.createdAt = createdAt;
        this.audiences = EnumSet.noneOf(Audience.class);
        if (audiences != null) this.audiences.addAll(audiences);
    }

    public void share(Audience audience) {
        audiences.add(audience);
    }

    public void revoke(Audience audience) {
        audiences.remove(audience);
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public Instant getCreatedAt() { return createdAt; }

    public Set<Audience> getAudiences() { return EnumSet.copyOf(audiences); }
}
