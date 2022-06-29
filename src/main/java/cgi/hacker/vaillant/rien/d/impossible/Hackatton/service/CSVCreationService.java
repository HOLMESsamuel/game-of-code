package cgi.hacker.vaillant.rien.d.impossible.Hackatton.service;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CSVCreationService {

    private static final String fileName = "input.csv";
    private static final String TITLE_CONTENT = "id,from,to,cc,subject,body,date,reply,references";

    //List d'objet en entrée -> sortir fichier csv.
    public void createCSVFromData(List<MailDto> mailDtos) throws FileNotFoundException {
        try(PrintWriter pw = new PrintWriter(new File(fileName))) {
            //Add title wanted by sam
            pw.println(TITLE_CONTENT);
            //stream on list -> convert into a list a string separate by | and print pw::printLn
            mailDtos.forEach(mail -> {
            System.out.printf("File - %s =====================\n", mail.getId());
                pw.println(getLine(mail));
            });
        }
    }

    private String getLine(MailDto mailDto) {
        return String.format(
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
