# HDFC Life Policy Ledger

Spring Boot 3 + Java 17 REST API using Spring Data JPA, Flyway, H2, PostgreSQL, Bean Validation, and Swagger.

## Run

```bash
mvn spring-boot:run

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui/index.html

H2 Console: http://localhost:8080/h2-console

Default profile: dev

API
Method	Endpoint	Description
GET	/api/policies	List/filter policies
GET	/api/policies/{policyNo}	Policy details
GET	/api/policies/search?minPremium=20000	Premium search
POST	/api/policies	Create policy
DELETE	/api/policies/{policyNo}	Delete policy
GET	/api/policies/{policyNo}/claims	Policy claims
POST	/api/claims	Create claim

Example Policy
{
  "policyNo": "HDFC-LIFE-1004",
  "customer": "Vikram Singh",
  "email": "vikram.singh@hdfclife.example",
  "type": "TERM",
  "basePremium": 15200,
  "status": "Active",
  "riderCodes": []
}

Create Policy
POST /api/policies
Content-Type: application/json

{
  "policyNo": "HDFC-LIFE-1007",
  "customer": "Kiran Rao",
  "email": "kiran.rao@hdfclife.example",
  "type": "TERM",
  "basePremium": 20000,
  "status": "Active"
}

Response:

201 Created
Location: /api/policies/HDFC-LIFE-1007

Duplicate policy numbers return 409 Conflict.

Create Claim
{
  "policyNo": "HDFC-LIFE-1001",
  "claimAmount": 25000,
  "urgency": "HIGH",
  "hospitalName": "Apollo Hospital"
}

Claims are automatically numbered CLM-01, CLM-02, etc. New claims have status SUBMITTED.

Database
Flyway owns the database schema. Hibernate does not create or modify tables.

spring:
  jpa:
    hibernate:
      ddl-auto: none
    open-in-view: false

Development database:

jdbc:h2:mem:hdfclife;MODE=PostgreSQL;DB_CLOSE_DELAY=-1

Production uses PostgreSQL with:

DB_URL
DB_USER
DB_PASSWORD

JPA Relationships
Customer 1 ──── * Policy
Policy   1 ──── * Claim
Policy   * ──── * Rider

Policy owns the policy_riders join table:

@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
    name = "policy_riders",
    joinColumns = @JoinColumn(name = "policy_id"),
    inverseJoinColumns = @JoinColumn(name = "rider_id")
)
private List<Rider> riders;

No CascadeType.ALL is used on the many-to-many relationship.

Seed Data
HDFC-LIFE-1001  Anita Sharma   TERM       18500  Active
HDFC-LIFE-1002  Rahul Mehta    ULIP       42000  Active
HDFC-LIFE-1003  Priya Nair     ENDOWMENT  27000  Lapsed
HDFC-LIFE-1004  Vikram Singh   TERM       15200  Active
HDFC-LIFE-1005  Sneha Patel    ULIP       36000  Active
HDFC-LIFE-1006  Anita Sharma   ENDOWMENT  22000  Pending

Reference riders:

ACCIDENT_COVER
CRITICAL_ILLNESS
WAIVER_OF_PREMIUM

Only HDFC-LIFE-1001 has:

ACCIDENT_COVER
WAIVER_OF_PREMIUM

The seeder skips execution when:

policyRepository.count() > 0

Validation & Errors
Request DTOs use Bean Validation.

Example:

{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fields": {
    "email": "must be a well-formed email address"
  }
}

Not found:

{
  "status": 404,
  "error": "Not Found",
  "message": "Policy not found: HDFC-LIFE-9999",
  "fields": {}
}

Exception hierarchy:

DeskException
├── PolicyNotFoundException
├── ClaimNotFoundException
├── DuplicatePolicyException
└── InvalidRequestException

DTO Design
Controllers return DTOs instead of JPA entities. With open-in-view=false, lazy collections may be unavailable when Jackson serializes the response and can cause LazyInitializationException. DTOs are created inside the service transaction and expose only the fields required by the REST API.

Expected Results
Policy count           → 6
Customer count         → 5
Active policies        → 4
TERM policies          → 2
Anita Sharma policies  → 2

Premium search order:

HDFC-LIFE-1002
HDFC-LIFE-1005
HDFC-LIFE-1003
HDFC-LIFE-1006



