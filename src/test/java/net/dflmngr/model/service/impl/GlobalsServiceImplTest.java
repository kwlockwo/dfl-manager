package net.dflmngr.model.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dflmngr.jpa.AbstractDatabaseTest;
import net.dflmngr.model.entity.Globals;

class GlobalsServiceImplTest extends AbstractDatabaseTest {

	private GlobalsServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new GlobalsServiceImpl();

		List<Globals> globals = new ArrayList<>();
		globals.add(timezoneRow("default", "Australia/Melbourne"));
		globals.add(timezoneRow("Perth Stadium", "Australia/Perth"));
		service.replaceAll(globals);
	}

	@AfterEach
	void tearDown() {
		service.close();
	}

	private Globals timezoneRow(String code, String value) {
		Globals row = new Globals();
		row.setCode(code);
		row.setGroupCode("timezone");
		row.setValue(value);
		return row;
	}

	@Test
	void getGroundTimeZone_shouldReturnGroundTimezone_whenGroundIsConfigured() {
		assertEquals("Australia/Perth", service.getGroundTimeZone("Perth Stadium"));
	}

	@Test
	void getGroundTimeZone_shouldFallBackToDefault_whenGroundIsUnknown() {
		assertEquals("Australia/Melbourne", service.getGroundTimeZone("Unknown Ground"));
	}
}
