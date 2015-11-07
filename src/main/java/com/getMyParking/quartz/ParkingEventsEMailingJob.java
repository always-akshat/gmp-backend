package com.getMyParking.quartz;

import com.getMyParking.dao.CompanyDAO;
import com.getMyParking.dao.ParkingDAO;
import com.getMyParking.dao.ParkingEventDAO;
import com.getMyParking.dto.ParkingEventDumpDTO;
import com.getMyParking.entity.CompanyEntity;
import com.getMyParking.entity.ParkingEntity;
import com.getMyParking.service.guice.GuiceHelper;
import com.google.common.base.Splitter;
import com.google.inject.Injector;
import com.sendgrid.SendGrid;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.util.List;

/**
 * Created by rahulgupta.s on 01/11/15.
 */
public class ParkingEventsEMailingJob implements Job {

    private ParkingEventDAO parkingEventDAO;
    private CompanyDAO companyDAO;
    private SessionFactory sessionFactory;
    private SendGrid sendgrid;

    private String[] sheetRowHeaders = new String[] {"Registration Number", "Mobile Number", "Valet Name", "Checked In Time",
            "Checked In Cost", "Checked Out Time", "Checked Out Cost", "Sub Lot Type", "Pass Validity Date", "Serial Number", "Operator Name",
            "Special"};

    public ParkingEventsEMailingJob() {
        Injector injector = GuiceHelper.getInjector();
        sessionFactory = injector.getInstance(SessionFactory.class);
        parkingEventDAO = injector.getInstance(ParkingEventDAO.class);
        companyDAO = injector.getInstance(CompanyDAO.class);
        sendgrid = new SendGrid("no-reply@getmyparking.com", "gmpgmp2015");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);

        List<CompanyEntity> companies = companyDAO.getAllCompaniesWithEmailID();
        for (CompanyEntity company : companies) {
            List<String> emailIds = Splitter.on(",").splitToList(company.getEmail());
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
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getRegistrationNumber());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getMobileNumber());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getValetName());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            parkingEvent.getCheckInEventTime().withZone(DateTimeZone.forOffsetHoursMinutes(5,30)).toString("dd-MM-YY HH:mm:ss")
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_NUMERIC).setCellValue(parkingEvent.getCheckInCost().doubleValue());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(
                            parkingEvent.getCheckOutEventTime().withZone(DateTimeZone.forOffsetHoursMinutes(5,30)).toString("dd-MM-YY HH:mm:ss")
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_NUMERIC).setCellValue(parkingEvent.getCheckOutCost().doubleValue());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getSubLotType());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getPassValidTime().withZone(
                                    DateTimeZone.forOffsetHoursMinutes(5,30)).toString("dd-MM-YY HH:mm:ss")
                    );
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getSerialNumber());
                    row.createCell(columnNumber++, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getOperatorName());
                    row.createCell(columnNumber, Cell.CELL_TYPE_STRING).setCellValue(parkingEvent.getSpecial());
                }

                InputStream workBookInputStream = null;
                try {
                    ByteArrayOutputStream workbookOutputStream = new ByteArrayOutputStream();
                    workbook.write(workbookOutputStream);
                    workBookInputStream = new ByteArrayInputStream(workbookOutputStream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SendGrid.Email email = new SendGrid.Email();



            }
        }

        session.flush();
        session.clear();
        session.close();
    }

}
