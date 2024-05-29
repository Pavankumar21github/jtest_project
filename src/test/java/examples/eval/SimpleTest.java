/**
 * 
 */
package examples.eval;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for Simple
 *
 * @see examples.eval.Simple
 * @author Pavan
 */
public class SimpleTest {

	/**
	 * Parasoft Jtest UTA: Test for map(int)
	 *
	 * @see examples.eval.Simple#map(int)
	 * @author Pavan
	 */
	@Test
	public void testMap() throws Throwable {
		// When
		int index = 10; // UTA: provided value
		int result = Simple.map(index);

		// Then
		assertEquals(-1, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for map(int)
	 *
	 * @see examples.eval.Simple#map(int)
	 * @author Pavan
	 */
	@Test
	public void testMap2() throws Throwable {
		// When
		int index = 2; // UTA: provided value
		int result = Simple.map(index);

		// Then
		assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for map(int)
	 *
	 * @see examples.eval.Simple#map(int)
	 * @author Pavan
	 */
	@Test
	public void testMap3() throws Throwable {
		// When
		int index = 1; // UTA: provided value
		int result = Simple.map(index);

		// Then
		assertEquals(-2, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for startsWith(String, String)
	 *
	 * @see examples.eval.Simple#startsWith(String, String)
	 * @author Pavan
	 */
	@Test
	public void testStartsWith() throws Throwable {
		// When
		String str = "str"; // UTA: default value
		String match = "fail"; // UTA: provided value
		boolean result = Simple.startsWith(str, match);

		// Then
		assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for startsWith(String, String)
	 *
	 * @see examples.eval.Simple#startsWith(String, String)
	 * @author Pavan
	 */
	@Test
	public void testStartsWith2() throws Throwable {
		// When
		String str = "str"; // UTA: default value
		String match = "invaild"; // UTA: provided value
		boolean result = Simple.startsWith(str, match);

		// Then
		assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test cloned from
	 * examples.eval.SimpleTest#testStartsWith2()
	 *
	 * @see examples.eval.Simple#startsWith(String, String)
	 * @author Pavan
	 */
	@Test
	public void testStartsWith3() throws Throwable {
		// When
		String str = "str"; // UTA: default value
		String match = "true"; // UTA: provided value
		boolean result = Simple.startsWith(str, match);

		// Then
		assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for startsWith(String, String)
	 *
	 * @see examples.eval.Simple#startsWith(String, String)
	 * @author Pavan
	 */
	@Test
	public void testStartsWith4() throws Throwable {
		// When
		String str = "str"; // UTA: default value
		String match = "match"; // UTA: default value
		boolean result = Simple.startsWith(str, match);

		// Then
		assertFalse(result);
	}
}
