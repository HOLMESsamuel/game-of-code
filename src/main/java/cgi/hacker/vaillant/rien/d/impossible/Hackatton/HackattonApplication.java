package cgi.hacker.vaillant.rien.d.impossible.Hackatton;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.service.CSVCreationService;
import cgi.hacker.vaillant.rien.d.impossible.Hackatton.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class HackattonApplication {

    private static final MainService mainService = new MainService(new CSVCreationService());

    public static void main(String[] args) {
        mainService.generatedCSVFile(args);
    }
}
