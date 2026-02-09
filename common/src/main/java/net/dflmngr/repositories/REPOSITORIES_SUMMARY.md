# Spring Data JPA Repositories - Implementation Summary

This document summarizes the 12+ complex Spring Data JPA repositories created/enhanced to replace EclipseLink Criteria API queries with JPQL.

## Repositories Created/Enhanced

### 1. **AflFixtureRepository** (NEW)
**Location:** `/common/src/main/java/net/dflmngr/repositories/AflFixtureRepository.java`

**Migrated from:** `AflFixtureDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all fixtures for a round
- `findByRoundAndTeam(int, String)` - Find fixture where team is home OR away
- `findIncompleteFixtures(ZonedDateTime)` - Find fixtures with NULL endTime and startTime < provided time
- `findFixturesToScrape(ZonedDateTime)` - Find fixtures where statsDownloaded is NULL or FALSE
- `findIncompleteFixturesForRound(int)` - Find incomplete fixtures for specific round
- `findMaxRound()` - MAX aggregate query for round with startTime NOT NULL
- `findRefreshFixtureStart()` - **Complex subquery**: Find minimum round where exactly 9 fixtures have statsDownloaded = false (for batch refresh logic)

**Complex Query Example:**
```java
@Query("SELECT MIN(f.round) FROM AflFixture f WHERE f.round IN " +
       "(SELECT f2.round FROM AflFixture f2 WHERE f2.statsDownloaded = false " +
       "GROUP BY f2.round HAVING COUNT(f2.round) = 9)")
Integer findRefreshFixtureStart();
```

---

### 2. **DflFixtureRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflFixtureRepository.java`

**Migrated from:** `DflFixtureDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all DFL fixtures for a round

---

### 3. **DflPlayerScoresRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflPlayerScoresRepository.java`

**Migrated from:** `DflPlayerScoresDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all player scores for a round
- `findByRoundAndPlayerId(int, int)` - Find specific player score
- `findByRoundAndTeamCode(int, String)` - Find all player scores for a team in a round
- `sumScoreByRoundAndTeamCode(int, String)` - Calculate total team score using SUM aggregate

**Score Calculation Example:**
```java
@Query("SELECT SUM(ps.score) FROM DflPlayerScores ps WHERE ps.round = :round AND ps.teamCode = :teamCode")
Integer sumScoreByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
```

---

### 4. **DflTeamScoresRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflTeamScoresRepository.java`

**Migrated from:** `DflTeamScoresDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all team scores for a round
- `findByRoundAndTeamCode(int, String)` - Find specific team score
- `countByRound(int)` - Count team scores for a round
- `deleteByRound(int)` - **@Modifying** query to delete all scores for a round

**Delete Pattern:**
```java
@Transactional
@Modifying
@Query("DELETE FROM DflTeamScores ts WHERE ts.round = :round")
void deleteByRound(@Param("round") int round);
```

---

### 5. **DflPlayerPredictedScoresRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflPlayerPredictedScoresRepository.java`

**Migrated from:** `DflPlayerPredictedScoresDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all predicted scores for a round
- `findByRoundAndPlayerId(int, int)` - Find specific player predicted score
- `findByRoundAndTeamCode(int, String)` - Find predicted scores for a team
- `deleteByRound(int)` - **@Modifying** delete for replaceAll pattern
- `deleteByRoundAndTeamCode(int, String)` - **@Modifying** delete for team-specific refresh

**ReplaceAll Pattern:**
Used for refreshing predicted scores - delete existing records then insert new ones.

---

### 6. **DflTeamPredictedScoresRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflTeamPredictedScoresRepository.java`

**Migrated from:** `DflTeamPredictedScoresDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all team predicted scores for a round
- `findByRoundAndTeamCode(int, String)` - Find specific team predicted score
- `deleteByRound(int)` - **@Modifying** delete for replaceAll pattern
- `deleteByRoundAndTeamCode(int, String)` - **@Modifying** delete for team-specific refresh
- `sumPredictedScoreByRound(int)` - Calculate total predicted scores using SUM aggregate

---

### 7. **RawPlayerStatsRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/RawPlayerStatsRepository.java`

**Migrated from:** `RawPlayerStatsDaoImpl`

