# ZPL Command: ^SF (Serialization Field)

## Description
The ^SF command allows you to serialize a standard ^FD string. Fields serialized with this command are right-justified or end with the last character of the string. The increment string is aligned with the mask, starting with the right-most position. The maximum size of the mask and increment string is 3K combined.

## Format
```
^SFa,b
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Mask string | Sets the serialization scheme. Placeholders: D/d (Decimal 0-9), H/h (Hexadecimal 0-9, a-f, A-F), O/o (Octal 0-7), A/a (Alphabetic a-z, A-Z), N/n (Alphanumeric 0-9, a-z, A-Z), % (Ignore character or skip) | Required |
| **b** | Increment string | Value to be added to the field on each label. Invalid characters are assumed to be zero. For alphabetic strings, 'A'/'a' is the zero placeholder. | Equivalent to decimal value of one |

## Examples

### Example 1: Basic alphanumeric serialization
```
^XA
^FO100,100
^CF0,100
^FD12A^SFnnA,F^FS
^PQ3
^XZ
```
This generates three labels with the sequence: 12A, 12F, 12K

### Example 2: Sequential numeric with prefix
```
^FDBL0000^SFAAdddd,1
```
Print sequence: BL0000, BL0001, ...BL0009, BL0010, ...BL9999, BM0000...

### Example 3: Skip character serialization
```
^FDBL00-0^SFAAdd%d,1%1
```
Print sequence: BL00-0, BL01-1, BL02-2, ...BL09-9, BL11-0, BL12-1...

## Important Notes
- Fields are right-justified and end with the last character of the string.
- The mask string defines the number of characters in the ^FD string to be serialized.
- The mask is aligned to characters starting with the right-most position.
- For characters that do not get incremented, the % character needs to be added to the increment string.
- The increment value for alphabetic strings starts with 'A' or 'a' as the zero placeholder.
- Maximum combined size of mask and increment string is 3K.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*