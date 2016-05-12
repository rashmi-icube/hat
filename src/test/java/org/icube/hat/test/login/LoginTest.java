package org.icube.hat.test.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.icube.hat.login.Login;
import org.icube.hat.user.User;
import org.junit.Test;

public class LoginTest {

	Login l = new Login();

	@Test
	public void testLoginPass() {
		try {
			User u = l.login("compadmin@flipkart.com", "abc123", "114.9.1.2");
			assertNotNull(u.getCompanyId());
			assertNotNull(u.getDisplayName());
			assertNotNull(u.getRoleId());
			assertNotNull(u.getUserId());
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid credentials!!!");
		}
	}

	@Test
	public void testLoginFail() {
		try {
			l.login("compadmin@flipkart.com", "wrongpassword", "114.9.1.2");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid credentials!!!");
		}
	}
}
