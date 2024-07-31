package com.bteam.Booking_Beacon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingBeaconApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingBeaconApplication.class, args);
	}

}
