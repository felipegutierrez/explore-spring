package com.github.felipegutierrez.explore.spring.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http GET
 * curl --location --request GET 'http://localhost:28080/auth/realms/appsdeveloperblog/protocol/openid-connect/auth?client_id=photo-app-code-flow-client&response_type=code&scope=openid%20profile&redirect_uri=http://localhost:8083/callback&state=afwerfgqawerf' \
 *
 * http POST
 * curl --location --request POST 'http://localhost:28080/auth/realms/appsdeveloperblog/protocol/openid-connect/token' \
 * --header 'Content-Type: application/x-www-form-urlencoded' \
 * --data-urlencode 'grant_type=authorization_code' \
 * --data-urlencode 'client_id=photo-app-code-flow-client' \
 * --data-urlencode 'client_secret=68ed3abb-2fc2-4edc-a734-2fca3d93e12f' \
 * --data-urlencode 'code=3d3d5615-5680-4724-8135-ae370b4e75e9.f1373996-ae7c-4776-9265-794ad0d8cf55.c9a39c20-15af-4caa-97f4-139806bdca80' \
 * --data-urlencode 'redirect_uri=http://localhost:8083/callback' \
 * --data-urlencode 'scope=openid profile'
 *
 * http GET
 * curl --location --request GET 'http://localhost:8081/token/jwt' \
 * --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvUFhkVjUxUDN5WlFjT0l2eEpGdlp5YV9sNXE4OTY5ZGx1VFYxakhnSlJnIn0.eyJleHAiOjE2MjU3NTY0MzEsImlhdCI6MTYyNTc1NjEzMSwiYXV0aF90aW1lIjoxNjI1NzU1ODQ3LCJqdGkiOiJmMWM5MzkwMC0yNDMwLTQyMmItYmYwZi05ZmVkNzgxMjBkNzEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjI4MDgwL2F1dGgvcmVhbG1zL2FwcHNkZXZlbG9wZXJibG9nIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjE4NTVkMGRhLTAxNTAtNDJkOC04MTBmLWQ4NWU3NzhhYTQyZiIsInR5cCI6IkJlYXJlciIsImF6cCI6InBob3RvLWFwcC1jb2RlLWZsb3ctY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6ImYxMzczOTk2LWFlN2MtNDc3Ni05MjY1LTc5NGFkMGQ4Y2Y1NSIsImFjciI6IjAiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1hcHBzZGV2ZWxvcGVyYmxvZyIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJGZWxpcGUgR3V0aWVycmV6IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZmVsaXBlLm8uZ3V0aWVycmV6IiwiZ2l2ZW5fbmFtZSI6IkZlbGlwZSIsImZhbWlseV9uYW1lIjoiR3V0aWVycmV6IiwiZW1haWwiOiJmZWxpcGUuby5ndXRpZXJyZXpAZ21haWwuY29tIn0.VkqfKuM9DuMW1afYnTaQpu6F5XUiQJCvKYPSI9X4eJRtSFmsNP5A2pEQNyZKlTz5os21QPhwW7bClKYs52lK8k3F8Xgm24DpIqNh5Xb-wKa7umLXnibalFSMagZ63eFTTPXq5Ej5pFO2y1FplDL77YwbvyzWOEMsfjBxpoZiKwk_8kVz4IIqRRidJm0yb3LNoDWhQkBWzOTKfuwwKpPndcAvOZOm2xpjvs5UZIsZZB3TI4hSFtfG-YmW0w-stPdhuBRmSwlTRu7z_qN5pQuyzb5uxOEaVfG8binK9eds1u1N-75ZFmYICbLHVROcqyO8EXiPF8nL_pxw9BCOw2oxNg' \
 *
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping("/jwt")
    public Jwt getToken(@AuthenticationPrincipal Jwt jwt) {
        return jwt;
    }

    @GetMapping("/map")
    public Map<String, Object> getTokenMap(@AuthenticationPrincipal Jwt jwt) {
        return Collections.singletonMap("principal", jwt);
    }
}
