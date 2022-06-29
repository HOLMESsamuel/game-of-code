package cgi.hacker.vaillant.rien.d.impossible.Hackatton.service;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final CSVCreationService service;
    private final MessageService messageService;

    //TODO: IMPROVE, will not work on a war
    private static final String RESOURCE_FOLDER = "src/main/resources/emls";

    public void generatedCSVFile(String[] args) {
        try {
            createCSV(String.valueOf(new File(args.length != 0 ? args[0] : RESOURCE_FOLDER)));
        } catch (FileNotFoundException e) {
            log.error("File not found");
        }
    }

    private void createCSV(final String directory) throws FileNotFoundException {
        List<File> fileList = listFiles(directory);

        AtomicInteger counter = new AtomicInteger(1);

        if (fileList.size() > 0) {
            service.createCSVFromData(fileList.stream()
                    .map(file -> display(file.getAbsoluteFile(), counter.getAndIncrement()))
                    .collect(toList()));
        } else {
            log.info("No mails to be parsed");
        }
    }

    private static List<File> listFiles(final String directory) {
        List<File> fileList = new ArrayList<>();
        File[] files = new File(directory).listFiles();
        if(nonNull(files) && files.length > 0) {
            for (final File element : files) {
                if (element.isDirectory()) {
                    fileList.addAll(listFiles(element.getPath()));
                } else {
                    fileList.add(element);
                }
            }
        }
        return fileList;
    }

    public MailDto display(File emlFile, int number) {
        try {
            return messageService.createMailDtoFromMessage(new MimeMessage(null, new FileInputStream(emlFile)), number);
        } catch (MessagingException | IOException e) {
            //TODO: improve if necessary
            return null;
        }
    }
}
