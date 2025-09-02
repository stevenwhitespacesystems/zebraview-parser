# ZPL Command: ^FV (Field Variable)

## Description
^FV replaces the ^FD (field data) command in a label format when the field is variable.

## Format
```
^FVa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Variable field data to be printed | 0 to 3072 character string | if no data is entered, the command is ignored |

## Examples
Using variable fields with ^MC command:
```zpl
^XA
^FO40,40
^GB300,203,8^FS
^FO55,60^CF0,25
^FVVARIABLE DATA #1^FS
^FO80,150
^FDFIXED DATA^FS
^MCN
^XZ

^XA
^FO55,60^CF0,25
^FVVARIABLE DATA #2^FS
^MCY
^XZ
```

## Important Notes
- ^FV fields are always cleared after the label is printed
- ^FD fields are not cleared
- Used for data that changes between labels
- Can hold up to 3072 characters

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*