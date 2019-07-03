package dependencies

class InstrumentationTestsDependencies {

    private val all by lazy {
        listOf(
            Libraries.jUnit,
            Libraries.assertj,
            Libraries.mockitoKotlin,
            Libraries.mockitoDexMaker,
            Libraries.espressoCore,
            Libraries.barista,
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