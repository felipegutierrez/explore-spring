package com.github.felipegutierrez.explore.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    Environment env;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port: " + env.getProperty("local.server.port");
    }

    /**
     * testing it using HTTP DELETE
     * curl --location --request DELETE 'http://localhost:8081/users/13654654' \
     * --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvUFhkVjUxUDN5WlFjT0l2eEpGdlp5YV9sNXE4OTY5ZGx1VFYxakhnSlJnIn0.eyJleHAiOjE2MjU4MTc4OTgsImlhdCI6MTYyNTgxNzU5OCwiYXV0aF90aW1lIjoxNjI1ODE3NTY3LCJqdGkiOiJkY2E1NjNiYS1iZmM2LTQ2YmUtYmU5YS1lOGYwOTI3NmUxYjYiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjI4MDgwL2F1dGgvcmVhbG1zL2FwcHNkZXZlbG9wZXJibG9nIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjE4NTVkMGRhLTAxNTAtNDJkOC04MTBmLWQ4NWU3NzhhYTQyZiIsInR5cCI6IkJlYXJlciIsImF6cCI6InBob3RvLWFwcC1jb2RlLWZsb3ctY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6IjQ1YTcwMTBkLWI1MjQtNDFiMy1iZTZlLTI1N2MzOGIzZDU0MiIsImFjciI6IjAiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1hcHBzZGV2ZWxvcGVyYmxvZyIsIm9mZmxpbmVfYWNjZXNzIiwiZGV2ZWxvcGVyIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJGZWxpcGUgR3V0aWVycmV6IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZmVsaXBlLm8uZ3V0aWVycmV6IiwiZ2l2ZW5fbmFtZSI6IkZlbGlwZSIsImZhbWlseV9uYW1lIjoiR3V0aWVycmV6IiwiZW1haWwiOiJmZWxpcGUuby5ndXRpZXJyZXpAZ21haWwuY29tIn0.X1_Fy-J2cFHBEuJ5M2jUEK7kfiLU96-pisCCBsL1uS3pk7m74OyjP8IjBPJi8ulUQvLBQcG-37hmLds_0bqZPZu3ZDuZuuY-c9CEMJ2LXMpbw3o_yQiE8xkg9IT8HQbJgPrq_QchY4LwkmBWSLwfld2TQo2gGnfnIh5vDYp68JijgzMjM6eUPv0yP9SELm8ddcwD2nKoGvKNm_lhiScdFB9HeQGWDYIwqTInDKC8fUT7ksVr0EhLlqegTwrM163UnqpLSJrE38eOatz1HyXj-tKRnMQll-Js_CRsoHKmBG7X8VqygbQ__AsydMMYvuL54Qz6-ey_c2jAIn2SISq7EA'
     *
     * @param id
     * @return
     */
    @Secured("ROLE_developer")
    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id) {
        return "Delete user with id " + id;
    }

    /**
     *
     * curl --location --request DELETE 'http://localhost:8081/users/pre/13654654' \
     * --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvUFhkVjUxUDN5WlFjT0l2eEpGdlp5YV9sNXE4OTY5ZGx1VFYxakhnSlJnIn0.eyJleHAiOjE2MjU4MTg0MTEsImlhdCI6MTYyNTgxODExMSwiYXV0aF90aW1lIjoxNjI1ODE3NTY3LCJqdGkiOiJiNzE2NGJjMi0yZWQwLTQxMWEtODg0MC03MGVkOTg1NmRlMzQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjI4MDgwL2F1dGgvcmVhbG1zL2FwcHNkZXZlbG9wZXJibG9nIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjE4NTVkMGRhLTAxNTAtNDJkOC04MTBmLWQ4NWU3NzhhYTQyZiIsInR5cCI6IkJlYXJlciIsImF6cCI6InBob3RvLWFwcC1jb2RlLWZsb3ctY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6IjQ1YTcwMTBkLWI1MjQtNDFiMy1iZTZlLTI1N2MzOGIzZDU0MiIsImFjciI6IjAiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1hcHBzZGV2ZWxvcGVyYmxvZyIsIm9mZmxpbmVfYWNjZXNzIiwiZGV2ZWxvcGVyIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJGZWxpcGUgR3V0aWVycmV6IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZmVsaXBlLm8uZ3V0aWVycmV6IiwiZ2l2ZW5fbmFtZSI6IkZlbGlwZSIsImZhbWlseV9uYW1lIjoiR3V0aWVycmV6IiwiZW1haWwiOiJmZWxpcGUuby5ndXRpZXJyZXpAZ21haWwuY29tIn0.FOJyaCft0DA7hlh3TOsej_YUld6h27HSswvL7j_6OuuUiLbX2SMSqKutCEb0YDrN8alBae9ylcO3_4DGpWU3itISkEVG8brGu6Ik7ZIBbDast_D0IMhxvsV15zql0siRer3qPG63cvqEunyt_a_cLcuGqykMu_9HXvl6NCgvNVvWnwTqmWH6qRzHBPfRekDZF3_XAz_cT1GAjVACDVejHddtDQxPzsDTz0u-5GarmrLud6bQ5XiOlsEBM-AFJU4k3dgc54s0RWrgu1dFnvQiWtS12GhojUNBmi0x0Hocr5esE7MVZyj4Jp4B1ard0jyzYe9GEqbtymtemcAxkfefpQ'
     * 
     * @param id
     * @return
     */
    // @PreAuthorize("hasRole('developer')")
    @PreAuthorize("hasAuthority('ROLE_developer')")
    @DeleteMapping(path = "/pre/{id}")
    public String deleteUserPreAuthorized(@PathVariable String id) {
        return "Delete user with pre authorization enabled and with id " + id;
    }
}
