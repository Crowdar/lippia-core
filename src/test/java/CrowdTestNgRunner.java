import com.crowdar.bdd.cukes.TestNgRunner;
import org.testng.annotations.AfterSuite;
import com.cucumber.listener.Reporter;

import java.io.File;

public class CrowdTestNgRunner extends TestNgRunner {



    @AfterSuite
    public static void writeExtentReport() {
        //Reporter.loadXMLConfig(new File("extent-config.xml"));
    }

}
