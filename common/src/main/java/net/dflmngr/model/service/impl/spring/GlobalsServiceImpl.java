package net.dflmngr.model.service.impl.spring;

import net.dflmngr.exceptions.MissingGlobalConfig;
import net.dflmngr.model.entity.Globals;
import net.dflmngr.model.entity.keys.GlobalsPK;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.repositories.GlobalsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class GlobalsServiceImpl implements GlobalsService {

    private static final String PARAMS = "params";
    private static final String VALUE = "value";
    private static final String DFL_REF = "dflRef";
    private static final String AFL_REF = "aflRef";

    private final GlobalsRepository repository;

    public GlobalsServiceImpl(GlobalsRepository repository) {
        this.repository = repository;
    }

    public Globals get(GlobalsPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<Globals> findAll() {
        return repository.findAll();
    }

    public void insert(Globals entity) {
        repository.save(entity);
    }

    public void update(Globals entity) {
        repository.save(entity);
    }

    public void delete(Globals entity) {
        repository.delete(entity);
    }

    public void insertAll(List<Globals> entities) {
        repository.saveAll(entities);
    }

    public void updateAll(List<Globals> entities) {
        repository.saveAll(entities);
    }

    public void replaceAll(List<Globals> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }

    public void refresh(Globals entity) {
        // No-op: Spring manages persistence context
    }

    public void close() {
        // No-op: Spring manages lifecycle
    }

    public Map<String, String> getValueAndParams(String code, String groupCode) {
        Map<String, String> valueAndParams = new HashMap<>();

        GlobalsPK pk = new GlobalsPK();
        pk.setCode(code);
        pk.setGroupCode(groupCode);

        Globals globals = repository.findById(pk).orElse(null);

        try {
            valueAndParams.put(VALUE, globals.getValue());
            valueAndParams.put(PARAMS, globals.getParams());
        } catch (NullPointerException e) {
            throw new MissingGlobalConfig(code, groupCode);
        }

        return valueAndParams;
    }

    public String getValue(String group, String code) {
        GlobalsPK pk = new GlobalsPK();
        pk.setGroupCode(group);
        pk.setCode(code);

        Globals globals = repository.findById(pk).orElse(null);

        if (globals != null) {
            return globals.getValue();
        } else {
            throw new MissingGlobalConfig(code, group);
        }
    }

    public String getCurrentYear() {
        return getValue(DFL_REF, "currentYear");
    }

    public List<String> getAflFixtureUrl() {
        List<String> aflFixtureUrl = new ArrayList<>();

        Map<String, String> valueAndParams = getValueAndParams("aflFixtureUrl", AFL_REF);

        aflFixtureUrl.add(valueAndParams.get(VALUE));

        if (valueAndParams.containsKey(PARAMS)) {
            String params = valueAndParams.get(PARAMS);
            if (params != null) {
                String[] parts = params.split(";");
                aflFixtureUrl.addAll(Arrays.asList(parts));
            }
        }

        return aflFixtureUrl;
    }

    public String getGroundTimeZone(String ground) {
        String timezone = getValue("timezone", ground);

        if (timezone.isEmpty()) {
            timezone = getValue("timezone", "default");
        }

        return timezone;
    }

    public Map<String, String> getGround(String groundName) {
        Map<String, String> ground = new HashMap<>();

        Map<String, String> data = getValueAndParams(groundName, "ground");

        ground.put("ground", data.get(VALUE));
        ground.put("timezone", data.get(PARAMS));

        return ground;
    }

    public List<String> getTeamCodes() {
        List<String> codes = new ArrayList<>();
        List<Globals> globalsList = repository.findByGroupCode("teamCode");

        for (Globals globals : globalsList) {
            codes.add(globals.getCode());
        }

        return codes;
    }

    public String getAppDir() {
        return getValue(DFL_REF, "appDir");
    }

    public String getReportDir() {
        return getValue(DFL_REF, "reportDir");
    }

    public String getStandardLockoutTime() {
        return getValue(DFL_REF, "standardLockoutTime");
    }

    public String getNonStandardLockout(int round) {
        return getValue("nonStandardLockout", Integer.toString(round));
    }

    public String getAflRoundsMax() {
        return getValue(AFL_REF, "aflRoundsMax");
    }

    public Map<String, String> getEmailConfig() {
        Map<String, String> emailConfig = new HashMap<>();
        List<Globals> globalsList = repository.findByGroupCode("email");

        for (Globals globals : globalsList) {
            emailConfig.put(globals.getCode(), globals.getValue());
        }

        return emailConfig;
    }

    public String getAflStatsUrl() {
        return getValue(AFL_REF, "aflStatsUrl");
    }

    public String getEmailerRoot() {
        return getValue(DFL_REF, "emailerRoot");
    }

    public String getTeamEmail(String teamCode) {
        return getValue("email", teamCode);
    }

    public String getCurrentRound() {
        return getValue(DFL_REF, "currentRound");
    }

    public String getPreSeasonFixtureUrl() {
        return getValue(AFL_REF, "preSeasonFixture");
    }

    public String getBrowserPath() {
        return getValue(DFL_REF, "browserPath");
    }

    public int getWebdriverWait() {
        return Integer.parseInt(getValue(DFL_REF, "webdriverWait"));
    }

    public int getWebdriverTimeout() {
        return Integer.parseInt(getValue(DFL_REF, "webdriverTimeout"));
    }

    public Map<String, String> getDraftOrder() {
        Map<String, String> draftOrder = new HashMap<>();
        List<Globals> globalsList = repository.findByGroupCode("draftOrder");

        for (Globals globals : globalsList) {
            draftOrder.put(globals.getCode(), globals.getValue());
        }

        return draftOrder;
    }

    public void setCurrentRound(int newRound) {
        GlobalsPK pk = new GlobalsPK();
        pk.setCode("currentRound");
        pk.setGroupCode(DFL_REF);

        Globals currentRound = repository.findById(pk).orElse(null);
        if (currentRound != null) {
            currentRound.setValue(Integer.toString(newRound));
            repository.save(currentRound);
        }
    }

    public String getAflTeamMap(String team) {
        return getValue("aflTeamMap", team);
    }

    public Map<Integer, Map<Integer, String[]>> getDflFixuteTemplate() {
        Map<Integer, Map<Integer, String[]>> fixtureTemplate = new HashMap<>();

        List<Globals> globalsList = repository.findByGroupCode("dflFixtureTemplate");

        for (Globals globals : globalsList) {
            int round = Integer.parseInt(globals.getCode());
            String roundTemplate = globals.getValue();

            String[] games = roundTemplate.substring(1, roundTemplate.length()-1).split("\\]\\[");

            Map<Integer, String[]> roundFixtures = new HashMap<>();

            for (int j = 0; j < games.length; j++) {
                String[] g = games[j].split(",");
                roundFixtures.put(j+1, g);
            }

            fixtureTemplate.put(round, roundFixtures);
        }

        return fixtureTemplate;
    }

    public Map<String, String> getDflFixtureOrder() {
        Map<String, String> fixtureOrder = new HashMap<>();
        List<Globals> globalsList = repository.findByGroupCode("dflFixtureOrder");

        for (Globals globals : globalsList) {
            fixtureOrder.put(globals.getCode(), globals.getValue());
        }

        return fixtureOrder;
    }

    public boolean getSendMedalReports(int currentRound) {
        int roundCutoff = Integer.parseInt(getValue(DFL_REF, "medalsRoundCutoff"));
        return currentRound > roundCutoff;
    }

    public String getOnlineBaseUrl() {
        return getValue(DFL_REF, "onlineBaseUrl");
    }

    public int getUseAverage(String teamCode) {
        String value = getValue("useAvg", teamCode);

        if (value != null && !value.isEmpty()) {
            return Integer.parseInt(value);
        }

        return 0;
    }

    public boolean getUseOfficalPlayers() {
        String value = getValue(AFL_REF, "useOfficialPlayers");
        return Boolean.parseBoolean(value);
    }

    public boolean getSplitDflRounds() {
        String value = getValue(DFL_REF, "splitDflRounds");
        return Boolean.parseBoolean(value);
    }

    public List<Integer> getStatRounds() {
        String value = getValue(DFL_REF, "statRounds");

        return Stream.of(value.split(","))
            .map(Integer::parseInt)
            .sorted()
            .toList();
    }

    public void insertAll(List<Globals> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }

    public void updateAll(List<Globals> entities, boolean inTx) {
        // inTx parameter ignored - Spring manages transactions via @Transactional
        repository.saveAll(entities);
    }
}
