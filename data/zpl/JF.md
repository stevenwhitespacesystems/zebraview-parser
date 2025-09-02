# ZPL Command: ~JF (Set Battery Condition)

## Description
Controls the battery condition settings for PA/PT400™ printers. There are two low battery voltage levels sensed by the PA/PT400™ printers. The first level triggers a green LED warning while printing continues. The second level triggers both green and orange LED warnings, and printing behavior depends on this setting.

## Format
```
~JFp
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **p** | Pause on low voltage | Y (pause on low voltage)<br/>N (do not pause) | Y |

## Examples
```zpl
~JFY
~JFN
```

## Important Notes
- When pause on low voltage is active (~JFY) and battery voltage falls below the second low voltage level, printing pauses and an error condition is displayed
- When pause on low voltage is not active (~JFN), printing continues even at low voltage levels
- N is suggested when the printer is powered by the Car Battery Adapter
- When using Car Battery Adapter without this option, you might need to press FEED to exit Pause Mode for each label
- Risk of losing label format information exists if voltage continues to decrease during low battery operation

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*