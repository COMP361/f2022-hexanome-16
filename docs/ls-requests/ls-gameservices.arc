{
    "createdAt": "2021-11-29T20:47:06.284Z",
    "kind": "ARC#Project",
    "projects": [
        {
            "created": 1600257696395,
            "key": "2a29af38-8220-4a55-b935-2a2ccfcfed3d",
            "kind": "ARC#ProjectData",
            "name": "BGP-Gameservices",
            "order": 0,
            "requests": [
                "3dfecdb1-9956-47ec-8bbd-1a989282a13d",
                "aa63f54e-38a0-45b1-b702-6a835ff7ea82",
                "cb11ffc6-df00-4c06-a195-b330fb0543c8",
                "cc591265-6504-4383-89c5-1c22ce5d6f46"
            ],
            "updated": 1600257696395
        }
    ],
    "requests": [
        {
            "authorization": [
                {
                    "config": {
                        "password": "bgp-client-pw",
                        "username": "bgp-client-name"
                    },
                    "enabled": true,
                    "type": "basic"
                }
            ],
            "created": 1591452450261,
            "description": "",
            "headers": "",
            "key": "3dfecdb1-9956-47ec-8bbd-1a989282a13d",
            "kind": "ARC#RequestData",
            "method": "DELETE",
            "midnight": 1600228800000,
            "name": "UnregisterGameservice",
            "projects": [
                "2a29af38-8220-4a55-b935-2a2ccfcfed3d"
            ],
            "requestActions": {
                "variables": [
                    {
                        "enabled": false,
                        "value": "",
                        "variable": "myVar"
                    }
                ]
            },
            "type": "saved",
            "updated": 1600258252949,
            "url": "http://127.0.0.1:4242/api/gameservices/DummyGame1?access_token=%2BU28OLgzRSVk56C/OMs2aQhvmgQ="
        },
        {
            "authorization": [
                {
                    "config": {
                        "password": "bgp-client-pw",
                        "username": "bgp-client-name"
                    },
                    "enabled": true,
                    "type": "basic"
                }
            ],
            "created": 1591452450261,
            "description": "",
            "headers": "",
            "key": "aa63f54e-38a0-45b1-b702-6a835ff7ea82",
            "kind": "ARC#RequestData",
            "method": "GET",
            "midnight": 1600228800000,
            "name": "GetGameservices",
            "projects": [
                "2a29af38-8220-4a55-b935-2a2ccfcfed3d"
            ],
            "requestActions": {
                "variables": [
                    {
                        "enabled": false,
                        "value": "",
                        "variable": "myVar"
                    }
                ]
            },
            "type": "saved",
            "updated": 1600257889075,
            "url": "http://127.0.0.1:4242/api/gameservices"
        },
        {
            "actions": {},
            "authorization": [
                {
                    "config": {
                        "password": "bgp-client-pw",
                        "username": "bgp-client-name"
                    },
                    "enabled": true,
                    "type": "basic"
                }
            ],
            "created": 1591452450261,
            "description": "",
            "headers": "Content-Type: application/json",
            "key": "cb11ffc6-df00-4c06-a195-b330fb0543c8",
            "kind": "ARC#RequestData",
            "method": "PUT",
            "midnight": 1638162000000,
            "name": "RegisterGameservice",
            "projects": [
                "2a29af38-8220-4a55-b935-2a2ccfcfed3d"
            ],
            "requestActions": {
                "variables": [
                    {
                        "enabled": false,
                        "value": "",
                        "variable": "myVar"
                    }
                ]
            },
            "type": "saved",
            "ui": {
                "body": {
                    "model": [
                        {
                            "type": "raw",
                            "viewModel": [
                                {
                                    "value": "{\n    \"location\": \"http://127.0.0.1:4243/DummyGameService\",\n    \"maxSessionPlayers\": 5,\n    \"minSessionPlayers\": 3,\n    \"name\": \"DummyGame2\",\n    \"displayName\": \"My second dummy game. YOLO.\",\n    \"webSupport\": \"true\"\n}"
                                }
                            ]
                        }
                    ],
                    "selected": "raw"
                },
                "selectedEditor": 1
            },
            "updated": 1638218793704,
            "url": "http://127.0.0.1:4242/api/gameservices/DummyGame2?access_token=%2BU28OLgzRSVk56C/OMs2aQhvmgQ="
        },
        {
            "authorization": [
                {
                    "config": {
                        "password": "bgp-client-pw",
                        "username": "bgp-client-name"
                    },
                    "enabled": true,
                    "type": "basic"
                }
            ],
            "created": 1591452450261,
            "description": "",
            "headers": "",
            "key": "cc591265-6504-4383-89c5-1c22ce5d6f46",
            "kind": "ARC#RequestData",
            "method": "GET",
            "midnight": 1600228800000,
            "name": "GetGameservice",
            "projects": [
                "2a29af38-8220-4a55-b935-2a2ccfcfed3d"
            ],
            "requestActions": {
                "variables": [
                    {
                        "enabled": false,
                        "value": "",
                        "variable": "myVar"
                    }
                ]
            },
            "type": "saved",
            "updated": 1600257962297,
            "url": "http://127.0.0.1:4242/api/gameservices/DummyGame1"
        }
    ],
    "version": "16.0.1"
}
