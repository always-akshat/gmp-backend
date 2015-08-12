package com.getMyParking.email;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.getMyParking.dao.ParkingLotDAO;
import com.getMyParking.entity.ParkingLotEntity;
import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by rahulgupta.s on 01/08/15.
 */
public class SESService {

    private ParkingLotDAO parkingLotDAO;
    private ReportClient reportClient;

    final String FROM = "rahul@getmyparking.com";  // Replace with your "From" address. This address must be verified.

    final String SUBJECT = "Parking Report";

    private interface ReportClient {

        @GET("/email/report/{parkingLotId}")
        public Response getReportData(@Path("parkingLotId")Integer parkingLotId, @Query("endDate")String endDate);

    }

    @Inject
    public SESService(ParkingLotDAO parkingLotDAO) {
        this.parkingLotDAO = parkingLotDAO;
        RestAdapter reportAdapter = new RestAdapter.Builder()
                                    .setEndpoint("http://52.74.237.153:3000")
                                    .setLogLevel(RestAdapter.LogLevel.FULL)
                                    .build();
        reportClient = reportAdapter.create(ReportClient.class);

    }

    public void sendEmail(Integer parkingLotId) throws IOException {

        try {

            //ParkingLotEntity parkingLot = parkingLotDAO.findById(parkingLotId);
            String endDate = DateTime.now().toString("yyyy-MM-dd");
            Response response = reportClient.getReportData(parkingLotId, endDate);
            String responseHtml = IOUtils.toString(response.getBody().in(), Charset.forName("UTF-8"));
            Destination destination = new Destination().withToAddresses("rahul@getmyparking.com");

            // Create the subject and body of the message.
            Content subject = new Content().withData(SUBJECT);
            Content textBody = new Content().withData(responseHtml);
            Body body = new Body().withHtml(textBody);

            // Create a message with the specified subject and body.
            Message message = new Message().withSubject(subject).withBody(body);

            // Assemble the email.
            SendEmailRequest request = new SendEmailRequest().withSource(FROM)
                    .withDestination(destination).withMessage(message);

            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            InputStream inputstream = getClass().getResourceAsStream("/config/aws/credential.properties");
            PropertiesCredentials credentials = new PropertiesCredentials(inputstream);
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
            Region REGION = Region.getRegion(Regions.US_WEST_2);
            client.setRegion(REGION);

            // Send the email.
            client.sendEmail(request);
            System.out.println("Email sent!");

        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
             throw ex;
        }
    }
}
