package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodeTest {

	
	
	@Test
	public void testpasswordencoder() {
		
		
		BCryptPasswordEncoder PasswordEncoder= new BCryptPasswordEncoder();
		String rawpassword="ABCD@123";
		
		String encodedpass=PasswordEncoder.encode(rawpassword);
		System.out.println(encodedpass);
		
		boolean matches = PasswordEncoder.matches(rawpassword, encodedpass);
		
		assertThat(matches).isTrue();
		
	}
	
}
