# ZPL Command: ^SQ (Halt ZebraNet Alert)

## Description
The ^SQ command is used to stop the ZebraNet Alert option for specific conditions and destinations.

## Format
```
^SQa,b,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Condition type | A = paper out<br>B = ribbon out<br>C = printhead over-temp<br>D = printhead under-temp<br>E = head open<br>F = power supply over-temp<br>G = ribbon-in warning (Direct Thermal Mode)<br>H = rewind full<br>I = cut error<br>J = printer paused<br>K = PQ job completed<br>L = label ready<br>M = head element out<br>P = power on<br>Q = clean printhead<br>R = media low<br>S = ribbon low<br>T = replace head<br>U = battery low<br>V = all errors | - |
| **b** | Destination | A = serial port<br>B = parallel port<br>C = e-mail address<br>D = TCP/IP<br>E = UDP/IP<br>F = SNMP trap<br>* = wild card (all destinations) | - |
| **c** | Halt messages | Y = halt messages<br>N = start messages | Y |

## Important Notes
- This command controls ZebraNet Alert notifications for various printer conditions
- Use wildcard (*) in destination parameter to stop alerts for all destinations
- Setting halt messages to N will start messages instead of stopping them
- Covers a comprehensive range of printer status conditions and alert destinations

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*