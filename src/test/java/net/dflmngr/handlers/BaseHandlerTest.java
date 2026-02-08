package net.dflmngr.handlers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.dflmngr.service.ServiceFactory;

/**
 * Tests for BaseHandler.
 * Verifies logging configuration and common handler initialization.
 */
class BaseHandlerTest {

	/**
	 * Concrete implementation of BaseHandler for testing.
	 */
	static class TestHandler extends BaseHandler {
		public TestHandler() {
			super("TestLogfile");
		}

		public boolean getIsExecutable() {
			return isExecutable;
		}

		public String getMdcKey() {
			return mdcKey;
		}

		public String getLoggerName() {
			return loggerName;
		}

		public String getLogfile() {
			return logfile;
		}
	}

	@Test
	void constructor_shouldInitializeWithDefaultLogfile() {
		// When
		TestHandler handler = new TestHandler();

		// Then
		assertThat(handler.defaultLogfile).isEqualTo("TestLogfile");
		assertThat(handler.serviceFactory).isNotNull();
		assertThat(handler.serviceFactory).isSameAs(ServiceFactory.getInstance());
	}

	@Test
	void constructor_shouldNotBeExecutableByDefault() {
		// When
		TestHandler handler = new TestHandler();

		// Then
		assertThat(handler.getIsExecutable()).isFalse();
	}

	@Test
	void configureLogging_shouldSetExecutableTrue() {
		// Given
		TestHandler handler = new TestHandler();

		// When
		handler.configureLogging("test.key", "test-logger", "test-logfile");

		// Then
		assertThat(handler.getIsExecutable()).isTrue();
		assertThat(handler.getMdcKey()).isEqualTo("test.key");
		assertThat(handler.getLoggerName()).isEqualTo("test-logger");
		assertThat(handler.getLogfile()).isEqualTo("test-logfile");
		assertThat(handler.loggerUtils).isNotNull();
	}

	@Test
	void ensureLoggingConfigured_shouldUseDefaultsIfNotConfigured() {
		// Given
		TestHandler handler = new TestHandler();
		assertThat(handler.getIsExecutable()).isFalse();

		// When
		handler.ensureLoggingConfigured();

		// Then
		assertThat(handler.getIsExecutable()).isTrue();
		assertThat(handler.getMdcKey()).isEqualTo("batch.name");
		assertThat(handler.getLoggerName()).isEqualTo("batch-logger");
		assertThat(handler.getLogfile()).isEqualTo("TestLogfile");
		assertThat(handler.loggerUtils).isNotNull();
	}

	@Test
	void ensureLoggingConfigured_shouldNotReconfigureIfAlreadyExecutable() {
		// Given
		TestHandler handler = new TestHandler();
		handler.configureLogging("custom.key", "custom-logger", "custom-logfile");
		String originalLogfile = handler.getLogfile();

		// When
		handler.ensureLoggingConfigured();

		// Then
		assertThat(handler.getLogfile()).isEqualTo(originalLogfile);
		assertThat(handler.getMdcKey()).isEqualTo("custom.key");
		assertThat(handler.getLoggerName()).isEqualTo("custom-logger");
	}

	@Test
	void defaultValues_shouldBeCorrect() {
		// When
		TestHandler handler = new TestHandler();

		// Then
		assertThat(handler.defaultMdcKey).isEqualTo("batch.name");
		assertThat(handler.defaultLoggerName).isEqualTo("batch-logger");
		assertThat(handler.defaultLogfile).isEqualTo("TestLogfile");
	}
}
