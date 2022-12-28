package io.github.deficuet.tools.string

import org.jetbrains.kotlin.gradle.targets.js.toHex
import java.io.PrintWriter
import java.io.StringWriter
import java.security.MessageDigest

fun StringBuilder.pop(): Char {
    if (isEmpty()) throw StringIndexOutOfBoundsException("index $lastIndex, length $length")
    val theLast = this[lastIndex]
    deleteCharAt(lastIndex)
    return theLast
}

fun Throwable.stackTraceString(): String {
    return StringWriter().use { sw ->
        PrintWriter(sw).use { pw ->
            printStackTrace(pw)
        }
        sw.toString()
    }
}

inline fun String.splitPattern(
    regex: Regex,
    matchProcessor: (MatchResult) -> String = { it.value }
): List<String> {
    val results = regex.findAll(this).iterator()
    if (!results.hasNext()) return listOf(this)
    val strList = mutableListOf<String>()
    var start = 0
    while (results.hasNext()) {
        val result = results.next()
        val patternRange = result.range
        val plainRange = start until patternRange.first
        if (!plainRange.isEmpty()) strList.add(this.slice(plainRange))
        strList.add(matchProcessor(result))
        start = patternRange.last + 1
    }
    val tailRange = start until length
    if (!tailRange.isEmpty()) strList.add(this.slice(tailRange))
    return strList
}

fun String.md5(): String {
    return MessageDigest.getInstance("MD5").apply {
        update(toByteArray())
    }.digest().toHex()
}
