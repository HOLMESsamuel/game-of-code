package cgi.hacker.vaillant.rien.d.impossible.Hackatton;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.service.*;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class HackattonApplication {

    private static final MainService mainService = new MainService(new CSVCreationService(), new MessageService());

    public static void main(String[] args) {
        mainService.generatedCSVFile(args);
    }
}
