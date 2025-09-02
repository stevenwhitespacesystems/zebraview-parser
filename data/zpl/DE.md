# ZPL Command: ~DE (Download Encoding)

## Description
The standard encoding for TrueType Windows® fonts is always Unicode. The ZPL II field data must be converted from some other encoding to Unicode that the Zebra printer understands. The required translation tables are provided with ZTools and downloaded with the ~DE command.

## Format
```
~DEd:o.x,s,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of table | R:, E:, B:, and A: | R: |
| **o** | Name of table | Any valid name, up to 8 characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .DAT (fixed value) | .DAT |
| **s** | Table size | The number of memory bytes required to hold the Zebra downloadable format of the font | If an incorrect value or no value is entered, the command is ignored |
| **data** | Data string | A string of ASCII hexadecimal values | If no data is entered, the command is ignored |

## Examples
This example shows how to download the required translation table:

```
~DER:JIS.DAT,27848,300021213001...
(27848 two-digit hexadecimal values)
```

## Important Notes
- This command is used to download encoding translation tables for TrueType fonts
- Translation tables convert field data from other encodings to Unicode for Zebra printer use
- Required translation tables are provided with ZTools software
- The table size parameter must accurately reflect the number of bytes in the encoding table
- Data is provided as ASCII hexadecimal values
- Files are saved with a .DAT extension
- For more information on ZTools, see the program documentation included with the software

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*