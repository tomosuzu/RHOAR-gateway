package com.redhat.freelancer.gateway.rest;

import com.redhat.freelancer.gateway.model.Freelancer;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@ContextName("rest-context")
public class FreelancerResource extends RouteBuilder {
    @Override
    public void configure() {
        String freelancerUrl = "http://freelancer-service-tosuzuki-freelancer.apps.na311.openshift.opentlc.com";

        restConfiguration()
                .component("undertow")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("/freelancers").description("Freelancer Service")
                .produces(MediaType.APPLICATION_JSON)

        .get("/").description("Get Freelancer List").outType(Freelancer[].class)
                .route().id("getFreelancersRoute")
                .setBody(simple("null"))
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_PATH, simple("freelancers"))
                .setHeader(Exchange.HTTP_URI, simple(freelancerUrl))
                .to("http4://DUMMY")
                .setHeader("CamelJacksonUnmarshalType", simple(Freelancer[].class.getName()))
                .unmarshal()
                .json(JsonLibrary.Jackson, Freelancer[].class)
                .endRest()

        .get("/{freelancerId}").description("Get a Freelancer by Id").param()
                .name("freelancerId").type(RestParamType.path).description("The Id of freelancer")
                .dataType("string").endParam().outType(Freelancer.class)
                .route().id("getFreelancerRoute")
                .setBody(simple("null"))
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_PATH, simple("freelancers/${header.freelancerId}"))
                .setHeader(Exchange.HTTP_URI, simple(freelancerUrl))
                .to("http4://DUMMY")
                .setHeader("CamelJacksonUnmarshalType", simple(Freelancer.class.getName()))
                .unmarshal()
                .json(JsonLibrary.Jackson, Freelancer.class)
                .endRest()
        ;

    }
}