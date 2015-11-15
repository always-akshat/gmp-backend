package com.getMyParking.quartz;

import com.getMyParking.client.SMTPClient;
import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dto.ParkingEventDumpDTO;
import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.ParkingEntity;
import com.getMyParking.service.guice.GuiceHelper;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.sendgrid.SendGrid;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rahulgupta.s on 01/11/15.
 */
public class ParkingEventsEMailingJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ParkingEventsEMailingJob.class);
    private ParkingEventDAO parkingEventDAO;
    private CompanyDAO companyDAO;
    private SessionFactory sessionFactory;
    private SMTPClient smtpClient;

    private String[] sheetRowHeaders = new String[] {"Registration Number", "Mobile Number", "Valet Name", "Checked In Time",
            "Checked In Cost", "Checked Out Time", "Checked Out Cost", "Sub Lot Type", "Pass Validity Date", "Serial Number", "Operator Name",
            "Special"};

    public ParkingEventsEMailingJob() {
        Injector injector = GuiceHelper.getInjector();
        sessionFactory = injector.getInstance(SessionFactory.class);
        parkingEventDAO = injector.getInstance(ParkingEventDAO.class);
        companyDAO = injector.getInstance(CompanyDAO.class);
        //sendgrid = new SendGrid("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");
        smtpClient = injector.getInstance(SMTPClient.class);


    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);

        List<CompanyEntity> companies = companyDAO.getAllCompaniesWithEmailID();
        for (CompanyEntity company : companies) {
            for (ParkingEntity parking : company.getParkings()) {
                DateTime reportDate = DateTime.now().minusDays(1);
                List<ParkingEventDumpDTO> parkingEventDumpDTOs = parkingEventDAO.getParkingEventsDump(parking.getId(),reportDate);

                SXSSFWorkbook workbook = new SXSSFWorkbook();
                Sheet sheet = workbook.createSheet("Parking Events");
                int columnNumber = 0;
                int rowNumber = 0;
                Row row = sheet.createRow(rowNumber++);
                for (String header : sheetRowHeaders) {
                    Cell cell = row.createCell(columnNumber++, Cell.CELL_TYPE_STRING);
                    cell.setCellValue(header);
                }

                for (ParkingEventDumpDTO parkingEvent : parkingEventDumpDTOs) {
                    row = sheet.createRow(rowNumber++);
                    columnNumber = 0;
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getRegistrationNumber())
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getMobileNumber())
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getValetName())
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            parkingEvent.getCheckInEventTime().withZone(DateTimeZone.forOffsetHoursMinutes(5,30)).toString("dd-MM-YY HH:mm:ss")
                    );

                    if (parkingEvent.getCheckInCost() != null) {
                        row.createCell(columnNumber++, Cell.CELL_TYPE_NUMERIC).setCellValue(parkingEvent.getCheckInCost().doubleValue());
                    } else {
                        row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue("NA");
                    }

                    if (parkingEvent.getCheckOutEventTime() != null) {
                        row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                                parkingEvent.getCheckOutEventTime().withZone(DateTimeZone.forOffsetHoursMinutes(5, 30)).toString("dd-MM-YY HH:mm:ss")
                        );
                        row.createCell(columnNumber++, Cell.CELL_TYPE_NUMERIC).setCellValue(parkingEvent.getCheckOutCost().doubleValue());
                    }
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getSubLotType())
                    );
                    if (parkingEvent.getPassValidTime() != null) {
                        row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getPassValidTime().withZone(
                                        DateTimeZone.forOffsetHoursMinutes(5, 30)).toString("dd-MM-YY HH:mm:ss")
                        );
                    }
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getSerialNumber())
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getOperatorName())
                    );
                    row.createCell(columnNumber, Cell.CELL_TYPE_STRING).setCellValue(
                            Strings.nullToEmpty(parkingEvent.getSpecial())
                    );
                }

                InputStream workBookInputStream = null;
                try {
                    ByteArrayOutputStream workbookOutputStream = new ByteArrayOutputStream();
                    workbook.write(workbookOutputStream);
                    workBookInputStream = new ByteArrayInputStream(workbookOutputStream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    List<String> emailAddress = Splitter.on(",").splitToList(company.getEmail());
                    String [] companyNames = new String[emailAddress.size()];
                    Arrays.fill(companyNames,company.getName());
                    String [] parkingNames = new String[emailAddress.size()];
                    Arrays.fill(parkingNames,parking.getName());
                    String [] dates = new String[emailAddress.size()];
                    Arrays.fill(dates,reportDate.toString("dd MMM YYYY"));

                    SendGrid.Email email = new SendGrid.Email();
                    email.addSmtpApiTo(emailAddress.toArray(new String[emailAddress.size()]));
                    email.setTemplateId("1e7f467f-5f1c-4532-9e6f-93525c963bc7");
                    email.addSubstitution(":companyName", companyNames);
                    email.addSubstitution(":parkingName", parkingNames);
                    email.addSubstitution(":date", dates);
                    email.addCategory("parking_event_report");

                    String attachmentFileName = "report_"+parking.getName()+"_"+reportDate.toString("ddMMYY")+".xlsx";
                    smtpClient.sendEmail(emailAddress,email.getSMTPAPI().rawJsonString(),workBookInputStream, attachmentFileName);

                } catch (Exception ex) {
                    logger.error("Email Exception ",ex);
                }

            }
        }

        session.flush();
        session.clear();
        session.close();
    }

}
