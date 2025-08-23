package org.example.tools.suites;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Test suite 1. All tests.")
@SelectPackages("org.example.tools.tests")
public class TestSuite1 {

}
