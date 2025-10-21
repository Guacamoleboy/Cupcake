// Package
package dk.cupcake.server;

// Imports
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TokenGenerator {

    // Attributes
    private static final Map<String, String> tokenMap = new HashMap<>(); // token -> userId
    private static final Map<String, Instant> tokenExpiry = new HashMap<>();
    private static final long TOKEN_LIFETIME = 5 * 60; // 5 minutter
    private static final Random random = new Random();

    // _________________________________________________________

    public static String generateResetToken(int userId) {
        String token = String.valueOf(100000 + random.nextInt(900000));
        tokenMap.put(token, String.valueOf(userId));
        tokenExpiry.put(token, Instant.now().plusSeconds(TOKEN_LIFETIME));
        return token;
    }

    // _________________________________________________________

    public static boolean isValidToken(String token) {
        Instant exp = tokenExpiry.get(token);
        if (exp == null) return false;
        if (Instant.now().isAfter(exp)) {
            invalidateToken(token);
            return false;
        }
        return true;
    }

    // _________________________________________________________

    public static int getUserIdByToken(String token) {
        if (tokenMap.containsKey(token)) {
            return Integer.parseInt(tokenMap.get(token));
        } else {
            return -1;
        }
    }

    // _________________________________________________________

    public static void invalidateToken(String token) {
        tokenMap.remove(token);
        tokenExpiry.remove(token);
    }

} // TokenGenerator