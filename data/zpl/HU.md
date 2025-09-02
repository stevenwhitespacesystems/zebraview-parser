# ZPL Command: ~HU (Return ZebraNet Alert Configuration)

## Description
This command returns the table of configured ZebraNet Alert settings to the host.

## Format
```
~HU
```

## Parameters
This command has no parameters.

## Examples
```
~HU
```

Example response with existing Alert messages set to go to e-mail and SNMP traps:
```
B,C,Y,Y,ADMIN@COMPANY.COM,0
J,F,Y,Y,,0
C,F,Y,Y,,0
D,F,Y,Y,,0
E,F,Y,N,,0
F,F,Y,N,,0
H,C,Y,N,ADMIN@COMPANY.COM,0
N,C,Y,Y,ADMIN@COMPANY.COM,0
O,C,Y,Y,ADMIN@COMPANY.COM,0
P,C,Y,Y,ADMIN@COMPANY.COM,0
```

## Response Format
Each line shows the settings for a different Alert condition as defined in the ^SX command:

| Position | Description | Example |
|----------|-------------|---------|
| **1st field** | Condition code (B = ribbon out, J/C/D/E/F/H/N/O/P = other conditions) | B |
| **2nd field** | Destination type (C = e-mail address, F = SNMP trap) | C |
| **3rd field** | Condition set option (Y = yes, N = no) | Y |
| **4th field** | Condition clear option (Y = yes, N = no) | Y |
| **5th field** | Destination address (e-mail address or empty for SNMP) | ADMIN@COMPANY.COM |
| **6th field** | Port number | 0 |

## Important Notes
- The first line example indicates condition B (ribbon out) is routed to destination C (e-mail address)
- The Y and Y characters indicate that both condition set and condition clear options have been set to yes
- The destination field contains the e-mail address where Alert messages should be sent
- The last field is the port number (0 in the example)
- See ^SX command for complete information on individual parameter settings

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*