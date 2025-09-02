# ZPL Command: ~KB (Kill Battery - Battery Discharge Mode)

## Description
To maintain performance of the rechargeable battery in portable printers, the battery must be fully discharged and recharged regularly. The ~KB command places the printer in battery discharge mode. This allows the battery to be drained without actually printing.

## Format
```
~KB
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~KB
```

## Important Notes
- Used to maintain rechargeable battery performance in portable printers
- Green power LED flashes in groups of three flashes while in Discharge Mode
- Discharge Mode can be terminated by sending a printing format or pressing front panel keys
- If battery charger is plugged in, battery automatically recharges once discharge is complete
- Battery must be fully discharged and recharged regularly for optimal performance

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
