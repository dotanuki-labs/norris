import java.io.File

object BuildSrcBuildScriptBridge {

    private val finder = "dependabot\\(.+\\)\\s+\\{.+}".toRegex()
    private val sanitizer = "dependabot\\(|\\)|\\s+\\{|\"|}".toRegex()

    fun extractDependencies(): Map<String, String> =
        File("buildSrc/build.gradle.kts").readText().trimIndent().let { contents ->
            finder.findAll(contents).map { it.parseDependabotMatch() }.toMap()
        }

    private fun MatchResult.parseDependabotMatch() =
        value.replace(sanitizer, "").split(" ").let { (coordinate, alias) -> alias to coordinate }
}
