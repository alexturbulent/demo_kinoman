package uz.oltinolma.producer.security.token;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import uz.oltinolma.producer.security.exceptions.JwtExpiredTokenException;

public class RawAccessJwtToken implements JwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private String token;
    private boolean fromMobileDevice;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid access token.", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException(this, "Access token is expired.", expiredEx);
        }
    }

    @Override
    public String getToken() {
        return token;
    }

    public boolean isFromMobileDevice() {
        return fromMobileDevice;
    }

    public void setFromMobileDevice(boolean fromMobileDevice) {
        this.fromMobileDevice = fromMobileDevice;
    }
}
