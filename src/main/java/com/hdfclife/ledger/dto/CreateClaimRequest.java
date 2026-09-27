package com.hdfclife.ledger.dto;

import jakarta.validation.constraints.*;

public record CreateClaimRequest(
        @NotBlank @Pattern(regexp = "HDFC-LIFE-[0-9]{4}") String policyNo,
        @NotNull @Min(1) @Max(500000) Integer claimAmount,
        @NotBlank @Pattern(regexp = "HIGH|MEDIUM|LOW") String urgency,
        @Size(max = 120) String hospitalName
) {
}
