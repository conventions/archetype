Story: manage users

Scenario: listing users by role

When i search users with role [name]

Then users found is equal to [rowCount]

Examples:
|name|rowCount|
|developer|2|
|administrator|1|
|secret|0|


Scenario: remove users

Given i login with user [user], [pass]

When i try to remove user [name]

Then i receive message [message]

Examples:
|user|pass|name|message|
|admin|admin|developer|be.user.remove|
|admin|admin|userWithoutGroups|user.delete.message|
|developer|developer|admin|default-security-message|




