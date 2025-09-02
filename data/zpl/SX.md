# ZPL Command: ^SX (Set ZebraNet Alert)

## Description
The ^SX command is used to configure the ZebraNet Alert System for monitoring various printer conditions and routing notifications to different destinations.

## Format
```
^SXa,b,c,d,e,f
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Condition type | A = paper out<br>B = ribbon out<br>C = printhead over-temp<br>D = printhead under-temp<br>E = head open<br>F = power supply over-temp<br>G = ribbon-in warning (Direct Thermal Mode)<br>H = rewind full<br>I = cut error<br>J = printer paused<br>K = PQ job completed<br>L = label ready<br>M = head element out<br>P = power on<br>Q = clean printhead<br>R = media low<br>S = ribbon low<br>T = replace head<br>U = battery low<br>V = all errors | Command ignored if missing/invalid |
| **b** | Destination for route alert | A = serial port<br>B = parallel port*<br>C = e-mail address<br>D = TCP/IP<br>E = UDP/IP<br>F = SNMP trap | Command ignored if missing/invalid |
| **c** | Enable condition set alert | Y = yes<br>N = no | Y or previously configured value |
| **d** | Enable condition clear alert | Y = yes<br>N = no | N or previously configured value |
| **e** | Destination setting | Internet e-mail address (e.g. user@company.com)<br>IP address (e.g. 10.1.2.123)<br>SNMP trap IP or IPX addresses | Ignored if missing/invalid |
| **f** | Port number | TCP port # (0 to 65535)<br>UDP port # (0 to 65535) | - |

## Examples
```zpl
^SXA,A,Y,Y          // Serial port alert for paper out
^SXA,B,Y,Y          // Parallel port alert for paper out
^SXA,C,Y,Y,admin@company.com         // E-mail alert for paper out
^SXA,D,Y,Y,123.45.67.89,1234        // TCP alert for paper out
^SXA,E,Y,Y,123.45.67.89,1234        // UDP alert for paper out
^SXA,F,Y,Y,255.255.255.255          // SNMP trap alert for paper out
```

## Important Notes
- Values apply to firmware V48_12_4 and above
- Parameters e and f are sub-options based on destination type
- Parallel port destination (B) requires bidirectional communication
- For SNMP Trap: entering 255.255.255.255 broadcasts to every SNMP manager on the network
- Use specific IP address (e.g., 123.45.67.89) to route to a single SNMP manager
- Command is ignored if parameters are missing or invalid

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*