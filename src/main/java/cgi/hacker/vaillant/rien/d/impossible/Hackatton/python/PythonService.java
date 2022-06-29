package cgi.hacker.vaillant.rien.d.impossible.Hackatton.python;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.csv.CSVCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.script.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;

@Component
@RequiredArgsConstructor
public class PythonService {

    private CSVCreationService csvCreationService;

    public void callPython() throws FileNotFoundException, ScriptException {
        StringWriter writer = new StringWriter();
        ScriptContext context = new SimpleScriptContext();
        context.setWriter(writer);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("python");
        engine.eval(new FileReader(resolvePythonScriptPath("hello.py")), context);
    }

    private String resolvePythonScriptPath(String filename) {
        File file = new File("src/main/resources/" + filename);
        return file.getAbsolutePath();
    }
}
