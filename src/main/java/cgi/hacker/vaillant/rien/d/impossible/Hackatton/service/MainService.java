package cgi.hacker.vaillant.rien.d.impossible.Hackatton.service;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto.MailDto;
import lombok.RequiredArgsConstructor;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class MainService {

    private final CSVCreationService service;

    //TODO: IMPROVE, will not work on a war
    private static final String RESOURCE_FOLDER = "src/main/resources/emls";

    public void generatedCSVFile(String[] args) {
        try {
            createCSV(String.valueOf(new File(args.length != 0 ? args[0] : RESOURCE_FOLDER)));
        } catch (FileNotFoundException e) {
            System.out.println("\n============");
            System.out.println("File not found");
            System.out.println("==============");
        }
    }

    private void createCSV(String directory) throws FileNotFoundException {
        List<File> fileList = listFiles(directory);

        AtomicInteger counter = new AtomicInteger(1);

        if (fileList.size() > 0) {
            service.createCSVFromData(fileList.stream()
                    .map(file -> display(file.getAbsoluteFile(), counter.getAndIncrement()))
                    .collect(toList()));
        } else {
            System.out.println("");
            System.out.println("=====================");
            System.out.println("No mails to be parsed");
            System.out.println("=====================");
        }
    }

    private static List<File> listFiles(final String directory) {
        if (directory == null) {
            return Collections.EMPTY_LIST;
        }
        List<File> fileList = new ArrayList<>();
        File[] files = new File(directory).listFiles();
        for (File element : files) {
            if (element.isDirectory()) {
                fileList.addAll(listFiles(element.getPath()));
            } else {
                fileList.add(element);
            }
        }
        return fileList;
    }

    public MailDto display(File emlFile, int number) {
        try {
//            System.out.printf(,"File - %s =====================\n" number);

            MimeMessage message = new MimeMessage(null, new FileInputStream(emlFile));

            String from = message.getHeader("From", ",");
            String to = message.getHeader("To", ",");
            String copy = message.getHeader("CC", ",");
            String subject = message.getSubject();
            String date = message.getHeader("Date", ",");
            String replyTo = message.getHeader("in-reply-to", ",");
            String references = message.getHeader("reference", ",");

            String body = "";

            if(message.isMimeType("text/plain")) {
                body = message.getContent().toString();
            } else if(message.isMimeType("multipart/*")) {
                try {
                    MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                    body = getTextFromMimeMultipart(mimeMultipart);
                } catch (Exception e) {
                    body = null;
                }
            }

            return MailDto.builder()
                    .id(number)
                    .from(replaceCommaInString(from))
                    .to(replaceCommaInString(to))
                    .copy(replaceCommaInString(copy))
                    .body(replaceCommaInString(body))
                    .subject(replaceCommaInString(subject))
                    .date(replaceCommaInString(date))
                    .inReplyTo(replaceCommaInString(replyTo))
                    .references(replaceCommaInString(references))
                    .build();

        } catch (Exception e) {
            // Error
            return null;
        }
    }

    private String replaceCommaInString(String text) {
        return text != null && text.contains(",") ? text.replaceAll(",", "") : text;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
}
