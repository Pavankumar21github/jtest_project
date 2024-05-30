import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(ParasoftImpactedTestSuiteRunner.class)
@SelectClasses({ examples.eval.SimpleTest.class, examples.flowanalysis.AlwaysCloseGSSTest.class,
		examples.junit.MoneyBagParameterizedTest.class, examples.junit.MoneyTest.class,
		examples.junit.NaiveStringBuilderParameterizedTest.class, examples.mock.FileExampleTest.class,
		examples.mock.InterpreterTest.class, examples.nbank.AccountTest.class, examples.nbank.BankTest.class,
		examples.nbank.CustomerTest.class, examples.servlets.ExampleServletTest.class })
@SuiteDisplayName("Parasoft impacted tests run from jtest-examples")
public class ParasoftImpactedTestSuite {
	public static Map<String, Set<String>> getMethods() {
		return new HashMap<String, Set<String>>() {
			{
				put("examples.eval.SimpleTest", new TreeSet<String>(Arrays.asList(new String[] { "testMap", "testMap2",
						"testMap3", "testStartsWith", "testStartsWith2", "testStartsWith3", "testStartsWith4" })));
				put("examples.flowanalysis.AlwaysCloseGSSTest",
						new TreeSet<String>(Arrays.asList(new String[] { "testProcessClose" })));
				put("examples.junit.MoneyBagParameterizedTest",
						new TreeSet<String>(Arrays.asList(new String[] { "testMultiply" })));
				put("examples.junit.MoneyTest", new TreeSet<String>(Arrays.asList(new String[] { "testBagMultiply",
						"testBagNegate", "testBagNotEquals", "testBagSimpleAdd", "testBagSubtract", "testBagSumAdd",
						"testIsZero", "testMixedSimpleAdd", "testMoneyBagEquals", "testMoneyBagHash", "testMoneyEquals",
						"testMoneyHash", "testNormalize2", "testNormalize3", "testNormalize4", "testPrint",
						"testSimpleAdd", "testSimpleBagAdd", "testSimpleMultiply", "testSimpleNegate",
						"testSimpleSubtract", "testSimplify", "zeroMoniesAreEqualRegardlessOfCurrency" })));
				put("examples.junit.NaiveStringBuilderParameterizedTest",
						new TreeSet<String>(Arrays.asList(new String[] { "testAppend" })));
				put("examples.mock.FileExampleTest",
						new TreeSet<String>(Arrays.asList(new String[] { "testAnalyze", "testIsOversize" })));
				put("examples.mock.InterpreterTest", new TreeSet<String>(Arrays.asList(new String[] { "testAdd34" })));
				put("examples.nbank.AccountTest", new TreeSet<String>(Arrays.asList(new String[] { "testApply",
						"testApply2", "testApply3", "testGetBalance", "testGetCustomer", "testGetID", "testGetStatus",
						"testIsOverdrawn", "testIsOverdrawn2", "testIsOverdrawn3", "testReportToCreditAgency",
						"testReportToCreditAgency2", "testSetBalance", "testSetBalance2", "testSetBalance3" })));
				put("examples.nbank.BankTest", new TreeSet<String>(
						Arrays.asList(new String[] { "testAddAccount", "testAddAccount2", "testAddAccount3" })));
				put("examples.nbank.CustomerTest", new TreeSet<String>(
						Arrays.asList(new String[] { "testGetName", "testGetName2", "testGetSSN", "testGetSSN2" })));
				put("examples.servlets.ExampleServletTest", new TreeSet<String>(Arrays
						.asList(new String[] { "testTryThis1", "testTryThis2", "testTryThis3", "testTryThis4" })));
			}
		};
	}
}