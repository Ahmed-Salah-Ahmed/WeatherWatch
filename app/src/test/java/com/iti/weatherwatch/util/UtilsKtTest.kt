package com.iti.weatherwatch.util

import junit.framework.TestCase
import org.junit.Test
import java.util.*

class UtilsKtTest : TestCase() {
    @Test
    fun convertLongToDay_long_returnDayAsString() {
        assertEquals("Thursday", convertCalenderToDayString(Calendar.getInstance()))
    }
}
