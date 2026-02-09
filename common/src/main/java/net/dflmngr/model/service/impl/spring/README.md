# Spring Service Implementations

This directory contains 24 Spring-based service implementations that replace the legacy EclipseLink-based implementations.

## Implementation Pattern

All services follow this pattern:

1. **@Service annotation** - Marks as Spring-managed bean
2. **@Transactional annotation** - Manages transaction boundaries at class level
3. **Constructor injection** - Repository injected via constructor
4. **GenericService method mapping**:
   - `get(K id)` → `repository.findById(id).orElse(null)`
   - `findAll()` → `repository.findAll()`
   - `insert(E entity)` → `repository.save(entity)`
   - `update(E entity)` → `repository.save(entity)`
   - `delete(E entity)` → `repository.delete(entity)`
   - `insertAll/updateAll` → `repository.saveAll()`
   - `replaceAll` → `repository.deleteAll(); repository.flush(); repository.saveAll()`
   - `refresh(E entity)` → no-op (Spring manages persistence context)
   - `close()` → no-op (Spring manages lifecycle)

## Service Implementations

### Player Services
1. **DflPlayerServiceImpl** - DFL player management
   - Custom: `findByPlayerId(int playerId)`

2. **AflPlayerServiceImpl** - AFL player management
   - Custom: `findByPlayerId(int playerId)`

3. **DflUnmatchedPlayerServiceImpl** - Unmatched player tracking
   - No custom methods

### Team Services
4. **DflTeamServiceImpl** - DFL team management
   - Custom: `findByTeamCode(String teamCode)`

5. **AflTeamServiceImpl** - AFL team management
   - Custom: `findByTeamId(String teamId)`

6. **DflTeamPlayerServiceImpl** - Team-player associations
   - Custom: `findForTeam(String teamCode)`
   - Custom: `findForTeamAndPlayer(String teamCode, int playerId)`

### Fixture Services
7. **DflFixtureServiceImpl** - DFL fixture management
   - Custom: `findByRound(int round)`
   - Custom: `findByRoundAndGame(int round, int game)`

8. **AflFixtureServiceImpl** - AFL fixture management
   - Custom: `findByRound(int round)`
   - Custom: `findByRoundAndGame(int round, int game)`

### Scoring Services
9. **DflPlayerScoresServiceImpl** - Player scores by round
   - Custom: `findByRound(int round)`
   - Custom: `findByRoundAndPlayer(int round, int playerId)`

10. **DflTeamScoresServiceImpl** - Team scores by round
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndTeam(int round, String teamCode)`

11. **DflTeamPredictedScoresServiceImpl** - Predicted team scores
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndTeam(int round, String teamCode)`

12. **DflPlayerPredictedScoresServiceImpl** - Predicted player scores
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndPlayer(int round, int playerId)`

13. **DflPreseasonScoresServiceImpl** - Preseason scoring
    - No custom methods

### Round Information
14. **DflRoundInfoServiceImpl** - Round metadata
    - Custom: `findByRound(int round)`

### Global Settings
15. **GlobalsServiceImpl** - Global configuration
    - No custom methods

### Selection Services
16. **DflSelectedPlayerServiceImpl** - Most complex service with team selections
    - Custom: `getAllForRound(int round)`
    - Custom: `getSelectedTeamForRound(int round, String teamCode)`
    - Custom: `replaceAllForRound(int round, List<DflSelectedPlayer> selectedTeam)`
    - Custom: `replaceTeamForRound(int round, String teamCode, List<DflSelectedPlayer> selectedTeam)`
    - Custom: `getForRoundWithKey(int round)` - Returns Map<Integer, DflSelectedPlayer>

17. **DflSelectionIdsServiceImpl** - Selection ID tracking
    - No custom methods

### Statistics Services
18. **RawPlayerStatsServiceImpl** - Raw player statistics
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndPlayer(int round, int playerId)`

19. **StatsRoundPlayerStatsServiceImpl** - Round-specific player stats
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndPlayer(int round, int playerId)`

### Ins and Outs Services
20. **InsAndOutsServiceImpl** - Team changes tracking
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndTeam(int round, String teamCode)`
    - Custom: `findByRoundAndTeamAndPlayer(int round, String teamCode, int playerId)`

21. **DflEarlyInsAndOutsServiceImpl** - Early team changes
    - Custom: `findByRound(int round)`
    - Custom: `findByRoundAndTeam(int round, String teamCode)`
    - Custom: `findByRoundAndTeamAndPlayer(int round, String teamCode, int playerId)`

### Ladder and Rankings
22. **DflLadderServiceImpl** - League standings
    - Custom: `findByTeamCode(String teamCode)`

23. **DflMatthewAllenServiceImpl** - Matthew Allen trophy tracking
    - Custom: `findByPlayerId(int playerId)`

24. **DflBest22ServiceImpl** - Best 22 selections
    - Custom: `findByRound(int round)`

## Key Features

- **Transaction Management**: All services use declarative transaction management via @Transactional
- **Constructor Injection**: Immutable dependencies injected via constructor
- **Backward Compatibility**: All methods match existing service interface signatures
- **Spring Data Integration**: Leverages Spring Data JPA repositories for persistence
- **No-op Methods**: `refresh()` and `close()` are no-ops as Spring manages lifecycle
- **Optional Handling**: Uses `.orElse(null)` for backward compatibility with existing code

## Usage Example

```java
@Autowired
private DflPlayerService dflPlayerService;

public void example() {
    // Find by ID
    DflPlayer player = dflPlayerService.get(123);
    
    // Find all
    List<DflPlayer> players = dflPlayerService.findAll();
    
    // Custom method
    DflPlayer playerByDflId = dflPlayerService.findByPlayerId(456);
    
    // Save
    dflPlayerService.insert(player);
}
```

## Migration Notes

- These implementations are drop-in replacements for EclipseLink-based implementations
- No code changes required in calling code
- Repository layer handles all JPA operations
- Spring Boot auto-configuration manages EntityManager lifecycle
