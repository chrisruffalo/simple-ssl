package com.github.chrisruffalo.simplessl.api.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cruffalo on 3/17/15.
 */
public abstract class Attempt<VALUE> {

    private final List<Warning> warnings;

    private Attempt() {
        this.warnings = new LinkedList<>();
    }

    private Attempt(List<Warning> warnings) {
        this();

        // add all if warnings exist
        if(warnings != null && !warnings.isEmpty()) {
            this.warnings.addAll(warnings);
        }
    }

    public abstract VALUE get();

    public abstract boolean hasErrors();

    public abstract List<Error> errors();

    public abstract boolean failed();

    public boolean successful() {
        return !this.failed();
    }

    public boolean hasWarnings() {
        return this.warnings.size() > 0;
    }

    public List<Warning> warnings() {
        if(this.warnings == null || this.warnings.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.warnings);
    }

    private static final class ValueAttempt<VALUE> extends Attempt<VALUE> {

        private final VALUE value;

        private ValueAttempt(VALUE value) {
            this(value, null);
        }

        private ValueAttempt(VALUE value, List<Warning> warnings) {
            super(warnings);

            if(value == null) {
                throw new IllegalStateException("A value must be provided for a ValueAttempt to be complete");
            }
            this.value = value;
        }

        @Override
        public VALUE get() {
            return this.value;
        }

        @Override
        public boolean failed() {
            return false;
        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public List<Error> errors() {
            return Collections.emptyList();
        }

    }

    private static final class FailedAttempt<VALUE> extends Attempt<VALUE> {

        private final List<Error> errors;

        private FailedAttempt() {
            this(null);
        }


        private FailedAttempt(List<Error> errors) {
            this(errors, null);
        }

        private FailedAttempt(List<Error> errors, List<Warning> warnings) {
            super(warnings);

            this.errors = new LinkedList<>();

            // add if errors exist
            if(errors != null && !errors.isEmpty()) {
                this.errors.addAll(errors);
            }
        }

        @Override
        public VALUE get() {
            throw new IllegalStateException("Failed attempt does not have a value component");
        }

        @Override
        public boolean failed() {
            return true;
        }

        @Override
        public boolean hasErrors() {
            return !this.errors.isEmpty();
        }

        @Override
        public List<Error> errors() {
            if(errors == null || errors.isEmpty()) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(this.errors);
        }
    }

    public static <VALUE> Attempt<VALUE> succeed(VALUE value) {
        return new ValueAttempt<>(value);
    }

    public static <VALUE> Attempt<VALUE> succeed(VALUE value, Warning... warnings) {
        if(warnings == null || warnings.length < 1) {
            return Attempt.succeed(value);
        }
        List<Warning> warningList = Arrays.asList(warnings);
        return Attempt.succeed(value, warningList);
    }

    public static <VALUE> Attempt<VALUE> succeed(VALUE value, List<Warning> warnings) {
        return new ValueAttempt<>(value, warnings);
    }

    public static <VALUE> Attempt<VALUE> fail() {
        return new FailedAttempt<>();
    }

    public static <VALUE> Attempt<VALUE> fail(Error error) {
        return new FailedAttempt<>(Collections.singletonList(error));
    }

    public static <VALUE> Attempt<VALUE> fail(Error... errors) {
        if(errors == null || errors.length < 1) {
            return Attempt.fail();
        }
        List<Error> errorList = Arrays.asList(errors);
        return Attempt.fail(errorList);
    }

    public static <VALUE> Attempt<VALUE> fail(List<Error> errors) {
        return new FailedAttempt<>(errors);
    }

    public static <VALUE> Attempt<VALUE> fail(Error error, List<Warning> warnings) {
        return new FailedAttempt<>(Collections.singletonList(error), warnings);
    }

    public static <VALUE> Attempt<VALUE> fail(List<Error> errors, List<Warning> warnings) {
        return new FailedAttempt<>(errors, warnings);
    }
}
