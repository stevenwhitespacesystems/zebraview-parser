# ZPL Command: ^WD (Print Directory Label)

## Description
The ^WD command is used to print a label listing bar codes, objects stored in DRAM, or fonts. For bar codes, the list shows the name of the bar code. For fonts, the list shows the name of the font, the number to use with ^Af command, and size. For objects stored in DRAM, the list shows the name of the object, extension, size, and option flags. All lists are enclosed in a double-line box.

## Format
```
~WDd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Source device (optional) | R:, E:, B:, A:, and Z: | R: |
| **o** | Object name (optional) | 1 to 8 alphanumeric characters | * |
| **x** | Extension (optional) | .FNT = font<br>.BAR = bar code<br>.ZPL = stored ZPL format<br>.GRF = GRF graphic<br>.CO = memory cache<br>.DAT = font encoding<br>.STO = data storage<br>.PNG = PNG graphic<br>* = all objects | * |

## Examples
```zpl
// Print a label listing all objects in DRAM
^XA
^WDR:*.*
^XZ

// Print a label listing all resident bar codes
^XA
^WDZ:*.BAR
^XZ

// Print a label listing all resident fonts
^XA
^WDZ:*.FNT
^XZ
```

## Important Notes
- Question mark (?) can be used as a wildcard character for object names and extensions
- Different file extensions provide different types of information:
  - Fonts: show name, ^A command number, and size
  - Bar codes: show name only
  - Objects: show name, extension, size, and option flags
- All directory listings are enclosed in a double-line box for easy reading
- Useful for inventory management and troubleshooting storage contents

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*