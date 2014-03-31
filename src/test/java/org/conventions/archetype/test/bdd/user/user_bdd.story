Story: manage users

Scenario: listing users by role

When i search users with role [name]

Then users found is equal to [total]

Examples:
|name|total|
|developer|2|
|administrator|1|
|secret|0|




