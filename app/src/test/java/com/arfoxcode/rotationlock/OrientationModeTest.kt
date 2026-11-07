package com.arfoxcode.rotationlock

import android.provider.Settings
import com.arfoxcode.rotationlock.data.OrientationMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OrientationModeTest {

    @Test
    fun `auto rotate mode has null userRotation`() {
        assertNull(OrientationMode.AUTO.userRotation)
    }

    @Test
    fun `auto rotate mode has accelerometerRotation 1`() {
        assertEquals(1, OrientationMode.AUTO.accelerometerRotation)
    }

    @Test
    fun `locked modes have accelerometerRotation 0`() {
        val lockedModes = listOf(
            OrientationMode.PORTRAIT,
            OrientationMode.LANDSCAPE,
            OrientationMode.REVERSE_PORTRAIT,
            OrientationMode.REVERSE_LANDSCAPE
        )
        lockedModes.forEach { mode ->
            assertEquals(
                "Mode $mode should have accelerometerRotation=0",
                0,
                mode.accelerometerRotation
            )
        }
    }

    @Test
    fun `portrait has USER_ROTATION_NATURAL`() {
        assertEquals(Settings.System.USER_ROTATION_NATURAL, OrientationMode.PORTRAIT.userRotation)
    }

    @Test
    fun `landscape has USER_ROTATION_90`() {
        assertEquals(Settings.System.USER_ROTATION_90, OrientationMode.LANDSCAPE.userRotation)
    }

    @Test
    fun `reverse portrait has USER_ROTATION_180`() {
        assertEquals(Settings.System.USER_ROTATION_180, OrientationMode.REVERSE_PORTRAIT.userRotation)
    }

    @Test
    fun `reverse landscape has USER_ROTATION_270`() {
        assertEquals(Settings.System.USER_ROTATION_270, OrientationMode.REVERSE_LANDSCAPE.userRotation)
    }

    @Test
    fun `auto is not locked`() {
        assertFalse(OrientationMode.AUTO.isLocked)
    }

    @Test
    fun `all non-auto modes are locked`() {
        OrientationMode.entries
            .filter { it != OrientationMode.AUTO }
            .forEach { mode ->
                assertTrue("Mode $mode should be locked", mode.isLocked)
            }
    }

    @Test
    fun `all modes have unique ids`() {
        val ids = OrientationMode.entries.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `all modes have non-empty ids`() {
        OrientationMode.entries.forEach { mode ->
            assertTrue("Mode $mode should have non-empty id", mode.id.isNotBlank())
        }
    }

    @Test
    fun `six orientation modes exist`() {
        assertEquals(6, OrientationMode.entries.size)
    }

    @Test
    fun `locked modes have non-null userRotation`() {
        OrientationMode.entries
            .filter { it.isLocked && it != OrientationMode.SENSOR_LANDSCAPE }
            .forEach { mode ->
                assertNotNull("Mode $mode should have non-null userRotation", mode.userRotation)
            }
    }
}
