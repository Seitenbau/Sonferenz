package de.bitnoise.sonferenz.testing;

import static org.fest.reflect.core.Reflection.staticMethod;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.testing.mockito.MockitoRule;

/**
 * Creates a wicketTester instance but no Spring context
 */
public abstract class WicketTestBase extends TestBase {

	@Mock
	public KonferenzSession session;

	protected WicketTester tester;

	@Before
	public void setupWicketTester() {
		tester = new WicketTester();
		staticMethod("setInternalMockForSession")
				.withParameterTypes(KonferenzSession.class)
				.in(KonferenzSession.class)
				.invoke(session);
	}

}
