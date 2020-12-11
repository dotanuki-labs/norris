
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesTest)
    implementation(Libraries.coroutinesDebug)
    implementation(Libraries.jUnit)
}
