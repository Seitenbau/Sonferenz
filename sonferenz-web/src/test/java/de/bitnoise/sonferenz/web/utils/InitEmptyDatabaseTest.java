package de.bitnoise.sonferenz.web.utils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.impl.EventingImpl;
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
	EventingImpl eventing;
	@Mock
	RoleRepository roles;

	@Before
	public void create() {
		sut = new InitEmptyDatabase();
		sut.config = config;
		sut.texte = texte;
		sut.user = user;
		sut.roles = roles;
		sut.eventing= eventing;
		when(config.getIntegerValue(-1, "intern.database-is-initialized"))
		.thenReturn(-1);
	}

	@Test
	public void testDoInitialize() {
		// prepare

		// execute
		sut.initAemptyDatabase();

		// verify
		verify(config,times(12)).getIntegerValue(-1, "intern.database-is-initialized");
	  	verify(config).initValue("intern.database-is-initialized", 1);
	  	verify(config).saveIntegerValue("intern.database-is-initialized", 2);
		
		// Many setup calls :
		// verifyNoMoreInteractions(config);
	}

	@Test
	@Ignore
	public void testLatestVersion() {
		// prepare
		when(config.getIntegerValue(-1, "intern.database-is-initialized"))
				.thenReturn(2);

		// execute
		sut.initAemptyDatabase();

		// verify
	    verify(config,times(4)).getIntegerValue(-1, "intern.database-is-initialized");
		verify(config).saveIntegerValue("intern.database-is-initialized", 3);
		
		verifyNoMoreInteractions(config);
//		verifyZeroInteractions(texte, user, roles);
	}
	
}
