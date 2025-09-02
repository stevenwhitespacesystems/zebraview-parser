# ZPL Command: ^LR (Label Reverse Print)

## Description
The ^LR command reverses the printing of all fields in the label format. It allows a field to appear as white over black or black over white.

Using the ^LR is identical to placing an ^FR command in all current and subsequent fields.

## Format
```
^LRa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Reverse print all fields | Y (yes) or N (no) | N or last permanently saved value |

## Important Notes
- The ^LR setting remains active unless turned off by ^LRN or the printer is turned off
- ^GB needs to be used together with ^LR
- Only fields following this command are affected

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*