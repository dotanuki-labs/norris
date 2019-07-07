package io.dotanuki.norris.architecture

import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.coroutines.testutils.collectForTesting
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class StateMachineTests {

    private lateinit var machine: StateMachine<String>

    @get:Rule val helper = CoroutinesTestHelper()

    @Before fun `before each test`() {
        machine = StateMachine(
            container = StateContainer.Unbounded(helper.scope),
            executor = TaskExecutor.Synchronous(helper.scope)
        )
    }

    @Test fun `should generate states, successful execution`() {
        runBlocking {

            // Given
            val emissions = machine.states().collectForTesting(helper.scope)

            // When
            machine.forward(::successfulExecution).join()

            // Then
            val expectedStates = listOf(
                FirstLaunch,
                Loading.FromEmpty,
                Success(MESSAGE)
            )

            assertThat(emissions).isEqualTo(expectedStates)
        }
    }

    @Test fun `should generate states, error execution`() {
        runBlocking {

            // Given
            val emissions = machine.states().collectForTesting(helper.scope)

            // When
            machine.forward(::brokenExecution).join()

            // Then
            val expectedStates = listOf(
                FirstLaunch,
                Loading.FromEmpty,
                Failed(ERROR)
            )

            assertThat(emissions).isEqualTo(expectedStates)
        }
    }

    @Test fun `should generate states, with previous execution`() {
        runBlocking {

            // Given
            val emissions = machine.states().collectForTesting(helper.scope)

            // When
            machine.forward(::successfulExecution).join()
            machine.forward(::successfulExecution).join()

            // Then
            val expectedStates = listOf(
                FirstLaunch,
                Loading.FromEmpty,
                Success(MESSAGE),
                Loading.FromPrevious(MESSAGE),
                Success(MESSAGE)
            )

            assertThat(emissions).isEqualTo(expectedStates)
        }
    }

    @Test fun `should generate states, ignoring previous broken execution`() {
        runBlocking {

            // Given
            val emissions = machine.states().collectForTesting(helper.scope)

            // When
            machine.forward(::brokenExecution).join()
            machine.forward(::successfulExecution).join()

            // Then
            val expectedStates = listOf(
                FirstLaunch,
                Loading.FromEmpty,
                Failed(ERROR),
                Loading.FromEmpty,
                Success(MESSAGE)
            )

            assertThat(emissions).isEqualTo(expectedStates)
        }
    }

    private suspend fun successfulExecution(): String {
        return suspendCoroutine { continuation ->
            continuation.resume(MESSAGE)
        }
    }

    private suspend fun brokenExecution(): String {
        return suspendCoroutine { continuation ->
            continuation.resumeWithException(ERROR)
        }
    }

    private companion object {
        const val MESSAGE = "Kotlin is awesome"
        val ERROR = IllegalStateException("Ouch")
    }
}