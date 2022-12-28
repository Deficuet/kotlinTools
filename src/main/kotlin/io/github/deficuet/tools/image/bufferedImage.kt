package io.github.deficuet.tools.image

import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image as ImageFX
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

inline fun BufferedImage(width: Int, height: Int, type: Int, initializer: Graphics2D.() -> Unit): BufferedImage {
    return BufferedImage(width, height, type).apply {
        with(createGraphics()) {
            setRenderingHints(mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_ALPHA_INTERPOLATION to RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
            ))
            initializer()
            dispose()
        }
    }
}

inline fun BufferedImage.edit(process: Graphics2D.() -> Unit) = apply {
    with(createGraphics()) {
        setRenderingHints(mapOf(
            RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
            RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY,
            RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
            RenderingHints.KEY_ALPHA_INTERPOLATION to RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
        ))
        process()
        dispose()
    }
}

fun BufferedImage.flipY(): BufferedImage {
    return AffineTransformOp(
        AffineTransform.getScaleInstance(1.0, -1.0).apply {
            translate(0.0, -height.toDouble())
        },
        AffineTransformOp.TYPE_BICUBIC
    ).filter(this, null)
}

fun BufferedImage.resize(w: Int, h: Int) = BufferedImage(w, h, type) {
    drawImage(this@resize, 0, 0, w, h, null)
}

fun BufferedImage.copy() = BufferedImage(colorModel, copyData(null), isAlphaPremultiplied, null)

fun BufferedImage.paste(other: Image, x: Int, y: Int) = edit {
    drawImage(other, x, y, null)
}

fun BufferedImage.toFXImage(): ImageFX = SwingFXUtils.toFXImage(this, null)

fun BufferedImage.toByteArray(format: String): ByteArray {
    return ByteArrayOutputStream().use {
        ImageIO.write(this, format, it)
        it.toByteArray()
    }
}
