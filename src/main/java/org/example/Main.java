package org.example;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Throwable {
        io.cucumber.core.cli.Main.main(new String[]{
                "--glue", "org.StepDefinitions",
//                "--plugin", "html:target/cucumber-reports/cucumber-pretty.html",
                "--plugin", "json:target/cucumber-reports/CucumberTestReport.json",
//                "--plugin", "junit:target/cucumber-reports/cucumber-report.xml",
                "classpath:Features/DemoSteps.feature"
        });

    }
}