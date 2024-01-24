package io.dotanuki.platform.android.testing.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TestNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "Test Navigation Screen launched with success", Toast.LENGTH_LONG).show()
        finish()
    }
}
