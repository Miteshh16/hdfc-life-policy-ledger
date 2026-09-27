package com.hdfclife.ledger.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "riders")
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "riders", fetch = FetchType.LAZY)
    private List<Policy> policies = new ArrayList<>();

    protected Rider() {
    }

    public Rider(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Policy> getPolicies() {
        return policies;
    }
}
