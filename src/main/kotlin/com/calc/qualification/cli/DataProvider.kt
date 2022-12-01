package com.calc.qualification.cli

import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

abstract class DataProvider : ValueProvider {

    protected abstract var choices: List<String>
    override fun complete(completionContext: CompletionContext?): MutableList<CompletionProposal> {
        val currentInput = completionContext!!.currentWordUpToCursor()

        return choices
            .filter { it.contains(currentInput, ignoreCase = true) }
            .map { CompletionProposal(it) }
            .toMutableList()
    }
}

@Component
class TeamValueProvider : DataProvider() {
    override var choices: List<String> = Data.teamNames
}

@Component
class GroupValueProvider : DataProvider() {
    override var choices: List<String> = Data.groupNames

}