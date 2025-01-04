package com.hr.onboard.service.email;

public interface EmailService {
    /**
     * send email to someone
     *
     * @param to the destination email address
     * @param subject the subject of email
     * @param text the content of email
     */
    void sendSimpleEmail(String to, String subject, String text);
}
