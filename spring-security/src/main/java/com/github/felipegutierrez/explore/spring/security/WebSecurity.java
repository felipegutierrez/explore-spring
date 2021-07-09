package com.github.felipegutierrez.explore.spring.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * HTTP GET
 * curl --location --request GET 'http://localhost:28080/auth/realms/appsdeveloperblog/protocol/openid-connect/auth?client_id=photo-app-code-flow-client&response_type=code&scope=openid%20profile&redirect_uri=http://localhost:8083/callback&state=afwerfgqawerf'
 * <p>
 * HTTP POST
 * curl --location --request POST 'http://localhost:28080/auth/realms/appsdeveloperblog/protocol/openid-connect/token' \
 * --header 'Content-Type: application/x-www-form-urlencoded' \
 * --data-urlencode 'grant_type=authorization_code' \
 * --data-urlencode 'client_id=photo-app-code-flow-client' \
 * --data-urlencode 'client_secret=68ed3abb-2fc2-4edc-a734-2fca3d93e12f' \
 * --data-urlencode 'code=712a2cad-b5dc-4bea-970c-9aa004867242.fa53feb8-2424-44c2-9d7c-465cd6df5cf9.c9a39c20-15af-4caa-97f4-139806bdca80' \
 * --data-urlencode 'redirect_uri=http://localhost:8083/callback' \
 * --data-urlencode 'scope=openid profile'
 * <p>
 * HTTP GET
 * curl --location --request GET 'http://localhost:8081/users/status/check' \
 * --header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvUFhkVjUxUDN5WlFjT0l2eEpGdlp5YV9sNXE4OTY5ZGx1VFYxakhnSlJnIn0.eyJleHAiOjE2MjU3NjA3NzAsImlhdCI6MTYyNTc2MDQ3MCwiYXV0aF90aW1lIjoxNjI1NzYwMDEzLCJqdGkiOiJjM2ZlZWJhZC0zOWQxLTQ3NmYtYWI3OS03ZjQyZGJkOWEyODUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjI4MDgwL2F1dGgvcmVhbG1zL2FwcHNkZXZlbG9wZXJibG9nIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjE4NTVkMGRhLTAxNTAtNDJkOC04MTBmLWQ4NWU3NzhhYTQyZiIsInR5cCI6IkJlYXJlciIsImF6cCI6InBob3RvLWFwcC1jb2RlLWZsb3ctY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6ImZhNTNmZWI4LTI0MjQtNDRjMi05ZDdjLTQ2NWNkNmRmNWNmOSIsImFjciI6IjAiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1hcHBzZGV2ZWxvcGVyYmxvZyIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJGZWxpcGUgR3V0aWVycmV6IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZmVsaXBlLm8uZ3V0aWVycmV6IiwiZ2l2ZW5fbmFtZSI6IkZlbGlwZSIsImZhbWlseV9uYW1lIjoiR3V0aWVycmV6IiwiZW1haWwiOiJmZWxpcGUuby5ndXRpZXJyZXpAZ21haWwuY29tIn0.EzgZCQko03Bv3R5KDpPw4S8v3ezjKEy9Q2BvDsnDPzjiV8jPq1UkMHSd3r-YGTVZqw87wtDBl275u7XKvjFJphtYKOB8cvXKwhDNqgbDCgAmTQb0xTarJ4XiDuUzDqgz75FrPHWMP7RPkmAvYLflr7KoaBLbsXKmtVMrpXMLYG3DdukEFBpyyqYIGdeObzc3GVHxMz0zjQLznhRYqIe5JrL_BBX36xPhTQAcXOuFQlSIi_5MABLnY9hwY1M006mvz45LSou_omb8Sj2dtnC-D6dZMKWfWmu0n8OxL6OOk0qzBv0ppT9-tkdB9CjsHKpRVlXzD_JtIh85xgLtETJlBw'
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/status/check")
                /** an authority needs a prefix SCOPE_ */
                // .hasAuthority("SCOPE_profile")
                /** a Role is a collection of authorities. Role does not require a prefix. we can use a collection of roles .hasAnyRole("developer", "user") */
                // .hasRole("developer")
                .hasAnyRole("developer", "user")
                /** Instead of use .hasAnyRole("developer", "user") we can also use .hasAnyAuthority("ROLE_developer", "ROLE_user") with prefix */
                // .hasAnyAuthority("ROLE_developer", "ROLE_user")
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                /** register the JWT authentication converter */
               .jwtAuthenticationConverter(jwtAuthenticationConverter)
        ;
    }
}