**Key Features:**
- `findByRoundAndTeamAndJumperNo(int, String, int)` - Find specific player stats
- `findByRound(int)` - Find all stats for a round
- `findByRoundAndTeam(int, String)` - Find all stats for a team in a round
- `deleteByRoundAndTeam(int, String)` - **@Modifying** delete for team stats refresh
- `deleteByRound(int)` - **@Modifying** delete for full round refresh
- `countByRound(int)` - Count stats for validation
- `findByRoundAndScrapingStatus(int, String)` - Find stats by scraping status

**Stats Refresh Pattern:**
```java
@Transactional
@Modifying
@Query("DELETE FROM RawPlayerStats rps WHERE rps.round = :round AND rps.team = :team")
void deleteByRoundAndTeam(@Param("round") int round, @Param("team") String team);
```

---

### 8. **DflSelectedPlayerRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflSelectedPlayerRepository.java`

**Migrated from:** `DflSelectedPlayerDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all selected players for a round
- `findByRoundAndTeamCode(int, String)` - Find selected team for a round
- `findByRoundAndPlayerId(int, int)` - Find specific selected player
- `selectedTeamExists(int, String)` - Boolean check using CASE WHEN
- `countByRoundAndTeamCode(int, String)` - Count selected players
- `findNonEmergencyByRoundAndTeamCode(int, String)` - Find regular selections (isEmergency = 0)
- `findEmergencyByRoundAndTeamCode(int, String)` - Find emergency players (isEmergency > 0)
- `findDnpByRoundAndTeamCode(int, String)` - Find DNP (Did Not Play) players
- `deleteByRound(int)` - **@Modifying** delete for round
- `deleteByRoundAndTeamCode(int, String)` - **@Modifying** delete for team

**Existence Check Pattern:**
```java
@Query("SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END FROM DflSelectedPlayer sp " +
       "WHERE sp.round = :round AND sp.teamCode = :teamCode")
boolean selectedTeamExists(@Param("round") int round, @Param("teamCode") String teamCode);
```

---

### 9. **DflBest22Repository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflBest22Repository.java`

**Migrated from:** `DflBest22DaoImpl`

**Key Features:**
- `findByRound(int)` - Find all Best 22 for a round
- `findByRoundAndTeamCode(int, String)` - Find Best 22 for a team
- `deleteByRound(int)` - **@Modifying** delete
- `deleteByRoundAndTeamCode(int, String)` - **@Modifying** delete for team
- `countByRound(int)` - Count entries for validation

---

### 10. **InsAndOutsRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/InsAndOutsRepository.java`

**Migrated from:** `InsAndOutsDaoImpl`

**Key Features:**
- `findByRound(int)` - Find all ins and outs for a round
- `findByRoundAndTeamCode(int, String)` - Find ins and outs for a team
- `findInsByRoundAndTeamCode(int, String)` - Find only ins (in_out = 'I')
- `findOutsByRoundAndTeamCode(int, String)` - Find only outs (in_out = 'O')
- `deleteByRound(int)` - **@Modifying** delete
- `deleteByRoundAndTeamCode(int, String)` - **@Modifying** delete for team
- `countByRound(int)` - Count entries

**Filtered Query Example:**
```java
@Query("SELECT io FROM InsAndOuts io WHERE io.round = :round AND io.teamCode = :teamCode AND io.inOut = 'I'")
List<InsAndOuts> findInsByRoundAndTeamCode(@Param("round") int round, @Param("teamCode") String teamCode);
```

---

### 11. **DflLadderRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflLadderRepository.java`

**Migrated from:** `DflLadderDaoImpl`

**Key Features:**
- `findByRound(int)` - Find ladder for a round
- `findCurrentDflLadder()` - Find current ladder (max round where live = false)
- `findLiveDflLadder()` - Find live ladder (max round where live = true)
- `findByRoundAndTeamCode(int, String)` - Find team ladder position
- `findMaxRound()` - Find maximum round
- `deleteByRound(int)` - **@Modifying** delete
- `existsByRound(int)` - Check if ladder exists

**Subquery Pattern for Current Ladder:**
```java
@Query("SELECT l FROM DflLadder l WHERE l.round = (SELECT MAX(l2.round) FROM DflLadder l2 WHERE l2.live = false) ORDER BY l.ladderPos")
List<DflLadder> findCurrentDflLadder();
```

---

### 12. **DflPlayerRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/DflPlayerRepository.java`

**Migrated from:** `DflPlayerDaoImpl`

