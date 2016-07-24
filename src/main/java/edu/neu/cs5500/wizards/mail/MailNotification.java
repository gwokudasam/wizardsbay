package edu.neu.cs5500.wizards.mail;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.content.Body;

/**
 * Created by amala on 24/07/16.
 */
public class MailNotification {
    private static final String MAILGUN_DOMAIN = "app8bfbc96f0e2142968e87558a2d0671db.mailgun.org";
    private static final String MAILGUN_API_KEY = "key-e9832d2be5768b87bd6cc56fad6ab3e5";
    private static final String MAILGUN_LOGIN = "wizardsbay-noreply@app8bfbc96f0e2142968e87558a2d0671db.mailgun.org";

    private static Configuration configuration = new Configuration()
            .domain(MAILGUN_DOMAIN)
            .apiKey(MAILGUN_API_KEY)
            .from("WizardsBay No-Reply", MAILGUN_LOGIN);

    public boolean send(String toAddress, String subject, Body body) {
        return Mail.using(configuration)
                .to(toAddress)
                .subject(subject)
                .content(body)
                .build()
                .send()
                .isOk();
    }
}