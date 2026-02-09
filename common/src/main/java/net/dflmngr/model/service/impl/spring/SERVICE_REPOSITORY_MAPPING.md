# Service to Repository Mapping

This document shows the mapping between Spring service implementations and their corresponding repositories.

| Service Implementation | Service Interface | Repository | Entity | ID Type |
|------------------------|-------------------|------------|--------|---------|
| AflFixtureServiceImpl | AflFixtureService | AflFixtureRepository | AflFixture | AflFixturePK |
| AflPlayerServiceImpl | AflPlayerService | AflPlayerRepository | AflPlayer | Integer |
| AflTeamServiceImpl | AflTeamService | AflTeamRepository | AflTeam | String |
| DflBest22ServiceImpl | DflBest22Service | DflBest22Repository | DflBest22 | DflBest22PK |
| DflEarlyInsAndOutsServiceImpl | DflEarlyInsAndOutsService | DflEarlyInsAndOutsRepository | DflEarlyInsAndOuts | Integer |
| DflFixtureServiceImpl | DflFixtureService | DflFixtureRepository | DflFixture | DflFixturePK |
| DflLadderServiceImpl | DflLadderService | DflLadderRepository | DflLadder | String |
| DflMatthewAllenServiceImpl | DflMatthewAllenService | DflMatthewAllenRepository | DflMatthewAllen | Integer |
| DflPlayerPredictedScoresServiceImpl | DflPlayerPredictedScoresService | DflPlayerPredictedScoresRepository | DflPlayerPredictedScores | DflPlayerPredictedScoresPK |
| DflPlayerScoresServiceImpl | DflPlayerScoresService | DflPlayerScoresRepository | DflPlayerScores | DflPlayerScoresPK |
| DflPlayerServiceImpl | DflPlayerService | DflPlayerRepository | DflPlayer | Integer |
| DflPreseasonScoresServiceImpl | DflPreseasonScoresService | DflPreseasonScoresRepository | DflPreseasonScores | Integer |
| DflRoundInfoServiceImpl | DflRoundInfoService | DflRoundInfoRepository | DflRoundInfo | Integer |
| DflSelectedPlayerServiceImpl | DflSelectedTeamService | DflSelectedPlayerRepository | DflSelectedPlayer | DflSelectedPlayerPK |
| DflSelectionIdsServiceImpl | DflSelectionIdsService | DflSelectionIdsRepository | DflSelectionIds | Integer |
| DflTeamPlayerServiceImpl | DflTeamPlayerService | DflTeamPlayerRepository | DflTeamPlayer | Integer |
| DflTeamPredictedScoresServiceImpl | DflTeamPredictedScoresService | DflTeamPredictedScoresRepository | DflTeamPredictedScores | DflTeamPredictedScoresPK |
| DflTeamScoresServiceImpl | DflTeamScoresService | DflTeamScoresRepository | DflTeamScores | DflTeamScoresPK |
| DflTeamServiceImpl | DflTeamService | DflTeamRepository | DflTeam | String |
| DflUnmatchedPlayerServiceImpl | DflUnmatchedPlayerService | DflUnmatchedPlayerRepository | DflUnmatchedPlayer | Integer |
| GlobalsServiceImpl | GlobalsService | GlobalsRepository | Globals | String |
| InsAndOutsServiceImpl | InsAndOutsService | InsAndOutsRepository | InsAndOuts | Integer |
| RawPlayerStatsServiceImpl | RawPlayerStatsService | RawPlayerStatsRepository | RawPlayerStats | RawPlayerStatsPK |
| StatsRoundPlayerStatsServiceImpl | StatsRoundPlayerStatsService | StatsRoundPlayerStatsRepository | StatsRoundPlayerStats | StatsRoundPlayerStatsPK |

## ID Type Categories

### Simple Integer IDs
- AflPlayer
- DflPlayer
- DflEarlyInsAndOuts
- DflLadder (String)
- DflMatthewAllen
- DflPreseasonScores
- DflRoundInfo
- DflSelectionIds
- DflTeamPlayer
- DflUnmatchedPlayer
- InsAndOuts

### String IDs
- AflTeam
- DflTeam
- Globals

### Composite Keys (PK classes)
- AflFixture (AflFixturePK)
- DflBest22 (DflBest22PK)
- DflFixture (DflFixturePK)
- DflPlayerPredictedScores (DflPlayerPredictedScoresPK)
- DflPlayerScores (DflPlayerScoresPK)
- DflSelectedPlayer (DflSelectedPlayerPK)
- DflTeamPredictedScores (DflTeamPredictedScoresPK)
- DflTeamScores (DflTeamScoresPK)
- RawPlayerStats (RawPlayerStatsPK)
- StatsRoundPlayerStats (StatsRoundPlayerStatsPK)

## Custom Query Methods Summary

### By Round
- AflFixtureService: `findByRound(int round)`, `findByRoundAndGame(int round, int game)`
- DflFixtureService: `findByRound(int round)`, `findByRoundAndGame(int round, int game)`
- DflPlayerScoresService: `findByRound(int round)`, `findByRoundAndPlayer(int round, int playerId)`
- DflTeamScoresService: `findByRound(int round)`, `findByRoundAndTeam(int round, String teamCode)`
- DflPlayerPredictedScoresService: `findByRound(int round)`, `findByRoundAndPlayer(int round, int playerId)`
- DflTeamPredictedScoresService: `findByRound(int round)`, `findByRoundAndTeam(int round, String teamCode)`
- RawPlayerStatsService: `findByRound(int round)`, `findByRoundAndPlayer(int round, int playerId)`
- StatsRoundPlayerStatsService: `findByRound(int round)`, `findByRoundAndPlayer(int round, int playerId)`
- DflBest22Service: `findByRound(int round)`
- InsAndOutsService: `findByRound(int round)`, `findByRoundAndTeam(int, String)`, `findByRoundAndTeamAndPlayer(int, String, int)`
- DflEarlyInsAndOutsService: `findByRound(int round)`, `findByRoundAndTeam(int, String)`, `findByRoundAndTeamAndPlayer(int, String, int)`

### By ID/Code
- AflPlayerService: `findByPlayerId(int playerId)`
- DflPlayerService: `findByPlayerId(int playerId)`
- AflTeamService: `findByTeamId(String teamId)`
- DflTeamService: `findByTeamCode(String teamCode)`
- DflLadderService: `findByTeamCode(String teamCode)`
- DflMatthewAllenService: `findByPlayerId(int playerId)`
- DflRoundInfoService: `findByRound(int round)`

### Team Player Associations
- DflTeamPlayerService: `findForTeam(String teamCode)`, `findForTeamAndPlayer(String teamCode, int playerId)`

### Complex Selection Logic
- DflSelectedTeamService (DflSelectedPlayerServiceImpl):
  - `getAllForRound(int round)`
  - `getSelectedTeamForRound(int round, String teamCode)`
  - `replaceAllForRound(int round, List<DflSelectedPlayer> selectedTeam)`
  - `replaceTeamForRound(int round, String teamCode, List<DflSelectedPlayer> selectedTeam)`
  - `getForRoundWithKey(int round)` - Returns Map<Integer, DflSelectedPlayer>

## Notes

- All services use constructor injection for repositories
- All services are annotated with @Service and @Transactional
- Complex queries leverage repository custom query methods
- replaceAll operations use deleteAll() + flush() + saveAll() pattern
- Map-based lookups are implemented in service layer (e.g., getForRoundWithKey)
