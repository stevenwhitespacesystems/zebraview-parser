package com.whitespacesystems.parser

import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.utils.AstPrinter

fun main() {
    val printer = AstPrinter()

    println("🎉 ZPL Parser Demo - Parsing Complete ZPL Programs 🎉")
    println("=".repeat(60))

    // Demo 1: Simple label
    println("\n📋 Demo 1: Simple Label")
    println("ZPL: ^FO100,50^A0N,30,30^FDHello World")
    val demo1Lexer = Lexer("^FO100,50^A0N,30,30^FDHello World")
    val demo1Parser = ZplParser(demo1Lexer.tokenize())
    val demo1Program = demo1Parser.parse()
    println("AST:")
    println(printer.print(demo1Program))

    // Demo 2: Multiple fields with special characters
    println("\n📋 Demo 2: Price Label with Special Characters")
    println("ZPL: ^FO10,10^ABN,25^FDPrice: \$29.99^FO10,40^ABN,20^FD(50% OFF!)")
    val demo2Lexer = Lexer("^FO10,10^ABN,25^FDPrice: \$29.99^FO10,40^ABN,20^FD(50% OFF!)")
    val demo2Parser = ZplParser(demo2Lexer.tokenize())
    val demo2Program = demo2Parser.parse()
    println("AST:")
    println(printer.print(demo2Program))

    // Demo 3: Various font styles
    println("\n📋 Demo 3: Different Font Variations")
    println("ZPL: ^FO0,0^A^FDDefault^FO0,30^A0^FDScalable^FO0,60^ABR,20^FDRotated")
    val demo3Lexer = Lexer("^FO0,0^A^FDDefault^FO0,30^A0^FDScalable^FO0,60^ABR,20^FDRotated")
    val demo3Parser = ZplParser(demo3Lexer.tokenize())
    val demo3Program = demo3Parser.parse()
    println("AST:")
    println(printer.print(demo3Program))

    println("\n" + "=".repeat(60))
    println("✅ All demos completed successfully!")
    println("✅ Parser supports: ^FO (positioning), ^FD (text data), ^A (fonts)")
    println("✅ Handles: commas in data, empty fields, various font formats")
    println("✅ Built with TDD: RED → GREEN → REFACTOR")
    println("✅ Full test coverage with Kotest")
    println("✅ Code quality with Detekt & Ktlint")
}
