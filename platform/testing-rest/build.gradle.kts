
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(project(":platform:networking"))
    implementation(project(":platform:rest-chucknorris"))
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("junit:junit:4.13.2")
}
