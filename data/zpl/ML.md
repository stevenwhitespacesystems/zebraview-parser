# ZPL Command: ^ML (Maximum Label Length)

## Description
The ^ML command lets you adjust the maximum label length.

## Format
```
^MLa,b,c,d
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Maximum label length (in dot rows) | 0 to maximum length of label | Last permanently saved value |
| **b** | Maximum logical paper out counter | Must be greater than the actual label length or the printer indicates a paper out error after each label | Set to one inch longer than twice the label length |
| **c** | Maximum physical paper out counter | Must be greater than the actual notch or hole or the printer indicates a paper out condition after each label | Set to one half an inch |
| **d** | Maximum ribbon out counter | Allows for the ribbon sensors to occasionally get an incorrect ribbon reading without causing an error condition | Set to one half of a millimeter |

## Important Notes
- For calibration to work properly, you must set the maximum label length equal to or greater than your actual label length
- The printer ignores ribbon indications that are less than one-half of a millimeter in length

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*