# ZPL Command: ^FX (Comment)

## Description
The ^FX command is useful when you want to add non-printing informational comments or statements within a label format. Any data after the ^FX command up to the next caret (^) or tilde (~) command does not have any effect on the label format. Therefore, you should avoid using the caret (^) or tilde (~) commands within the ^FX statement.

## Format
```
^FXc
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **c** | Non-printing comment | Any text string | none |

## Examples
Using comments in label format:
```zpl
^XA
^LH100,100^FS
^FXSHIPPING LABEL^FS
^FO10,10^GB470,280,4^FS
^FO10,190^GB470,4,4^FS
^FO10,80^GB240,2,2^FS
^FO250,10^GB2,100,2^FS
^FO250,110^GB226,2,2^FS
^FO250,60^GB226,2,2^FS
^FO156,190^GB2,95,2^FS
^FO312,190^GB2,95,2^FS
^XZ
```

## Important Notes
- Comments are non-printing and do not affect label output
- Avoid using caret (^) or tilde (~) commands within the ^FX statement
- Correct usage includes following it with the ^FS command
- Useful for documentation and label format organization

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*