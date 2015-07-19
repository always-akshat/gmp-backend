package com.getMyParking.service.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class ResponseFilter implements ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);
    private ObjectMapper objectMapper;

    @Inject
    public ResponseFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        Object response  = containerResponseContext.getEntity();
        logger.info("Response :- \n {}",objectMapper.writeValueAsString(response));
    }
}