**Key Features:**
- `findByPlayerIdIn(List<Integer>)` - Find players by ID list
- `findByAflPlayerId(String)` - Find DFL player by AFL player ID
- `findAdamGoodesEligible()` - Find first-year players (isFirstYear = true)
- `findByAflClub(String)` - Find players by AFL club/team
- `findWithAflPlayerId()` - Find players with non-null AFL player ID
- `findByPosition(String)` - Find players by position

---

### 13. **AflPlayerRepository** (ENHANCED)
**Location:** `/common/src/main/java/net/dflmngr/repositories/AflPlayerRepository.java`

**Migrated from:** `AflPlayerDaoImpl`

**Key Features:**
- `findByDflPlayerId(Integer)` - Find AFL player by DFL player ID
- `findByTeam(String)` - Find AFL players by team
- `findWithDflPlayerId()` - Find mapped AFL players
- `findUnmapped()` - Find unmapped AFL players (dflPlayerId IS NULL)

---

## Key JPQL Patterns Used

### 1. Complex Predicates with OR
```java
@Query("SELECT f FROM AflFixture f WHERE f.round = :round AND (f.homeTeam = :team OR f.awayTeam = :team)")
```

### 2. NULL Checks
```java
@Query("SELECT f FROM AflFixture f WHERE f.startTime < :time AND f.endTime IS NULL")
```

### 3. Aggregate Functions (MAX, MIN, SUM, COUNT)
```java
@Query("SELECT MAX(f.round) FROM AflFixture f WHERE f.startTime IS NOT NULL")
@Query("SELECT SUM(ps.score) FROM DflPlayerScores ps WHERE ps.round = :round AND ps.teamCode = :teamCode")
```

### 4. Subqueries
```java
@Query("SELECT MIN(f.round) FROM AflFixture f WHERE f.round IN " +
       "(SELECT f2.round FROM AflFixture f2 WHERE f2.statsDownloaded = false " +
       "GROUP BY f2.round HAVING COUNT(f2.round) = 9)")
```

### 5. GROUP BY and HAVING
Used in subqueries for complex aggregation logic.

### 6. Modifying Queries with @Transactional
```java
@Transactional
@Modifying
@Query("DELETE FROM DflTeamScores ts WHERE ts.round = :round")
void deleteByRound(@Param("round") int round);
```

### 7. Boolean Existence Checks
```java
@Query("SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END FROM DflSelectedPlayer sp " +
       "WHERE sp.round = :round AND sp.teamCode = :teamCode")
boolean selectedTeamExists(@Param("round") int round, @Param("teamCode") String teamCode);
```

### 8. Named Parameters
All queries use `@Param` annotations for named parameters instead of positional parameters.

---

## Migration Benefits

1. **Cleaner Code**: JPQL is more readable than Criteria API
2. **Type Safety**: Compile-time validation of entity names and fields
3. **Performance**: JPQL queries are easier for JPA providers to optimize
4. **Maintainability**: Queries are easier to understand and modify
5. **Spring Data Integration**: Leverages Spring Data's method naming conventions
6. **Reduced Boilerplate**: No need for CriteriaBuilder, CriteriaQuery, Root, etc.
7. **Testability**: Easier to test with Spring's @DataJpaTest

---

## Next Steps

These repositories can now be used to replace the DAO implementations throughout the codebase. Services should be updated to:

1. Inject the repository instead of the DAO
2. Call repository methods instead of DAO methods
3. Remove DAO dependencies from service classes
4. Update tests to use repository methods

Example migration:
```java
// OLD
@Inject
private AflFixtureDao aflFixtureDao;
List<AflFixture> fixtures = aflFixtureDao.findAflFixturesForRound(round);

// NEW
@Autowired
private AflFixtureRepository aflFixtureRepository;
List<AflFixture> fixtures = aflFixtureRepository.findByRound(round);
```

---

## Testing Recommendations

Each repository should be tested with:
- `@DataJpaTest` for repository layer testing
- Test data setup using `@Sql` scripts or TestEntityManager
- Verification of query results
- Testing of @Modifying queries within transactions
- Testing of complex subqueries and aggregates

Example test structure:
```java
@DataJpaTest
class AflFixtureRepositoryTest {
    
    @Autowired
    private AflFixtureRepository repository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testFindByRoundAndTeam() {
        // Setup test data
        // Execute query
        // Assert results
    }
}
```
