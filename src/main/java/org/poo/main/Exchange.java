package org.poo.main;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class Exchange{
    private static final Map<String, Map<String, Double>> exchangeRates = new HashMap<>();

    public static void addExchangeRate(String from, String to, double rate) {
        exchangeRates.computeIfAbsent(from, k -> new HashMap<>()).put(to, rate);
        exchangeRates.computeIfAbsent(to, k -> new HashMap<>()).put(from, 1/rate);

    }

    public static void loadRates(JsonNode rates) {
        for (JsonNode rate : rates) {
            String from = rate.get("from").asText();
            String to = rate.get("to").asText();
            double value = rate.get("rate").asDouble();
            addExchangeRate(from, to, value);
        }
    }

    public static double convert(String from, String to, double amount) {
        if (from.equals(to)) {
            return amount;
        }

        // Direct conversion
        Double directRate = exchangeRates.getOrDefault(from, new HashMap<>()).get(to);
        if (directRate != null) {
            return amount * directRate;
        }

        // Attempt indirect conversion
        for (String intermediate : exchangeRates.getOrDefault(from, new HashMap<>()).keySet()) {
            Double firstStepRate = exchangeRates.get(from).get(intermediate);
            Double secondStepRate = exchangeRates.getOrDefault(intermediate, new HashMap<>()).get(to);
            if (firstStepRate != null && secondStepRate != null) {
                return amount * firstStepRate * secondStepRate;
            }
        }

        throw new IllegalArgumentException("Exchange rate not found for " + from + " to " + to);
    }
}
