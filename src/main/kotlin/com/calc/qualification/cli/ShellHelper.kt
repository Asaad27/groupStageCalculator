package com.calc.qualification.cli

import org.jline.terminal.Terminal
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.springframework.beans.factory.annotation.Value

class ShellHelper(private val terminal: Terminal) {
    @Value("\${shell.out.info}")
    var infoColor: String? = null

    @Value("\${shell.out.success}")
    var successColor: String? = null

    @Value("\${shell.out.warning}")
    var warningColor: String? = null

    @Value("\${shell.out.error}")
    var errorColor: String? = null

    fun getColored(message: String?, color: PromptColor): String {
        return AttributedStringBuilder().append(
            message,
            AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle())
        ).toAnsi()
    }

    fun getInfoMessage(message: String?): String {
        return getColored(message, PromptColor.valueOf(infoColor!!))
    }

    fun getSuccessMessage(message: String?): String {
        return getColored(message, PromptColor.valueOf(successColor!!))
    }

    fun getWarningMessage(message: String?): String {
        return getColored(message, PromptColor.valueOf(warningColor!!))
    }

    fun getErrorMessage(message: String?): String {
        return getColored(message, PromptColor.valueOf(errorColor!!))
    }
}