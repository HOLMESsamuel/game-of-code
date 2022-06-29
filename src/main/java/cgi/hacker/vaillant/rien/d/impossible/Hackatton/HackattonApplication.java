package cgi.hacker.vaillant.rien.d.impossible.Hackatton;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.csv.CSVCreationService;
import cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@SpringBootApplication
@RequiredArgsConstructor
public class HackattonApplication {

    //TODO: IMPROVE, will not work on a war
    private static final String RESOURCE_FOLDER = "src/main/resources/emls";
    private static final CSVCreationService service = new CSVCreationService();

    public static void main(String[] args) throws FileNotFoundException {
        createCSV(new File(args.length != 0 ? args[0]: RESOURCE_FOLDER));
    }

    public static void createCSV(File path) throws FileNotFoundException {
        File[] files = path.listFiles();
        AtomicInteger counter = new AtomicInteger(1);

        if (files != null && files.length > 0) {
            service.createCSVFromData(stream(files)
                    .map(file -> display(file.getAbsoluteFile(), counter.getAndIncrement()))
                    .collect(toList()));
        } else {
            System.out.println("\n===================");
            System.out.println("No mails to be parsed");
            System.out.println("=====================");
        }
    }

    public static MailDto display(File emlFile, int number) {
        try {
            System.out.printf("File - %s =====================\n", number);

            MimeMessage message = new MimeMessage(null, new FileInputStream(emlFile));

            return MailDto.builder()
                    .id(number)
                    .from(message.getHeader("From", ","))
                    .to(message.getHeader("To", ","))
                    .copy(message.getHeader("CC", ","))
                    .subject(message.getSubject())
                    .date(message.getHeader("Date", ","))
                    .build();

        } catch (Exception e) {
            // Error
            return null;
        }
    }
}
