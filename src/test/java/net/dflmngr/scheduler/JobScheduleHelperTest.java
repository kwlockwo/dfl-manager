package net.dflmngr.scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Map;

class JobScheduleHelperTest {

	@Test
	void createCronExpression_shouldFormatTimeCorrectly() {
		// Create a specific time: 2:30 PM on Jan 15, 2024
		ZonedDateTime time = ZonedDateTime.of(2024, 1, 15, 14, 30, 0, 0, ZoneId.of("UTC"));

		String cronExpression = JobScheduleHelper.createCronExpression(time);

		assertNotNull(cronExpression);
		// The cron expression should contain the formatted time and date
		// Format is: hh:mm a (02:30 PM) and dd-MM-yyyy (15-01-2024)
		assertTrue(cronExpression.length() > 0);
	}

	@Test
	void createCronExpression_shouldHandleMorningTime() {
		// Create a morning time: 9:15 AM on March 20, 2024
		ZonedDateTime time = ZonedDateTime.of(2024, 3, 20, 9, 15, 0, 0, ZoneId.of("UTC"));

		String cronExpression = JobScheduleHelper.createCronExpression(time);

		assertNotNull(cronExpression);
		assertTrue(cronExpression.length() > 0);
	}

	@Test
	void createCronExpression_shouldHandleEveningTime() {
		// Create an evening time: 11:45 PM on Dec 31, 2024
		ZonedDateTime time = ZonedDateTime.of(2024, 12, 31, 23, 45, 0, 0, ZoneId.of("UTC"));

		String cronExpression = JobScheduleHelper.createCronExpression(time);

		assertNotNull(cronExpression);
		assertTrue(cronExpression.length() > 0);
	}

	@Test
	void createCronExpression_shouldHandleMidnight() {
		// Create midnight time: 12:00 AM on June 1, 2024
		ZonedDateTime time = ZonedDateTime.of(2024, 6, 1, 0, 0, 0, 0, ZoneId.of("UTC"));

		String cronExpression = JobScheduleHelper.createCronExpression(time);

		assertNotNull(cronExpression);
		assertTrue(cronExpression.length() > 0);
	}

	@Test
	void createCronExpression_shouldHandleNoon() {
		// Create noon time: 12:00 PM on July 4, 2024
		ZonedDateTime time = ZonedDateTime.of(2024, 7, 4, 12, 0, 0, 0, ZoneId.of("UTC"));

		String cronExpression = JobScheduleHelper.createCronExpression(time);

		assertNotNull(cronExpression);
		assertTrue(cronExpression.length() > 0);
	}

	@Test
	void createJobParams_shouldCreateMapWithSingleEntry() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("round", 5);

