package com.deliver.finances.config.flags;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum FeatureFlags implements Feature {


    @Label("Ativa ou desativa o calculo de juros composto.")
    @EnabledByDefault
    CALCULATE_COMPOSED_INTEREST;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

    public boolean isInactive() {
        return !isActive();
    }
}
