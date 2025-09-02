## FEATURE:

Modify the existing Maverick Media Clients List screen to display reference and name fields with search and filter capabilities. Currently the Clients List screen has no visible fields, so this enhancement will make the basic client information accessible and searchable through the Filament table interface.

## EXAMPLES:

No code examples yet.

## DOCUMENTATION:

- Filament Tables Overview: https://filamentphp.com/docs/4.x/tables/overview
- Text Columns: https://filamentphp.com/docs/4.x/tables/columns/text
- Filters Overview: https://filamentphp.com/docs/4.x/tables/filters/overview
- Empty State: https://filamentphp.com/docs/4.x/tables/empty-state
- Testing Filament: https://filamentphp.com/docs/4.x/testing/overview
- Testing Filament Resources: https://filamentphp.com/docs/4.x/testing/testing-resources
- Testing Filament Tables: https://filamentphp.com/docs/4.x/testing/testing-tables

## OTHER CONSIDERATIONS:

- Maverick Media tenant only - ensure proper tenant isolation
- Changes should be made to existing List view's $table configuration
- Reference filter must be range-style (e.g., find records where reference is 10000...10005)
- Both reference and name fields need to be searchable, filterable, and sortable
- Follow existing project patterns for Filament table configuration
