package de.bitnoise.sonferenz.web.component.footer;

import org.junit.Test;

import de.bitnoise.sonferenz.Version;
import de.bitnoise.sonferenz.testing.WicketTestBase;

public class VersionPanelTest extends WicketTestBase {

	@Test
	public void testSuccessfull() {
		tester.startPanel(VersionPanel.class);
		tester.assertLabel("panel:versionText", Version.VERSION);
	}
}
