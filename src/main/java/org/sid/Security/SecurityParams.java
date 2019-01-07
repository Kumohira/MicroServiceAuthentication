package org.sid.Security;

public class SecurityParams {
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final String PRIVATE_SECRET = "kumohira";
    public static final long JWT_EXPIRATION = 10 * 24 * 3600;
    public static final String JWT_HEADER_PREFIX = "Bearer ";
}
