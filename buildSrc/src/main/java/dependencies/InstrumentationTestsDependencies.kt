package dependencies

class InstrumentationTestsDependencies {

    private val all by lazy {
        listOf(
            Libraries.jUnit,
            Libraries.espressoCore,
            Libraries.androidTestCore,
            Libraries.androidTestCoreKtx,
            Libraries.androidTestExtJunit,
            Libraries.androidTestExtJunitKtx,
            Libraries.androidTestRunner,
            Libraries.androidTestRules
        )
    }

    fun forEachDependency(consumer: (String) -> Unit) =
        all.forEach { consumer.invoke(it) }

    companion object {
        fun instrumentationTest(block: InstrumentationTestsDependencies.() -> Unit) =
            InstrumentationTestsDependencies().apply(block)
    }
}