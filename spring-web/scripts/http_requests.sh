#!/bin/bash

# curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://127.0.0.1:8080/hplus/rest/products | jq
curl -s GET http://127.0.0.1:8080/hplus/rest/products | jq '.'

