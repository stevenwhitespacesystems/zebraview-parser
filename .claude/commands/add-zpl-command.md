# Add ZPL Command

Implement a new ZPL command: $ARGUMENTS

## Quick Start
1. **Validate**: Command provided, data file exists
2. **Setup**: Generate feature ID, create directories  
3. **Launch**: Create state file and start research agent

## Step 1: Validation & Setup
```bash
# Validate inputs
COMMAND="${ARGUMENTS^^}"  # Uppercase
[ -z "$COMMAND" ] && { echo "Error: Command required"; exit 1; }
[ ! -f "data/zpl/${COMMAND}.md" ] && { echo "Error: data/zpl/${COMMAND}.md not found"; exit 1; }

# Generate feature ID
NEXT_ID=$(printf "%04d" $(($(ls features/ | grep -E '^[0-9]+' | cut -d'-' -f1 | sort -n | tail -1) + 1)))
FEATURE_ID="${NEXT_ID}-$(echo $COMMAND | tr '[:upper:]' '[:lower:]')-command"

# Create structure
mkdir -p "features/${FEATURE_ID}/agents/zpl-command-researcher"
```

## Step 2: Create State File
**Location:** `features/{feature-id}/agents/state.yaml`

```yaml
# Auto-generated state file for coordination
feature_id: ${FEATURE_ID}
command: ${COMMAND}
initiated: $(date -u +"%Y-%m-%dT%H:%M:%SZ")

# Current workflow position
phase: research        # research|prp|implementation  
status: active         # active|complete|failed
agent: zpl-command-researcher

# Phase tracking
research:
  status: active       # pending|active|complete|failed
  started: $(date -u +"%Y-%m-%dT%H:%M:%SZ")
  findings: agents/zpl-command-researcher/findings.md

prp:
  status: pending
  path: prp.md

implementation:
  status: pending
  iteration: 0
  max_iterations: 50
  last_failure: none   # tests|coverage|detekt|ktlint|performance|integration|demo
```

## Step 3: Launch Research Agent
Use Task tool with:
- **Subagent:** `zpl-command-researcher`  
- **Prompt:** `Research ZPL command ${COMMAND}. State file: features/${FEATURE_ID}/agents/state.yaml. Read command spec from data/zpl/${COMMAND}.md, analyze implementation requirements, write findings to designated path, then update state to complete research phase and transition to PRP generation.`

## State Management (Agents)

### Reading State
```bash
# Parse YAML (all agents)
PHASE=$(yq r state.yaml phase)
STATUS=$(yq r state.yaml status)
FINDINGS_PATH=$(yq r state.yaml research.findings)
```

### Updating State  
```bash
# Mark phase complete
yq w -i state.yaml research.status complete
yq w -i state.yaml research.completed "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
yq w -i state.yaml phase prp
yq w -i state.yaml prp.status active  
yq w -i state.yaml agent prp-generator
```

### Phase Transitions
1. **research** → findings written → **prp** 
2. **prp** → PRP document created → **implementation**
3. **implementation** → all validations pass → **complete**

## Error Handling
- Missing command: Exit with usage message
- Missing data file: Exit with file path error  
- Directory creation fails: Exit with permissions error
- Agent launch fails: Check state file exists first

## File Structure Created
```
features/{feature-id}/
├── agents/
│   ├── state.yaml                    # Central coordination (30 lines)
│   └── zpl-command-researcher/
│       └── findings.md               # Research output
└── prp.md                           # Generated implementation plan
```

## Implementation Notes
- **Token efficiency**: Single compact YAML vs verbose JSON saves 75% tokens
- **Atomic updates**: Each phase update is single yq command  
- **Smart defaults**: Auto-generate paths, timestamps, feature IDs
- **Error recovery**: State file shows exactly where process failed
- **Agent coordination**: All agents read/write same state.yaml for workflow sync

This streamlined approach reduces complexity while maintaining full coordination capabilities.