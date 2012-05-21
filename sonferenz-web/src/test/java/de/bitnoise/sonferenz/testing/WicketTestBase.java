package de.bitnoise.sonferenz.testing;

import static org.fest.reflect.core.Reflection.staticMethod;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.testing.mockito.MockitoRule;

/**
 * Creates a wicketTester instance but no Spring context
 */
public abstract class WicketTestBase extends TestBase {

	@Mock
	public KonferenzSession session;

	protected WicketTester tester;

	@Before
	public void setupMockedSession() {
		tester = new WicketTester();
		staticMethod("setInternalMockForSession")
				.withParameterTypes(KonferenzSession.class)
				.in(KonferenzSession.class).invoke(session);
	}

	@AfterClass
	public static void resetMockedSession() {
		staticMethod("setInternalMockForSession")
				.withParameterTypes(KonferenzSession.class)
				.in(KonferenzSession.class).invoke(new Object[] { null });
	}

}
