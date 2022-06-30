package cgi.hacker.vaillant.rien.d.impossible.Hackatton.service;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSVCreationService {

    private static final String fileName = "input.csv";
    private static final String TITLE_CONTENT = "id,from,to,cc,subject,body,date,reply,references";

    public void createCSVFromData(List<MailDto> mailDtos) throws FileNotFoundException {
        try(PrintWriter pw = new PrintWriter(fileName)) {
            pw.println(TITLE_CONTENT);
            //stream on list -> convert into a list a string separate by | and print pw::printLn
            mailDtos.forEach(mail -> {
                log.info("File - {} =====================\n", mail.getId());
                pw.println(getLine(mail));
            });
        }
    }

    private String getLine(MailDto mailDto) {
        return format(
                "%s,".repeat(TITLE_CONTENT.split(",").length - 1).concat("%s"),
                mailDto.getId(),
                mailDto.getFrom(),
                mailDto.getTo(),
                mailDto.getCopy(),
                mailDto.getSubject(),
                mailDto.getBody(),
                mailDto.getDate(),
                mailDto.getInReplyTo(),
                mailDto.getReferences()).replaceAll("\t", "").replaceAll("(?:\\n|\\r)", "");
    }
}
