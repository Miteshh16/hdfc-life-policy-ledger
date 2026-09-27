package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.*;
import com.hdfclife.ledger.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name="Policies")
public class PolicyController {
    private final PolicyService service;
    private final ClaimService claims;
    public PolicyController(PolicyService service, ClaimService claims){this.service=service;this.claims=claims;}

    @GetMapping
    @Operation(summary="List policies")
    @ApiResponse(responseCode="200")
    public List<PolicyResponse> all(@RequestParam(required=false) String status,@RequestParam(required=false,name="type") String type,@RequestParam(required=false) String customer){
        if(status!=null)return service.byStatus(status);
        if(type!=null)return service.byType(type);
        if(customer!=null)return service.byCustomer(customer);
        return service.all();
    }
    @GetMapping("/{policyNo}")
    @Operation(summary="Get policy")
    public PolicyResponse get(@PathVariable String policyNo){return service.get(policyNo);}
    @GetMapping("/search")
    @Operation(summary="Search by minimum premium")
    public List<PolicyResponse> search(@RequestParam(required=false) Integer minPremium){return service.premium(minPremium);}
    @PostMapping
    @Operation(summary="Create policy")
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody CreatePolicyRequest r){
        PolicyResponse p=service.create(r);
        return ResponseEntity.created(URI.create("/api/policies/"+p.policyNo())).body(p);
    }
    @DeleteMapping("/{policyNo}")
    @Operation(summary="Delete policy")
    public ResponseEntity<Void> delete(@PathVariable String policyNo){service.delete(policyNo);return ResponseEntity.noContent().build();}
    @GetMapping("/{policyNo}/claims")
    @Operation(summary="List claims for policy")
    public List<ClaimResponse> claims(@PathVariable String policyNo){return claims.forPolicy(policyNo);}
}
