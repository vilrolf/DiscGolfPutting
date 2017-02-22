package com.myApp.vilrolf.discgolfputting;

import com.myApp.vilrolf.discgolfputting.Utils.DateUtil;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void dateIsCorrect() throws Exception {
        String dateString = "2017-02-19 17:14:25";
        Calendar calendar = DateUtil.stringToCal(dateString);
        assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH));



    }

}