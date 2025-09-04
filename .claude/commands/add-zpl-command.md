# /add-zpl-command

Initiates the workflow for adding a new ZPL command to the parser by triggering research and documentation generation.

## Usage
```
/add-zpl-command <command>
```

## Parameters
- `command` (required): The ZPL command name (e.g., BC, BD, XA, XZ)

## Description
This command starts the sub-agents workflow for implementing a new ZPL command:

1. **Research Phase**: Launches the `zpl-command-researcher` agent to analyze the command specification from `data/zpl/{command}.md`
2. **Documentation**: Creates structured findings in `features/{number}-{command}-command/agents/zpl-command-researcher/findings.md`

## Example
```
/add-zpl-command BC
```
This will:
- Research the ^BC (Code 128 Bar Code) command
- Create feature directory structure
- Generate research findings for implementation planning

## Workflow
The command triggers the `zpl-command-researcher` agent which analyzes command specifications and requirements.

## Output Structure
```
features/
├── {number}-{command}-command/
│   ├── agents/
│   │   └── zpl-command-researcher/
│   │       └── findings.md
```

## Notes
- Command names are case-insensitive but will be normalized to uppercase
- Requires corresponding documentation in `data/zpl/{command}.md`
- Creates feature numbering based on existing features in the directory