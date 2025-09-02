# ZPL Command: ^CF (Change Alphanumeric Default Font)

## Description
The ^CF command sets the default font used in your printer. You can use the ^CF command to simplify your programs. This command specifies the default font for every alphanumeric field, along with default height and width values.

## Format
```
^CFf,h,w
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **f** | Specified default font | A through Z and 0 to 9 | A |
| **h** | Individual character height (in dots) | 0 to 32000 | 9 |
| **w** | Individual character width (in dots) | 0 to 32000 | 5 or last permanent saved value |

## Examples
This is an example of ^CF code and the result of the code: *(Note: Original example content was not provided in the source text)*

## Important Notes
- Parameter f specifies the default font for every alphanumeric field
- Parameter h is the default height for every alphanumeric field  
- Parameter w is the default width value for every alphanumeric field
- The default alphanumeric font is A. If you do not change the alphanumeric default font and do not use any alphanumeric field command (^AF) or enter an invalid font value, any data you specify prints in font A
- Defining only the height or width forces the magnification to be proportional to the parameter defined
- If neither value is defined, the last ^CF values given or the default ^CF values for height and width are used
- Any font in the printer, including downloaded fonts, EPROM stored fonts, and fonts A through Z and 0 to 9, can also be selected with ^CW

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*