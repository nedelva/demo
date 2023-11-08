package com.example;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Single-value HTTP header handling tests
 * For micronaut-function-aws-api-proxy version in range [4.0.0,4.0.4] both tests succeed,
 * while for version in range [4.0.5,4.1.1] second test fails
 */
public class AnotherHomeControllerTest {
    private static ApiGatewayProxyRequestEventFunction handler;

    @BeforeAll
    public static void setupSpec() {
        handler = new ApiGatewayProxyRequestEventFunction();
    }
    @AfterAll
    public static void cleanupSpec() {
        handler.getApplicationContext().close();
    }

    @Test
    void testValidateTokenNoHeader() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/validate/token");
        request.setHttpMethod(HttpMethod.GET.toString());
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, lambdaContext);
        assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode());
        assertEquals("{\"_links\":{\"self\":[{}]},\"_embedded\":{\"errors\":[{\"message\":\"Required Header [accessToken] not specified\",\"path\":\"/oAuthToken\"}]},\"message\":\"Bad Request\"}",  response.getBody());
    }
    @Test
    void testValidateTokenHeaderPresent() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/validate/token");
        request.setHttpMethod(HttpMethod.GET.toString());
        Map<String, String> headers = Map.of("accessToken","some-token-here");
        request.setHeaders(headers);

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, lambdaContext);
        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals("User 'AuthorizedUser' VALIDATED",  response.getBody());
    }

   static final Context lambdaContext = new Context() {

        @Override
        public String getAwsRequestId() {
            return null;
        }

        @Override
        public String getLogGroupName() {
            return null;
        }

        @Override
        public String getLogStreamName() {
            return null;
        }

        @Override
        public String getFunctionName() {
            return null;
        }

        @Override
        public String getFunctionVersion() {
            return null;
        }

        @Override
        public String getInvokedFunctionArn() {
            return null;
        }

        @Override
        public CognitoIdentity getIdentity() {
            return null;
        }

        @Override
        public ClientContext getClientContext() {
            return null;
        }

        @Override
        public int getRemainingTimeInMillis() {
            return 0;
        }

        @Override
        public int getMemoryLimitInMB() {
            return 0;
        }

        @Override
        public LambdaLogger getLogger() {
            return null;
        }
    };
}
