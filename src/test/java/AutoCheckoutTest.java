import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.entity.ParkingEventEntity;
import com.getMyParking.entity.ParkingLotEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.getMyParking.pricing.PricingFunction;
import com.google.common.collect.Lists;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ManagedSessionContext;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rahulgupta.s on 23/07/15.
 */
public class AutoCheckoutTest {

    @Test
    public void testAutoCheckout() throws Exception {

        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        ParkingEventDAO parkingEventDAO = new ParkingEventDAO(sessionFactory);
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        Transaction tx = session.beginTransaction();
        DateTime toDate = DateTime.now();
        DateTime fromDate = toDate.minusDays(2);
        SQLQuery query = session.createSQLQuery("SELECT * from `parking_event` where `event_type` = 'CHECKED_IN' " +
                "AND parking_lot_id = :id AND vehicle_type = 'BIKE' AND `event_time` between :fromDate AND :toDate AND `serial_number` NOT IN " +
                "(Select `serial_number` from `parking_event` where `event_type` = 'CHECKED_OUT' AND " +
                "`event_time` between :fromDate AND :toDate AND vehicle_type = 'BIKE')");

        query.setParameter("id", 1);
        query.setParameter("fromDate", fromDate.toString());
        query.setParameter("toDate", toDate.toString());

        query.addEntity(ParkingEventEntity.class);
        List<ParkingEventEntity> parkingEvents = query.list();
        int count = 0;
        if (parkingEvents != null && parkingEvents.size() > 0) {
            ParkingLotEntity parkingLot = parkingEvents.get(0).getParkingLotByParkingLotId();

            for (ParkingEventEntity oldParkingEvent : parkingEvents) {
                ParkingEventEntity parkingEvent = new ParkingEventEntity();
                parkingEvent.setEventTime(DateTime.now());
                parkingEvent.setEventType("CHECKED_OUT");
                parkingEvent.setParkingLotByParkingLotId(parkingLot);
                parkingEvent.setRegistrationNumber(oldParkingEvent.getRegistrationNumber());
                parkingEvent.setSerialNumber(oldParkingEvent.getSerialNumber());
                parkingEvent.setType(oldParkingEvent.getType());
                parkingEvent.setVehicleType(oldParkingEvent.getVehicleType());
                if (oldParkingEvent.getShiftNumber() != null) {
                    parkingEvent.setShiftNumber(oldParkingEvent.getShiftNumber());
                }
                if (oldParkingEvent.getParkingPassByParkingPassId() != null) {
                    parkingEvent.setParkingPassByParkingPassId(oldParkingEvent.getParkingPassByParkingPassId());
                }
                List<PricingSlotEntity> pricingSlotList = Lists.newArrayList(parkingLot.getPricingSlotsById());
                parkingEvent.setCost(new BigDecimal(PricingFunction.calculateInitialCost(pricingSlotList, DateTime.now())));
                parkingEvent.setUpdatedTime(DateTime.now());
                session.persist(parkingEvent);
                count ++;
                if (count == 20) {
                    session.flush();
                    session.clear();
                }
            }
        }
        session.flush();
        session.clear();
        tx.commit();
        session.close();


    }
}
