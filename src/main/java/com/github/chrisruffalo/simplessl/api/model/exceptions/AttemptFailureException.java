package com.github.chrisruffalo.simplessl.api.model.exceptions;

import com.github.chrisruffalo.simplessl.api.model.Attempt;

/**
 * Created by cruffalo on 5/26/15.
 */
public class AttemptFailureException extends Exception {

    private final Attempt<?> failed;

    public AttemptFailureException(final Attempt<?> failed) {
        if(failed == null) {
            throw new IllegalStateException("Cannot throw attempt failure using null failure as source");
        }
        if(!failed.failed()) {
            throw new IllegalStateException("Cannot throw attempt failure from non-failed attempt");
        }
        this.failed = failed;
    }

    @Override
    public String getMessage() {
        return AttemptFailures.failedAttemptToMessage(this.failed);
    }

    @Override
    public String getLocalizedMessage() {
        return this.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return AttemptFailures.getPrimaryFailureCause(this.failed);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        final StackTraceElement[] elements = super.getStackTrace();
        final Throwable cause = this.getCause();
        return AttemptFailures.getStackTrace(elements, cause);
    }
}
