package com.arfoxcode.rotationlock

import com.arfoxcode.rotationlock.data.OrientationMode
import com.arfoxcode.rotationlock.data.RotationState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RotationStateTest {

    @Test
    fun `default state has auto mode`() {
        val state = RotationState()
        assert(state.activeMode == OrientationMode.AUTO)
    }

    @Test
    fun `default state is not locked`() {
        val state = RotationState()
        assertFalse(state.isLocked)
    }

    @Test
    fun `default state has no permission`() {
        val state = RotationState()
        assertFalse(state.hasWriteSettingsPermission)
    }

    @Test
    fun `default state has no error`() {
        val state = RotationState()
        assertNull(state.errorMessage)
    }

    @Test
    fun `portrait mode is locked`() {
        val state = RotationState(activeMode = OrientationMode.PORTRAIT)
        assertTrue(state.isLocked)
    }

    @Test
    fun `landscape mode is locked`() {
        val state = RotationState(activeMode = OrientationMode.LANDSCAPE)
        assertTrue(state.isLocked)
    }

    @Test
    fun `auto mode is not locked`() {
        val state = RotationState(activeMode = OrientationMode.AUTO)
        assertFalse(state.isLocked)
    }

    @Test
    fun `state copy preserves fields correctly`() {
        val original = RotationState(
            activeMode = OrientationMode.PORTRAIT,
            hasWriteSettingsPermission = true,
            isLoading = false,
            errorMessage = null
        )
        val updated = original.copy(isLoading = true, errorMessage = "Error")
        assert(updated.activeMode == OrientationMode.PORTRAIT)
        assertTrue(updated.hasWriteSettingsPermission)
        assertTrue(updated.isLoading)
        assert(updated.errorMessage == "Error")
    }
}
