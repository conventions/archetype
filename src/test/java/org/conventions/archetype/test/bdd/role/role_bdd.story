Story: manage roles

Scenario: should list roles

When i search roles with name [name]

Then roles found is equal to [total]

Examples:
|name|total|
|administrator|1|
|operator|0|
|a|2|

Scenario: removing roles

When i try to remove role with name [name]

Then i receive message [message]

Examples:
|name|message|
|simpleRole|role.delete.message|
|administrator|role.be.admin|
|developer|role.be.groups|


Scenario: inserting roles

When i insert role with name roleName

Then i receive message role.create.message
