package com.github.chrisruffalo.simplessl.api.model.exceptions;

import com.github.chrisruffalo.simplessl.api.model.Attempt;
import com.github.chrisruffalo.simplessl.api.model.Error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by cruffalo on 5/26/15.
 */
final class AttemptFailures {

    private AttemptFailures() {

    }

    /**
     * Converts a failed Attempt into an exception message
     *
     * @param failed
     * @return
     */
    public static String failedAttemptToMessage(final Attempt<?> failed) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Attempt failed due to: '");
        builder.append(failed.errors().get(0).message());
        if(failed.errors().size() > 1) {
            builder.append("' (and " + failed.errors().size() + " other errors)");
        } else {
            builder.append("'");
        }

        return builder.toString();
    }

    /**
     * Returns the first Throwable cause that was encountered during the Attempt
     *
     * @param failed
     * @return
     */
    public static Throwable getPrimaryFailureCause(final Attempt<?> failed) {
        for(final Error error : failed.errors()) {
            if(error.hasCause()) {
                return error.cause();
            }
        }
        return null;
    }

    /**
     * Creates a stack trace based on the first Throwable cause and the location
     * that the attempt was thrown from.
     *
     * @param current
     * @param cause
     * @return
     */
    public static StackTraceElement[] getStackTrace(final StackTraceElement[] current, final Throwable cause) {
        StackTraceElement[] elements = current;
        if(cause == null) {
            return elements;
        }
        final StackTraceElement[] causeElements = cause.getStackTrace();
        if(causeElements != null && causeElements.length > 0) {
            final List<StackTraceElement> fullCause = new ArrayList<>(elements.length + causeElements.length);
            fullCause.addAll(Arrays.asList(elements));
            fullCause.addAll(Arrays.asList(causeElements));
            elements = fullCause.toArray(new StackTraceElement[fullCause.size()]);
        }
        return elements;
    }
}
