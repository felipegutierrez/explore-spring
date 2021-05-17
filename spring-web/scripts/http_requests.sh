#!/bin/bash

# curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://127.0.0.1:8080/hplus/rest/products | jq
echo "1 - "
curl -s GET http://127.0.0.1:8080/hplus/rest/products | jq '.'

echo "2 - "
curl -s GET http://127.0.0.1:8080/hplus/rest/product?name=water | jq '.'

echo "3 - "
curl -s GET http://127.0.0.1:8080/hplus/rest/product/water | jq '.'
