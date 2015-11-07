package com.getMyParking;

import com.sendgrid.*;

public class SendGridExample {
    public static void main(String[] args) {
        SendGrid sendgrid = new SendGrid("no-reply@getmyparking.com", "gmpgmp2015");

        SendGrid.Email email = new SendGrid.Email();
        email.addTo("chirag@getmyparking.com");
        email.setFrom("no-reply@getmyparking.com");
        email.setSubject("Hello World");
        email.setText("My first email with SendGrid Java!");

        try {
            SendGrid.Response response = sendgrid.send(email);
            System.out.println(response.getMessage());
        }
        catch (SendGridException e) {
            System.err.println(e);
        }
    }
}