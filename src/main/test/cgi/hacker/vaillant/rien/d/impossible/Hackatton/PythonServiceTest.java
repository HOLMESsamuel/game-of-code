package cgi.hacker.vaillant.rien.d.impossible.Hackatton;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptException;
import java.io.FileNotFoundException;

@SpringBootTest
public class PythonServiceTest {

    private final PythonService pythonService = new PythonService();

    @Test
    void callPython() throws ScriptException, FileNotFoundException {
        pythonService.callPython();
    }
}
