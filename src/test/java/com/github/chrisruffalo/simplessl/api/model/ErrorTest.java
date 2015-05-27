package com.github.chrisruffalo.simplessl.api.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cruffalo on 3/17/15.
 */
public class ErrorTest {

    @Test(expected = IllegalStateException.class)
    public void testNullMessage() {
        new Error(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyMessage() {
        new Error("");
    }

    @Test
    public void testMessageWithoutThrow() {
        Error error = new Error("error!");

        Assert.assertNotNull(error.message());
        Assert.assertFalse(error.hasCause());
    }

    @Test(expected = IllegalStateException.class)
    public void testMessageWithNullThrow() {
        Error error = new Error("error!", null);

        Assert.assertNotNull(error.message());
        Assert.assertFalse(error.hasCause());

        error.cause();
    }

    @Test
    public void testMessageWithThrowable() {
        Error error = new Error("error!", new IllegalStateException("illegal!"));

        Assert.assertNotNull(error.message());
        Assert.assertTrue(error.hasCause());

        Throwable throwable = error.cause();
        Assert.assertNotNull(throwable);
    }

}
