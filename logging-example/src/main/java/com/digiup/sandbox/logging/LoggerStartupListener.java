package com.digiup.sandbox.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import org.springframework.beans.factory.annotation.Value;

public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    @Value("${log.pattern}")
    private String logPattern;

    private boolean hasStarted = false;

    @Override
    public void start() {
        if (hasStarted) {
            return;
        }

        hasStarted = true;

        Context ctx = getContext();
        ctx.putProperty("LOG_PATTERN", logPattern);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext loggerContext) {

    }

    @Override
    public void onReset(LoggerContext loggerContext) {

    }

    @Override
    public void onStop(LoggerContext loggerContext) {

    }

    @Override
    public void onLevelChange(Logger logger, Level level) {

    }
}
