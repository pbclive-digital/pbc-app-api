tasks {
    register("clean", Delete::class.java) {
        finalizedBy(gradle.includedBuilds.map{ it.task("clean")})
        doLast { delete(project.layout.buildDirectory.get().asFile) }
    }
}