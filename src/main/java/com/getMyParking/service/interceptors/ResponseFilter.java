package com.getMyParking.service.interceptors;

/**
 * User: shanthoosh
 * Date: 29/1/13
 * Time: 12:15 AM
 */

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.core.Response;

//import org.apache.log4j.MDC;

public class ResponseFilter implements ContainerResponseFilter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    public ContainerResponse filter(ContainerRequest containerRequest, ContainerResponse containerResponse) {
        Response.ResponseBuilder response = Response.fromResponse(containerResponse.getResponse());
        response.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        String requestHeader = containerRequest.getHeaderValue("Access-Control-Request-Headers");
        if (null != requestHeader) {
            logger.info("setting access control headers.");
            response.header("Access-Control-Allow-Headers", requestHeader);
        }
        containerResponse.setResponse(response.build());
        removeUidForRequest();
        return containerResponse;
    }

    private void removeUidForRequest() {
        MDC.remove("id");
        logger.info("Request Complete.");
    }
}