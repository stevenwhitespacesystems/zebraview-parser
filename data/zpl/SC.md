# ZPL Command: ^SC (Set Serial Communications)

## Description
The ^SC command allows you to change the serial communications parameters you are using.

## Format
```
^SCa,b,c,d,e,f
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Baud rate | 110; 600; 1,200; 2400; 4800; 9600; 14400; 19200; 28800; 38400; or 57600; 115200 | Must be specified or the parameter is ignored |
| **b** | Word length (in data bits) | 7 or 8 | Must be specified |
| **c** | Parity | N (none), E (even), or O (odd) | Must be specified |
| **d** | Stop bits | 1 or 2 | Must be specified |
| **e** | Protocol mode | X (XON/XOFF), D (DTR/DSR), or R (RTS) | Must be specified |
| **f** | Zebra protocol | A (ACK/NAK), N (none), Z (Zebra) | Must be specified |

## Examples
```
^SC9600,8,N,1,X,N
```

## Important Notes
- If any of the parameters are missing, out of specification, not supported by a particular printer, or have a ZPL-override DIP switch set, the command is ignored.
- A ^JUS command causes the changes in Communications Mode to persist through power-up and software resets.
- All parameters must be specified for the command to be effective.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*