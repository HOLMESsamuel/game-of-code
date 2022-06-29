package cgi.hacker.vaillant.rien.d.impossible.Hackatton.service;


import cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

@Component
@Slf4j
public class MessageService {

    private static final String MIME_TYPE_TEXT = "text/plain";
    private static final String MIME_TYPE_MULTIPART = "multipart/*";
    private static final String COMMA_SEPARATOR = ",";

    public MailDto createMailDtoFromMessage(final MimeMessage message, final int number) throws MessagingException, IOException {
        return MailDto.builder()
                .id(number)
                .from(replaceCommaInString(message.getHeader("From", COMMA_SEPARATOR)))
                .to(replaceCommaInString(message.getHeader("To", COMMA_SEPARATOR)))
                .copy(replaceCommaInString(message.getHeader("CC", COMMA_SEPARATOR)))
                .body(replaceCommaInString(getBody(message)))
                .subject(replaceCommaInString(message.getSubject()))
                .date(replaceCommaInString(message.getHeader("Date", COMMA_SEPARATOR)))
                .inReplyTo(replaceCommaInString(message.getHeader("in-reply-to", COMMA_SEPARATOR)))
                .references(replaceCommaInString(message.getHeader("reference", COMMA_SEPARATOR)))
                .build();
    }

    private String getBody(final MimeMessage message) throws MessagingException, IOException {
        String body = "";
        if(message.isMimeType(MIME_TYPE_TEXT)) {
            body = message.getContent().toString();
        } else if(message.isMimeType(MIME_TYPE_MULTIPART)) {
            try {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                body = getTextFromMimeMultipart(mimeMultipart);
            } catch (Exception e) {
                body = null;
            }
        }
        return body;
    }

    private String replaceCommaInString(String text) {
        return text != null && text.contains(COMMA_SEPARATOR) ? text.replaceAll(COMMA_SEPARATOR, "") : text;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException {
        String result = "";
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType(MIME_TYPE_TEXT)) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result.concat(getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent()));
            }
        }
        return result;
    }
}
