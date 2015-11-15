package com.getMyParking.client;

import com.google.inject.Inject;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by rahulgupta.s on 14/11/15.
 */
public class SMTPClient {

    private Session mailSession;

    @Inject
    public SMTPClient() {
        Properties properties = System.getProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp.sendgrid.net");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", "true");
        mailSession = javax.mail.Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("no-reply@getmyparking.com","gmpgmp2015");
                //return new PasswordAuthentication("","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");
            }
        });
    }

    public void sendEmail(List<String> emailAddress, String smtpApi, InputStream attachmentInputStream, String attachmentFileName) throws MessagingException, IOException {

        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipients(Message.RecipientType.TO, emailAddress.stream().map(s -> {
            try {
                return new InternetAddress(s);
            } catch (AddressException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()).toArray(new Address[emailAddress.size()]));

        Multipart multipart = new MimeMultipart("alternative");
        BodyPart part1 = new MimeBodyPart();
        part1.setText("");
        BodyPart part2 = new MimeBodyPart();
        part2.setContent("", "text/html");
        multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);
        message.setFrom(new InternetAddress("no-reply@getmyparking.com"));
        message.setSubject("");
        message.setContent(multipart);
        message.addHeader("X-SMTPAPI", smtpApi);
        // Attachment part
        MimeBodyPart attachmentPart = getMimeBodyPart(attachmentFileName, attachmentInputStream, "application/octet-stream");
        multipart.addBodyPart(attachmentPart);

        Transport transport = mailSession.getTransport();
        // Connect the transport object.
        transport.connect();
        // Send the message.
        transport.sendMessage(message, message.getAllRecipients());
        // Close the connection.
        transport.close();
    }


    private static MimeBodyPart getMimeBodyPart(String name,InputStream attachmentInputStream, String type)
            throws IOException, MessagingException {
        MimeBodyPart attachment = new MimeBodyPart();

        DataSource dataSource = new ByteArrayDataSource(attachmentInputStream, type);
        DataHandler dataHandler = new DataHandler(dataSource);
        attachment.setDataHandler(dataHandler);
        attachment.setFileName(MimeUtility.encodeWord(name));
        attachment.setContentID("<xlsx>");
        attachment.setDisposition(MimeBodyPart.ATTACHMENT);
        return attachment;
    }
}