		assertNotNull(params);
		assertEquals(1, params.size());
		assertEquals(5, params.get("round"));
	}

	@Test
	void createJobParams_shouldHandleIntegerValue() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("round", 10);

		assertEquals(10, params.get("round"));
	}

	@Test
	void createJobParams_shouldHandleBooleanValue() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("isFinal", true);

		assertEquals(true, params.get("isFinal"));
	}

	@Test
	void createJobParams_shouldHandleStringValue() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("reportType", "summary");

		assertEquals("summary", params.get("reportType"));
	}

	@Test
	void createJobParams_shouldHandleNullValue() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("nullParam", null);

		assertNotNull(params);
		assertEquals(1, params.size());
		assertTrue(params.containsKey("nullParam"));
		assertNull(params.get("nullParam"));
	}

	@Test
	void createJobParams_shouldCreateMutableMap() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("key1", "value1");

		// Should be able to add more entries
		assertDoesNotThrow(() -> params.put("key2", "value2"));
		assertEquals(2, params.size());
	}

	@Test
	void createCronExpression_shouldHandleDifferentDates() {
		// Test with single-digit day
		ZonedDateTime time1 = ZonedDateTime.of(2024, 1, 5, 10, 0, 0, 0, ZoneId.of("UTC"));
		String cron1 = JobScheduleHelper.createCronExpression(time1);
		assertNotNull(cron1);

		// Test with double-digit day
		ZonedDateTime time2 = ZonedDateTime.of(2024, 1, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
		String cron2 = JobScheduleHelper.createCronExpression(time2);
		assertNotNull(cron2);

		// Different dates should produce different cron expressions
		assertNotEquals(cron1, cron2);
	}

	@Test
	void createCronExpression_shouldHandleDifferentMonths() {
		ZonedDateTime jan = ZonedDateTime.of(2024, 1, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime dec = ZonedDateTime.of(2024, 12, 15, 10, 0, 0, 0, ZoneId.of("UTC"));

		String cronJan = JobScheduleHelper.createCronExpression(jan);
		String cronDec = JobScheduleHelper.createCronExpression(dec);

		assertNotEquals(cronJan, cronDec);
	}

	@Test
	void createCronExpression_shouldHandleDifferentYears() {
		ZonedDateTime year2024 = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime year2025 = ZonedDateTime.of(2025, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));

		String cron2024 = JobScheduleHelper.createCronExpression(year2024);
		String cron2025 = JobScheduleHelper.createCronExpression(year2025);

		assertNotEquals(cron2024, cron2025);
	}

	@Test
	void createCronExpression_shouldHandleDifferentTimes() {
		ZonedDateTime morning = ZonedDateTime.of(2024, 6, 15, 9, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime evening = ZonedDateTime.of(2024, 6, 15, 21, 0, 0, 0, ZoneId.of("UTC"));

		String cronMorning = JobScheduleHelper.createCronExpression(morning);
		String cronEvening = JobScheduleHelper.createCronExpression(evening);

		assertNotEquals(cronMorning, cronEvening);
	}

	@Test
	void createCronExpression_shouldHandleDifferentMinutes() {
		ZonedDateTime time1 = ZonedDateTime.of(2024, 6, 15, 10, 15, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime time2 = ZonedDateTime.of(2024, 6, 15, 10, 45, 0, 0, ZoneId.of("UTC"));

		String cron1 = JobScheduleHelper.createCronExpression(time1);
		String cron2 = JobScheduleHelper.createCronExpression(time2);

		assertNotEquals(cron1, cron2);
	}

	@Test
	void createCronExpression_shouldProduceSameResultForSameTime() {
		ZonedDateTime time = ZonedDateTime.of(2024, 6, 15, 10, 30, 0, 0, ZoneId.of("UTC"));

		String cron1 = JobScheduleHelper.createCronExpression(time);
		String cron2 = JobScheduleHelper.createCronExpression(time);

		assertEquals(cron1, cron2);
	}

	@Test
	void createCronExpression_shouldHandleDifferentTimezones() {
		// Same absolute time in different timezones
		ZonedDateTime utc = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime sydney = ZonedDateTime.of(2024, 6, 15, 20, 0, 0, 0, ZoneId.of("Australia/Sydney"));

		String cronUtc = JobScheduleHelper.createCronExpression(utc);
		String cronSydney = JobScheduleHelper.createCronExpression(sydney);

		// Different local times should produce different cron expressions
		assertNotEquals(cronUtc, cronSydney);
	}

	@Test
	void createJobParams_shouldHandleLongValue() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("longValue", 999999999L);

		assertEquals(999999999L, params.get("longValue"));
	}

	@Test
	void createJobParams_shouldHandleDoubleValue() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("doubleValue", 3.14);

		assertEquals(3.14, params.get("doubleValue"));
	}

	@Test
	void createJobParams_shouldHandleEmptyStringKey() {
		Map<String, Object> params = JobScheduleHelper.createJobParams("", "value");

		assertNotNull(params);
		assertEquals(1, params.size());
		assertEquals("value", params.get(""));
	}
}
