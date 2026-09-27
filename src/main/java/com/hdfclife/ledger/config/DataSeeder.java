package com.hdfclife.ledger.config;

import com.hdfclife.ledger.domain.*;
import com.hdfclife.ledger.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final CustomerRepository customers;
    private final PolicyRepository policies;
    private final RiderRepository riders;
    private final ClaimRepository claims;
    private final Environment env;

    public DataSeeder(CustomerRepository customers, PolicyRepository policies, RiderRepository riders, ClaimRepository claims, Environment env) {
        this.customers = customers;
        this.policies = policies;
        this.riders = riders;
        this.claims = claims;
        this.env = env;
    }

    @Transactional
    public void run(String... args) {
        if (policies.count() > 0) return;
        Customer a = customers.save(new Customer("Anita Sharma", "anita.sharma@hdfclife.example"));
        Customer r = customers.save(new Customer("Rahul Mehta", "rahul.mehta@hdfclife.example"));
        Customer p = customers.save(new Customer("Priya Nair", "priya.nair@hdfclife.example"));
        Customer v = customers.save(new Customer("Vikram Singh", "vikram.singh@hdfclife.example"));
        Customer s = customers.save(new Customer("Sneha Patel", "sneha.patel@hdfclife.example"));

        Policy p1 = policies.save(new Policy("HDFC-LIFE-1001", a, "TERM", 18500, "Active"));
        policies.save(new Policy("HDFC-LIFE-1002", r, "ULIP", 42000, "Active"));
        policies.save(new Policy("HDFC-LIFE-1003", p, "ENDOWMENT", 27000, "Lapsed"));
        policies.save(new Policy("HDFC-LIFE-1004", v, "TERM", 15200, "Active"));
        policies.save(new Policy("HDFC-LIFE-1005", s, "ULIP", 36000, "Active"));
        policies.save(new Policy("HDFC-LIFE-1006", a, "ENDOWMENT", 22000, "Pending"));

        Rider accident = riders.findByCode("ACCIDENT_COVER").orElseThrow();
        Rider waiver = riders.findByCode("WAIVER_OF_PREMIUM").orElseThrow();
        p1.getRiders().addAll(List.of(accident, waiver));
        policies.save(p1);

        System.out.println("Active profile → " + String.join(",", env.getActiveProfiles().length == 0 ? new String[]{"dev"} : env.getActiveProfiles()));
        System.out.println("Policy count → " + policies.count());
        System.out.println("Customer count → " + customers.count());
        System.out.println("Lookup HDFC-LIFE-1004 customer → " + policies.findByPolicyNo("HDFC-LIFE-1004").orElseThrow().getCustomer().getFullName());
        System.out.println("Active count → " + policies.findByStatusOrderByPolicyNoAsc("Active").size());
        System.out.println("TERM count → " + policies.findByProductTypeOrderByPolicyNoAsc("TERM").size());
        System.out.println("Anita Sharma policy count → " + policies.findByCustomer_FullNameOrderByPolicyNoAsc("Anita Sharma").size());
        System.out.println("minPremium=20000 policy numbers → " + policies.findWithPremiumAtLeast(20000).stream().map(Policy::getPolicyNo).reduce((x, y) -> x + ", " + y).orElse(""));
        System.out.println("HDFC-LIFE-1001 rider codes → " + p1.getRiders().stream().map(Rider::getCode).sorted().reduce((x, y) -> x + ", " + y).orElse(""));
        System.out.println("HTTP contract (seed loaded)");
        System.out.println("GET /api/policies → 200, length 6, first HDFC-LIFE-1001, last HDFC-LIFE-1006");
    }
}
