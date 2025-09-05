# Add ZPL Command

Implement a new ZPL command: $ARGUMENTS

## Requirements

- NEVER use chained bash commands!
   - I don't want to have to be prompted to confirm complex bash commands
   - Execute bash command individually

## Reading/Updating State
You can make use of the `yq` command

Example:
```shell
yq eval '.stages.planner.status = "started"' -i state.yaml
```

## Workflow
### Setup Feature Folder Structure

1. Validate command documentation exists
   - Convert $ARGUMENTS to uppercase → Store as COMMAND
   - Check if `data/zpl/{COMMAND}.md` exists
   - If not found, try with @ replaced by -AT (e.g., A@ becomes A-AT)
   - If still not found, list similar files in data/zpl/ and confirm with user
   - When we get the right file  → Store as COMMAND but without the .md extension
     - Example: A-AT, BT, B1

2. Calculate feature ID
   - Count directories in /features starting with digits (exclude "templates")
   - Add 1 to this count → Store as NEXT_NUMBER
   - Pad with zeros to 4 digits → Store as FEATURE_ID
   - Example: 3 becomes 0003, 10 becomes 0010

3. Generate feature name
   - Convert COMMAND to lowercase
   - Format: `{FEATURE_ID}-{lowercase_command}-command` → Store as FEATURE_NAME
   - Example: 0005-bt-command

4. Create folder structure
   - Create directory: `features/{FEATURE_NAME}`
   - Create directory: `features/{FEATURE_NAME}/stages`
   - Create directory: `features/{FEATURE_NAME}/stages/research`
   - Create directory: `features/{FEATURE_NAME}/stages/planning`
   - Create directory: `features/{FEATURE_NAME}/stages/implementation`

### Setup Feature State

1. Get current UTC ISO timestamp → Store as CURRENT_TIME

2. Create state.yml at `features/{FEATURE_NAME}/state.yml` with content:
```yaml
feature: {FEATURE_NAME}
command: {COMMAND}
status: active
phase: null
start: {CURRENT_TIME}
end: null
elapsed: null
stages:
  research:
    status: inactive
    start: null
    end: null
    elapsed: null
    error: null
    output:
       - features/{FEATURE_NAME}/stages/research/results.md
  planning:
    status: inactive
    start: null
    end: null
    elapsed: null
    error: null
    templates:
       - features/templates/zpl-command-prp.md
       - features/templates/zpl-command-tasks.md
    output:
       - features/{FEATURE_NAME}/stages/planning/zpl-command-prp.md
  implementation:
     status: inactive
     start: null
     end: null
     elapsed: null
     error: null
     references:
        prp: features/{FEATURE_NAME}/stages/planning/zpl-command-prp.md
     tasks:
       red:
          status: inactive
          start: null
          end: null
          elapsed: null
          error: null
          output:
             - features/{FEATURE_NAME}/stages/implementation/red/tests-created.md
       green:
          status: inactive
          start: null
          end: null
          elapsed: null
          error: null
       refactor: null
```

### Automatically call the Researcher

1. Call the researcher agent
   - Agent: @zpl-command-researcher
   - $ARGUMENTS: {FEATURE_NAME}

### Automatically call the Planner

1. Call the planner agent
   - Agent: @zpl-command-planner
   - $ARGUMENTS: {FEATURE_NAME}

### Setup Implementation Flow

1. Validate that the planner phase complete
   - `.stages.planning.status` → Store as PLANNING_STATE
   - If PLANNING_STATE != "complete", then don't continue, report to user and halt session

2. Update STATE with status updates
   - Get current UTC ISO timestamp → Store as CURRENT_TIME
   - Update `.phase` to "implementation"
   - Update `.stages.implementation.status` to "active"
   - Update `.stages.implementation.start` to CURRENT_TIME

### Start Implementation Flow

1. Call the red phase TDD agent
   - Agent: @kotlin-tdd-red-phase
   - $ARGUMENTS: {FEATURE_NAME}

2. END IMPLEMENTATION FLOW

### Completion

1. Confirm creation with message:
   - "✓ Feature initialized: {FEATURE_NAME}"
   - "✓ Command: {COMMAND}"
   - "✓ State file: features/{FEATURE_NAME}/state.yml"
   - "✓ Ready for research phase"