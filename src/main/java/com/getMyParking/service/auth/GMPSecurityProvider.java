package com.getMyParking.service.auth;

/**
 * Created by rahulgupta.s on 03/06/15.
 */

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public class GMPSecurityProvider implements InjectableProvider<Auth, Parameter> {

    public final static String CUSTOM_HEADER = "GMP_AUTH";

    private final GMPAuthenticator authenticator;

    @Inject
    public GMPSecurityProvider(GMPAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Auth auth, Parameter parameter) {
        return new GMPSecurityInjectable<GMPUser>(authenticator, auth.required());
    }

    private static class GMPSecurityInjectable<T> extends AbstractHttpContextInjectable<T> {

        private final Authenticator<GMPCredentials, T> authenticator;
        private final boolean required;

        private GMPSecurityInjectable(Authenticator<GMPCredentials, T> authenticator, boolean required) {
            this.authenticator = authenticator;
            this.required = required;
        }

        @Override
        public T getValue(HttpContext c) {
            // This is where the credentials are extracted from the request
            final String header = c.getRequest().getHeaderValue(CUSTOM_HEADER);
            try {
                if (header != null) {
                    final Optional<T> result = authenticator.authenticate(new GMPCredentials(header));
                    if (result.isPresent()) {
                        return result.get();
                    }
                }
            } catch (AuthenticationException e) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            if (required) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            return null;
        }
    }
}
