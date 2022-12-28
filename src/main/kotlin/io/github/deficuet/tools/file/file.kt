package io.github.deficuet.tools.file

import java.nio.file.Files
import java.nio.file.Path

fun deleteDirectory(pathString: String) {
    val path = Path.of(pathString)
    Files.newDirectoryStream(path).use { stream ->
        stream.forEach {
            when {
                Files.isDirectory(it) -> deleteDirectory(it.toString())
                Files.isRegularFile(it) -> Files.delete(it)
            }
        }
    }
    Files.delete(path)
}
