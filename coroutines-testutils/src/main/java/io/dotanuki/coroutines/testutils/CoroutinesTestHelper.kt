package io.dotanuki.coroutines.testutils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.ExternalResource

class CoroutinesTestHelper : ExternalResource() {

    val scope = TestCoroutineScope()
    private val singleThread = newSingleThreadContext("Testing thread")

    override fun before() {
        Dispatchers.setMain(singleThread)
        super.before()
    }

    override fun after() {
        Dispatchers.resetMain()
        singleThread.close()
        scope.cleanupTestCoroutines()
        super.after()
    }

    companion object {
        fun runWithTestScope(
            scope: TestCoroutineScope,
            block: suspend CoroutineScope.() -> Unit
        ) {
            scope.runBlockingTest { block.invoke(this) }
        }
    }
}