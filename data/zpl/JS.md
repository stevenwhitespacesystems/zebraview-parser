# ZPL Command: ~JS (Change Backfeed Sequence)

## Description
The ~JS command is used to control the backfeed sequence. This command can be used on printers with or without built-in cutters. Primary applications include programming the rest point of the cut edge of continuous media and providing immediate backfeed after peel-off when the printer is used in a print/apply application configuration.

## Format
```
~JSb
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **b** | Backfeed order in relation to printing | A = 100% backfeed after printing and cutting<br/>B = 0% backfeed after printing and cutting, 100% before printing next label<br/>N = normal (90% backfeed after label is printed)<br/>O = off (turn backfeed off completely)<br/>10-90 = percentage value (multiples of 10) | N |

## Examples
```zpl
~JSN
~JSA
~JSB
~JSO
~JS50
```

## Important Notes
- Setting stays in effect only until printer is turned off, new ~JS command is sent, or setting is changed on front panel
- Overrides current front panel setting for Backfeed Sequence
- Most common way of eliminating backfeed is to operate in Rewind Mode
- When using percentage values, difference between entered value and 100% is calculated before next label prints
- Values not divisible by 10 are rounded to nearest acceptable value
- Setting is reflected in Backfeed parameter on printer configuration label

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
