# ZPL Command: ^BC (Code 128 Bar Code - Subsets A, B, and C)

## Description
The ^BC command creates the Code 128 bar code, a high-density, variable length, continuous, alphanumeric symbology. It was designed for complexly encoded product identification.

Code 128 has three subsets of characters. There are 106 encoded printing characters in each set, and each character can have up to three different meanings, depending on the character subset being used. Each Code 128 character consists of six elements: three bars and three spaces.

### Key Features
- Supports a fixed print ratio
- Field data (^FD) is limited to the width (or length, if rotated) of the label
- High-density, variable length symbology
- Three character subsets (A, B, C) with different encoding capabilities

## Format
```
^BCo,h,f,g,e,m
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y = yes<br>N = no | Y |
| **g** | Print interpretation line above code | Y = yes<br>N = no | N |
| **e** | UCC check digit | Y = turns on<br>N = turns off | N |
| **m** | Mode | N = no selected mode<br>U = UCC Case Mode<br>A = Automatic Mode<br>D = New Mode (x.11.x and newer firmware) | N |

## Mode Details

### N - No Selected Mode
Basic mode without special processing.

### U - UCC Case Mode
- More than 19 digits in ^FD or ^SN are eliminated
- Fewer than 19 digits in ^FD or ^SN add zeros to the right to bring count to 19 (produces invalid interpretation line)

### A - Automatic Mode
- Analyzes data sent and automatically determines the best packing method
- Full ASCII character set can be used in ^FD statement
- Printer determines when to shift subsets
- String of four or more numeric digits causes automatic shift to Subset C

### D - New Mode (x.11.x and newer firmware)
- Allows dealing with UCC/EAN with and without chained application identifiers
- Code starts in appropriate subset followed by FNC1 to indicate UCC/EAN 128 bar code
- Printer automatically strips out parentheses and spaces for encoding but prints them in human-readable section
- Printer automatically determines if check digit is required, calculates it, and prints it
- Automatically sizes the human readable

## Code 128 Subsets
The Code 128 character subsets are referred to as Subset A, Subset B, and Subset C. A subset can be selected:
- By including a special Invocation Code in the field data (^FD) string
- By placing the desired Start Code at the beginning of the field data (if no Start Code is entered, Subset B is used)

### Subset Selection
To change subsets within a bar code, place the Invocation Code at appropriate points within the field data (^FD) string. The new subset stays in effect until changed with the Invocation Code.

## Code 128 Invocation Characters

| Invocation Code | Decimal Value | Subset A Character | Subset B Character | Subset C Character |
|-----------------|---------------|-------------------|-------------------|-------------------|
| >< | 62 | > | > | - |
| >0 | 30 | ~ | ~ | USP |
| >= | 94 | DEL | DEL | FNC 3 |
| >1 | 95 | FNC 3 | FNC 3 | FNC 2 |
| >2 | 96 | FNC 2 | FNC 2 | SHIFT |
| >3 | 97 | SHIFT | SHIFT | CODE C |
| >4 | 98 | CODE C | CODE C | CODE B |
| >5 | 99 | FNC 4 | FNC 4 | FNC 4 |
| >6 | 100 | CODE A | CODE A | FNC 1 |
| >7 | 101 | FNC 1 | FNC 1 | - |
| >8 | 102 | - | - | - |
| >9 | 103 | Start Code A | - | - |
| >: | 104 | - | Start Code B | - |
| >; | 105 | - | - | Start Code C |

### Character Set Summary
- **Subset A:** Normal Alpha/Numeric
- **Subset B:** Numeric Pairs give Alpha/Numerics  
- **Subset C:** All numeric (00-99)

## Code 128 Character Sets Table

### Code A and Code B (Values 0-52)
| Value | Code A | Code B | Code C |
|-------|--------|--------|--------|
| 0-31 | Control chars (SP, !, ", #, etc.) | Control chars (SP, !, ", #, etc.) | 00-31 |
| 32-52 | Printable chars (SP to T) | Printable chars (SP to T) | 32-52 |

### Code A and Code B (Values 53-105)  
| Value | Code A | Code B | Code C |
|-------|--------|--------|--------|
| 53-95 | U-Z, [, \, ], ^, _, control chars | U-Z, [, \, ], ^, _, lowercase a-z | 53-95 |
| 96-105 | FNC3, FNC2, SHIFT, Code C, Code B, FNC4, FNC1 | {, \|, }, ~, DEL, FNC3, FNC2, SHIFT, Code C, FNC4, Code B, Code A, FNC1 | 96-105 |

## UCC/EAN-128 Symbology
The symbology specified for Application Identifier data is UCC/EAN-128, a variant of Code 128 exclusively reserved to EAN International and the Uniform Code Council (UCC).

### UCC/EAN-128 Advantages:
- Complete alphanumeric one-dimensional symbology
- Three different character sets (A, B, C) facilitate encoding full 128 ASCII character set
- Compact linear bar code symbology
- Character set C enables numeric data in double density mode
- Concatenated (multiple AIs and fields may be combined into single bar code)
- Very reliable with two independent self-checking features

### UCC/EAN-128 Structure:
- Leading quiet zone
- Code 128 start character (A, B, or C)
- FNC 1 character
- Data (Application Identifier plus data field)
- Symbol check character
- Stop character
- Trailing quiet zone

## UCC/EAN Examples

### Using N Mode
```zpl
^XA
^FO90,200^BY4
^BCN,256,Y,N,Y,N
^FD>;>80012345123451234512^FS
^XZ
```
- `>;>8` sets to subset C, function 1
- `00` is the application identifier followed by 17 characters
- Check digit selected using Y for (e) parameter

### Using U Mode
```zpl
^XA
^FO90,200
^BY4^BC,256,Y,N,,U
^FD0012345123451234512^FS
^XZ
```
- Choosing U selects UCC Case mode
- Exactly 19 characters available in ^FD
- Subset C using FNC1 values automatically selected
- Check digit automatically inserted

### Using D Mode
```zpl
^XA
^FO50,200^BCN,150,Y,N,,D
^FD(00)10084423 7449200940^FS
^XZ
```
- Subset C using FNC1 values automatically selected
- Parentheses and spaces can be in field data
- Check digit automatically calculated and inserted
- Interpretation line shows parentheses and spaces but strips them from actual bar code

## Application Identifier Examples

### Chaining Multiple AIs (Mode A)
```zpl
^XA
^BY2,2.5,193
^FO33,400
^BCN,,N,N,N,A
^FD>;>80204017773003486100008535>8910001>837252^FS
^FT33,625^AEN,0,0^FD(02)04017773003486(10)0008535(91)0001(37)252^FS
^XZ
```

### Chaining Multiple AIs (Mode D)
```zpl
^XA
^PON
^LH0,0
^BY2,2.5,145
^FO218,343
^BCB,,Y,N,N,D
^FD(91)0005886>8(10)0000410549>8(99)05^FS
^XZ
```

## UCC Application Identifier Table (Partial)

| Data Content | AI | Data Structure |
|--------------|----|----|
| Serial Shipping Container Code (SSCC) | 00 | exactly 18 digits |
| Shipping Container Code | 01 | exactly 14 digits |
| Batch Numbers | 10 | up to 20 alpha numerics |
| Production Date (YYMMDD) | 11 | exactly 6 digits |
| Packaging Date (YYMMDD) | 13 | exactly 6 digits |
| Sell By Date (YYMMDD) | 15 | exactly 6 digits |
| Expiration Date (YYMMDD) | 17 | exactly 6 digits |
| Product Variant | 20 | exactly 2 digits |
| Serial Number | 21 | up to 20 alpha numerics |
| Customer PO Number | 400 | up to 29 alpha numerics |
| Ship To Location Code | 410 | exactly 13 digits |
| Bill To Location Code | 411 | exactly 13 digits |

*Note: This is a partial table. For complete information, search the Internet for UCC Application Identifier.*

## Special Notes
- Code 128 Subset B is the most commonly used subset
- ZPL II defaults to Subset B if no start character is specified
- Mod 103 check digit is always present and cannot be turned off
- Mod 10 and 103 appear together when UCC check digit (e) is turned on
- Interpretation line can be printed in any font by placing font command before bar code command
- FNC1 is inserted before AIs so scanners know an AI follows
- When using special characters like ^, >, ~, they must be programmed using invocation codes

## Important Notes
- For additional information about Code 128 bar codes, visit www.aimglobal.org
- The interpretation line prints below the code with UCC check digit turned off
- Good practice to include Invocation Codes in commands even when using defaults
- Nonintegers as first character of digit pair are ignored
- Nonintegers as second character of digit pair invalidate the entire pair
- Extra unpaired digit before code shift is ignored

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*