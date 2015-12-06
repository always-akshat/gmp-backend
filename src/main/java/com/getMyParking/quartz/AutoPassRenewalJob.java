package com.getMyParking.quartz;

import com.getMyParking.constants.PassType;
import com.getMyParking.dao.ParkingPassDAO;
import com.getMyParking.dao.ParkingPassMasterDAO;
import com.getMyParking.entity.ParkingPassEntity;
import com.getMyParking.entity.ParkingPassMasterEntity;
import com.getMyParking.service.guice.GuiceHelper;
import com.google.inject.Injector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by rahulgupta.s on 06/12/15.
 */
public class AutoPassRenewalJob implements Job {

    private ParkingPassDAO parkingPassDAO;
    private ParkingPassMasterDAO parkingPassMasterDAO;
    private SessionFactory sessionFactory;

    public AutoPassRenewalJob() {
        Injector injector = GuiceHelper.getInjector();
        this.parkingPassDAO = injector.getInstance(ParkingPassDAO.class);
        this.parkingPassMasterDAO = injector.getInstance(ParkingPassMasterDAO.class);
        this.sessionFactory = injector.getInstance(SessionFactory.class);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        Transaction transaction = session.beginTransaction();
        try {
            List<ParkingPassMasterEntity> passMasters = parkingPassMasterDAO.getAutoRenewalPassMasters();

            passMasters.forEach(passMaster -> {
                List<ParkingPassEntity> activePasses = parkingPassDAO.getToBeExpiredPassesByPassMasterId(passMaster.getId());
                activePasses.forEach( pass -> {
                    ParkingPassEntity parkingPass = parkingPassDAO.getLastPassByRegistrationNumberAndMasterId(
                            pass.getRegistrationNumber(),pass.getParkingPassMaster().getId());
                    if (pass.getId().equals(parkingPass.getId())) {
                        ParkingPassEntity renewedPass = new ParkingPassEntity();
                        renewedPass.copy(pass);
                        renewedPass.setIsPaid(0);
                        renewedPass.setIsDeleted(0);
                        renewedPass.setCreatedAt(DateTime.now());
                        renewedPass.setValidFrom(pass.getValidTime().plusSeconds(1));
                        if (passMaster.getPassType().equalsIgnoreCase(PassType.DAY.name())) {
                            renewedPass.setValidTime(renewedPass.getValidFrom().plusDays(passMaster.getNumbers()).minusSeconds(1));
                        } else if (passMaster.getPassType().equalsIgnoreCase(PassType.WEEK.name())) {
                            renewedPass.setValidTime(renewedPass.getValidFrom().plusWeeks(passMaster.getNumbers()).minusSeconds(1));
                        } else if (passMaster.getPassType().equalsIgnoreCase(PassType.MONTH.name())) {
                            renewedPass.setValidTime(renewedPass.getValidFrom().plusMonths(passMaster.getNumbers()).minusSeconds(1));
                        } else if (passMaster.getPassType().equalsIgnoreCase(PassType.HOUR.name())) {
                            renewedPass.setValidTime(renewedPass.getValidFrom().plusHours(passMaster.getNumbers()).minusSeconds(1));
                        }
                        parkingPassDAO.saveOrUpdateParkingPass(renewedPass);
                    }
                });
            });
            transaction.commit();
            session.flush();
            session.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }
}
