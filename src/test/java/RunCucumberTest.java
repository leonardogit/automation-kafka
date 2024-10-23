import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin   = {"json:tests/target/cucumber-reports/cucumber.json"},
        glue     = {"steps"}
)
public class RunCucumberTest {
}
