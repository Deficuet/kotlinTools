package io.github.deficuet.tools.file

import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

fun deleteDirectory(pathString: String) {
    val path = Path(pathString)
    Files.newDirectoryStream(path).use { stream ->
        stream.forEach {
            when {
                it.isDirectory() -> deleteDirectory(it.toString())
                it.isRegularFile() -> it.deleteExisting()
            }
        }
    }
    path.deleteExisting()
}
