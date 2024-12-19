package org.poo.main;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class Exchange {
    private static final Map<String, Map<String, Double>> EXCHANGERATES = new HashMap<>();

    /**
     * Adds the exchange rate between two currencies to the internal map
     * This method ensures that both direct and inverse exchange rates are recorded
     * @param from The currency code from which the conversion starts
     * @param to The currency code to which the conversion goes
     * @param rate
     */

    public static void addExchangeRate(final String from, final String to, final double rate) {
        EXCHANGERATES.computeIfAbsent(from, k -> new HashMap<>()).put(to, rate);
        EXCHANGERATES.computeIfAbsent(to, k -> new HashMap<>()).put(from, 1 / rate);
    }

    /**
     * Loads multiple exchange rates into the system from a JSON node structure,
     * @param rates The JSON node containing an array of rate definitions,
     * where each rate includes 'from', 'to', and 'rate' fields.
     */
    public static void loadRates(final JsonNode rates) {
        for (JsonNode rate : rates) {
            String from = rate.get("from").asText();
            String to = rate.get("to").asText();
            double value = rate.get("rate").asDouble();
            addExchangeRate(from, to, value);
        }
    }

    /**
     * Converts a given amount from one currency to another using the stored exchange rates
     * @param from
     * @param to
     * @param amount
     * @return The converted amount in the 'to' currency
     * @throws IllegalArgumentException
     * If no exchange rate is found for the requested currency conversion
     */
    public static double convert(final String from, final String to, final double amount) {
        if (from.equals(to)) {
            return amount;
        }

        // Direct conversion
        Double directRate = EXCHANGERATES.getOrDefault(from, new HashMap<>()).get(to);
        if (directRate != null) {
            return amount * directRate;
        }

        // Attempt indirect conversion
        for (String intermediate : EXCHANGERATES.getOrDefault(from, new HashMap<>()).keySet()) {
            Double firstStepRate = EXCHANGERATES.get(from).get(intermediate);
            Double secondStepRate = EXCHANGERATES.getOrDefault(intermediate,
                    new HashMap<>()).get(to);
            if (firstStepRate != null && secondStepRate != null) {
                return amount * firstStepRate * secondStepRate;
            }
        }
        throw new IllegalArgumentException("Exchange rate not found for " + from + " to " + to);
    }
}
