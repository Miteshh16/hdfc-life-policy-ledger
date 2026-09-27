package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.*;
import com.hdfclife.ledger.dto.*;
import com.hdfclife.ledger.exception.*;
import com.hdfclife.ledger.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClaimService {
    private final ClaimRepository claims;
    private final PolicyRepository policies;

    public ClaimService(ClaimRepository claims, PolicyRepository policies) {
        this.claims = claims;
        this.policies = policies;
    }

    @Transactional
    public ClaimResponse create(CreateClaimRequest r) {
        Policy p = policies.findByPolicyNo(r.policyNo()).orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + r.policyNo()));
        long n = claims.count() + 1;
        String no;
        do {
            no = String.format("CLM-%02d", n++);
        } while (claims.findByClaimNo(no).isPresent());
        Claim c = claims.save(new Claim(no, p, r.claimAmount(), r.urgency(), "SUBMITTED"));
        return toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> forPolicy(String no) {
        if (policies.findByPolicyNo(no).isEmpty()) throw new PolicyNotFoundException("Policy not found: " + no);
        return claims.findByPolicy_PolicyNoOrderByClaimNoAsc(no).stream().map(this::toResponse).toList();
    }

    private ClaimResponse toResponse(Claim c) {
        return new ClaimResponse(c.getClaimNo(), c.getPolicy().getPolicyNo(), c.getAmount(), c.getUrgency(), c.getStatus());
    }
}
