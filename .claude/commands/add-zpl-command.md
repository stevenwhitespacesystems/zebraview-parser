# Add ZPL Command

Implement a new ZPL command: $ARGUMENTS

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
       - features/templates/prp.md
       - features/templates/tasks.md
    output:
       - features/{FEATURE_NAME}/stages/planning/prp.md
       - features/{FEATURE_NAME}/stages/planning/tasks.md
```

### Automatically call the Researcher

1. Call the researcher agent
   - Agent: @zpl-command-researcher
   - $ARGUMENTS: {FEATURE_NAME}

### Automatically call the Planner

1. Call the planner agent
   - Agent: @zpl-command-planner
   - $ARGUMENTS: {FEATURE_NAME}

### Completion

1. Confirm creation with message:
   - "✓ Feature initialized: {FEATURE_NAME}"
   - "✓ Command: {COMMAND}"
   - "✓ State file: features/{FEATURE_NAME}/state.yml"
   - "✓ Ready for research phase"