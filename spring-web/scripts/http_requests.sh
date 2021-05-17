#!/bin/bash

# curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://127.0.0.1:8080/hplus/rest/products | jq
echo "1 - "
curl -s GET http://127.0.0.1:8080/hplus/rest/products | jq '.'

echo "2 - "
curl -s GET http://127.0.0.1:8080/hplus/rest/product?name=water | jq '.'

echo "3 - "
curl -s GET http://127.0.0.1:8080/hplus/rest/product/water | jq '.'

echo "4 - login correct"
curl -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin\"}" -X POST http://127.0.0.1:8080/hplus/rest/loginuser

echo "5 - login wrong"
curl -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"adminnnnn\"}" -X POST http://127.0.0.1:8080/hplus/rest/loginuser

echo "5 - login user not found"
curl -H "Content-Type: application/json" -d "{\"username\":\"adminnnn\",\"password\":\"admin\"}" -X POST http://127.0.0.1:8080/hplus/rest/loginuser
