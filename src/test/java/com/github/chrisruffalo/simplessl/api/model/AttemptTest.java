package com.github.chrisruffalo.simplessl.api.model;

import com.github.chrisruffalo.simplessl.api.model.exceptions.AttemptFailureException;
import com.github.chrisruffalo.simplessl.api.model.exceptions.RuntimeAttemptFailureException;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cruffalo on 3/17/15.
 */
public class AttemptTest {

    @Test
    public void testSuccess() {
        final String initial = "success!";
        final Attempt<String> stringAttempt = Attempt.succeed(initial);
        final String value = stringAttempt.get();

        Assert.assertTrue(stringAttempt.successful());
        Assert.assertEquals(initial, value);
        Assert.assertFalse(stringAttempt.hasErrors());
        Assert.assertFalse(stringAttempt.hasWarnings());
        Assert.assertFalse(stringAttempt.failed());

        Assert.assertEquals(0, stringAttempt.errors().size());
        Assert.assertEquals(0, stringAttempt.warnings().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuccessNullValue() {
        Attempt.succeed(null);
    }

    @Test
    public void testSuccessWithWarnings() {
        final Attempt<String> stringAttempt = Attempt.succeed("success, with warnings", new Warning("bad string"), new Warning("not a good string"));

        Assert.assertTrue(stringAttempt.successful());
        Assert.assertTrue(stringAttempt.hasWarnings());
        Assert.assertFalse(stringAttempt.hasErrors());
        Assert.assertNotNull(stringAttempt.warnings());
        Assert.assertEquals(2, stringAttempt.warnings().size());
    }

    @Test
    public void testFailed() {
        final Attempt<String> failed = Attempt.fail();

        // check default state
        Assert.assertTrue(failed.failed());
        Assert.assertFalse(failed.successful());
        Assert.assertFalse(failed.hasErrors()); // a blank failure is still a failure
        Assert.assertNotNull(failed.errors());
        Assert.assertNotNull(failed.warnings());
        Assert.assertEquals(0, failed.errors().size());
        Assert.assertEquals(0, failed.warnings().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testFailedValueAccess() {
        Attempt.fail().get();
    }

    @Test
    public void testFailedWithError() {
        final Attempt<String> failed = Attempt.fail(new Error("bad!"));

        Assert.assertNotNull(failed.errors());
        Assert.assertTrue(failed.hasErrors());
        Assert.assertEquals(1, failed.errors().size());
    }

    @Test
    public void testFailedWithErrorArray() {
        final Attempt<String> failed = Attempt.fail(new Error("bad1"), new Error("bad2"));

        Assert.assertNotNull(failed.errors());
        Assert.assertTrue(failed.hasErrors());
        Assert.assertEquals(2, failed.errors().size());
    }

    @Test
    public void testFailedWithErrorList() {
        final List<Error> errors = new LinkedList<>();
        errors.add(new Error("error1"));
        errors.add(new Error("error2"));
        errors.add(new Error("error3"));

        final Attempt<String> failed = Attempt.fail(errors);
        Assert.assertNotNull(failed.errors());
        Assert.assertFalse(failed.hasWarnings());
        Assert.assertTrue(failed.hasErrors());
        Assert.assertEquals(3, failed.errors().size());
    }

    @Test
    public void testFailedWithErrorListAndWarningList() {
        final List<Error> errors = new LinkedList<>();
        errors.add(new Error("error1"));
        errors.add(new Error("error2"));
        errors.add(new Error("error3"));

        final List<Warning> warnings = new LinkedList<>();
        warnings.add(new Warning("warning1"));
        warnings.add(new Warning("warning2"));
        warnings.add(new Warning("warning3"));
        warnings.add(new Warning("warning4"));

        final Attempt<String> failed = Attempt.fail(errors, warnings);

        Assert.assertNotNull(failed.errors());
        Assert.assertNotNull(failed.warnings());
        Assert.assertTrue(failed.hasErrors());
        Assert.assertTrue(failed.hasWarnings());
        Assert.assertEquals(3, failed.errors().size());
        Assert.assertEquals(4, failed.warnings().size());
    }

    @Test
    public void testFailedWithSingleErrorAndWarningList() {
        final List<Warning> warnings = new LinkedList<>();
        warnings.add(new Warning("warning1"));
        warnings.add(new Warning("warning2"));
        warnings.add(new Warning("warning3"));

        final Attempt<String> failed = Attempt.fail(new Error("error!"), warnings);
        Assert.assertNotNull(failed.errors());
        Assert.assertNotNull(failed.warnings());
        Assert.assertTrue(failed.hasErrors());
        Assert.assertTrue(failed.hasWarnings());
        Assert.assertEquals(1, failed.errors().size());
        Assert.assertEquals(3, failed.warnings().size());
    }

    @Test
    public void testFailedWithDefaultValue() {
        final Attempt<String> stringAttempt = Attempt.succeed("success!");
        final String value = stringAttempt.orRuntimeException();
        Assert.assertEquals("success!", value);

        final Attempt<String> failedStringAttempt = Attempt.fail();
        final String valueFailed = failedStringAttempt.or("default value!");
        Assert.assertEquals("default value!", valueFailed);
    }

    @Test(expected = AttemptFailureException.class)
    public void testFailedWithCheckedException() throws AttemptFailureException {
        final Attempt<String> stringAttempt = Attempt.succeed("success!");
        final String value = stringAttempt.orThrow();
        Assert.assertEquals("success!", value);

        final Attempt<String> failedStringAttempt = Attempt.fail();
        failedStringAttempt.orThrow();
    }

    @Test(expected = RuntimeAttemptFailureException.class)
    public void testFailedWithRuntimeException() throws AttemptFailureException {
        final Attempt<String> stringAttempt = Attempt.succeed("success!");
        final String value = stringAttempt.orThrow();
        Assert.assertEquals("success!", value);

        final Attempt<String> failedStringAttempt = Attempt.fail();
        failedStringAttempt.orRuntimeException();
    }
}
