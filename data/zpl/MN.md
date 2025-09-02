# ZPL Command: ^MN (Media Tracking)

## Description
The ^MN command relays to the printer what type of media is being used (continuous or non-continuous) for purposes of tracking. This command supports different types of media:

- **Continuous Media** – this media has no physical characteristic (web, notch, perforation, mark, et cetera) to separate labels. Label length is determined by the ^LL command
- **Non-continuous Media** – this media has some type of physical characteristic (web, notch, perforation, mark, et cetera) to separate the labels

## Format
```
^MNa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Media being used | N = continuous media<br>Y = non-continuous media web sensing*<br>W = non-continuous media web sensing*<br>M = non-continuous media mark sensing | A value must be entered or the command is ignored |

*Y and W provide the same result.

## Important Notes
- A value must be entered or the command is ignored

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*