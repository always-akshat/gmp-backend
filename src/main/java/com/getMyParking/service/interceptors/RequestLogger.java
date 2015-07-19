package com.getMyParking.service.interceptors;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class RequestLogger implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    private void setUidForRequest(ContainerRequestContext containerRequest) {

        MultivaluedMap<String, String> requestHeaders = containerRequest.getHeaders();
        logger.info("Request headers : ");

        for(MultivaluedMap.Entry entry : requestHeaders.entrySet())
            logger.info(entry.getKey() + " : " + entry.getValue());

        logger.info("Method : " + containerRequest.getMethod() + "  Request url :" + containerRequest.getUriInfo().toString());
        try {
            String body = IOUtils.toString(containerRequest.getEntityStream());
            logger.info("body : {}", body);
        } catch (Exception e) {

        }

    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        setUidForRequest(containerRequestContext);
    }
}
