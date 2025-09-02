# ZPL Command: ~DT (Download TrueType Font)

## Description
Use ZTools to convert a TrueType font to a Zebra-downloadable format. ZTools creates a downloadable file that includes a ~DT command. For information on converting and downloading Intellifont information, see the ~DS command on page 127.

## Format
```
~DTd:o.x,s,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Font location | R:, E:, B:, and A: | R: |
| **o** | Font name | Any valid TrueType name, up to 8 characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .DAT (fixed value) | .DAT |
| **s** | Font size | The number of memory bytes required to hold the Zebra-downloadable format of the font | If an incorrect value or no value is entered, the command is ignored |
| **data** | Data string | A string of ASCII hexadecimal values (two hexadecimal digits/byte). The total number of two-digit values must match parameter s. | If no data is entered, the command is ignored |

## Examples
This example shows how to download a TrueType font:

```
~DTR:FONT,52010,00AF01B0C65E...
(52010 two-digit hexadecimal values)
```

## Important Notes
- Use ZTools to convert TrueType fonts to Zebra-downloadable format
- ZTools creates the downloadable file that includes the ~DT command
- Font files are saved with a .DAT extension
- The font size parameter must accurately reflect the number of bytes in the font data
- Data is provided as ASCII hexadecimal values (two hexadecimal digits per byte)
- The total number of two-digit hexadecimal values must match the font size parameter
- For Intellifont data, use the ~DS command instead

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*