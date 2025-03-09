package edu.eci.cvds.proyect.booking;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.Test;
@ContextConfiguration(classes = BookingApplication.class)
@SpringBootTest
@EnableAutoConfiguration
class BookingApplicationTests {

	@Test
	void contextLoads() {
	}
}
