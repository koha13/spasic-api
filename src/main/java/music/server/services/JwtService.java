package music.server.services;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import music.server.config.CustomUserDetails;
import music.server.entities.User;
import music.server.models.Payload;
import music.server.repositories.UserRepository;

@Service
public class JwtService {

    @Autowired
    UserRepository userRepository;

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder().setSubject(Integer.toString(userDetails.getUser().getId()))
                .signWith(SignatureAlgorithm.HS256, userDetails.getUser().getPassword()).compact();
    }

    public User getUserFromToken(String token) throws JsonParseException, JsonMappingException, IOException {
        String[] part = token.split("\\.");
        byte[] decodebytes = Base64.getUrlDecoder().decode(part[1]);
        String payload = new String(decodebytes);
        ObjectMapper mapper = new ObjectMapper();
        Payload result = mapper.readValue(payload, Payload.class);
        int id = result.getSub();
        User user = userRepository.findById(id).get();
        if (user == null)
            return null;

        Claims claims = Jwts.parser().setSigningKey(user.getPassword()).parseClaimsJws(token).getBody();
        if (Integer.parseInt(claims.getSubject()) == id && validateToken(token, user.getPassword()))
            return user;
        return null;
    }

    public boolean validateToken(String authToken, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}