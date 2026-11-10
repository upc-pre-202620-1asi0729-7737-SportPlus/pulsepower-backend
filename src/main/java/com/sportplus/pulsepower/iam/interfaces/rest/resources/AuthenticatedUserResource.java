package com.sportplus.pulsepower.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(UserResource account, String token) {
}
