package com.calc.qualification

import org.jline.terminal.Terminal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class SpringShellConfig {

    @Bean
    fun shellHelper(@Lazy terminal: Terminal) = ShellHelper(terminal)
}