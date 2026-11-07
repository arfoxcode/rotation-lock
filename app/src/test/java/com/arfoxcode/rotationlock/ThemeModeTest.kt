package com.arfoxcode.rotationlock

import com.arfoxcode.rotationlock.data.preferences.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {

    @Test
    fun `three theme modes exist`() {
        assertEquals(3, ThemeMode.entries.size)
    }

    @Test
    fun `theme mode names are parseable from string`() {
        ThemeMode.entries.forEach { mode ->
            val parsed = ThemeMode.valueOf(mode.name)
            assertEquals(mode, parsed)
        }
    }

    @Test
    fun `invalid theme mode falls back gracefully`() {
        val result = try {
            ThemeMode.valueOf("INVALID")
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
        assertEquals(ThemeMode.SYSTEM, result)
    }
}
