# ZPL Command: ^CV (Code Validation)

## Description
The ^CV command acts as a switch to turn the code validation function on and off. When this command is turned on, all bar code data is checked for these error conditions:
- Character not in character set
- Check-digit incorrect
- Data field too long (too many characters)
- Data field too short (too few characters)
- Parameter string contains incorrect data or missing parameter

When invalid data is detected, an error message and code is printed in reverse image in place of the bar code. The message reads "INVALID - X" where X is one of the error codes listed below.

## Format
```
^CVa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Code validation | Y (yes) or N (no) | N |

## Examples
The examples below show the error labels ^CVY generates when incorrect field data is entered:

```
^XA
^CVY
^FO50,50
^BEN,100,Y,N
^FD97823456 890^FS
^XZ
```

```
^XA
^CVY
^FO50,50
^BEN,100,Y,N
^FD9782345678907^FS
^XZ
```

```
^XA
^CVY
^FO50,50
^BEN,100,Y,N
^FD97823456789081^FS
^XZ
```

```
^XA
^CVY
^FO50,50
^BEN,100,Y,N
^FD97823456789^FS
^XZ
```

```
^XA
^CVY
^FO50,50
^BQN2,3
^FDHM,BQRCODE-22^FS
^XZ
```

## Error Codes
When validation fails, the following error codes are displayed:
- **C** = Character not in character set
- **E** = Check-digit incorrect
- **L** = Data field too long
- **S** = Data field too short
- **P** = Parameter string contains incorrect data (occurs only on select bar codes)

## Important Notes
- Once turned on, the ^CV command remains active from format to format until turned off by another ^CV command or the printer is turned off
- The command is not permanently saved
- If more than one error exists, the first error detected is the one displayed
- The ^CV command tests the integrity of the data encoded into the bar code. It is not used for (or to be confused with) testing the scan-integrity of an image or bar code

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*