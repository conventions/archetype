Story: manage user roles

GivenStories: org/conventions/archetype/test/at/logon/logon_at.story

Scenario: insert new role

Given user go to role home

When user clicks in new button

Then should insert role with name new role

Scenario: search roles

Given user go to role home

When user filter role by name [name]

Then should list only roles with name [name]

And return [total] rows

Examples:
|name|total|
|developer|1|
|admin|1|
|a|2|





