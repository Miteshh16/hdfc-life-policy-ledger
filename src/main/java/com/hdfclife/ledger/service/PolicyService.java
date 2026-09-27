package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.*;
import com.hdfclife.ledger.dto.*;
import com.hdfclife.ledger.exception.*;
import com.hdfclife.ledger.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PolicyService {
    private final PolicyRepository policies;
    private final CustomerRepository customers;
    private final RiderRepository riders;

    public PolicyService(PolicyRepository policies, CustomerRepository customers, RiderRepository riders) {
        this.policies = policies;
        this.customers = customers;
        this.riders = riders;
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> all() {
        return policies.findAll(org.springframework.data.domain.Sort.by("policyNo")).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PolicyResponse get(String no) {
        return toResponse(policies.findByPolicyNo(no).orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + no)));
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> byStatus(String s) {
        return policies.findByStatusOrderByPolicyNoAsc(s).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> byType(String t) {
        return policies.findByProductTypeOrderByPolicyNoAsc(t).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> byCustomer(String c) {
        return policies.findByCustomer_FullNameOrderByPolicyNoAsc(c).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> premium(Integer min) {
        if (min == null || min < 0) throw new InvalidRequestException("minPremium must be >= 0");
        return policies.findWithPremiumAtLeast(min).stream().map(this::toResponse).toList();
    }

    @Transactional
    public PolicyResponse create(CreatePolicyRequest r) {
        if (policies.existsByPolicyNo(r.policyNo()))
            throw new DuplicatePolicyException("Policy already exists: " + r.policyNo());
        Customer c = customers.findByEmail(r.email()).orElseGet(() -> customers.save(new Customer(r.customer(), r.email())));
        Policy p = policies.save(new Policy(r.policyNo(), c, r.type(), r.basePremium(), r.status()));
        return toResponse(p);
    }

    @Transactional
    public void delete(String no) {
        Policy p = policies.findByPolicyNo(no).orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + no));
        policies.delete(p);
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> allForStartup() {
        return all();
    }

    public PolicyResponse toResponse(Policy p) {
        List<String> codes = p.getRiders().stream().map(Rider::getCode).sorted().collect(Collectors.toList());
        return new PolicyResponse(p.getPolicyNo(), p.getCustomer().getFullName(), p.getCustomer().getEmail(), p.getProductType(), p.getBasePremium(), p.getStatus(), codes);
    }
}
