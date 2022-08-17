package io.dotanuki.testing.truth

import com.google.common.truth.Fact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject

class ExpectingErrorSubject(
    metadata: FailureMetadata?,
    private val actual: Throwable?,
    private val execution: () -> Any
) : Subject(metadata, actual) {

    fun hasBeingThrown() {
        try {
            execution()
            failWithActual(Fact.simpleFact("Execution ran without error"))
        } catch (error: Throwable) {
            if (error != actual) {
                failWithActual(Fact.fact("Actual error does not match expectation", error))
            }
        }
    }
}
