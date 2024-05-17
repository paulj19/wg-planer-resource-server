package com.wgplaner.registration;

import com.wgplaner.core.AuthServer;

public record UserProfileDto(Long id, String username, String email, String floorId, Long oid, AuthServer authServer) {}
