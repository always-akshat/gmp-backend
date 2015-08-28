package com.getMyParking.service.auth;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import io.dropwizard.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.nio.charset.StandardCharsets;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
public class GMPAuthFactory extends AuthFactory<GMPCredentials, GMPUser> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GMPAuthFactory.class);
    public final static String CUSTOM_HEADER = "GMP_AUTH";
    private final boolean required;
    private final String realm;
    private String prefix = "Basic";
    private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

    @Context
    private HttpServletRequest request;

    public GMPAuthFactory(final GMPAuthenticator authenticator,
                            final String realm) {
        super(authenticator);
        this.required = true;
        this.realm = realm;
    }

    private GMPAuthFactory(final boolean required,
                             final GMPAuthenticator authenticator,
                             final String realm) {
        super(authenticator);
        this.required = required;
        this.realm = realm;
    }

    public GMPAuthFactory prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }


    public GMPAuthFactory responseBuilder(UnauthorizedHandler unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        return this;
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public AuthFactory<GMPCredentials, GMPUser> clone(boolean required) {
        return new GMPAuthFactory(required, (GMPAuthenticator) authenticator(), this.realm).prefix(prefix).responseBuilder(unauthorizedHandler);
    }

    @Override
    public GMPUser provide() {
        if (request != null) {
            final String header = request.getHeader(CUSTOM_HEADER);

            try {
                if (header != null) {
                    final Optional<GMPUser> result = authenticator().authenticate(new GMPCredentials(header));
                    if (result.isPresent()) {
                        return result.get();
                    }
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Error decoding credentials", e);
            } catch (AuthenticationException e) {
                LOGGER.warn("Error authenticating credentials", e);
                throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
            }
        }

        if (required) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        return null;
    }

    @Override
    public Class<GMPUser> getGeneratedClass() {
        return GMPUser.class;
    }
}
