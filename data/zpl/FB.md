# ZPL Command: ^FB (Field Block)

## Description
The ^FB command allows you to print text into a defined block type format. This command formats an ^FD or ^SN string into a block of text using the origin, font, and rotation specified for the text string. The ^FB command also contains an automatic word-wrap function.

## Format
```
^FBa,b,c,d,e
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Width of text block line (in dots) | 0 to the width of the label (or 9999) | 0 |
| **b** | Maximum number of lines in text block | 1 to 9999 | 1 |
| **c** | Add or delete space between lines (in dots) | -9999 to 9999 | 0 |
| **d** | Text justification | L (left), C (center), R (right), J (justified) | L |
| **e** | Hanging indent (in dots) of the second and remaining lines | 0 to 9999 | 0 |

## Examples
These are examples of how the ^FB command affects field data. *(Note: Specific code examples were not provided in the original source text)*

## Special Function Schemes
The following schemes can be used to facilitate special functions:
- **\\&** = carriage return/line feed
- **\\(*)** = soft hyphen (word break with a dash)
- **\\\\** = backslash (\\)

Where (*) = any alphanumeric character

## Important Notes
- If the value for parameter a is less than font width or not specified, text does not print
- Text exceeding the maximum number of lines overwrites the last line
- Changing the font size automatically increases or decreases the size of the block
- Numbers for parameter c are considered positive unless preceded by a minus sign. Positive values add space; negative values delete space
- Last line is left-justified if J (justified) is used
- ^CI13 must be selected to print a backslash (\\)
- If a soft hyphen is placed near the end of a line, the hyphen is printed. If it is not placed near the end of the line, it is ignored
- If a word is too long to print on one line by itself (and no soft hyphen is specified), a hyphen is automatically placed in the word at the right edge of the block
- The position of the hyphen depends on word length, not a syllable boundary. Use a soft hyphen within a word to control where the hyphenation occurs
- Maximum data-string length is 3K, including control characters, carriage returns, and line feeds
- Normal carriage returns, line feeds, and word spaces at line breaks are discarded
- When using ^FT (Field Typeset), ^FT uses the baseline origin of the last possible line of text. Increasing the font size causes the text block to increase in size from bottom to top. This could cause a label to print past its top margin
- When using ^FO (Field Origin), increasing the font size causes the text block to increase in size from top to bottom
- If ^SN is used instead of ^FD, the field does not print
- ^FS terminates an ^FB command. Each block requires its own ^FB command

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*