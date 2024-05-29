import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.FilterResult;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.PostDiscoveryFilter;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class ParasoftImpactedTestSuiteRunner extends Runner {
	private static Map<String, Set<String>> TEST_METHODS_TO_RUN = null;
	private Launcher _launcher;
	private TestPlan _testPlan;
	private Class<?> _testClass;
	private Description _testSuiteDescription;
	private Map<TestIdentifier, Description> _descriptions = new HashMap<TestIdentifier, Description>();

	public ParasoftImpactedTestSuiteRunner(Class<?> testClass) throws InitializationError {
		_launcher = LauncherFactory.create();
		_testClass = testClass;
		extractMethodNamesFromTestsuite(testClass);
		_testPlan = _launcher.discover(createDiscoveryRequest());
		_testSuiteDescription = generateTestSuiteDescription(_testPlan);
	}

	private Description generateTestSuiteDescription(TestPlan testPlan) {
		Description rootDescription = Description
				.createSuiteDescription(_testClass.getAnnotation(SuiteDisplayName.class).value());
		for (TestIdentifier root : testPlan.getRoots()) {
			buildDescription(rootDescription, root, testPlan);
		}
		return rootDescription;
	}

	private void buildDescription(Description parentDescription, TestIdentifier testIdentifier, TestPlan testPlan) {
		Description newDescription = createDescriptor(testIdentifier, testPlan);
		parentDescription.addChild(newDescription);
		_descriptions.put(testIdentifier, newDescription);
		for (TestIdentifier identifier : testPlan.getChildren(testIdentifier)) {
			buildDescription(newDescription, identifier, testPlan);
		}
	}

	private Description createDescriptor(TestIdentifier testIdentifier, TestPlan testPlan) {
		TestSource source = testIdentifier.getSource().orElse(null);
		if (source instanceof MethodSource) {
			MethodSource methodSource = (MethodSource) source;
			return Description.createTestDescription(methodSource.getClassName(), methodSource.getMethodName());
		}
		return Description.createSuiteDescription(testIdentifier.getLegacyReportingName(),
				testIdentifier.getUniqueId());
	}

	private static void extractMethodNamesFromTestsuite(Class<?> clazz) {
		Method getMethods;
		try {
			getMethods = clazz.getMethod("getMethods", new Class<?>[0]);
			Object o = getMethods.invoke(null, new Object[0]);
			if (o instanceof Map<?, ?>) {
				TEST_METHODS_TO_RUN = (Map<String, Set<String>>) o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(RunNotifier notifier) {
		_launcher.execute(_testPlan, new TestExecListener(notifier));
	}

	private LauncherDiscoveryRequest createDiscoveryRequest() {
		List<DiscoverySelector> selectors = getSelectorsFromAnnotations();
		LauncherDiscoveryRequestBuilder requestBuilder = LauncherDiscoveryRequestBuilder.request()
				.filters(new TestMethodFilter()).selectors(selectors);
		return requestBuilder.build();
	}

	private List<DiscoverySelector> getSelectorsFromAnnotations() {
		List<DiscoverySelector> selectors = new ArrayList<>();
		selectors.addAll(transform(getSelectedClasses(), DiscoverySelectors::selectClass));
		return selectors;
	}

	private <T> List<DiscoverySelector> transform(T[] sourceElements, Function<T, DiscoverySelector> transformer) {
		return stream(sourceElements).map(transformer).collect(toList());
	}

	private Class<?>[] getSelectedClasses() {
		return _testClass.getAnnotation(SelectClasses.class).value();
	}

	private class TestExecListener implements TestExecutionListener {
		private final RunNotifier _notifier;

		public TestExecListener(RunNotifier notifier) {
			_notifier = notifier;
		}

		@Override
		public void executionStarted(TestIdentifier testIdentifier) {
			_notifier.fireTestStarted(getCurrentTestDescription(testIdentifier));
		}

		@Override
		public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
			Description description = getCurrentTestDescription(testIdentifier);
			if (testIdentifier.isTest()) {
				Status status = testExecutionResult.getStatus();
				if (status.equals(Status.ABORTED)) {
					_notifier.fireTestAssumptionFailed(
							new Failure(description, testExecutionResult.getThrowable().orElse(null)));
				} else if (status.equals(Status.FAILED)) {
					_notifier
							.fireTestFailure(new Failure(description, testExecutionResult.getThrowable().orElse(null)));
				}
			}
			_notifier.fireTestFinished(description);
		}

		@Override
		public void executionSkipped(TestIdentifier testIdentifier, String reason) {
			Description currentTestDescription = getCurrentTestDescription(testIdentifier);
			_notifier.fireTestIgnored(currentTestDescription);
			if (!testIdentifier.isTest()) {
				fireSuiteIgnored(currentTestDescription);
			}
		}

		private void fireSuiteIgnored(Description currentTestDescription) {
			for (Description child : currentTestDescription.getChildren()) {
				_notifier.fireTestIgnored(child);
				if (child.isSuite()) {
					fireSuiteIgnored(child);
				}
			}
		}
	}

	private class TestMethodFilter implements PostDiscoveryFilter {

		@Override
		public FilterResult apply(TestDescriptor descriptor) {
			boolean shouldRun = descriptor.getSource().filter(MethodSource.class::isInstance)
					.map(MethodSource.class::cast).map(this::shouldRun).orElse(true);

			return FilterResult.includedIf(shouldRun);
		}

		private boolean shouldRun(MethodSource source) {
			String testClass = source.getClassName();
			String testMethod = source.getMethodName();
			return shouldRun(testClass, testMethod);
		}

		private boolean shouldRun(String testClass, String testMethod) {
			Set<String> methodsForClass = TEST_METHODS_TO_RUN.get(testClass);
			if (methodsForClass != null) {
				return methodsForClass.contains(testMethod);
			}
			return false;
		}
	}

	private Description getCurrentTestDescription(TestIdentifier testIdentifier) {
		Description currentDescription = _descriptions.get(testIdentifier);
		if (currentDescription == null) {
			TestIdentifier parentIdentifier = _testPlan.getTestIdentifier(testIdentifier.getParentId().get());
			Description parentDescription = getCurrentTestDescription(parentIdentifier);
			if (parentDescription == null) {
				parentDescription = _testSuiteDescription;
			}
			currentDescription = Description.createTestDescription(parentDescription.getClassName(),
					parentIdentifier.getLegacyReportingName() + " - " + testIdentifier.getDisplayName());
			parentDescription.addChild(currentDescription);
			_descriptions.put(testIdentifier, currentDescription);
		}
		return currentDescription;
	}

	@Override
	public Description getDescription() {
		return _testSuiteDescription;
	}
}