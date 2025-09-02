# ZPL Command: ^BY (Bar Code Field Default)

## Description
The ^BY command is used to change the default values for the module width (in dots), the wide bar to narrow bar width ratio, and the bar code height (in dots). It can be used as often as necessary within a label format.

## Format
```
^BYw,r,h
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **w** | Module width (in dots) | 1 to 10 | 2 (initial power-up value) |
| **r** | Wide bar to narrow bar width ratio | 2.0 to 3.0 (in 0.1 increments) | 3.0 |
| **h** | Bar code height (in dots) | No specified range | 10 (initial power-up value) |

## Ratio Calculation
For parameter r, the actual ratio generated is a function of the number of dots in parameter w (module width). The printer rounds to the nearest dot, so actual ratios may vary slightly from the requested ratio.

### Example
If module width (w) is set to 9 and ratio (r) to 2.4:
- Narrow bar width: 9 dots
- Wide bar calculation: 9 × 2.4 = 21.6 dots
- Actual wide bar width: 22 dots (rounded)
- Actual ratio: 22 ÷ 9 = 2.44

## Module Width Ratios Table
The following table shows actual ratios achieved based on selected ratio and module width:

| Selected Ratio | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 |
|----------------|---|---|---|---|---|---|---|---|---|---|
| 2.0 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 |
| 2.1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2:1 | 2.1:1 |
| 2.2 | 2:1 | 2:1 | 2:1 | 2:1 | 2.2:1 | 2.16:1 | 2.1:1 | 2.12:1 | 2.1:1 | 2.2:1 |
| 2.3 | 2:1 | 2:1 | 2.3:1 | 2.25:1 | 2.2:1 | 2.16:1 | 2.28:1 | 2.25:1 | 2.2:1 | 2.3:1 |
| 2.4 | 2:1 | 2:1 | 2.3:1 | 2.25:1 | 2.4:1 | 2.3:1 | 2.28:1 | 2.37:1 | 2.3:1 | 2.4:1 |
| 2.5 | 2:1 | 2.5:1 | 2.3:1 | 2.5:1 | 2.4:1 | 2.5:1 | 2.4:1 | 2.5:1 | 2.4:1 | 2.5:1 |
| 2.6 | 2:1 | 2.5:1 | 2.3:1 | 2.5:1 | 2.6:1 | 2.5:1 | 2.57:1 | 2.5:1 | 2.5:1 | 2.6:1 |
| 2.7 | 2:1 | 2.5:1 | 2.6:1 | 2.5:1 | 2.6:1 | 2.6:1 | 2.57:1 | 2.65:1 | 2.6:1 | 2.7:1 |
| 2.8 | 2:1 | 2.5:1 | 2.6:1 | 2.75:1 | 2.8:1 | 2.6:1 | 2.7:1 | 2.75:1 | 2.7:1 | 2.8:1 |
| 2.9 | 2:1 | 2.5:1 | 2.6:1 | 2.75:1 | 2.8:1 | 2.8:1 | 2.85:1 | 2.87:1 | 2.8:1 | 2.9:1 |
| 3.0 | 3:1 | 3:1 | 3:1 | 3:1 | 3:1 | 3:1 | 3:1 | 3:1 | 3:1 | 3:1 |

## Important Notes
- Module width and height (w and h) can be changed at any time with the ^BY command, regardless of the symbology selected
- The ratio parameter has no effect on fixed-ratio bar codes
- Once a ^BY command is entered into a label format, it stays in effect until another ^BY command is encountered
- Only full dots are printed, so ratios are approximated to the nearest achievable value

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
