package com.hitachi.jwtqr.controller;

import com.hitachi.jwtqr.service.JwtService;
import com.hitachi.jwtqr.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class JwtController {

    private final JwtService jwtService;
    private final QrCodeService qrCodeService;

    @Autowired
    public JwtController(JwtService jwtService, QrCodeService qrCodeService) {
        this.jwtService = jwtService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/generate-jwt")
    public ResponseEntity<byte[]> generateJwtAndQrCode(
            @RequestParam int expiresIn,
            @RequestParam int startingStationNumber,
            @RequestParam int validStations) throws Exception {

        // Get the current system time for createdAt
        long createdAt = System.currentTimeMillis();

        // Generate the JWT
        String jwt = jwtService.generateJwt(createdAt, expiresIn, startingStationNumber, validStations);

        // Generate the QR code with the JWT as the payload
        byte[] qrCodeImage = qrCodeService.generateQrCode(jwt);

        // Return the QR code image as a response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
        return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
    }
}
