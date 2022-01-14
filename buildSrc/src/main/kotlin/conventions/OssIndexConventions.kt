package conventions

import org.gradle.api.Project

private val vulnerabilitiesTracking = mapOf(
    "7ea56ad4-8a8b-4e51-8ed9-5aad83d8efb1" to "Affecst junit4 for temp folder, which we dont use",
    "281b25c5-5aea-41ab-97bd-8065e85f430b" to "Ant is not used in this project",
    "0ad53e01-ee26-4fb6-9178-dc45c1fcea32" to "Ant is not used in this project",
    "9e56f765-fe13-4d65-925a-241a8047f1b6" to "BouncyCastle not used in the production artefact",
    "42765c46-9e37-43e0-8386-34f61a348df5" to "BouncyCastle not used in the production artefact",
    "fff4d007-8be8-4150-8213-7892b22103fb" to "BouncyCastle not used in the production artefact",
    "fd2210b6-0f22-45aa-aab6-1b9013f32653" to "BouncyCastle not used in the production artefact",
    "c0ed9602-d5c5-4c45-af48-c757161879ee" to "Apache HTTP Client not used in the production artefact",
    "8ea14e38-e6cc-48d9-bfe4-ec89f93596e7" to "Apache Commons Compress not used in the production artefact",
    "7a6a9dd2-67de-4e2a-b406-7aa4a4ce29cc" to "Apache Commons Compress not used in the production artefact",
    "69b8043a-3002-48fa-9762-8f6040d83de1" to "Apache Commons Compress not used in the production artefact",
    "68232267-bb25-4b04-8dec-caf7c11c7293" to "Apache Commons Compress not used in the production artefact",
    "5dbdb043-212c-4971-9653-d04e1cfc5080" to "Jsoup not used in the production artefact",
    "82848549-29bd-4594-b983-e61e4b2c6924" to "Android Lint not invoked in this project",
)

fun Project.ignoredVulnerabilities(): Set<String> =
    vulnerabilitiesTracking.let {
        logger.lifecycle("This project deliberately ignores CVEs for some of its transistive dependencies")
        it.entries
            .onEach { (id, reason) -> logger.info(formattedMessage(id, reason)) }
            .map { entry -> entry.key }
            .toSet()
    }

private fun formattedMessage(vulnerabilityId: String, reason: String): String =
    """
    Ignoring : https://ossindex.sonatype.org/vulnerability/$vulnerabilityId
    Reason : $reason
    
    """.trimIndent()
