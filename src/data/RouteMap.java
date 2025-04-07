package data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import entity.Routes;

public class RouteMap {

    public static HashMap<String, String> routeMap = new HashMap<>();
    public static HashMap<String, Double> routeDistance = new HashMap<>();
    public static HashMap<String, Double> routeFare = new HashMap<>();
    public static HashMap<String, LocalDate> routeDepartureDate = new HashMap<>();

    static {
        // Define journey routes
        routeMap.put("journey1", "Chennai-Mumbai");
        routeMap.put("journey2", "Bangalore-Hyderabad");
        routeMap.put("journey3", "Mumbai-Delhi");
        routeMap.put("journey4", "Delhi-Kolkata");
        routeMap.put("journey5", "Chennai-Coimbatore");

        // Define journey distances
        routeDistance.put("journey1", 1330.0);
        routeDistance.put("journey2", 570.0);
        routeDistance.put("journey3", 1410.0);
        routeDistance.put("journey4", 1500.0);
        routeDistance.put("journey5", 510.0);

        // Define journey fares
        routeFare.put("journey1", 999.0);
        routeFare.put("journey2", 450.0);
        routeFare.put("journey3", 1100.0);
        routeFare.put("journey4", 1200.0);
        routeFare.put("journey5", 400.0);

        // Define departure dates
        routeDepartureDate.put("journey1", LocalDate.of(2025, 4, 10));
        routeDepartureDate.put("journey2", LocalDate.of(2025, 4, 12));
        routeDepartureDate.put("journey3", LocalDate.of(2025, 4, 15));
        routeDepartureDate.put("journey4", LocalDate.of(2025, 4, 18));
        routeDepartureDate.put("journey5", LocalDate.of(2025, 4, 20));
    }

    public static String getRoute(String journeyKey) {
        return routeMap.get(journeyKey);
    }

    public static Double getDistance(String journeyKey) {
        return routeDistance.get(journeyKey);
    }

    public static LocalDate getDepartureDate(String journeyKey) {
        return routeDepartureDate.get(journeyKey);
    }

    public static void displayAvailableRoute() {
        System.out.println("Available Routes:");
        for (Map.Entry<String, String> entry : routeMap.entrySet()) {
            String key = entry.getKey();
            String route = entry.getValue();
            Double fare = routeFare.get(key);
            LocalDate date = routeDepartureDate.get(key);
            System.out.println(key + ": " + route + " | Fare: ₹" + fare + " | Departure Date: " + date);
        }
    }

    public static Routes getRouteDetails(String journeyKey) {
        String routeString = routeMap.get(journeyKey);
        if (routeString == null) return null;

        String[] parts = routeString.split("-");
        if (parts.length != 2) return null;

        double distance = routeDistance.getOrDefault(journeyKey, 0.0);
        double fare = routeFare.getOrDefault(journeyKey, 0.0);
        LocalDate departureDate = routeDepartureDate.getOrDefault(journeyKey, null);

        return new Routes(parts[0], parts[1], distance, fare, departureDate); // make sure Routes class supports this constructor
    }
}
