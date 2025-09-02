# ZPL Command: ~DU (Download Unbounded TrueType Font)

## Description
Some international fonts, such as Asian fonts, have more than 256 printable characters. These fonts are supported as large TrueType fonts and are downloaded to the printer with the ~DU command. Use ZTools to convert the large TrueType fonts to a Zebra-downloadable format.

The Field Block (^FB) command cannot support the large TrueType fonts.

## Format
```
~DUd:o.x,s,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Font location | R:, E:, B:, and A: | R: |
| **o** | Font name | 1 to 8 alphanumeric characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .FNT (fixed value) | .FNT |
| **s** | Font size | The number of memory bytes required to hold the Zebra-downloadable format of the font | If no data is entered, the command is ignored |
| **data** | Data string | A string of ASCII hexadecimal values (two hexadecimal digits/byte). The total number of two-digit values must match parameter s. | If no data is entered, the command is ignored |

## Examples
This example shows how to download an unbounded TrueType font:

```
~DUR:KANJI,86753,60CA017B0CE7...
(86753 two-digit hexadecimal values)
```

## Important Notes
- This command is specifically designed for international fonts with more than 256 printable characters
- Asian fonts are a primary use case for this command
- Use ZTools to convert large TrueType fonts to Zebra-downloadable format
- The Field Block (^FB) command cannot support large TrueType fonts
- Font files are saved with a .FNT extension
- The font size parameter must accurately reflect the number of bytes in the font data
- Data is provided as ASCII hexadecimal values (two hexadecimal digits per byte)
- The total number of two-digit hexadecimal values must match the font size parameter

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*