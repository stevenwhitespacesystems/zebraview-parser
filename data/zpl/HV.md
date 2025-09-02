# ZPL Command: ^HV (Host Verification)

## Description
This command is used to return data from specified fields, along with an optional ASCII header, to the host computer. The command can be used with any field that has been assigned a number with the ^RT command or the ^FN and ^RF commands.

## Format
```
^HV#,n,h
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **#** | Field number specified with another command | 0 to 9999 | 0 |
| **n** | Number of bytes to be returned | 1 to 256 | 64 |
| **h** | Header in uppercase ASCII characters to be returned with the data | 0 to 3072 characters | No header |

## Examples
```
^HV5,128,DATA
^HV10,64,FIELD10
```

## Important Notes
- The value assigned to the field number parameter should be the same as the one used in another command (^RT, ^FN, or ^RF)
- The header will be returned with the data in uppercase ASCII characters
- This command works in conjunction with field numbering commands to verify and return field data

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*