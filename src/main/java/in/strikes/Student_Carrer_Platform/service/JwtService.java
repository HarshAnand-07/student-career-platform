package in.strikes.Student_Carrer_Platform.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        this.jwtEncoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(key.getEncoded())
        );
    }

    public String generateToken(String email , String role){
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Student Carrer Platform")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(email)
                .claim("role" , role)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        org.springframework.security.oauth2.jwt
                                .JwsHeader.with(MacAlgorithm.HS256)
                                .build(),
                        claims
                )
        ).getTokenValue();
    }


}
