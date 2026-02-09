# Repository Migration Quick Reference Guide

## Quick Migration Examples

### Example 1: Simple Find by Round

**OLD DAO Approach:**
```java
@Inject
private DflPlayerScoresDao dflPlayerScoresDao;

List<DflPlayerScores> scores = dflPlayerScoresDao.findForRound(round);
```

**NEW Repository Approach:**
```java
@Autowired
private DflPlayerScoresRepository dflPlayerScoresRepository;

List<DflPlayerScores> scores = dflPlayerScoresRepository.findByRound(round);
```

---

### Example 2: Find with Multiple Conditions

**OLD DAO Approach:**
```java
@Inject
private AflFixtureDao aflFixtureDao;

AflFixture fixture = aflFixtureDao.findAflFixtureForRoundAndTeam(round, team);
```

**NEW Repository Approach:**
```java
@Autowired
private AflFixtureRepository aflFixtureRepository;

AflFixture fixture = aflFixtureRepository.findByRoundAndTeam(round, team);
```

---

### Example 3: Delete Operation

**OLD DAO Approach:**
```java
@Inject
private RawPlayerStatsDao rawPlayerStatsDao;

rawPlayerStatsDao.deleteStatsForRoundAndTeam(round, team);
```

**NEW Repository Approach:**
```java
@Autowired
private RawPlayerStatsRepository rawPlayerStatsRepository;

rawPlayerStatsRepository.deleteByRoundAndTeam(round, team);
```

---

### Example 4: Existence Check

**OLD DAO Approach:**
```java
@Inject
private DflSelectedPlayerDao dflSelectedPlayerDao;

List<DflSelectedPlayer> players = dflSelectedPlayerDao.findSelectedTeamForRound(round, teamCode);
boolean exists = !players.isEmpty();
```

**NEW Repository Approach:**
```java
@Autowired
private DflSelectedPlayerRepository dflSelectedPlayerRepository;

boolean exists = dflSelectedPlayerRepository.selectedTeamExists(round, teamCode);
```

---

### Example 5: Complex Query with Subquery

**OLD DAO Approach:**
```java
@Inject
private AflFixtureDao aflFixtureDao;

int startRound = aflFixtureDao.findRefreshFixtureStart();
```

**NEW Repository Approach:**
```java
@Autowired
private AflFixtureRepository aflFixtureRepository;

Integer startRound = aflFixtureRepository.findRefreshFixtureStart();
```

---

## Method Name Mapping Reference

### AflFixture
| DAO Method | Repository Method | Notes |
|------------|------------------|-------|
| `findAflFixturesForRound(int)` | `findByRound(int)` | Simpler name |
| `findAflFixtureForRoundAndTeam(int, String)` | `findByRoundAndTeam(int, String)` | Simplified |
| `findIncompleteAflFixtures(ZonedDateTime)` | `findIncompleteFixtures(ZonedDateTime)` | Shorter name |
| `findMaxAflRound()` | `findMaxRound()` | Cleaner name |

### DflPlayerScores
| DAO Method | Repository Method | Notes |
|------------|------------------|-------|
| `findForRound(int)` | `findByRound(int)` | Spring Data convention |
| `findForRoundAndTeam(int, String)` | `findByRoundAndTeamCode(int, String)` | Explicit field name |
| N/A | `findByRoundAndPlayerId(int, int)` | New method |
| N/A | `sumScoreByRoundAndTeamCode(int, String)` | New aggregation method |

### DflTeamScores
| DAO Method | Repository Method | Notes |
|------------|------------------|-------|
| `findForRound(int)` | `findByRound(int)` | Spring Data convention |
| N/A | `findByRoundAndTeamCode(int, String)` | New method |
| N/A | `deleteByRound(int)` | New delete method |

### RawPlayerStats
| DAO Method | Repository Method | Notes |
|------------|------------------|-------|
| `findForRound(int)` | `findByRound(int)` | Spring Data convention |
| `findForRoundAndTeam(int, String)` | `findByRoundAndTeam(int, String)` | Same name |
| `deleteStatsForRoundAndTeam(int, String)` | `deleteByRoundAndTeam(int, String)` | Simplified name |

### DflSelectedPlayer
| DAO Method | Repository Method | Notes |
|------------|------------------|-------|
| `findAllForRound(int)` | `findByRound(int)` | Simpler name |
| `findSelectedTeamForRound(int, String)` | `findByRoundAndTeamCode(int, String)` | Clearer name |
| N/A | `selectedTeamExists(int, String)` | New boolean method |
| N/A | `findNonEmergencyByRoundAndTeamCode(int, String)` | New filtered method |
| N/A | `findEmergencyByRoundAndTeamCode(int, String)` | New filtered method |

---

## Common Patterns

### Pattern 1: ReplaceAll (Delete + Insert)

**OLD:**
```java
// Delete old records
List<DflTeamPredictedScores> oldScores = dao.findForRound(round);
for (DflTeamPredictedScores score : oldScores) {
    dao.delete(score);
}

// Insert new records
for (DflTeamPredictedScores newScore : newScores) {
    dao.create(newScore);
}
```

**NEW:**
```java
// Delete old records in one query
repository.deleteByRound(round);

// Insert new records
repository.saveAll(newScores);
```

---

### Pattern 2: Conditional Delete

**OLD:**
```java
List<RawPlayerStats> stats = dao.findForRoundAndTeam(round, team);
for (RawPlayerStats stat : stats) {
    dao.delete(stat);
}
```

**NEW:**
```java
repository.deleteByRoundAndTeam(round, team);
```

---

### Pattern 3: Existence Check Before Create

