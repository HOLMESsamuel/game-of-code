package cgi.hacker.vaillant.rien.d.impossible.Hackatton;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

@SpringBootApplication
public class HackattonApplication {

    public static void main(String[] args) throws Exception {
        try {
            int counter = 0;

            while (true) {
                display(new ClassPathResource(String.format("emls/%s.eml", counter)).getFile(), counter);

                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("\n==========================");
            System.out.println("No more mails to be parsed");
            System.out.println("==========================");
        }
    }

    public static void display(File emlFile, int number) throws Exception {
        System.out.printf("\n\nFile - %s =====================\n", number);
        Properties props = System.getProperties();
        props.put("mail.host", "smtp.dummydomain.com");
        props.put("mail.transport.protocol", "smtp");

        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream(emlFile);
        MimeMessage message = new MimeMessage(mailSession, source);

//        System.out.println("Header - from : " + message.getHeader("From", ","));
        System.out.println("From : " + message.getFrom()[0]);
        System.out.println("Subject : " + message.getSubject());
        System.out.println("--------------");
        System.out.println("Body : " +  message.getContent());
    }
}
