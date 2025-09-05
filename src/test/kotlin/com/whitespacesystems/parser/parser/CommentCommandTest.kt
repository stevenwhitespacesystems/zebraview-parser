package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * Comprehensive tests for the FX (Comment) command parsing.
 * 
 * These tests are written in the RED phase of TDD - they WILL FAIL initially
 * until the CommentCommand implementation is created.
 * 
 * The FX command syntax is: ^FXc where c is comment text until next delimiter
 */
class CommentCommandTest : StringSpec({

    // ========== Basic Functionality Tests ==========
    
    "should parse minimal FX comment command with empty text" {
        val lexer = Lexer("^FX^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe ""
    }

    "should parse FX comment command with simple text" {
        val lexer = Lexer("^FXThis is a simple comment^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "This is a simple comment"
    }

    "should parse FX comment command with single word" {
        val lexer = Lexer("^FXComment^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Comment"
    }

    "should parse FX comment command with numeric text" {
        val lexer = Lexer("^FX123456789^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "123456789"
    }

    // ========== Whitespace and Special Character Tests ==========

    "should preserve leading whitespace in comment text" {
        val lexer = Lexer("^FX   Leading spaces comment^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "   Leading spaces comment"
    }

    "should preserve trailing whitespace in comment text" {
        val lexer = Lexer("^FXTrailing spaces comment   ^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Trailing spaces comment   "
    }

    "should preserve internal whitespace in comment text" {
        val lexer = Lexer("^FXComment with   multiple   spaces^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Comment with   multiple   spaces"
    }

    "should preserve tabs in comment text" {
        val lexer = Lexer("^FXComment\twith\ttabs\there^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Comment\twith\ttabs\there"
    }

    "should handle comment with special characters" {
        val lexer = Lexer("^FXSpecial chars: !@#$%&*()_+-=[]{}|;:,.<>?^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Special chars: !@#$%&*()_+-=[]{}|;:,.<>?"
    }

    "should handle comment with quotes" {
        val lexer = Lexer("^FXComment with \"double quotes\" and 'single quotes'^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Comment with \"double quotes\" and 'single quotes'"
    }

    "should handle comment with backslashes" {
        val lexer = Lexer("^FXPath\\to\\file\\or\\backslashes^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Path\\to\\file\\or\\backslashes"
    }

    // ========== Command Delimiter Tests ==========

    "should stop parsing comment text at caret delimiter" {
        val lexer = Lexer("^FXComment text here^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val commentCommand = program.commands[0]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "Comment text here"
    }

    "should stop parsing comment text at tilde delimiter" {
        val lexer = Lexer("^FXComment text here~GA")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val commentCommand = program.commands[0]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "Comment text here"
    }

    "should handle multiple consecutive carets in comment text correctly" {
        val lexer = Lexer("^FXThis has ^^ double carets but continues^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "This has ^^ double carets but continues"
    }

    // ========== Edge Cases ==========

    "should handle very long comment text" {
        val longText = "This is a very long comment text that goes on and on ".repeat(20)
        val lexer = Lexer("^FX${longText}^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe longText
    }

    "should handle comment with only whitespace" {
        val lexer = Lexer("^FX     ^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "     "
    }

    "should handle comment with single character" {
        val lexer = Lexer("^FXA^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "A"
    }

    "should handle comment text ending with various punctuation" {
        val lexer = Lexer("^FXComment ending with period.^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Comment ending with period."
    }

    // ========== Integration Tests ==========

    "should parse FX command in sequence with other commands" {
        val lexer = Lexer("^XA^FXThis is a comment^FO10,20^FS^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 4
        val commentCommand = program.commands[1]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "This is a comment"
    }

    "should parse multiple FX commands in sequence" {
        val lexer = Lexer("^FXFirst comment^FXSecond comment^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val firstComment = program.commands[0]
        val secondComment = program.commands[1]
        
        firstComment.shouldBeInstanceOf<CommentCommand>()
        secondComment.shouldBeInstanceOf<CommentCommand>()
        
        (firstComment as CommentCommand).text shouldBe "First comment"
        (secondComment as CommentCommand).text shouldBe "Second comment"
    }

    "should parse FX command at beginning of format" {
        val lexer = Lexer("^XA^FXHeader comment for this label^FO10,10^FDHello^FS^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 5
        val commentCommand = program.commands[1]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "Header comment for this label"
    }

    "should parse FX command at end of format" {
        val lexer = Lexer("^XA^FO10,10^FDHello^FS^FXFooter comment^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 5
        val commentCommand = program.commands[3]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "Footer comment"
    }

    // ========== Real-World Usage Tests ==========

    "should parse comment describing label purpose" {
        val lexer = Lexer("^FXShipping label for order #12345 - customer: John Doe^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Shipping label for order #12345 - customer: John Doe"
    }

    "should parse comment with version information" {
        val lexer = Lexer("^FXGenerated by LabelSystem v2.1.0 on 2023-09-05^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Generated by LabelSystem v2.1.0 on 2023-09-05"
    }

    "should parse comment with field descriptions" {
        val lexer = Lexer("^FXFO commands define field positions: X=10, Y=20^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "FO commands define field positions: X=10, Y=20"
    }

    "should parse comment with debugging information" {
        val lexer = Lexer("^FXDEBUG: Processing batch #001, item count: 150^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "DEBUG: Processing batch #001, item count: 150"
    }

    // ========== Error Scenario Tests ==========

    "should throw ParseException for incomplete FX command at end of input" {
        val lexer = Lexer("^FX")
        val parser = ZplParser(lexer.tokenize())
        
        val exception = shouldThrow<ParseException> {
            parser.parse()
        }
        exception.message shouldContain "Unexpected end of input"
    }

    "should throw ParseException for FX command without proper termination" {
        val lexer = Lexer("^FXIncomplete comment without delimiter")
        val parser = ZplParser(lexer.tokenize())
        
        val exception = shouldThrow<ParseException> {
            parser.parse()
        }
        exception.message shouldContain "expected text or command delimiter"
    }

    "should throw ParseException for malformed FX command structure" {
        val lexer = Lexer("^F")  // Incomplete command identifier
        val parser = ZplParser(lexer.tokenize())
        
        val exception = shouldThrow<ParseException> {
            parser.parse()
        }
        exception.message shouldContain "Unknown command"
    }

    // ========== Performance and Memory Tests ==========

    "should handle extremely long comment efficiently" {
        val massiveText = "Large comment text ".repeat(1000) // 20,000 characters
        val lexer = Lexer("^FX${massiveText}^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe massiveText
    }

    "should parse comment with international characters" {
        val lexer = Lexer("^FXInternational: café, naïve, piñata, résumé^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "International: café, naïve, piñata, résumé"
    }

    "should parse comment with emoji and unicode characters" {
        val lexer = Lexer("^FXUnicode test: 🚀 ★ ♥ ⚡ 中文 العربية^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe "Unicode test: 🚀 ★ ♥ ⚡ 中文 العربية"
    }

    // ========== Boundary Condition Tests ==========

    "should handle comment immediately followed by another command" {
        val lexer = Lexer("^FXComment^XA")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val commentCommand = program.commands[0]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "Comment"
    }

    "should handle comment with maximum realistic length" {
        val realWorldComment = "Label Template v3.2 | Customer: ACME Corp | Order: SO-2023-0905-001 | " +
                "Generated: 2023-09-05T15:30:00Z | Items: Widget A (Qty: 50), Widget B (Qty: 25) | " +
                "Shipping Address: 123 Main St, Anytown, ST 12345 | Special Instructions: Handle with care"
        val lexer = Lexer("^FX${realWorldComment}^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        (command as CommentCommand).text shouldBe realWorldComment
    }
})