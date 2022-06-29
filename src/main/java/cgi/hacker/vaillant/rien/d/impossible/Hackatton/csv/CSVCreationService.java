package cgi.hacker.vaillant.rien.d.impossible.Hackatton.csv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class CSVCreationService {

    private static final String fileName = "input.csv";

    //List d'objet en entrée -> sortir fichier csv.
    public File createCSVFromData() throws FileNotFoundException {
        File csvInputFile = new File(fileName);
        try(PrintWriter pw = new PrintWriter(csvInputFile)) {
            //Add title wanted by sam
            pw.println("");
            //stream on list -> convert into a list a string separate by | and print pw::printLn
        }
        return csvInputFile;
    }
}
