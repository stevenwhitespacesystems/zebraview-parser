# ZPL Command: ^XB (Suppress Backfeed)

## Description
The ^XB command suppresses forward feed of media to tear-off position depending on the current printer mode. Because no forward feed occurs, a backfeed before printing of the next label is not necessary; this improves throughput. When printing a batch of labels, the last label should not contain this command.

## Format
```
^XB
```

## Parameters
This command takes no parameters.

## Mode Operations

### Tear-off Mode
- **Normal Operation:** backfeed, print, and feed to rest
- **^XB Operation:** print (Rewind Mode)

### Peel-off Mode
- **Normal Operation:** backfeed, print, and feed to rest
- **^XB Operation:** print (Rewind Mode)

## Important Notes
- Improves throughput by eliminating unnecessary media movement
- Should not be used on the last label in a batch
- Effectively changes operation to rewind mode regardless of current mode
- Prevents forward feed to tear-off position
- Eliminates need for backfeed before next label printing

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*