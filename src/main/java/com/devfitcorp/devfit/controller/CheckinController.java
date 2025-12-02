package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.CheckinRequest;
import com.devfitcorp.devfit.model.Checkin;
import com.devfitcorp.devfit.service.CheckinService;
import com.devfitcorp.devfit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    private final CheckinService checkinService;
    private final UsuarioService usuarioService;

    public CheckinController(CheckinService checkinService, UsuarioService usuarioService) {
        this.checkinService = checkinService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Checkin> registrarCheckin(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CheckinRequest checkinRequest //Recebe "ENTRADA" ou "SAIDA"
    ) {

        Long usuarioId = usuarioService.getUserIdFromUsername(userDetails.getUsername());

        Checkin checkin = checkinService.registrarCheckin(
                usuarioId,
                checkinRequest.tipo()
        );
        return ResponseEntity.ok(checkin);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/picos")
    public ResponseEntity<?> getPeakHoursStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        if (data == null) {
            data = LocalDate.now();
        }
        return ResponseEntity.ok(checkinService.getPeakHoursStats(data));
    }
}