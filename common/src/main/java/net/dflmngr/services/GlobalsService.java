package net.dflmngr.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dflmngr.exceptions.MissingGlobalConfig;
import net.dflmngr.model.entity.Globals;
import net.dflmngr.model.entity.keys.GlobalsPK;
import net.dflmngr.repositories.GlobalsRepository;

@Service
public class GlobalsService {

    private static final String PARAMS = "params";
    private static final String VALUE = "value";
    private static final String DFL_REF = "dflRef";
    private static final String AFL_REF = "aflRef";

    private final GlobalsRepository repository;

    public GlobalsService(GlobalsRepository repository) {
        this.repository = repository;
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

    public String getValue(String code, String groupCode) {
        String value = "";

        GlobalsPK pk = new GlobalsPK();
        pk.setGroupCode(groupCode);
        pk.setCode(code);

        Globals globals = repository.findById(pk).orElse(null);

        if(globals != null) {
            value = globals.getValue();
        } else {
            throw new MissingGlobalConfig(code, groupCode);
        }

        return value;
    }

    public List<String> getCodes(String groupCode) {
        List<String> codes = new ArrayList<>();

        List<Globals> globalsList = repository.findByGroupCode(groupCode);

        for(Globals globals : globalsList) {
            codes.add(globals.getCode());
        }

        return codes;
    }

    public Map<String, String> getGroupValues(String groupCode) {
        Map<String, String> codesValues = new HashMap<>();

        List<Globals> globalsList = repository.findByGroupCode(groupCode);

        for(Globals globals : globalsList) {
            codesValues.put(globals.getCode(), globals.getValue());
        }

        return codesValues;
    }

    public String getCurrentYear() {
        String code = "currentYear";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public List<String> getAflFixtureUrl() {
        List<String> aflFixtureUrl = new ArrayList<>();
        String code = "aflFixtureUrl";
        String groupCode = AFL_REF;

        Map<String, String> valueAndParams = getValueAndParams(code, groupCode);

        aflFixtureUrl.add(valueAndParams.get(VALUE));

        if(valueAndParams.containsKey(PARAMS)) {
            String params = valueAndParams.get(PARAMS);
            if(params != null) {
                String[] parts = params.split(";");
                aflFixtureUrl.addAll(Arrays.asList(parts));
            }
        }

        return aflFixtureUrl;
    }

    public String getGroundTimeZone(String ground) {
        String timezone = "";
        String code = ground;
        String groupCode = "timezone";

        timezone = getValue(code, groupCode);

        if(timezone.equals("")) {
            code = "default";
            timezone = getValue(code, groupCode);
        }

        return timezone;
    }

    public Map<String, String> getGround(String groundName) {
        Map<String, String> ground = new HashMap<>();

        String code = groundName;
        String groupCode = "ground";

        Map<String, String> data = getValueAndParams(code, groupCode);

        ground.put("ground", data.get(VALUE));
        ground.put("timezone", data.get(PARAMS));

        return ground;
    }

    public List<String> getTeamCodes() {
        String groupCode = "teamCode";
        return getCodes(groupCode);
    }

    public String getAppDir() {
        String code = "appDir";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public String getReportDir() {
        String code = "reportDir";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public String getStandardLockoutTime() {
        String code = "standardLockoutTime";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public String getNonStandardLockout(int round) {
        String code = Integer.toString(round);
        String groupCode = "nonStandardLockout";
        return getValue(code, groupCode);
    }

    public String getAflRoundsMax() {
        String code = "aflRoundsMax";
        String groupCode = AFL_REF;
        return getValue(code, groupCode);
    }

    public Map<String, String> getEmailConfig() {
        Map<String, String> emailConfig = new HashMap<>();

        String emailParam = "";
        String code = "dflmngrEmailAddr";
        String groupCode = DFL_REF;

        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "dflgroupEmailAddr";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "incomingMailHost";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "incomingMailPort";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "outgoingMailHost";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "outgoingMailPort";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "mailUsername";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        code = "mailPassword";
        emailParam = getValue(code, groupCode);
        emailConfig.put(code, emailParam);

        return emailConfig;
    }

    public String getAflStatsUrl() {
        String code = "aflStatsUrl";
        String groupCode = AFL_REF;
        return getValue(code, groupCode);
    }

    public String getEmailerRoot() {
        String code = "emailerRoot";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public String getTeamEmail(String teamCode) {
        String code = teamCode;
        String groupCode = "teamEmail";
        return getValue(code, groupCode);
    }

    public String getCurrentRound() {
        String code = "currentRound";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public String getPreSeasonFixtureUrl() {
        String code = "preSeasonFixture";
        String groupCode = AFL_REF;
        return getValue(code, groupCode);
    }

    public String getBrowserPath() {
        String code = "browserPath";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public int getWebdriverWait() {
        String code = "webdriverWait";
        String groupCode = DFL_REF;
        return Integer.parseInt(getValue(code, groupCode));
    }

    public int getWebdriverTimeout() {
        String code = "webdriverTimeout";
        String groupCode = DFL_REF;
        return Integer.parseInt(getValue(code, groupCode));
    }

    public Map<String, String> getDraftOrder() {
        String groupCode = "draftOrder";
        return getGroupValues(groupCode);
    }

    @Transactional
    public void setCurrentRound(int newRound) {
        GlobalsPK pk = new GlobalsPK();
        pk.setCode("currentRound");
        pk.setGroupCode(DFL_REF);
        Globals currentRound = repository.findById(pk).orElse(null);
        if(currentRound != null) {
            currentRound.setValue(Integer.toString(newRound));
            repository.save(currentRound);
        }
    }

    public String getAflTeamMap(String team) {
        String code = team;
        String groupCode = "aflTeamMap";
        return getValue(code, groupCode);
    }

    public Map<Integer, Map<Integer, String[]>> getDflFixuteTemplate() {
        Map<Integer, Map<Integer, String[]>> fixtureTemplate = new HashMap<>();

        String groupCode = "dflFixtureTemplate";

        Map<String, String> globalsTemplate = getGroupValues(groupCode);

        for (Map.Entry<String, String> entry : globalsTemplate.entrySet()) {
            int round = Integer.parseInt(entry.getKey());
            String roundTemplate = entry.getValue();

            String[] games = roundTemplate.substring(0, roundTemplate.length()-1).substring(1).split("\\]\\[");

            Map<Integer, String[]> roundFixtures = new HashMap<>();

            for(int j = 0; j < games.length; j++) {
                String[] g = games[j].split(",");
                roundFixtures.put(j+1, g);
            }

            fixtureTemplate.put(round, roundFixtures);
        }

        return fixtureTemplate;
    }

    public Map<String, String> getDflFixtureOrder() {
        String groupCode = "dflFixtureOrder";
        return getGroupValues(groupCode);
    }

    public boolean getSendMedalReports(int currentRound) {
        boolean cutoff = false;
        int roundCutoff = 0;
        String code = "medalsRoundCutoff";
        String groupCode = DFL_REF;

        roundCutoff = Integer.parseInt(getValue(code, groupCode));

        if(currentRound > roundCutoff) {
            cutoff = true;
        }

        return cutoff;
    }

    public String getOnlineBaseUrl() {
        String code = "onlineBaseUrl";
        String groupCode = DFL_REF;
        return getValue(code, groupCode);
    }

    public int getUseAverage(String teamCode) {
        int round = 0;
        String value;
        String groupCode = "useAvg";

        value = getValue(teamCode, groupCode);

        if(value != null && !value.isEmpty()) {
            round = Integer.parseInt(value);
        }

        return round;
    }

    public boolean getUseOfficalPlayers() {
        String code = "useOfficialPlayers";
        String groupCode = AFL_REF;

        String value = getValue(code, groupCode);

        return Boolean.parseBoolean(value);
    }

    public boolean getSplitDflRounds() {
        String code = "splitDflRounds";
        String groupCode = DFL_REF;

        String value = getValue(code, groupCode);

        return Boolean.parseBoolean(value);
    }

    public List<Integer> getStatRounds() {
        String code = "statRounds";
        String groupCode = DFL_REF;

        String value = getValue(code, groupCode);

        return Stream.of(value.split(","))
            .map(Integer::parseInt)
            .sorted()
            .toList();
    }

    // Generic repository methods
    public Globals get(GlobalsPK id) {
        return repository.findById(id).orElse(null);
    }

    public List<Globals> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void insert(Globals entity) {
        repository.save(entity);
    }

    @Transactional
    public void update(Globals entity) {
        repository.save(entity);
    }

    @Transactional
    public void insertAll(List<Globals> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void updateAll(List<Globals> entities) {
        repository.saveAll(entities);
    }

    @Transactional
    public void delete(Globals entity) {
        repository.delete(entity);
    }

    @Transactional
    public void replaceAll(List<Globals> entities) {
        repository.deleteAll();
        repository.flush();
        repository.saveAll(entities);
    }
}
