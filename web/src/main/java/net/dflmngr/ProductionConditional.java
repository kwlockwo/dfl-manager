package net.dflmngr;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

public class ProductionConditional implements Condition {

	@Override
	public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
		String appEnv = System.getenv("APP_ENV");
		return (appEnv != null && appEnv.equalsIgnoreCase("production"));
	}
}
