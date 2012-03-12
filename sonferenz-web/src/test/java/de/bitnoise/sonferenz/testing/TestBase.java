package de.bitnoise.sonferenz.testing;

import org.junit.Rule;

import de.bitnoise.testing.mockito.MockitoRule;

public abstract class TestBase {

	@Rule
	public MockitoRule mock = new MockitoRule();

}
