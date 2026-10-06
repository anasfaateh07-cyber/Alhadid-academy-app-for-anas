package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `verify app name and production build config`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Al Hadid Academy", appName)
    assertEquals("com.alhadid.academy.nasirabad", BuildConfig.APPLICATION_ID)
    assertEquals(11, BuildConfig.VERSION_CODE)
    assertEquals("2.1-Production-Real", BuildConfig.VERSION_NAME)
  }
}
