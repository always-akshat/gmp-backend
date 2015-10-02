package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.*;
import com.getMyParking.entity.*;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Strings;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.DateTimeParam;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by rahulgupta.s on 04/06/15.
 */
@Api(value = "/v1/user", description = "User Resource")
@Path("/v1/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserB2BDAO userB2BDAO;
    private SessionDAO sessionDAO;
    private ParkingLotHasUserB2BDAO parkingLotHasUserB2BDAO;
    private LoadingCache<String,GMPUser> authTokenCache;
    private CompanyDAO companyDAO;
    private ParkingEventDAO parkingEventDAO;

    @Inject
    public UserResource(UserB2BDAO userB2BDAO, SessionDAO sessionDAO, ParkingLotHasUserB2BDAO parkingLotHasUserB2BDAO,
                        CompanyDAO companyDAO,@Named("authTokenCache")LoadingCache<String, GMPUser> authTokenCache,
                        ParkingEventDAO parkingEventDAO) {
        this.userB2BDAO = userB2BDAO;
        this.sessionDAO = sessionDAO;
        this.parkingLotHasUserB2BDAO = parkingLotHasUserB2BDAO;
        this.authTokenCache = authTokenCache;
        this.companyDAO = companyDAO;
        this.parkingEventDAO = parkingEventDAO;
    }

    @ApiOperation(value = "Login User Api", response = GMPUser.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @POST
    @Path("/login")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public GMPUser login(@FormParam("username") String userName, @FormParam("password") String password) throws AuthenticationException {
        if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password)) {

            UserB2BEntity userB2BEntity = userB2BDAO.loginUser(userName, password);
            if (userB2BEntity == null) {
                throw new WebApplicationException("Invalid Credentials",Response.Status.UNAUTHORIZED);
            }

            long millis = DateTime.now().getMillis();
            String authToken = millis + UUID.randomUUID().toString().substring(0,6);
            DateTime validTillDate = DateTime.now().plusYears(3);
            SessionEntity sessionEntity = new SessionEntity(authToken,validTillDate,userB2BEntity);
            sessionDAO.saveSession(sessionEntity);

            GMPUser user = new GMPUser(userB2BEntity,authToken,sessionEntity.getValidTime());
            authTokenCache.put(authToken,user);
            return user;
        } else {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Change Password Api", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @POST
    @Path("/changePassword")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public void changePassword( @Auth GMPUser gmpUser, @FormParam("oldPassword") String oldPassword,
                                @FormParam("newPassword") String newPassword) {
        if (!Strings.isNullOrEmpty(oldPassword) && !Strings.isNullOrEmpty(newPassword)) {
            UserB2BEntity user = userB2BDAO.findById(gmpUser.getUserName());
            if (user != null && user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userB2BDAO.updateUser(user);
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } else {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Create User Api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Username already exists"),
    })
    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(@ApiParam(value = "Create User Object", required = true) @Valid UserB2BEntity user) {
        userB2BDAO.saveUser(user);
    }

    @ApiOperation(value = "Add parking sub lot to user Api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Username already exists"),
    })
    @POST
    @Path("/{username}/parking_sub_lot")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void addParkingSubLot(@ApiParam(value = "List of Parking Lot Ids", required = true)
                              List<ParkingSubLotUserAccessEntity> parkingSubLotUserAccessList,
                              @PathParam("username") String username,
                              @Auth GMPUser gmpUser) {
        List<Integer> companyIds = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity entity : parkingSubLotUserAccessList) {
            if (!companyIds.contains(entity.getCompanyId())) entity.getCompanyId();
        }

        if (gmpUser.getCompanyIds().containsAll(companyIds)) {
            UserB2BEntity user = userB2BDAO.findById(username);
            if (user != null) {
                parkingLotHasUserB2BDAO.saveUserAccess(parkingSubLotUserAccessList);
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "Get User Access Data",  response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Username already exists"),
    })
    @GET
    @Path("/access")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<CompanyEntity> getUserAccessData(@Auth GMPUser gmpUser) {
        List<CompanyEntity> responseCompanies = Lists.newArrayList();
        for (Integer companyId : gmpUser.getCompanyIds()) {
            CompanyEntity companyEntity = companyDAO.findById(companyId);
            Set<ParkingEntity> parkingEntities = Sets.newHashSet();

            for (ParkingEntity parking : companyEntity.getParkings()) {

                if (gmpUser.getParkingIds().contains(parking.getId())) {
                    parkingEntities.add(parking);
                    Set<ParkingLotEntity> parkingLotEntities = Sets.newHashSet();

                    for (ParkingLotEntity parkingLot : parking.getParkingLots()) {

                        if (gmpUser.getParkingLotIds().contains(parkingLot.getId())) {
                            parkingLotEntities.add(parkingLot);
                            Set<ParkingSubLotEntity> parkingSubLotEntities = Sets.newHashSet();

                            for (ParkingSubLotEntity parkingSubLot : parkingLot.getParkingSubLots()) {

                                if (gmpUser.getParkingSubLotIds().contains(parkingSubLot.getId())) {
                                    parkingSubLotEntities.add(parkingSubLot);
                                }
                            }
                            parkingLot.setParkingSubLots(parkingSubLotEntities);
                        }
                    }
                    parking.setParkingLots(parkingLotEntities);
                }
            }
            companyEntity.setParkings(parkingEntities);
            responseCompanies.add(companyEntity);
        }
        return responseCompanies;
    }

    @Path("/{operatorName}/report")
    @GET
    @Timed
    @UnitOfWork
    @ApiOperation(value = "Report by Operator Name", response = ParkingReport.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ParkingReport report( @PathParam("operatorName") String operatorName,
                                 @QueryParam("from")DateTimeParam fromDate, @QueryParam("to")DateTimeParam toDate) {
        return parkingEventDAO.createUserReport(operatorName,fromDate.get(),toDate.get());
    }

}
