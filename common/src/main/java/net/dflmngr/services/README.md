# DFL Manager Service Layer

This directory contains Spring-based service classes that wrap Spring Data JPA repositories and provide business logic for the DFL Manager application. These services replace the old EclipseLink service implementations.

## Overview

All services follow a consistent pattern:
- Annotated with `@Service` for Spring component scanning
- Use constructor injection for repositories
- Provide `@Transactional` methods for write operations
- Include both business logic methods and generic CRUD operations
- Use Spring Data JPA repository methods instead of manual DAO calls

## Services Created

### AFL (Australian Football League) Services

1. **AflFixtureService**
   - Manages AFL fixture data
   - Business logic: round blocks, played fixtures, incomplete fixtures, teams played
   - Methods: `getAflFixturesForRound()`, `getAflFixturneRoundBlocks()`, `getAflFixturesPlayedForRound()`, etc.

2. **AflPlayerService**
   - Manages AFL player data
   - Business logic: bulk DFL player ID updates
   - Methods: `bulkUpdateDflPlayerId()`

3. **AflTeamService**
   - Manages AFL team data
   - Methods: `getAflTeamByName()`

### DFL (Draft Fantasy League) Services

4. **DflFixtureService**
   - Manages DFL fixture data
   - Methods: `getFixturesForRound()`

5. **DflLadderService**
   - Manages DFL ladder/standings
   - Business logic: round-specific ladder retrieval, previous round lookup, keyed maps
   - Methods: `getLadderForRound()`, `getPreviousRoundLadder()`, `replaceAllForRound()`

6. **DflPlayerService**
   - Manages DFL player data
   - Business logic: Adam Goodes eligibility, cross-reference mapping, bulk AFL player ID updates
   - Methods: `getByAflPlayerId()`, `getAdamGoodesEligible()`, `getCrossRefPlayers()`, `bulkUpdateAflPlayerId()`

7. **DflPlayerScoresService**
   - Manages DFL player scores by round
   - Business logic: round-specific retrieval, team filtering, keyed maps, up-to-round aggregation
   - Methods: `getForRound()`, `getForRoundAndTeam()`, `getForRoundWithKey()`, `getUptoRoundWithKey()`, `replaceAllForRound()`

8. **DflPlayerPredictedScoresService**
   - Manages predicted player scores
   - Business logic: round-specific retrieval, keyed maps
   - Methods: `getForRound()`, `getForRoundWithKey()`, `replaceAllForRound()`

9. **DflSelectedTeamService**
   - Manages selected teams/lineups
   - Business logic: round/team filtering, keyed maps, replace patterns
   - Methods: `getAllForRound()`, `getSelectedTeamForRound()`, `replaceAllForRound()`, `replaceTeamForRound()`

10. **DflTeamService**
    - Manages DFL team data
    - Basic CRUD operations

11. **DflTeamPlayerService**
    - Manages DFL team player relationships
    - Methods: `getTeamPlayerForTeam()`

12. **DflTeamScoresService**
    - Manages DFL team scores by round
    - Business logic: round-specific retrieval, keyed maps
    - Methods: `getForRound()`, `getForRoundWithKey()`, `replaceAllForRound()`

13. **DflEarlyInsAndOutsService**
    - Manages early ins and outs data
    - Business logic: team/round filtering, save with replace pattern
    - Methods: `getByTeamAndRound()`, `saveTeamInsAndOuts()`

14. **DflRoundInfoService**
    - Manages DFL round information
    - Business logic: AFL round mapping
    - Methods: `getRoundsByAflRounds()`

### Stats & Analysis Services

15. **RawPlayerStatsService**
    - Manages raw player statistics
    - Business logic: round/team filtering, keyed maps, replace patterns
    - Methods: `getForRound()`, `getForRoundWithKey()`, `getForRoundAndTeam()`, `replaceAllForRound()`, `removeStatsForRoundAndTeam()`

16. **StatsRoundPlayerStatsService**
    - Manages processed player statistics by round
    - Business logic: round/team filtering, keyed maps, replace patterns
    - Methods: `getForRound()`, `getForRoundWithKey()`, `getForRoundAndTeam()`, `replaceAllForRound()`, `removeStatsForRoundAndTeam()`

### Utility Services

17. **InsAndOutsService**
    - Manages ins and outs tracking
    - Business logic: team/round filtering, save with replace pattern
    - Methods: `getByTeamAndRound()`, `saveTeamInsAndOuts()`, `removeForRound()`

18. **GlobalsService**
    - Manages global configuration values
    - Business logic: configuration retrieval, email config, fixture templates, lockout times, etc.
    - Key methods: `getCurrentYear()`, `getCurrentRound()`, `getAflFixtureUrl()`, `getEmailConfig()`, `getDflFixuteTemplate()`, `getTeamCodes()`, `setCurrentRound()`
    - Provides 30+ specialized configuration getters

## Migration from Old Implementation

### Key Changes

1. **Dependency Injection**: Services now use constructor injection instead of manual instantiation
2. **Transaction Management**: `@Transactional` annotation replaces manual transaction handling
3. **Repository Methods**: Spring Data JPA methods replace DAO methods
4. **No Manual EntityManager**: Spring manages persistence context automatically

### Method Mapping

Old Pattern → New Pattern:
- `dao.findXxx()` → `repository.findXxx()`
- `dao.findById(id)` → `repository.findById(id).orElse(null)`
- `dao.persist(entity)` → `repository.save(entity)`
- `dao.merge(entity)` → `repository.save(entity)`
- `dao.remove(entity)` → `repository.delete(entity)`
- `dao.findAll()` → `repository.findAll()`
- `updateAll(entities, false)` → `repository.saveAll(entities)` with `@Transactional`
- `dao.beginTransaction()` / `dao.commit()` → `@Transactional` method
- `dao.flush()` → `repository.flush()`

### Generic Methods

All services provide these standard CRUD operations:
- `get(ID id)` - Find by primary key
- `findAll()` - Retrieve all entities
- `insert(Entity entity)` - Create new entity
- `update(Entity entity)` - Update existing entity
- `insertAll(List<Entity> entities)` - Bulk insert
- `updateAll(List<Entity> entities)` - Bulk update
- `delete(Entity entity)` - Delete entity
- `replaceAll(List<Entity> entities)` - Delete all and insert new

## Usage Example

```java
@Service
public class MyBusinessService {
    private final DflPlayerScoresService playerScoresService;
    
    @Autowired
    public MyBusinessService(DflPlayerScoresService playerScoresService) {
        this.playerScoresService = playerScoresService;
    }
    
    public void processRoundScores(int round) {
        List<DflPlayerScores> scores = playerScoresService.getForRound(round);
        // Business logic here
    }
}
```

## Testing

Services can be easily tested with Spring Boot test annotations:

```java
@SpringBootTest
class DflPlayerScoresServiceTest {
    @Autowired
    private DflPlayerScoresService service;
    
    @Test
    void testGetForRound() {
        List<DflPlayerScores> scores = service.getForRound(1);
        assertNotNull(scores);
    }
}
```

## Notes

- All services use Spring's declarative transaction management
- Repository methods are automatically transactional for read operations
- Write operations are marked with `@Transactional`
- The `flush()` method is used in replace patterns to ensure delete operations complete before inserts
- Bulk update methods that iterate over entities and modify them rely on JPA's dirty checking within transactions
