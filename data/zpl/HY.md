# ZPL Command: ^HY (Upload Graphics)

## Description
The ^HY command is an extension of the ^HG command. ^HY is used to upload graphic objects from the printer in any supported format.

## Format
```
^HYd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of object | R:, E:, B:, and A: | Search priority |
| **o** | Object name | 1 to 8 alphanumeric characters | An object name must be specified |
| **x** | Extension | G = .GRF (raw bitmap format)<br>P = .PNG (compressed bitmap format) | Format of stored image |

## Examples
```
^HYR:LOGO.G
^HYE:SYMBOL.P
```

## Important Notes
- The image is uploaded in the form of a ~DY command
- The data field of the returned ~DY command is always encoded in the ZB64 format
- This command is an extension of the ^HG command with additional upload capabilities

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*