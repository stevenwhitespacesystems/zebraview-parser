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

    // Demo 4: NEW! Complete ZPL Label with ^XA/^XZ boundaries
    println("\n📋 Demo 4: Complete ZPL Label with ^XA/^XZ Format Commands")
    println("ZPL: ^XA^FO100,50^A0N,30^FDComplete Label^XZ")
    val demo4Lexer = Lexer("^XA^FO100,50^A0N,30^FDComplete Label^XZ")
    val demo4Parser = ZplParser(demo4Lexer.tokenize())
    val demo4Program = demo4Parser.parse()
    println("AST:")
    println(printer.print(demo4Program))

    // Demo 5: Real-world product label with boundaries
    println("\n📋 Demo 5: Real-World Product Label with Format Boundaries")
    println("ZPL: ^XA^FO300,30^A0N,30^FDProduct Label^FO20,100^A0N,25^FDSKU: 123456^XZ")
    val demo5Lexer = Lexer("^XA^FO300,30^A0N,30^FDProduct Label^FO20,100^A0N,25^FDSKU: 123456^XZ")
    val demo5Parser = ZplParser(demo5Lexer.tokenize())
    val demo5Program = demo5Parser.parse()
    println("AST:")
    println(printer.print(demo5Program))

    // Demo 6: Multiple complete labels  
    println("\n📋 Demo 6: Multiple Complete ZPL Labels")
    println("ZPL: ^XA^FO10,10^FDFirst Label^XZ^XA^FO20,20^FDSecond Label^XZ")
    val demo6Lexer = Lexer("^XA^FO10,10^FDFirst Label^XZ^XA^FO20,20^FDSecond Label^XZ")
    val demo6Parser = ZplParser(demo6Lexer.tokenize())
    val demo6Program = demo6Parser.parse()
    println("AST:")
    println(printer.print(demo6Program))

    println("\n" + "=".repeat(60))
    println("✅ All demos completed successfully!")
    println("✅ Parser supports: ^XA/^XZ (format boundaries), ^FO (positioning), ^FD (text data), ^A (fonts)")
    println("✅ Handles: complete ZPL II labels, commas in data, empty fields, various font formats")
    println("✅ Built with TDD: RED → GREEN → REFACTOR")
    println("✅ Full test coverage with Kotest")
    println("✅ Code quality with Detekt & Ktlint")
    println("✅ NEW: Support for industry-standard ZPL II format boundaries!")
}
