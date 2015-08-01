package com.getmyparking.email;

import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.email.SESService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ManagedSessionContext;
import org.testng.annotations.Test;

/**
 * Created by rahulgupta.s on 01/08/15.
 */
public class SESServiceTest {

    @Test
    public void testSendEmail() throws Exception {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        ParkingLotDAO parkingLotDAO = new ParkingLotDAO(sessionFactory);
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        SESService sesService = new SESService(parkingLotDAO);
        sesService.sendEmail(1);
        session.close();

    }
}
