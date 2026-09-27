package com.hdfclife.ledger.dto;

import com.hdfclife.ledger.validation.PolicyType;
import jakarta.validation.constraints.*;

public record CreatePolicyRequest(
        @NotBlank @Pattern(regexp = "HDFC-LIFE-[0-9]{4}") String policyNo,
        @NotBlank @Size(min = 2, max = 120) String customer,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "TERM|ULIP|ENDOWMENT") @PolicyType String type,
        @NotNull @Min(1) Integer basePremium,
        @NotBlank @Pattern(regexp = "Active|Lapsed|Pending") String status
) {
}
