package com.gemini.mixerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MixerDemoApplication {



	//TODO check for sufficient diversity
	//for each unique deposit address, check that the coins outside > deposit amount (maybe a percentage), maybe also a min number of mixins (input parties)

	//TODO mixCoins
	//Randomly select coins

	//TODO sendMixedCoins
	//send from randomly selected source to random selection from destination addresses
	//possibly apply fee

	public static void main(String[] args) {
		SpringApplication.run(MixerDemoApplication.class, args);
	}

}
