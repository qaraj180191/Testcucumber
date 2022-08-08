package cucumberJava;
 
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/java/cucumberJava/Feature/cucumberJava.feature",glue={"cucumberJava/StepDefinition"},plugin = { "pretty", "html:target/cucumber-reports" },publish = true)

public class runTest { }