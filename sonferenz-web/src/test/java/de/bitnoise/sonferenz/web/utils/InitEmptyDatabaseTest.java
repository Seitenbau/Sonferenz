package de.bitnoise.sonferenz.web.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.testing.TestBase;
import static org.mockito.Mockito.*;

public class InitEmptyDatabaseTest extends TestBase {
	InitEmptyDatabase sut;
	@Mock
	ConfigurationService config;
	@Mock
	StaticContentService texte;
	@Mock
	UserService user;
	@Mock
	RoleRepository roles;

	@Before
	public void create() {
		sut = new InitEmptyDatabase();
		sut.config = config;
		sut.texte = texte;
		sut.user = user;
		sut.roles = roles;
		when(config.getIntegerValue(-1, "intern.database-is-initialized"))
				.thenReturn(-1);
	}

	@Test
	public void testDoInitialize() {
		// prepare

		// execute
		sut.initAemptyDatabase();

		// verify
		verify(config).initValue(anyString(), anyInt());
		verify(config).initValue("intern.database-is-initialized", 1);
	}

	@Test
	public void testDoNotInitialize() {
		// prepare
		when(config.getIntegerValue(-1, "intern.database-is-initialized"))
				.thenReturn(1);

		// execute
		sut.initAemptyDatabase();

		// verify
		verify(config).getIntegerValue(-1, "intern.database-is-initialized");
		verifyNoMoreInteractions(config);
		verifyZeroInteractions(texte, user, roles);
	}
}
