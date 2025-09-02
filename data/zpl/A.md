# ZPL Command: ^A (Scalable/Bitmapped Font)

## Description
The ^A command is a scalable/bitmapped font that uses built-in or TrueType® fonts. ^A designates the font for the current ^FD statement or field. The font specified by ^A is used only once for that ^FD entry. If a value for ^A is not specified again, the default ^CF font is used for the next ^FD entry.

## Format
```
^Afo,h,w
```

**Important:** Parameter `f` is required. If `f` is omitted it defaults to the last value of the ^CF command.

## Parameters

| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **f** | Font name | A through Z, and 1 to 9<br>Any font in the printer (downloaded, EPROM, stored fonts) | A |
| **o** | Font orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Last accepted ^FW value or ^FW default |
| **h** | Character height (in dots) | **Scalable:** 10 to 32000<br>**Bitmapped:** multiples of height from 2 to 10 times standard height | **Scalable:** 15 or last ^CF value<br>**Bitmapped:** last ^CF value |
| **w** | Width (in dots) | **Scalable:** 10 to 32000<br>**Bitmapped:** multiples of width from 2 to 10 times standard width | **Scalable:** 12 or last ^CF value<br>**Bitmapped:** last ^CF value |

## Font Examples
Available fonts include:
- **FONT B** - ABCDwxyz 12345
- **FONT D** - ABCDwxyz 12345  
- **FONT E** - (OCR-B) ABCDwxyz 12345
- **FONT F** - ABCDwxyz 12345
- **FONT G** - Az 4
- **FONT H** - (OCR-A) UPPER CASE ONLY
- **FONT 0** - (Scalable) ABCD wxyz 12345
- **FONT P-V** - Various spacing styles: A B C D w x y z 1 2 3 4 5

## Comments
Fonts are built using a matrix that defines standard height-to-width ratios. If you specify only the height or width value, the standard matrix for that font automatically determines the other value. If the value is not given or a 0 (zero) is entered, the height or width is determined by the standard font matrix.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*