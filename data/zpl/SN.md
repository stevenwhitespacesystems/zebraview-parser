# ZPL Command: ^SN (Serialization Data)

## Description
The ^SN command allows the printer to index data fields by a selected increment or decrement value, making the data fields increase or decrease by a specified value each time a label is printed. This can be performed on 100 to 150 fields in a given format and can be performed on both alphanumeric and bar code fields. A maximum of 12 of the rightmost integers are subject to indexing.

## Format
```
^SNv,n,z
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **v** | Starting value | 12-digits maximum for the portion to be indexed | 1 |
| **n** | Increment or decrement value | 12-digit maximum (use minus sign for decrement) | 1 |
| **z** | Add leading zeros (if needed) | Y = yes<br>N = no | N |

## Important Notes
- The first integer found when scanning from right to left starts the indexing portion of the data field
- If the alphanumeric field ends with an alpha character, scanning occurs from right to left until a numeric character is encountered
- Incrementing and decrementing takes place for each serial-numbered field when all replicates have been printed (as specified in ^PQ command)
- The ^SN command replaces the Field Data (^FD) command within a label formatting program

### Leading Zeros Behavior
- **Print Leading Zeros (z=Y):** The starting value consists of the right-most consecutive sequence of digits. Width is determined by scanning from right to left until the first non-digit is encountered.
- **Suppress Leading Zeros (z=N):** The starting value includes any leading spaces. Width is determined by scanning until the first alpha character (except space) is encountered. Suppressed zeros are replaced by spaces.

### Error Recovery
If the printer runs out of paper or ribbon during serialization, the first label printed after replacement has the same serial number as the partial label that didn't fully print. This behavior is controlled by the ^JZ command.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*