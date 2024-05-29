/**
 * 
 */
package examples.flowanalysis;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.junit.After;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Parasoft Jtest UTA: Test class for AlwaysCloseGSS
 *
 * @see examples.flowanalysis.AlwaysCloseGSS
 * @author Pavan
 */
public class AlwaysCloseGSSTest {

	/**
	 * Parasoft Jtest UTA: Test for processClose(byte[])
	 *
	 * @see examples.flowanalysis.AlwaysCloseGSS#processClose(byte[])
	 * @author Pavan
	 */
	@Test
	public void testProcessClose() throws Throwable {
		// Given
		MockedStatic<GSSManager> mocked = mockStatic(GSSManager.class);
		mocks.add(mocked);

		GSSManager getInstanceResult = mock(GSSManager.class);
		GSSContext createContextResult = mock(GSSContext.class);
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				return null;
			}
		}).when(createContextResult).dispose();
		byte[] initSecContextResult = new byte[10];
		when(createContextResult.initSecContext(nullable(byte[].class), anyInt(), anyInt()))
				.thenReturn(initSecContextResult);
		when(getInstanceResult.createContext(nullable(byte[].class))).thenReturn(createContextResult);
		mocked.when(() -> GSSManager.getInstance()).thenReturn(getInstanceResult);

		AlwaysCloseGSS underTest = new AlwaysCloseGSS();

		// When
		byte[] tokens = new byte[10]; // UTA: default value
		underTest.processClose(tokens);

	}

	Set<AutoCloseable> mocks = new HashSet<>();

	@After
	public void closeMocks() throws Throwable {
		for (AutoCloseable mocked : mocks) {
			mocked.close();
		}
	}
}
