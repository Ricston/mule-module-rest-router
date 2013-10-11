/**
 * Mule Rest Module
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.rest;

import org.mule.api.MuleEvent;
import org.mule.api.transport.PropertyScope;
import org.mule.construct.Flow;
import org.mule.modules.entity.Person;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class RestRouterModuleBasicTransformationTest extends FunctionalTestCase {
    private static final String FLOW_NAME = "basicTransformation";
    
    private static final Person TEST_PERSON = new Person("Alan", "Cassar");

	@Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Test
    public void testGetPerson() throws Exception {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("http.method", "get");
        properties.put("http.request.path", "http://alan.blog.com/users");
        runFlowWithPayloadAndExpect(FLOW_NAME, "{\"name\":\"Alan\",\"surname\":\"Cassar\"}", null, properties);
    }
    
    @Test
    public void testPutPerson() throws Exception {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("http.method", "put");
        properties.put("http.request.path", "http://alan.blog.com/users");
        runFlowWithPayloadAndExpect(FLOW_NAME, TEST_PERSON, "{\"name\":\"Alan\",\"surname\":\"Cassar\"}", properties);
    }

    @Test
    public void testPostPerson() throws Exception {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("http.method", "post");
        properties.put("http.request.path", "http://alan.blog.com/users");
        runFlowWithPayloadAndExpect(FLOW_NAME, TEST_PERSON, "{\"name\":\"Alan\",\"surname\":\"Cassar\"}", properties);
    }

     /**
     * Run the flow specified by name and assert equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param expect   The expected output
     */
    protected <T> void runFlowAndExpect(String flowName, T expect) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = getTestEvent(null);
        MuleEvent responseEvent = flow.process(event);

        Assert.assertEquals(expect, responseEvent.getMessage().getPayload());
    }

    /**
     * Run the flow specified by name using the specified payload and assert
     * equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param expect   The expected output
     * @param payload  The payload of the input event
     */
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, T expect, U payload, Map<String, Object> inboundProperties) throws Exception {
        Flow flow = lookupFlowConstruct(flowName);
        MuleEvent event = getTestEvent(payload);

        for (String propertyKey : inboundProperties.keySet()) {
            event.getMessage().setProperty(propertyKey, inboundProperties.get(propertyKey), PropertyScope.INBOUND);
        }
        MuleEvent responseEvent = flow.process(event);

        Assert.assertEquals(expect, responseEvent.getMessage().getPayload());
    }

    /**
     * Retrieve a flow by name from the registry
     *
     * @param name Name of the flow to retrieve
     */
    protected Flow lookupFlowConstruct(String name) {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
