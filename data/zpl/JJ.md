# ZPL Command: ^JJ (Set Auxiliary Port)

## Description
The ^JJ command allows you to control an online verifier or applicator device through the auxiliary port settings.

## Format
```
^JJa,b,c,d,e,f
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Operational Mode for auxiliary port | 0 = off<br/>1 = reprint on error<br/>2 = maximum throughput | 0 |
| **b** | Application Mode | 0 = off<br/>1 = End Print signal normally high, low when moving label forward<br/>2 = End Print signal normally low, high when moving label forward<br/>3 = End Print signal normally high, low for 20ms when label printed<br/>4 = End Print signal normally low, high for 20ms when label printed | 0 |
| **c** | Application Mode start signal print | p = Pulse Mode<br/>l = Level Mode | 0 |
| **d** | Application Label Error Mode | e = error mode (asserts Service Required signal)<br/>f = Feed Mode (prints blank label when web not found) | f |
| **e** | Reprint Mode | e = enabled (ignores Reprint signal)<br/>d = disabled (reprints last label when signal asserted) | d |
| **f** | Ribbon Low Mode | e = enabled (warning issued when ribbon low)<br/>d = disabled (no warning when ribbon low) | e |

## Examples
```zpl
^JJ1,0,p,f,d,e
^JJ2,3,l,e,e,d
```

## Important Notes
- Used for controlling verifiers and applicators
- Reprint on error mode (a=1) stops on verification errors and allows reprinting
- Maximum throughput mode (a=2) provides fastest operation but may not stop immediately on errors
- Pulse mode requires start signal to be de-asserted before next label
- Level mode allows continuous printing while start signal is asserted
- Disabled reprint mode consumes more memory as it retains the last printed label

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*