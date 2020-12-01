package com.yu.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.yu.model.Role;
import com.yu.model.dto.AuthRefreshDto;
import com.yu.model.dto.AuthResultDto;
import com.yu.model.dto.LoginDto;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

@Component
public class JwtTokenController {

    @Value("${authService.jwt.jwsAlgorithmName}")
    protected String jwsAlgorithmName;

    /**
     * private key generated with EC256, in PEM format
     */
    @Value("${authService.jwt.privateKey_PEM}")
    protected String privateKeyPem;

    /**
     * public key generated with EC256, in PEM format
     */
    @Value("${authService.jwt.publicKey_PEM}")
    protected String publicKeyPem;

    @Value("${authService.jwt.tokenValidTimeSec}")
    protected int tokenValidTimeSec;

    @Value("${authService.jwt.tokenIssuer}")
    protected String tokenIssuer;

    private JWSHeader jwsHeader;
    private JWSSigner signer;
    private JWSVerifier verifier;

    @Autowired
    protected CurrentTimeController currentTimeController;

    @Autowired
    protected CurrentAuthController currentAuthController;

    private static final String CLAIM_FIELD_USERNAME = "username";
    private static final String CLAIM_FIELD_ROLE_LIST = "role";

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenController.class);

    @PostConstruct
    public void init(){
        Security.addProvider(new BouncyCastleProvider());

        // setup jwt tools: algorithm, header, signer, verifier
        try {
            JWSAlgorithm jwsAlgorithm = new JWSAlgorithm(this.jwsAlgorithmName);
            this.jwsHeader = new JWSHeader(jwsAlgorithm);
            PrivateKey privateKey = this.createPrivateKey();
            PublicKey publicKey = this.createPublicKey();
            signer = new ECDSASigner((ECPrivateKey) privateKey);
            verifier = new ECDSAVerifier((ECPublicKey) publicKey);
        } catch (JOSEException ex) {
            logger.error("failed to create JWT toolset: {}", ex.getMessage(), ex);
            throw new RuntimeException("failed to create JWT toolset", ex);
        } catch (IOException ex) {
            logger.error("failed to create JWT toolset: {}", ex.getMessage(), ex);
            throw new RuntimeException("failed to create JWT toolset", ex);
        }
    }

    private PrivateKey createPrivateKey() throws IOException {
        PrivateKey privateKey;
        String pemBase64 = "-----BEGIN PRIVATE KEY-----\n"
                +this.privateKeyPem
                .replaceAll("[-]*BEGIN PRIVATE KEY[-]*", "")
                .replaceAll("[-]*END PRIVATE KEY[-]*", "")
                .replace(" ", "")+"\n"
                +"-----END PRIVATE KEY-----";
//        logger.debug("private key raw: {}", pemBase64);
        PEMParser parser = new PEMParser(new StringReader(pemBase64));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(parser.readObject());
        privateKey = converter.getPrivateKey(keyInfo);
//        logger.debug("private key parsed: {}", privateKey);
        return privateKey;
    }

    private PublicKey createPublicKey() throws IOException {
        PublicKey publicKey;
        String pemBase64 = "-----BEGIN PUBLIC KEY-----\n"
                +this.publicKeyPem
                .replaceAll("[-]*BEGIN PUBLIC KEY[-]*", "")
                .replaceAll("[-]*END PUBLIC KEY[-]*", "")
                .replace(" ", "")+"\n"
                +"-----END PUBLIC KEY-----";
//        logger.debug("public key raw: {}", pemBase64);
        PEMParser parser = new PEMParser(new StringReader(pemBase64));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(parser.readObject());
        publicKey = converter.getPublicKey(keyInfo);
//        logger.debug("public key parsed: {}", publicKey);
        return publicKey;
    }

    public AuthResultDto createLoginToken(String username, Role[] roleList){
        AuthResultDto dto = new AuthResultDto();
        this.createAuthToken(username, roleList, tokenStr -> {
            dto.access_token = tokenStr;
        });
        return dto;
    }

    /**
     *
     * @return username
     */
    public String createAuthRefreshToken(AuthRefreshDto dto){
        String username = currentAuthController.getCurrentUsername();
        Role[] roleList = currentAuthController.getCurrentRoleList();
        this.createAuthToken(username, roleList, tokenStr -> {
            dto.access_token = tokenStr;
            dto.expires_in = this.tokenValidTimeSec;
        });
        return username;
    }

    private void createAuthToken(String username, Role[] roleList,
                                  Consumer<String> tokenConsumer)
    {
        try {
            Instant currentTime = this.currentTimeController.getCurrentTime();
            Date expiryTime = Date.from(
                    currentTime.plus(this.tokenValidTimeSec, ChronoUnit.SECONDS)
            );
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(this.tokenIssuer)
                    .expirationTime(expiryTime)
                    .claim(CLAIM_FIELD_USERNAME, username)
                    .claim(CLAIM_FIELD_ROLE_LIST, roleList)
                    .build();
            SignedJWT signedToken = new SignedJWT(jwsHeader, claimsSet);
            signedToken.sign(this.signer);

            // encode token in hex/compact format
            String tokenStr = signedToken.serialize();
            logger.debug("jwt signed, token = {}", tokenStr);
            tokenConsumer.accept(tokenStr);
        } catch (JOSEException ex) {
            logger.error("failed to create jwt token", ex);
            throw new RuntimeException("failed to create jwt token", ex);
        }
    }

    /**
     * @return token if verification passed, null for failure
     */
    public SignedJWT verifyToken(AuthResultDto token) {
        try {
            SignedJWT parsedToken = SignedJWT.parse(token.access_token);
//            logger.debug("token claim[username] = {}",
//                    parsedToken.getJWTClaimsSet().getClaim("username"));
            boolean isTokenValid = parsedToken.verify(this.verifier);
//            logger.debug("verify valid ? {}", isTokenValid);
            Instant currentTime = this.currentTimeController.getCurrentTime();
            if (currentTime.isAfter(
                parsedToken.getJWTClaimsSet().getExpirationTime().toInstant()))
            {
                // token expired
                return null;
            }
            return isTokenValid ? parsedToken : null;
        } catch (ParseException ex) {
            logger.error("failed while verifying the token", ex);
            return null;
        } catch (JOSEException ex) {
            logger.error("failed while verifying the token", ex);
            return null;
        }
    }

    public void jwtSignTest(){
        try {
            AuthResultDto token = this.createLoginToken(
                    "user101", new Role[]{Role.ROOT_ADMIN, Role.USER_ADMIN});
            SignedJWT verifiedToken = this.verifyToken(token);
            logger.info("token valid = {}, {}", verifiedToken!=null, verifiedToken);
        } catch (Exception ex) {
            logger.error("error in jwtSignTest: {}", ex.getMessage(), ex);
        }
    }

    public String getUsernameInToken(JWTClaimsSet claimsSet) throws ParseException {
        return claimsSet.getStringClaim(CLAIM_FIELD_USERNAME);
    }

    public Role[] getRoleListInToken(JWTClaimsSet claimsSet) throws ParseException {
        Role[] roleList = Arrays.stream(claimsSet.getStringArrayClaim(CLAIM_FIELD_ROLE_LIST))
                .map(r -> Enum.valueOf(Role.class, r))
                .toArray(Role[]::new);
        return roleList;
    }

}
