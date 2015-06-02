package com.getMyParking.service.auth;

import com.getMyParking.entity.UserB2BEntity;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class GMPAuthenticator implements Authenticator<GMPCredentials,GMPUser> {
    @Override
    public Optional<GMPUser> authenticate(GMPCredentials gmpCredentials) throws AuthenticationException {
        return null;
    }
}
