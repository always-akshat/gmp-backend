package com.getMyParking.service.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.getMyParking.dao.ParkingLotHasUserB2BDAO;
import com.getMyParking.dao.SessionDAO;
import com.getMyParking.dao.UserB2BDAO;
import com.getMyParking.entity.ParkingLotHasUserB2BEntity;
import com.getMyParking.entity.SessionEntity;
import com.getMyParking.entity.UserB2BEntity;
import com.getMyParking.service.auth.GMPUser;
import com.google.common.base.Strings;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by rahulgupta.s on 04/06/15.
 */
@Api(value = "/v1/user", description = "User Resource" , authorizations = {
        @Authorization(type = "Simple Auth", value = "GMP_AUTH")
})
@Path("/v1/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserB2BDAO userB2BDAO;
    private SessionDAO sessionDAO;
    private ParkingLotHasUserB2BDAO parkingLotHasUserB2BDAO;
    private LoadingCache<String,GMPUser> authTokenCache;

    @Inject
    public UserResource(UserB2BDAO userB2BDAO, SessionDAO sessionDAO, ParkingLotHasUserB2BDAO parkingLotHasUserB2BDAO,
                        @Named("authTokenCache")LoadingCache<String, GMPUser> authTokenCache) {
        this.userB2BDAO = userB2BDAO;
        this.sessionDAO = sessionDAO;
        this.parkingLotHasUserB2BDAO = parkingLotHasUserB2BDAO;
        this.authTokenCache = authTokenCache;
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
                throw new AuthenticationException("Invalid Credentials");
            }

            long millis = DateTime.now().getMillis();
            String authToken = millis + UUID.randomUUID().toString().substring(0,6);
            Timestamp validTillTimestamp = new Timestamp(DateTime.now().plusYears(3).getMillis());
            SessionEntity sessionEntity = new SessionEntity(authToken,validTillTimestamp,userB2BEntity);
            sessionDAO.saveSession(sessionEntity);

            List<Integer> parkingLotIds = Lists.newArrayList();

            for (ParkingLotHasUserB2BEntity entity : userB2BEntity.getParkingSubLots()) {
                parkingLotIds.add(entity.getParkingSubLotId());
            }
            GMPUser user = new GMPUser(userName,userB2BEntity.getName(),parkingLotIds,
                    Lists.newArrayList(userB2BEntity.getUserAccesses()),authToken);
            authTokenCache.put(authToken,user);
            return user;
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
    public void addParkingLot(@ApiParam(value = "List of Parking Lot Ids", required = true)List<Integer> parkingSubLotIds, @PathParam("username") String username,
                              @Auth GMPUser gmpUser) {
        if (gmpUser.getParkingSubLotIds().containsAll(parkingSubLotIds)) {
            UserB2BEntity user = userB2BDAO.findById(username);
            if (user != null) {
                parkingLotHasUserB2BDAO.saveParkingLotId(parkingSubLotIds, user);
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }


}
