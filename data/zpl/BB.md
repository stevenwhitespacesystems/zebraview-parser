# ZPL Command: ^BB (CODABLOCK Bar Code)

## Description
The ^BB command produces a two-dimensional, multirow, stacked symbology. It is ideally suited for applications that require large amounts of information. Depending on the mode selected, the code consists of one to 44 stacked rows. Each row begins and ends with a start and stop pattern.

### Key Features
- CODABLOCK A supports variable print ratios
- CODABLOCK E and F support only fixed print ratios
- Can handle 1 to 44 stacked rows depending on mode
- Suitable for applications requiring large amounts of information

## Format
```
^BBo,h,s,c,r,m
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | N |
| **h** | Bar code height for individual rows (in dots) | 2 to 32000 | 8 |
| **s** | Security level | Y = yes<br>N = no | Y |
| **c** | Number of characters per row (data columns) | 2 to 62 characters | - |
| **r** | Number of rows to encode | **CODABLOCK A:** 1 to 22<br>**CODABLOCK E and F:** 2 to 4 | - |
| **m** | Mode | A = CODABLOCK A (Code 39 character set)<br>E = CODABLOCK E (Code 128 + FNC1)<br>F = CODABLOCK F (Code 128 character set) | F |

## Parameter Usage Rules

### When c and r are not specified:
- A single row is produced

### When r is not specified and c exceeds maximum range:
- A single row equal to the field data length is produced

### When c is not specified:
- Number of characters per row is derived by dividing field data by the value of r

### When both parameters are specified:
- Amount of field data must be less than the product of the specified parameters
- If field data exceeds the product value, either no symbol or an error code is printed (if ^CV is active)

### Data-dependent behavior:
- If field data contains primarily numeric data, fewer than the specified rows might be printed
- If field data contains several shift and code-switch characters, more than the specified number of rows might be printed

## CODABLOCK Modes

### CODABLOCK A
- Uses the Code 39 character set
- Supports variable print ratios

### CODABLOCK E
- Uses the Code 128 character set
- Automatically adds FNC1
- Supports only fixed print ratios

### CODABLOCK F
- Uses the Code 128 character set
- Supports only fixed print ratios

## Special Considerations for ^BY with CODABLOCK

When used with ^BB, the ^BY command parameters are:

| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **w** | Module width (in dots) | 2 to 10 (CODABLOCK A only) | 2 |
| **r** | Ratio | Fixed value: 3 | 3 |
| **h** | Height of bars (in dots) | 1 to 32000 | 10 |

**Note:** CODABLOCK uses the ^BY height parameter as the overall symbol height only when row height is not specified in the ^BB h parameter. Ratio has no effect on CODABLOCK E or F.

## Special Considerations for ^FD Character Set

The character set depends on the mode selected:

### CODABLOCK A
Uses the same character set as Code 39. If any other character is used in the ^FD statement, either no bar code is printed or an error message is printed (if ^CV is active).

### CODABLOCK E
The Automatic Mode includes the full ASCII set except for characters with special meaning to the printer. Function codes or the Code 128 Subset A <nul> character can be inserted using the ^FH command:
- `<fnc1>` = 80 hex
- `<fnc2>` = 81 hex  
- `<fnc3>` = 82 hex
- `<fnc4>` = 83 hex
- `<nul>` = 84 hex

### CODABLOCK F
Uses the full ASCII set, except for characters with special meaning to the printer. Function codes or the Code 128 Subset A <nul> character can be inserted using the ^FH command:
- `<fnc1>` = 80 hex
- `<fnc2>` = 81 hex
- `<fnc3>` = 82 hex
- `<fnc4>` = 83 hex
- `<nul>` = 84 hex

## Important Notes
- Security level determines whether symbol check-sums are generated and added to the symbol
- Check sums are never generated for single-row symbols
- Security can be turned off only if parameter m is set to A
- Height multiplier number multiplied by the module equals the height of individual rows in dots
- For additional information about CODABLOCK bar codes, visit www.aimglobal.org
- For any character above 84 hex (in modes E and F), either no bar code is printed or an error message is printed (if ^CV is active)

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*