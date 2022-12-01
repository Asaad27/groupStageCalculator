package com.calc.qualification.cli

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class PromptProvider : PromptProvider {
    override fun getPrompt(): AttributedString {
        return AttributedString("WC-22:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
    }

}