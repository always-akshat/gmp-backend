package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.AccessMasterDao;
import com.getMyParking.dao.StyleMasterDao;
import com.getMyParking.dao.SubLotTypeDao;
import com.getMyParking.entity.AccessMasterEntity;
import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.StyleMasterEntity;
import com.getMyParking.entity.SubLotTypeEntity;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by karan on 13/10/15.
 */
@Api(value = "/v1/master", description = "Company Resource")
@Path("/v1/master")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasterResource {

    private AccessMasterDao accessMasterDao;
    private SubLotTypeDao subLotTypeDao;
    private StyleMasterDao styleMasterDao;

    @Inject
    public MasterResource(AccessMasterDao accessMasterDao,SubLotTypeDao subLotTypeDao,StyleMasterDao styleMasterDao) {
        this.accessMasterDao = accessMasterDao;
        this.subLotTypeDao = subLotTypeDao;
        this.styleMasterDao = styleMasterDao;
    }

    @ApiOperation(value = "Get master list for access types", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @GET
    @Path("/access")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public List<AccessMasterEntity> getAccessMasterList(){
        return accessMasterDao.findAll();
    }

    @ApiOperation(value = "Get master list for sublot types", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @GET
    @Path("/sublot")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public List<SubLotTypeEntity> getSublotTypeMasterList(){
        return subLotTypeDao.findAll();
    }

    @GET
    @Path("/style")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public List<StyleMasterEntity> getStyleMasterList(){
        return styleMasterDao.findAll();
    }
}
