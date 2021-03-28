package edu.njit.cs630;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}


	/*@Bean
	CommandLineRunner start(SystemService systemService) {
		return args -> {
			System.out.println("****** System BIOS Information ******");
			Set<Map.Entry<String, String>> biosInfo = systemService.getBiosInfo().getFullInfo().entrySet();
			for (final Map.Entry<String, String> fullInfo : biosInfo) {
				System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
			}

			System.out.println("\n****** OS Information ******");
			Set<Map.Entry<String, String>> osInfo = systemService.getOSInfo().getFullInfo().entrySet();
			for (final Map.Entry<String, String> fullInfo : osInfo) {
				System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
			}

			System.out.println("\n****** System Processor Information ******");
			Set<Map.Entry<String, String>> processorInfo = systemService.getProcessorInfo().getFullInfo().entrySet();
			for (final Map.Entry<String, String> fullInfo : processorInfo) {
				System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
			}

			System.out.println("\n****** System Memory Information ******");
			Set<Map.Entry<String, String>> memoryInfo = systemService.getMemoryInfo().getFullInfo().entrySet();
			for (final Map.Entry<String, String> fullInfo : memoryInfo) {
				System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
			}

			System.out.println("\n****** System Motherboard Information ******");
			Set<Map.Entry<String, String>> motherBoardInfo = systemService.getMotherboardInfo().getFullInfo().entrySet();
			for (final Map.Entry<String, String> fullInfo : motherBoardInfo) {
				System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
			}

			System.out.println("\n****** System Display Information ******");
			List<Display> displays = systemService.getDisplayInfo().getDisplayDevices();
			for (Display display: displays) {
				System.out.println("Display Name: " + display.getName());
				System.out.println("Display Resolution: " + display.getCurrentResolution());
				System.out.println("Display Refresh Rate: " + display.getRefreshRate());
			}

			System.out.println("\n****** System Network Information ******");
			List<NetworkInterfaceInfo> networkInterfaceList = systemService.getNetworkInfo().getNetworkInterfaces();
			for (NetworkInterfaceInfo networkInterface: networkInterfaceList) {
				System.out.println("Network Interface Name: " + networkInterface.getName());
				System.out.println("IP Address: " + networkInterface.getIpv4());
				System.out.println("Transmitted Packets: " + networkInterface.getTransmittedPackets());
			}

			System.out.println("\n****** System Graphic Card Information ******");
			List<GraphicsCard> GraphicsCardList = systemService.getGraphicsCardInfo().getGraphicsCards();
			for (GraphicsCard graphicsCard: GraphicsCardList) {
				System.out.println("Graphic Card Name: " + graphicsCard.getName());
				System.out.println("Chip Type: " + graphicsCard.getChipType());
				System.out.println("Graphic Temperature: " + graphicsCard.getTemperature());
				System.out.println("Fan Speed: " + graphicsCard.getFanSpeed());
			}
		};
	}*/

}
