# ZPL Command: ^BA (Code 93 Bar Code)

## Description
The ^BA command creates a variable length, continuous symbology. Code 93 bar code is used in many of the same applications as Code 39. It uses the full 128-character ASCII set. Each character in the Code 93 bar code is composed of six elements: three bars and three spaces.

### Key Features
- Supports a fixed print ratio
- Field data (^FD) is limited to the width (or length, if rotated) of the label
- Uses the full 128-character ASCII set
- Variable length, continuous symbology

## Format
```
^BAo,h,f,g,e
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y = yes<br>N = no | Y |
| **g** | Print interpretation line above code | Y = yes<br>N = no | N |
| **e** | Print check digit | Y = yes<br>N = no | N |

## ZPL II Substitute Characters for Control Codes
ZPL II does not support ASCII control codes or escape sequences directly. It uses these substitute characters:

| Control Code | ZPL II Substitute |
|--------------|------------------|
| Ctrl $ | & |
| Ctrl % | ' |
| Ctrl / | ( |
| Ctrl + | ) |

**Note:** All control codes are used in pairs. Although invoked differently, the human-readable interpretation line prints as though the control code has been used.

## Full ASCII Mode for Code 93
Code 93 can generate the full 128-character ASCII set using paired characters. Here are key examples:

### Control Characters (ASCII 0-31)
| ASCII | Code 93 | ASCII | Code 93 | ASCII | Code 93 | ASCII | Code 93 |
|-------|---------|-------|---------|-------|---------|-------|---------|
| NUL | 'U | SOH | &A | STX | &B | ETX | &C |
| EOT | &D | ENQ | &E | ACK | &F | BEL | &G |
| BS | &H | HT | &I | LF | &J | VT | &K |
| FF | &L | CR | &M | SO | &N | SI | &O |

### Printable Characters (ASCII 32-95)
| ASCII | Code 93 | ASCII | Code 93 | ASCII | Code 93 | ASCII | Code 93 |
|-------|---------|-------|---------|-------|---------|-------|---------|
| SP | Space | ! | (A | " | (B | # | (C |
| $ | (D | % | (E | & | (F | ' | (G |
| ( | (H | ) | (I | * | (J | + | + |
| , | (L | - | . | . | . | / | / |
| 0-9 | 0-9 | : | (Z | ; | 'F | < | 'G |
| = | 'H | > | 'I | ? | 'J | @ | 'V |
| A-Z | A-Z | [ | 'K | \ | 'L | ] | 'M |
| ^ | 'N | _ | 'O |  |  |  |  |

### Lowercase and Extended Characters (ASCII 96-127)
| ASCII | Code 93 | ASCII | Code 93 | ASCII | Code 93 | ASCII | Code 93 |
|-------|---------|-------|---------|-------|---------|-------|---------|
| ` | 'W | a | )A | b | )B | c | )C |
| d | )D | e | )E | f | )F | g | )G |
| h | )H | i | )I | j | )J | k | )K |
| l | )L | m | )M | n | )N | o | )O |
| p | )P | q | )Q | r | )R | s | )S |
| t | )T | u | )U | v | )V | w | )W |
| x | )X | y | )Y | z | )Z | { | 'P |
| \| | 'Q | } | 'R | ~ | 'S | DEL | 'T |

## Important Notes
- All control codes are used in pairs
- Human-readable interpretation line displays as though actual control codes were used
- Field data is limited by label width (or length if rotated)
- Code 93 is suitable for applications requiring the full ASCII character set
- For additional information about Code 93 bar codes, visit www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*