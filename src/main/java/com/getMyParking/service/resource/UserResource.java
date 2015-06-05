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
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
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

    @POST
    @Path("/login")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public GMPUser login(@FormParam("username") String userName, @FormParam("password") String password) {
        if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password)) {

            UserB2BEntity userB2BEntity = userB2BDAO.loginUser(userName, password);
            if (userB2BEntity == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            long millis = DateTime.now().getMillis();
            String authToken = millis + UUID.randomUUID().toString();
            Timestamp validTillTimestamp = new Timestamp(DateTime.now().plusDays(7).getMillis());
            SessionEntity sessionEntity = new SessionEntity(authToken,validTillTimestamp,userB2BEntity);
            sessionDAO.saveSession(sessionEntity);

            List<Integer> parkingLotIds = Lists.newArrayList();

            for (ParkingLotHasUserB2BEntity entity : userB2BEntity.getParkingLotHasUserB2BsByUsername()) {
                parkingLotIds.add(entity.getParkingLotId());
            }
            GMPUser user = new GMPUser(authToken,userName,userB2BEntity.getRole(),parkingLotIds);
            authTokenCache.put(authToken,user);
            return user;
        } else {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(@Valid UserB2BEntity user) {
        userB2BDAO.saveUser(user);
    }

    @POST
    @Path("/{username}/parking_lot")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public void addParkingLot(@Valid List<Integer> parkingLotIds, @PathParam("username") String username) {
        UserB2BEntity user = userB2BDAO.findById(username);
        if (user != null) {
            parkingLotHasUserB2BDAO.saveParkingLotId(parkingLotIds,user);
        } else {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }


}
