package testRunner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = ".//Features/FlightPriceComparator.feature",
        glue="stepDefinitions",
        dryRun = false,
        monochrome = true,
        plugin = {"pretty","html:target/CucumberReports/CucumberReport.html"}
)
public class TestRunner {
}
