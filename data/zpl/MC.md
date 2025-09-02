# ZPL Command: ^MC (Map Clear)

## Description
In normal operation, the bitmap is cleared after the format has been printed. The ^MC command is used to retain the current bitmap. This applies to current and subsequent labels until cleared with ^MCY.

## Format
```
^MCa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Map clear | Y (clear bitmap) or N (do not clear bitmap) | Y |

## Important Notes
- To produce a label template, ^MC must be used with ^FV
- The ^MC command retains the image of the current label after formatting. It appears in the background of the next label printed

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*