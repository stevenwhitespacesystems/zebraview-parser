# ZPL Command: ^SZ (Set ZPL)

## Description
The ^SZ command is used to select the programming language used by the printer. This command gives you the ability to print labels formatted in both ZPL and ZPL II. This command remains active until another ^SZ command is sent to the printer or the printer is turned off.

## Format
```
^SZa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | ZPL version | 1 = ZPL<br>2 = ZPL II | 2 |

## Important Notes
- Allows switching between ZPL and ZPL II programming languages
- Command remains active until another ^SZ command is sent or printer is turned off
- Command is ignored if parameter is missing or invalid
- ZPL II is the default programming language
- Essential for maintaining compatibility with different label format versions

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*