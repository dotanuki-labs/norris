package io.dotanuki.coroutines.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.ExternalResource

class CoroutinesTestHelper : ExternalResource() {

    private val singleThread = newSingleThreadContext("Testing thread")
    val testScope = TestCoroutineScope()

    override fun before() {
        Dispatchers.setMain(singleThread)
        super.before()
    }

    override fun after() {
        Dispatchers.resetMain()
        singleThread.close()
        testScope.cleanupTestCoroutines()
        super.after()
    }
}