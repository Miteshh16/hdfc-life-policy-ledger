package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.*;
import com.hdfclife.ledger.service.ClaimService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/claims")
@Tag(name="Claims")
public class ClaimController {
    private final ClaimService service;
    public ClaimController(ClaimService service){this.service=service;}
    @PostMapping
    @Operation(summary="Create claim")
    public ResponseEntity<ClaimResponse> create(@Valid @RequestBody CreateClaimRequest r){
        ClaimResponse c=service.create(r);
        return ResponseEntity.created(URI.create("/api/claims/"+c.claimNo())).body(c);
    }
}
