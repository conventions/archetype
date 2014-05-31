Story: manage groups

Scenario: should edit group

Given i search group with name Manager

When i edit group name to Manager2

Then group name must be Manager2


Scenario: remove groups

Given i search group with name [name]

When i try to remove group with name [name]

Then i receive message [message]

Examples:
|name|message|
|Devs|group.business01|
|groupWithoutRole|group.business02|
|groupWithoutUserAndRole|group.delete.message|


