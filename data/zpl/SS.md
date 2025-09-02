# ZPL Command: ^SS (Set Media Sensors)

## Description
The ^SS command is used to change the values for media, web, ribbon, and label length set during the media calibration process. This command allows manual override of automatically calibrated sensor values.

## Format
```
^SSw,m,r,l,m2,r2,a,b,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **w** | Web (three-digit value) | 000 to 100 | Value from media sensor profile |
| **m** | Media (three-digit value) | 000 to 100 | Value from media sensor profile |
| **r** | Ribbon (three-digit value) | 001 to 100 | Value from media sensor profile |
| **l** | Label length in dots (four-digit value) | 0001 to 32000 | Value from calibration process |
| **m2** | Intensity of media LED (three-digit value) | 000 to 100 | Value from calibration process |
| **r2** | Intensity of ribbon LED (three-digit value) | 000 to 100 | Value from calibration process |
| **a** | Mark sensing (three-digit value) | 000 to 100 | Value from calibration process |
| **b** | Mark media sensing (three-digit value) | 000 to 100 | Value from calibration process |
| **c** | Mark LED sensing (three-digit value) | 000 to 100 | Value from calibration process |

## Important Notes
- Values can be found on the media sensor profile or configuration label
- The m2 and r2 parameters have no effect in Stripe S-300 and S-500 printers
- Maximum values for parameters depend on which printer platform is being used
- Media sensor profiles show numbers from 000 to 100 with WEB, MEDIA, and RIBBON indicators
- Black vertical spikes in profiles represent transitions from media-to-web-to-media
- Media and sensor profile appearance varies between printer models

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*