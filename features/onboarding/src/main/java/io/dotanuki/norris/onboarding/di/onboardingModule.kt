package io.dotanuki.norris.onboarding.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.onboarding.OnboardingViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val onboardingModule = DI.Module("onboarding") {

    bind() from provider {
        @Suppress("UNCHECKED_CAST") val factory = object : ViewModelProvider.Factory {

            val usecase = FetchCategories(
                categoriesCache = instance(),
                remoteFacts = instance()
            )

            override fun <VM : ViewModel> create(klass: Class<VM>) =
                OnboardingViewModel(usecase) as VM
        }

        val host: FragmentActivity = instance(KodeinTags.hostActivity)
        ViewModelProvider(host, factory).get(OnboardingViewModel::class.java)
    }
}
