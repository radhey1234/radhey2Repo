package com.iexpress.spring.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.repo.UserRepo;
import com.iexpress.spring.domain.DeviceDetail;
import com.iexpress.spring.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    public static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final long serialVersionUID = -3301605591108950415L;
	 public static final String SECRET = "~sbA{SK^z(~7X%A";
    
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_DEVICE_KEY = "SINGLE_USER_LOGIN";
    private static final String CLAIM_NOT_FOUND_LOG = "Claim Not Found -";
    private static final String CLAIM_DEVICE_ID = "DEVICE_ID";
    @Autowired private UserRepo userRepo;
    
    /**
     * Get User Name from Token
     * @param token
     * @return userName
     */
    public int getUsernameFromToken(String token) {
        int username;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                logger.debug(CLAIM_NOT_FOUND_LOG);
                return 0;
            }
            username =  (int) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            logger.error("UserName Not Found -", e);
            username = 0;
        }
        return username;
    }
    
    /**
     * Get Expiration date from Token
     * @param token
     * @return date
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);

            if (claims == null) {
            	logger.debug(CLAIM_NOT_FOUND_LOG);
                return null;
            }

            expiration = claims.getExpiration();
        } catch (Exception e) {
            logger.error("Date parsing problem -", e);
            expiration = null;
        }
        return expiration;
    }

    /**
     * Get Audience From Token
     * @param token
     * @return audience
     */
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
            	logger.debug(CLAIM_NOT_FOUND_LOG);
                return null;
            }
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            logger.error("Audience parsing problem -", e);
            audience = null;
        }
        return audience;
    }

    /**
     * Get device id or device token 
     * @param token
     * @return
     */
    public String getDeviceTokenFromToken(String token) {
        String pass = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
            	logger.debug(CLAIM_NOT_FOUND_LOG);
            }
            pass = (String) claims.get(CLAIM_DEVICE_KEY);
        } catch (Exception e) {
            logger.error("Password parsing problem -", e);
        }
        return pass;
    }

    /**
     * Get device id or device token 
     * @param token
     * @return
     */
    public int getDeviceIdFromToken(String token) {
        int pass = 0;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
            	logger.debug(CLAIM_NOT_FOUND_LOG);
            }
            pass = (int) claims.get(CLAIM_DEVICE_ID);
        } catch (Exception e) {
            logger.error("Password parsing problem -", e);
        }
        return pass;
    }

    /**
     * Get Claim from token
     * @param token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

   

    /**
     * Generates the Token from User Details object
     * @param userDetails
     * @return
     */
    public String generateToken(JwtUser user) {
	    logger.info("Inside generateToken ");	
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getId());
        claims.put(CLAIM_KEY_AUDIENCE, user.getPassword());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_DEVICE_KEY, user.getDeviceToken());
        claims.put(CLAIM_DEVICE_ID, user.getDeviceId());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    @Transactional
	public boolean validateToken(JwtUser jwtUser) {
    	
    	if(userRepo.findById(jwtUser.getId()).isPresent()) {
    		User user = userRepo.findById(jwtUser.getId()).get();
    		if(userRepo.findById(jwtUser.getId()).isPresent() && null != user.getDeviceDetails() && user.getDeviceDetails().size() != 0) {

    		DeviceDetail device = user.getDeviceDetails().get(0);
			if(device.getDeviceToken().equalsIgnoreCase(jwtUser.getDeviceToken())  && device.getId() == jwtUser.getDeviceId()) {
				return true;
			}
		}
    	}
		return false;
	}

	public JwtUser buildJwtUserFromAuthHeader(String headerValue) {
		return new JwtUser(getUsernameFromToken(headerValue), getAudienceFromToken(headerValue), getDeviceIdFromToken(headerValue), getDeviceTokenFromToken(headerValue));
	}

}