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

```bash
# Generate timestamp once for consistency
CURRENT_TIME=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

# Create state file with proper timestamps
cat > "features/${FEATURE_ID}/agents/state.yaml" << EOF
# Auto-generated state file for coordination
feature_id: ${FEATURE_ID}
command: ${COMMAND}
initiated: ${CURRENT_TIME}

# Current workflow position
phase: research        # research|prp|implementation  
status: active         # active|complete|failed
agent: zpl-command-researcher

# Phase tracking
research:
  status: active       # pending|active|complete|failed
  started: ${CURRENT_TIME}
  completed: null
  elapsed_seconds: null
  findings: agents/zpl-command-researcher/findings.md

prp:
  status: pending
  started: null
  completed: null
  elapsed_seconds: null
  path: prp.md

implementation:
  status: pending
  started: null
  completed: null
  elapsed_seconds: null
  iteration: 0
  max_iterations: 50
  last_failure: none   # tests|coverage|detekt|ktlint|performance|integration|demo
EOF
```

## Step 3: Launch Research Agent
Use Task tool with:
- **Subagent:** `zpl-command-researcher`  
- **Prompt:** `Research ZPL command ${COMMAND} using state file features/${FEATURE_ID}/agents/state.yaml`

## Step 4: Launch PRP Generator (After Research Completes)
Use Task tool with:
- **Subagent:** `prp-generator`
- **Prompt:** `Generate PRP for ZPL command ${COMMAND} using state file features/${FEATURE_ID}/agents/state.yaml`

## State Management (Agents)

### Reading State
```bash
# Parse YAML (all agents) - yq v4 syntax
PHASE=$(yq eval '.phase' state.yaml)
STATUS=$(yq eval '.status' state.yaml)
FINDINGS_PATH=$(yq eval '.research.findings' state.yaml)
```

### Updating State  
```bash
# Mark phase complete with elapsed time
START_TIME=$(yq eval '.research.started' state.yaml)
END_TIME=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

# Calculate elapsed seconds (macOS compatible)
START_EPOCH=$(date -jf "%Y-%m-%dT%H:%M:%SZ" "$START_TIME" +%s)
END_EPOCH=$(date -jf "%Y-%m-%dT%H:%M:%SZ" "$END_TIME" +%s)
ELAPSED=$((END_EPOCH - START_EPOCH))

# Update state with completion and elapsed time
yq eval '.research.status = "complete"' -i state.yaml
yq eval ".research.completed = \"$END_TIME\"" -i state.yaml
yq eval ".research.elapsed_seconds = $ELAPSED" -i state.yaml
yq eval '.phase = "prp"' -i state.yaml
yq eval '.prp.status = "active"' -i state.yaml
yq eval ".prp.started = \"$END_TIME\"" -i state.yaml
yq eval '.agent = "prp-generator"' -i state.yaml
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