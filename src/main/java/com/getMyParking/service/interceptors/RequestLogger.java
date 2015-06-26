package com.getMyParking.service.interceptors;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.core.MultivaluedMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class RequestLogger implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    private void setUidForRequest(ContainerRequest containerRequest) {

        MultivaluedMap<String, String> requestHeaders = containerRequest.getRequestHeaders();
        logger.info("Request headers : ");

        for(MultivaluedMap.Entry entry : requestHeaders.entrySet())
            logger.info(entry.getKey() + " : " + entry.getValue());


        logger.info("Method : " + containerRequest.getMethod() + "  Request url :" + containerRequest.getRequestUri().toString());
    }


    public ContainerRequest filter(ContainerRequest containerRequest) {
        setUidForRequest(containerRequest);
        return containerRequest;
    }
}