**OLD:**
```java
List<DflSelectedPlayer> existing = dao.findSelectedTeamForRound(round, teamCode);
if (existing.isEmpty()) {
    // Create new selections
}
```

**NEW:**
```java
if (!repository.selectedTeamExists(round, teamCode)) {
    // Create new selections
}
```

---

### Pattern 4: Aggregation

**OLD:**
```java
List<DflPlayerScores> scores = dao.findForRoundAndTeam(round, teamCode);
int total = 0;
for (DflPlayerScores score : scores) {
    total += score.getScore();
}
```

**NEW:**
```java
Integer total = repository.sumScoreByRoundAndTeamCode(round, teamCode);
```

---

## Transaction Management

### OLD (Manual Transaction Management)
```java
@Transactional
public void updateScores(int round) {
    entityManager.getTransaction().begin();
    try {
        // Delete old scores
        // Insert new scores
        entityManager.getTransaction().commit();
    } catch (Exception e) {
        entityManager.getTransaction().rollback();
        throw e;
    }
}
```

### NEW (Declarative Transaction Management)
```java
@Transactional
public void updateScores(int round) {
    // Spring manages transaction automatically
    repository.deleteByRound(round);
    repository.saveAll(newScores);
    // Auto-commit on success, auto-rollback on exception
}
```

---

## Testing Migration

### OLD (DAO Testing)
```java
public class ServiceTest {
    @Mock
    private DflPlayerScoresDao dao;
    
    @Test
    public void testFindScores() {
        when(dao.findForRound(1)).thenReturn(mockScores);
        // Test service logic
    }
}
```

### NEW (Repository Testing)
```java
@DataJpaTest
public class ServiceTest {
    @Autowired
    private DflPlayerScoresRepository repository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    public void testFindScores() {
        // Insert test data
        entityManager.persist(testScore);
        entityManager.flush();
        
        // Test repository
        List<DflPlayerScores> scores = repository.findByRound(1);
        assertThat(scores).hasSize(1);
    }
}
```

---

## Dependency Injection Changes

### OLD (CDI @Inject)
```java
import jakarta.inject.Inject;

@Service
public class MyService {
    @Inject
    private DflPlayerScoresDao dflPlayerScoresDao;
}
```

### NEW (Spring @Autowired)
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    private final DflPlayerScoresRepository dflPlayerScoresRepository;
    
    @Autowired  // Optional for constructor injection
    public MyService(DflPlayerScoresRepository dflPlayerScoresRepository) {
        this.dflPlayerScoresRepository = dflPlayerScoresRepository;
    }
    
    // Or field injection (less preferred)
    // @Autowired
    // private DflPlayerScoresRepository dflPlayerScoresRepository;
}
```

---

## Best Practices

### 1. Use Constructor Injection
```java
private final DflPlayerScoresRepository repository;

@Autowired
public MyService(DflPlayerScoresRepository repository) {
    this.repository = repository;
}
```

### 2. Handle Optional Results
```java
// For findById which returns Optional<T>
Optional<DflPlayerScores> score = repository.findById(new DflPlayerScoresPK(round, playerId));
score.ifPresent(s -> processScore(s));

// For custom queries that return single result
DflPlayerScores score = repository.findByRoundAndPlayerId(round, playerId);
if (score != null) {
    processScore(score);
}
```

### 3. Batch Operations
```java
// Instead of multiple save() calls
List<DflPlayerScores> scores = new ArrayList<>();
// ... populate scores
repository.saveAll(scores);  // Batch insert

// Instead of multiple delete() calls
repository.deleteByRound(round);  // Bulk delete
```

### 4. Transaction Boundaries
```java
@Service
public class MyService {
    
    @Transactional  // Read-only transactions for queries
    public List<DflPlayerScores> getScores(int round) {
        return repository.findByRound(round);
    }
    
    @Transactional  // Read-write for modifications
    public void updateScores(int round, List<DflPlayerScores> newScores) {
        repository.deleteByRound(round);
        repository.saveAll(newScores);
    }
}
```

---

## Troubleshooting

### Issue: NoSuchBeanDefinitionException
**Solution:** Ensure @EnableJpaRepositories is configured:
```java
@Configuration
@EnableJpaRepositories(basePackages = "net.dflmngr.repositories")
public class JpaConfig {
}
```

### Issue: Query Method Not Found
**Solution:** Check method name follows Spring Data conventions or use @Query annotation.

### Issue: Transaction Not Rolling Back
**Solution:** Ensure @Transactional is on public methods and exceptions are not caught.

### Issue: N+1 Query Problem
**Solution:** Use @EntityGraph or JOIN FETCH in @Query:
```java
@Query("SELECT sp FROM DflSelectedPlayer sp JOIN FETCH sp.dflPlayer WHERE sp.round = :round")
List<DflSelectedPlayer> findByRoundWithPlayer(@Param("round") int round);
```

---

## Migration Checklist

- [ ] Replace @Inject with @Autowired
- [ ] Change DAO interface to Repository interface
- [ ] Update method names to match repository methods
- [ ] Remove manual transaction management
- [ ] Update exception handling (DAO exceptions → DataAccessException)
- [ ] Update unit tests to use @DataJpaTest
- [ ] Verify query results match old behavior
- [ ] Test transaction rollback scenarios
- [ ] Update documentation
- [ ] Remove @Deprecated DAO after successful migration

---

## Getting Help

1. Check `REPOSITORIES_SUMMARY.md` for detailed query examples
2. Review existing tests in `src/test/java/net/dflmngr/repositories/`
3. Consult Spring Data JPA documentation: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
4. Check repository method signatures for proper usage
