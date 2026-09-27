package com.hdfclife.ledger.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "claim_no", nullable = false, unique = true)
    private String claimNo;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;
    @Column(nullable = false)
    private int amount;
    @Column(nullable = false)
    private String urgency;
    @Column(nullable = false)
    private String status;

    protected Claim() {
    }

    public Claim(String claimNo, Policy policy, int amount, String urgency, String status) {
        this.claimNo = claimNo;
        this.policy = policy;
        this.amount = amount;
        this.urgency = urgency;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public Policy getPolicy() {
        return policy;
    }

    public int getAmount() {
        return amount;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getStatus() {
        return status;
    }
}
