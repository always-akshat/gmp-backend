package com.getMyParking.service.auth;

/**
 * Created by rahulgupta.s on 03/06/15.
 */

import com.google.common.base.Optional;
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

/**
 * An example security provider that will look at each request when received by an endpoint using the auth attribute
 * and check that it has a header value containing a token and will authenticate the token to get the Principle (User)
 * for the request (otherwise throw an AuthenticationException). That Principle is the authenticated User associated
 * with the request and the resource method handling the request can use it to check authorisation to perform actions.
 *
 * @param <T> The Principle class (User) to be returned when a request is authenticated
 */
public class GMPSecurityProvider<T> implements InjectableProvider<Auth, Parameter> {

    public final static String CUSTOM_HEADER = "custom-security-token";

    private final Authenticator<GMPCredentials, T> authenticator;

    public GMPSecurityProvider(Authenticator<GMPCredentials, T> authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Auth auth, Parameter parameter) {
        return new GMPSecurityInjectable<T>(authenticator, auth.required());
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
