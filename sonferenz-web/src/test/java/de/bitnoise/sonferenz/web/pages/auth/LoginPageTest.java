package de.bitnoise.sonferenz.web.pages.auth;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import de.bitnoise.sonferenz.testing.SpringWicketTest;

public class LoginPageTest extends SpringWicketTest {
	 
//	@Test
//	public void testSuccessfull() {
//		// prepare
//		 
//		LoginPage sut = new LoginPage();
//		tester.startPage(sut);
//		tester.assertNoErrorMessage();
//		FormTester form = tester.newFormTester("pageContent:form");
//		form.setValue("username", "username");
//		form.setValue("password", "userpwd");
//		form.submit();
//		tester.assertNoErrorMessage();
//	}

	@Test
	public void testInvalidUser() {
		LoginPage sut = new LoginPage();
		tester.startPage(sut);
		tester.assertNoErrorMessage();
		FormTester form = tester.newFormTester("pageContent:form");
		form.setValue("username", "username-wrong");
		form.setValue("password", "userpwd");
		form.submit();
		tester.assertErrorMessages(new String[] { "Login failed : Bad credentials" });
		tester.assertRenderedPage(LoginPage.class);
	}

	@Test
	public void testInvalidPassword() {
		LoginPage sut = new LoginPage();
		tester.startPage(sut);
		tester.assertNoErrorMessage();
		FormTester form = tester.newFormTester("pageContent:form");
		form.setValue("username", "username");
		form.setValue("password", "userpwd-wrong");
		form.submit();
		tester.assertErrorMessages(new String[] { "Login failed : Bad credentials" });
		tester.assertRenderedPage(LoginPage.class);
	}
}
