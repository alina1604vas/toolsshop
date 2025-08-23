package org.example.tools.infra;

import org.example.tools.driver.DriverSingleton;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class ExecutionListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        DriverSingleton.initDriver();
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        DriverSingleton.closeDriver();
    }

}
