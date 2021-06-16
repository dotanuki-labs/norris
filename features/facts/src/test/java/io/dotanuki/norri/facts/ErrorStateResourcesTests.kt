package io.dotanuki.norri.facts

import com.google.common.truth.Truth.assertThat
import io.dotanuki.burster.using
import io.dotanuki.norris.domain.errors.NetworkingError.HostUnreachable
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.ClientOrigin
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.RemoteSystem
import io.dotanuki.norris.domain.errors.SearchFactsError.EmptyTerm
import io.dotanuki.norris.domain.errors.SearchFactsError.NoResultsFound
import io.dotanuki.norris.facts.ErrorStateResources
import io.dotanuki.norris.sharedassets.R
import org.junit.Test

class ErrorStateResourcesTests {

    @Test fun `should map resources for an error`() {

        using<Throwable, Int, Int> {

            burst {
                values(RemoteSystem, R.drawable.img_server_down, R.string.error_server_down)
                values(HostUnreachable, R.drawable.img_network_issue, R.string.error_network)
                values(NoResultsFound, R.drawable.img_no_results, R.string.error_no_results)
                values(EmptyTerm, R.drawable.img_bug_found, R.string.error_bug_found)
                values(ClientOrigin, R.drawable.img_bug_found, R.string.error_bug_found)
            }

            thenWith { error, imageResource, messageResource ->
                val expected = ErrorStateResources(error)
                assertThat(expected.image).isEqualTo(imageResource)
                assertThat(expected.message).isEqualTo(messageResource)
            }
        }
    }
}
