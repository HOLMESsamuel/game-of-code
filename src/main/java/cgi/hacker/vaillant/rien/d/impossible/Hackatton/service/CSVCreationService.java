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

    //List d'objet en entrée -> sortir fichier csv.
    public File createCSVFromData(List<MailDto> mailDtos) throws FileNotFoundException {
        File csvInputFile = new File(fileName);
        try(PrintWriter pw = new PrintWriter(csvInputFile)) {
            //Add title wanted by sam
            pw.println("id,from,to,cc,subject,date");
            //stream on list -> convert into a list a string separate by | and print pw::printLn
            mailDtos.forEach(mail -> pw.println(getLine(mail)));
        }
        return csvInputFile;
    }

    private String getLine(MailDto mailDto) {
        return String.format(
                "%s,%s,%s,%s,%s,%s",
                mailDto.getId(),
                mailDto.getFrom(),
                mailDto.getTo(),
                mailDto.getCopy(),
                mailDto.getSubject(),
                mailDto.getDate()).replaceAll("\t", "").replaceAll("(?:\\n|\\r)", "");
    }
}
