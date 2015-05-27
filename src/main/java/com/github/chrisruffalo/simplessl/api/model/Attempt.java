package com.github.chrisruffalo.simplessl.api.model;

import com.github.chrisruffalo.simplessl.api.model.exceptions.AttemptFailureException;
import com.github.chrisruffalo.simplessl.api.model.exceptions.RuntimeAttemptFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <p>An <code>Attempt</code> is a wrapper for an attempt to complete
 * an action that may fail. If the action succeeds then the attempt
 * will contain the result of the action. It cannot contain a null
 * result.</p>
 * <p>If the action fails then an attempt will be still be returned
 * and can be inspected via the failed() method which will return
 * false.</p>
 * <p>If warnings are created during the attempt they can be checked
 * with the warnings() method.</p>
 */
public abstract class Attempt<VALUE> {

    private final String id;

    private final List<Warning> warnings;

    private Attempt() {
        // generate id for the attempt
        this.id = UUID.randomUUID().toString();
        this.warnings = new LinkedList<>();
    }

    private Attempt(List<Warning> warnings) {
        this();

        // add all if warnings exist
        if(warnings != null && !warnings.isEmpty()) {
            this.warnings.addAll(warnings);
        }
    }

    /**
     * A unique id for the attempt. This is helpful for
     * auditing and differentiating between multiple
     * different attempts.
     *
     * @return
     */
    public String id() {
        return this.id;
    }

    public abstract VALUE get();

    public abstract VALUE or(final VALUE defaultValue);

    public abstract VALUE orThrow() throws AttemptFailureException;

    public abstract VALUE orRuntimeException() throws RuntimeAttemptFailureException;

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

    /**
     * Log an audit message directly from the attempt. This method
     * uses it's own logger.
     */
    public void audit() {
        final Logger logger = LoggerFactory.getLogger("attempt-" + this.id);
        this.audit(logger);
    }

    /**
     * Print audit log message to provided logger.
     *
     * @param logger
     */
    public void audit(Logger logger) {
        final StringBuilder builder = new StringBuilder("\n");

        // print warnings
        final List<Warning> warnings = this.warnings();
        if(!warnings.isEmpty()) {
            builder.append("There were ");
            builder.append(warnings.size());
            builder.append(" warnings");
            for(final Warning warning : warnings) {
                final String msg = warning.message();
                if(msg != null && !msg.isEmpty()) {
                    builder.append("\n");
                    builder.append(msg);
                }
            }
        }

        // print errors
        final List<Error> errors = this.errors();
        if(!errors.isEmpty()) {
            builder.append("There were ");
            builder.append(errors.size());
            builder.append(" errors");
            for(final Error error : errors) {
                final String msg = error.message();
                if(msg != null && !msg.isEmpty()) {
                    builder.append("\n");
                    builder.append(msg);
                    if(error.hasCause()) {
                        final Throwable throwable = error.cause();
                        final String cause = throwable.getMessage();
                        if(cause != null && !cause.isEmpty()) {
                            builder.append("\tCause: " + cause);
                        }
                    }
                }
            }
        }
    }

    /**
     * Encapsulates a value with an attempt succeeds. That value can
     * never be null.
     *
     * @param <VALUE> the value type encapsulated by the attempt
     */
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
        public VALUE or(final VALUE defaultValue) {
            return this.value;
        }

        @Override
        public VALUE orThrow() throws AttemptFailureException {
            return this.value;
        }

        @Override
        public VALUE orRuntimeException() throws RuntimeAttemptFailureException {
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

    /**
     * Represents a failed attempt. The get() operation will throw
     * an exception if called when failed() is true. If any errors
     * were produced they can be checked with the errors() method.
     *
     * @param <VALUE>
     */
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
        public VALUE or(final VALUE defaultValue) {
            return defaultValue;
        }

        @Override
        public VALUE orThrow() throws AttemptFailureException {
            throw new AttemptFailureException(this);
        }

        @Override
        public VALUE orRuntimeException() throws RuntimeAttemptFailureException {
            throw new RuntimeAttemptFailureException(this);
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
        final List<Warning> warningList = Arrays.asList(warnings);
        return Attempt.succeed(value, warningList);
    }

    public static <VALUE> Attempt<VALUE> succeed(VALUE value, List<Warning> warnings) {
        if(value == null) {
            return new FailedAttempt<>();
        }
        return new ValueAttempt<>(value, warnings);
    }

    public static <VALUE> Attempt<VALUE> fail() {
        return new FailedAttempt<>();
    }

    public static <VALUE> Attempt<VALUE> fail(String message) {
        return Attempt.fail(new Error(message));
    }

    public static <VALUE> Attempt<VALUE> fail(String message, Throwable cause) {
        return Attempt.fail(new Error(message, cause));
    }

    public static <VALUE> Attempt<VALUE> fail(Error error) {
        return new FailedAttempt<>(Collections.singletonList(error));
    }

    public static <VALUE> Attempt<VALUE> fail(Error... errors) {
        if(errors == null || errors.length < 1) {
            return Attempt.fail();
        }
        final List<Error> errorList = Arrays.asList(errors);
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

    public static List<Object> values(List<Attempt<?>> attempts) {
        if(attempts == null || attempts.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Object> values = new LinkedList<>();
        for(Attempt<?> attempt : attempts) {
            if(attempt.failed()) {
                continue;
            }

            Object value = attempt.get();
            if(value != null) {
                values.add(value);
            }
        }

        // return values
        return Collections.unmodifiableList(values);
    }

    public static List<Warning> warnings(List<Attempt<?>> attempts) {
        if(attempts == null || attempts.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Warning> warnings = new LinkedList<>();
        for(Attempt<?> attempt : attempts) {
            if(!attempt.hasWarnings()) {
                continue;
            }

            warnings.addAll(attempt.warnings());
        }

        // return warnings
        return Collections.unmodifiableList(warnings);
    }

    public static List<Error> errors(List<Attempt<?>> attempts) {
        if(attempts == null || attempts.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Error> errors = new LinkedList<>();
        for(Attempt<?> attempt : attempts) {
            if(!attempt.hasErrors()) {
                continue;
            }

            errors.addAll(attempt.errors());
        }

        // return warnings
        return Collections.unmodifiableList(errors);
    }
}
